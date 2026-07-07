package com.NTFOODS.Api_tanty.modules.stock.application.reception.usecase.valider;

import com.NTFOODS.Api_tanty.modules.auth.domain.utilisateur.aggregate.Utilisateur;
import com.NTFOODS.Api_tanty.modules.stock.application.reception.dto.response.ReceptionResponse;
import com.NTFOODS.Api_tanty.modules.stock.application.reception.mapper.ReceptionMapper;
import com.NTFOODS.Api_tanty.modules.stock.domain.catalogue.model.Produit;
import com.NTFOODS.Api_tanty.modules.stock.domain.catalogue.repository.ProduitRepository;
import com.NTFOODS.Api_tanty.modules.stock.domain.catalogue.valueobject.CodeProduit;
import com.NTFOODS.Api_tanty.modules.stock.domain.commande.model.BonCommande;
import com.NTFOODS.Api_tanty.modules.stock.domain.commande.repository.BonCommandeRepository;
import com.NTFOODS.Api_tanty.modules.stock.domain.entrepot.enums.TypeEntrepot;
import com.NTFOODS.Api_tanty.modules.stock.domain.mouvement.enums.TypeMouvement;
import com.NTFOODS.Api_tanty.modules.stock.domain.mouvement.model.MouvementStock;
import com.NTFOODS.Api_tanty.modules.stock.domain.mouvement.repository.MouvementStockRepository;
import com.NTFOODS.Api_tanty.modules.stock.domain.niveau.model.NiveauStock;
import com.NTFOODS.Api_tanty.modules.stock.domain.niveau.repository.NiveauStockRepository;
import com.NTFOODS.Api_tanty.modules.stock.domain.niveau.service.GestionDeStock;
import com.NTFOODS.Api_tanty.modules.stock.domain.reception.enums.SourceReception;
import com.NTFOODS.Api_tanty.modules.stock.domain.reception.model.LigneReception;
import com.NTFOODS.Api_tanty.modules.stock.domain.reception.model.Reception;
import com.NTFOODS.Api_tanty.modules.stock.domain.reception.repository.ReceptionRepository;
import com.NTFOODS.Api_tanty.shared.kernel.event.EventPublisher;
import com.NTFOODS.Api_tanty.shared.kernel.exception.EntiteNonTrouveeException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

/**
 * Use Case — 2ᵉ validation d'une réception. Effets ACID :
 * 1) validation métier (rôle selon catégorie) ;
 * 2) entrée de chaque ligne dans le stock central (conversion conditionnement → unités base) ;
 * 3) enregistrement d'un mouvement de stock par ligne ;
 * 4) mise à jour des quantités reçues du BC (réception fournisseur) ;
 * 5) publication de l'événement de réception validée.
 */
@Service
@Transactional
public class ValiderReceptionEtape2UseCase {

    private static final Logger log = LoggerFactory.getLogger(ValiderReceptionEtape2UseCase.class);
    private static final DateTimeFormatter FMT = DateTimeFormatter.ofPattern("yyyyMMdd");

    private final ReceptionRepository receptionRepository;
    private final NiveauStockRepository niveauStockRepository;
    private final MouvementStockRepository mouvementRepository;
    private final ProduitRepository produitRepository;
    private final BonCommandeRepository bonCommandeRepository;
    private final ReceptionMapper receptionMapper;
    private final EventPublisher eventPublisher;

    public ValiderReceptionEtape2UseCase(ReceptionRepository receptionRepository,
                                         NiveauStockRepository niveauStockRepository,
                                         MouvementStockRepository mouvementRepository,
                                         ProduitRepository produitRepository,
                                         BonCommandeRepository bonCommandeRepository,
                                         ReceptionMapper receptionMapper,
                                         EventPublisher eventPublisher) {
        this.receptionRepository = receptionRepository;
        this.niveauStockRepository = niveauStockRepository;
        this.mouvementRepository = mouvementRepository;
        this.produitRepository = produitRepository;
        this.bonCommandeRepository = bonCommandeRepository;
        this.receptionMapper = receptionMapper;
        this.eventPublisher = eventPublisher;
    }

    public ReceptionResponse executer(String numero, Utilisateur acteur) {
        Reception reception = receptionRepository.trouverParNumero(numero)
            .orElseThrow(() -> EntiteNonTrouveeException.de("Réception", numero));

        reception.validerEtape2(acteur.getMatricule().getValeur(), acteur.getRole());

        TypeMouvement typeMouvement = reception.getSource() == SourceReception.PRODUCTION
            ? TypeMouvement.ENTREE_RECEPTION_PRODUCTION
            : TypeMouvement.ENTREE_RECEPTION_FOURNISSEUR;

        String entrepot = reception.getCodeEntrepotCible();
        BonCommande bonCommande = chargerBonCommandeSiFournisseur(reception);

        for (LigneReception ligne : reception.getLignes()) {
            if (ligne.getQuantiteRecue() <= 0) continue;

            long quantiteBase = convertirEnBase(ligne.getCodeProduit(), ligne.getSku(), ligne.getQuantiteRecue());

            NiveauStock niveau = niveauStockRepository.trouver(ligne.getCodeProduit(), entrepot)
                .orElseGet(() -> NiveauStock.creer(ligne.getCodeProduit(), entrepot, TypeEntrepot.CENTRAL));

            MouvementStock mouvement = GestionDeStock.appliquerEntree(
                niveau, typeMouvement, genererReferenceMouvement(), ligne.getSku(), quantiteBase,
                acteur.getMatricule().getValeur(), reception.getNumero(),
                "Réception " + reception.getSource().getLibelle());

            niveauStockRepository.sauvegarder(niveau);
            mouvementRepository.enregistrer(mouvement);

            if (bonCommande != null && ligne.getIdLigneBC() != null) {
                bonCommande.enregistrerReceptionLigne(ligne.getIdLigneBC(), ligne.getQuantiteRecue());
            }
        }

        if (bonCommande != null) {
            bonCommandeRepository.sauvegarder(bonCommande);
        }

        Reception sauvegarde = receptionRepository.sauvegarder(reception);
        reception.getDomainEvents().forEach(eventPublisher::publish);
        reception.clearDomainEvents();

        log.info("Réception {} validée (étape 2) — stock central mis à jour", numero);
        return receptionMapper.versResponse(sauvegarde);
    }

    private BonCommande chargerBonCommandeSiFournisseur(Reception reception) {
        if (reception.getSource() != SourceReception.FOURNISSEUR || reception.getNumeroBonCommande() == null) {
            return null;
        }
        return bonCommandeRepository.trouverParNumero(reception.getNumeroBonCommande()).orElse(null);
    }

    /** Convertit une quantité exprimée dans le conditionnement {@code sku} en unités de base. */
    private long convertirEnBase(String codeProduit, String sku, long quantite) {
        if (sku == null || sku.isBlank()) return quantite;
        return produitRepository.trouverParCode(CodeProduit.de(codeProduit))
            .flatMap(p -> p.trouverParSku(sku))
            .map(c -> quantite * c.getFacteurConversionVersBase())
            .orElse(quantite);
    }

    private String genererReferenceMouvement() {
        long sequence = mouvementRepository.compter() + 1;
        return "MVT-" + FMT.format(LocalDate.now()) + "-" + String.format("%05d", sequence);
    }
}
