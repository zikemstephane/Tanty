package com.NTFOODS.Api_tanty.modules.stock.application.transfert.usecase.executer;

import com.NTFOODS.Api_tanty.modules.auth.domain.utilisateur.aggregate.Utilisateur;
import com.NTFOODS.Api_tanty.modules.auth.domain.utilisateur.enums.Role;
import com.NTFOODS.Api_tanty.modules.stock.application.transfert.dto.request.CreerTransfertRequest;
import com.NTFOODS.Api_tanty.modules.stock.application.transfert.dto.response.TransfertResponse;
import com.NTFOODS.Api_tanty.modules.stock.application.transfert.mapper.TransfertMapper;
import com.NTFOODS.Api_tanty.modules.stock.domain.catalogue.repository.ProduitRepository;
import com.NTFOODS.Api_tanty.modules.stock.domain.catalogue.valueobject.CodeProduit;
import com.NTFOODS.Api_tanty.modules.stock.domain.entrepot.enums.TypeEntrepot;
import com.NTFOODS.Api_tanty.modules.stock.domain.entrepot.repository.EntrepotRepository;
import com.NTFOODS.Api_tanty.modules.stock.domain.mouvement.model.MouvementStock;
import com.NTFOODS.Api_tanty.modules.stock.domain.mouvement.repository.MouvementStockRepository;
import com.NTFOODS.Api_tanty.modules.stock.domain.niveau.model.NiveauStock;
import com.NTFOODS.Api_tanty.modules.stock.domain.niveau.repository.NiveauStockRepository;
import com.NTFOODS.Api_tanty.modules.stock.domain.niveau.service.GestionDeStock;
import com.NTFOODS.Api_tanty.modules.stock.domain.transfert.model.BonTransfert;
import com.NTFOODS.Api_tanty.modules.stock.domain.transfert.model.LigneTransfert;
import com.NTFOODS.Api_tanty.modules.stock.domain.transfert.repository.BonTransfertRepository;
import com.NTFOODS.Api_tanty.shared.kernel.event.EventPublisher;
import com.NTFOODS.Api_tanty.shared.kernel.exception.AccesRefuseException;
import com.NTFOODS.Api_tanty.shared.kernel.exception.EntiteNonTrouveeException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

/**
 * Use Case — créer et exécuter un transfert Central → Tampon.
 * Pour chaque ligne : sortie du central + entrée au tampon (2 effets atomiques),
 * conversion conditionnement → unités de base, mouvement TRANSFERT enregistré.
 */
@Service
@Transactional
public class ExecuterTransfertUseCase {

    private static final DateTimeFormatter FMT = DateTimeFormatter.ofPattern("yyyyMMdd");

    private final BonTransfertRepository transfertRepository;
    private final EntrepotRepository entrepotRepository;
    private final NiveauStockRepository niveauStockRepository;
    private final MouvementStockRepository mouvementRepository;
    private final ProduitRepository produitRepository;
    private final TransfertMapper transfertMapper;
    private final EventPublisher eventPublisher;

    public ExecuterTransfertUseCase(BonTransfertRepository transfertRepository,
                                    EntrepotRepository entrepotRepository,
                                    NiveauStockRepository niveauStockRepository,
                                    MouvementStockRepository mouvementRepository,
                                    ProduitRepository produitRepository,
                                    TransfertMapper transfertMapper,
                                    EventPublisher eventPublisher) {
        this.transfertRepository = transfertRepository;
        this.entrepotRepository = entrepotRepository;
        this.niveauStockRepository = niveauStockRepository;
        this.mouvementRepository = mouvementRepository;
        this.produitRepository = produitRepository;
        this.transfertMapper = transfertMapper;
        this.eventPublisher = eventPublisher;
    }

    public TransfertResponse executer(CreerTransfertRequest request, Utilisateur acteur) {
        if (acteur.getRole() != Role.GESTIONNAIRE_STOCK && acteur.getRole() != Role.ADMIN) {
            throw new AccesRefuseException("Seul le Gestionnaire de Stock peut effectuer un transfert");
        }
        String source = resoudre(request.codeEntrepotSource(), TypeEntrepot.CENTRAL);
        String cible = resoudre(request.codeEntrepotCible(), TypeEntrepot.TAMPON);

        BonTransfert bon = BonTransfert.creer(genererNumero(), source, cible, acteur.getMatricule().getValeur(), false);
        request.lignes().forEach(l -> bon.ajouterLigne(l.codeProduit(), l.sku(), l.quantite()));

        for (LigneTransfert ligne : bon.getLignes()) {
            long base = convertirEnBase(ligne.getCodeProduit(), ligne.getSku(), ligne.getQuantite());

            NiveauStock niveauSource = niveauStockRepository.trouver(ligne.getCodeProduit(), source)
                .orElseThrow(() -> new EntiteNonTrouveeException(
                    "Aucun stock central pour le produit " + ligne.getCodeProduit()));
            NiveauStock niveauCible = niveauStockRepository.trouver(ligne.getCodeProduit(), cible)
                .orElseGet(() -> NiveauStock.creer(ligne.getCodeProduit(), cible, TypeEntrepot.TAMPON));

            MouvementStock mouvement = GestionDeStock.appliquerTransfert(niveauSource, niveauCible,
                genererReferenceMouvement(), ligne.getSku(), base, acteur.getMatricule().getValeur(),
                bon.getNumero(), "Transfert central → tampon");

            niveauStockRepository.sauvegarder(niveauSource);
            niveauStockRepository.sauvegarder(niveauCible);
            mouvementRepository.enregistrer(mouvement);
        }

        bon.marquerExecute();
        BonTransfert sauvegarde = transfertRepository.sauvegarder(bon);
        bon.getDomainEvents().forEach(eventPublisher::publish);
        bon.clearDomainEvents();
        return transfertMapper.versResponse(sauvegarde);
    }

    private String resoudre(String codeFourni, TypeEntrepot type) {
        if (codeFourni != null && !codeFourni.isBlank()) return codeFourni.trim().toUpperCase();
        return entrepotRepository.trouverUniqueParType(type)
            .map(e -> e.getCode())
            .orElseThrow(() -> new EntiteNonTrouveeException("Aucun entrepôt " + type.getLibelle() + " configuré"));
    }

    private long convertirEnBase(String codeProduit, String sku, long quantite) {
        if (sku == null || sku.isBlank()) return quantite;
        return produitRepository.trouverParCode(CodeProduit.de(codeProduit))
            .flatMap(p -> p.trouverParSku(sku))
            .map(c -> quantite * c.getFacteurConversionVersBase())
            .orElse(quantite);
    }

    private String genererNumero() {
        int annee = LocalDate.now().getYear();
        long sequence = transfertRepository.compterParAnnee(annee) + 1;
        return String.format("BT-%d-%04d", annee, sequence);
    }

    private String genererReferenceMouvement() {
        long sequence = mouvementRepository.compter() + 1;
        return "MVT-" + FMT.format(LocalDate.now()) + "-" + String.format("%05d", sequence);
    }
}
