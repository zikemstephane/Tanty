package com.NTFOODS.Api_tanty.modules.stock.application.retour.usecase.enregistrer;

import com.NTFOODS.Api_tanty.modules.auth.domain.utilisateur.aggregate.Utilisateur;
import com.NTFOODS.Api_tanty.modules.auth.domain.utilisateur.enums.Role;
import com.NTFOODS.Api_tanty.modules.stock.application.retour.dto.request.EnregistrerRetourRequest;
import com.NTFOODS.Api_tanty.modules.stock.application.retour.dto.response.RetourResponse;
import com.NTFOODS.Api_tanty.modules.stock.application.retour.mapper.RetourMapper;
import com.NTFOODS.Api_tanty.modules.stock.domain.catalogue.repository.ProduitRepository;
import com.NTFOODS.Api_tanty.modules.stock.domain.catalogue.valueobject.CodeProduit;
import com.NTFOODS.Api_tanty.modules.stock.domain.entrepot.enums.TypeEntrepot;
import com.NTFOODS.Api_tanty.modules.stock.domain.entrepot.model.Entrepot;
import com.NTFOODS.Api_tanty.modules.stock.domain.entrepot.repository.EntrepotRepository;
import com.NTFOODS.Api_tanty.modules.stock.domain.mouvement.enums.TypeMouvement;
import com.NTFOODS.Api_tanty.modules.stock.domain.mouvement.model.MouvementStock;
import com.NTFOODS.Api_tanty.modules.stock.domain.mouvement.repository.MouvementStockRepository;
import com.NTFOODS.Api_tanty.modules.stock.domain.niveau.model.NiveauStock;
import com.NTFOODS.Api_tanty.modules.stock.domain.niveau.repository.NiveauStockRepository;
import com.NTFOODS.Api_tanty.modules.stock.domain.retour.model.BonRetour;
import com.NTFOODS.Api_tanty.modules.stock.domain.retour.model.LigneRetour;
import com.NTFOODS.Api_tanty.modules.stock.domain.retour.repository.BonRetourRepository;
import com.NTFOODS.Api_tanty.shared.kernel.exception.AccesRefuseException;
import com.NTFOODS.Api_tanty.shared.kernel.exception.EntiteNonTrouveeException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

/**
 * Use Case — enregistrement d'un retour d'invendus. Pour chaque ligne :
 * débit du stock mobile du commercial, recrédit du stock tampon, mouvement RETOUR_COMMERCIAL.
 */
@Service
@Transactional
public class EnregistrerRetourUseCase {

    private static final DateTimeFormatter FMT = DateTimeFormatter.ofPattern("yyyyMMdd");

    private final BonRetourRepository retourRepository;
    private final EntrepotRepository entrepotRepository;
    private final NiveauStockRepository niveauStockRepository;
    private final MouvementStockRepository mouvementRepository;
    private final ProduitRepository produitRepository;
    private final RetourMapper retourMapper;

    public EnregistrerRetourUseCase(BonRetourRepository retourRepository, EntrepotRepository entrepotRepository,
                                    NiveauStockRepository niveauStockRepository,
                                    MouvementStockRepository mouvementRepository, ProduitRepository produitRepository,
                                    RetourMapper retourMapper) {
        this.retourRepository = retourRepository;
        this.entrepotRepository = entrepotRepository;
        this.niveauStockRepository = niveauStockRepository;
        this.mouvementRepository = mouvementRepository;
        this.produitRepository = produitRepository;
        this.retourMapper = retourMapper;
    }

    public RetourResponse executer(EnregistrerRetourRequest request, Utilisateur acteur) {
        if (acteur.getRole() != Role.GESTIONNAIRE_STOCK && acteur.getRole() != Role.ADMIN) {
            throw new AccesRefuseException("Seul le Gestionnaire de Stock peut enregistrer un retour");
        }
        String matriculeCommercial = request.matriculeCommercial();
        String tampon = entrepotRepository.trouverUniqueParType(TypeEntrepot.TAMPON)
            .map(Entrepot::getCode)
            .orElseThrow(() -> new EntiteNonTrouveeException("Aucun entrepôt tampon configuré"));
        String mobile = entrepotRepository.trouverMobileParCommercial(matriculeCommercial)
            .map(Entrepot::getCode)
            .orElseThrow(() -> new EntiteNonTrouveeException("Aucun stock mobile pour le commercial " + matriculeCommercial));

        BonRetour bon = BonRetour.creer(genererNumero(), matriculeCommercial, acteur.getMatricule().getValeur(), LocalDate.now());
        request.lignes().forEach(l -> bon.ajouterLigne(l.codeProduit(), l.sku(), l.quantite(), l.motif()));
        bon.exigerNonVide();

        for (LigneRetour ligne : bon.getLignes()) {
            long base = convertirEnBase(ligne.getCodeProduit(), ligne.getSku(), ligne.getQuantite());

            NiveauStock niveauMobile = niveauStockRepository.trouver(ligne.getCodeProduit(), mobile)
                .orElseThrow(() -> new EntiteNonTrouveeException(
                    "Aucun stock mobile pour le produit " + ligne.getCodeProduit()));
            NiveauStock niveauTampon = niveauStockRepository.trouver(ligne.getCodeProduit(), tampon)
                .orElseGet(() -> NiveauStock.creer(ligne.getCodeProduit(), tampon, TypeEntrepot.TAMPON));

            niveauMobile.sortir(base);
            niveauTampon.entrer(base);

            MouvementStock mouvement = MouvementStock.enregistrer(genererReferenceMouvement(),
                TypeMouvement.RETOUR_COMMERCIAL, ligne.getCodeProduit(), ligne.getSku(), base,
                mobile, tampon, acteur.getMatricule().getValeur(), bon.getNumero(),
                "Retour invendus " + matriculeCommercial);

            niveauStockRepository.sauvegarder(niveauMobile);
            niveauStockRepository.sauvegarder(niveauTampon);
            mouvementRepository.enregistrer(mouvement);
        }

        return retourMapper.versResponse(retourRepository.sauvegarder(bon));
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
        long sequence = retourRepository.compterParAnnee(annee) + 1;
        return String.format("BRET-%d-%04d", annee, sequence);
    }

    private String genererReferenceMouvement() {
        long sequence = mouvementRepository.compter() + 1;
        return "MVT-" + FMT.format(LocalDate.now()) + "-" + String.format("%05d", sequence);
    }
}
