package com.NTFOODS.Api_tanty.modules.stock.infrastructure.messaging;

import com.NTFOODS.Api_tanty.modules.production.domain.lot.events.LotValideParStockEvent;
import com.NTFOODS.Api_tanty.modules.production.domain.pph.aggregate.PlanProductionHebdomadaire;
import com.NTFOODS.Api_tanty.modules.production.domain.pph.repository.PPHRepository;
import com.NTFOODS.Api_tanty.modules.stock.domain.catalogue.model.Conditionnement;
import com.NTFOODS.Api_tanty.modules.stock.domain.catalogue.model.Produit;
import com.NTFOODS.Api_tanty.modules.stock.domain.catalogue.repository.ProduitRepository;
import com.NTFOODS.Api_tanty.modules.stock.domain.catalogue.valueobject.CodeProduit;
import com.NTFOODS.Api_tanty.modules.stock.domain.commande.model.BonCommande;
import com.NTFOODS.Api_tanty.modules.stock.domain.commande.repository.BonCommandeRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

/**
 * Lorsqu'un lot de production fini est validé par le stock, le taux de
 * réalisation du bon de commande (référencé par le PPH) est mis à jour
 * avec la quantité effectivement produite.
 */
@Component
public class BonCommandeRealisationListener {

    private static final Logger log = LoggerFactory.getLogger(BonCommandeRealisationListener.class);

    private final BonCommandeRepository bonCommandeRepository;
    private final PPHRepository pphRepository;
    private final ProduitRepository produitRepository;

    public BonCommandeRealisationListener(BonCommandeRepository bonCommandeRepository,
                                        PPHRepository pphRepository,
                                        ProduitRepository produitRepository) {
        this.bonCommandeRepository = bonCommandeRepository;
        this.pphRepository = pphRepository;
        this.produitRepository = produitRepository;
    }

    @EventListener
    @Transactional
    public void surLotValideParStock(LotValideParStockEvent event) {
        String referencePPH = event.getReferencePPH();
        Optional<PlanProductionHebdomadaire> pphOpt = pphRepository.trouverParReference(referencePPH);
        if (pphOpt.isEmpty()) {
            log.warn("[BC-REALISATION] PPH {} introuvable pour le lot {}", referencePPH, event.getNumeroLot());
            return;
        }
        String referenceBC = pphOpt.get().getReferenceBC();
        if (referenceBC == null || referenceBC.isBlank()) {
            log.debug("[BC-REALISATION] PPH {} non lié à un bon de commande", referencePPH);
            return;
        }

        Optional<BonCommande> bcOpt = bonCommandeRepository.trouverParNumero(referenceBC);
        if (bcOpt.isEmpty()) {
            log.warn("[BC-REALISATION] BC {} introuvable (référencé par PPH {})", referenceBC, referencePPH);
            return;
        }
        BonCommande bc = bcOpt.get();

        long quantiteEnUnitesCommande = convertirKgEnUnitesCommande(
            event.getProductSku(), event.getDeclaredQuantityKg(), bc);
        if (quantiteEnUnitesCommande <= 0) {
            log.warn("[BC-REALISATION] Conversion impossible pour {} / {} kg — taux non mis à jour",
                event.getProductSku(), event.getDeclaredQuantityKg());
            return;
        }

        try {
            bc.enregistrerProduction(event.getProductSku(), quantiteEnUnitesCommande);
            bonCommandeRepository.sauvegarder(bc);
            log.info("[BC-REALISATION] BC {} / {} : +{} unités → taux {}%",
                referenceBC, event.getProductSku(), quantiteEnUnitesCommande, bc.tauxRealisation());
        } catch (IllegalArgumentException e) {
            log.warn("[BC-REALISATION] Produit {} absent du BC {} : {}",
                event.getProductSku(), referenceBC, e.getMessage());
        }
    }

    private long convertirKgEnUnitesCommande(String codeProduit, double quantiteKg, BonCommande bc) {
        Optional<Produit> produitOpt = produitRepository.trouverParCode(CodeProduit.de(codeProduit));
        if (produitOpt.isEmpty()) {
            return fallbackEnUnitesBase(codeProduit, quantiteKg);
        }
        Produit produit = produitOpt.get();

        String skuCommande = bc.getLignes().stream()
            .filter(l -> l.getCodeProduit().equalsIgnoreCase(codeProduit))
            .map(l -> l.getSku())
            .findFirst()
            .orElse(null);

        Conditionnement conditionnement = null;
        if (skuCommande != null && !skuCommande.isBlank()) {
            conditionnement = produit.trouverParSku(skuCommande).orElse(null);
        }
        if (conditionnement == null) {
            try {
                conditionnement = produit.trouverParNiveau(com.NTFOODS.Api_tanty.modules.stock.domain.catalogue.enums.NiveauConditionnement.UNITE_BASE);
            } catch (IllegalArgumentException ignored) {
                return fallbackEnUnitesBase(codeProduit, quantiteKg);
            }
        }

        double poidsUnitaireGrammes = conditionnement.getPoidsUnitaireGrammes();
        if (poidsUnitaireGrammes <= 0) {
            return fallbackEnUnitesBase(codeProduit, quantiteKg);
        }
        return Math.max(1, Math.round((quantiteKg * 1000.0) / poidsUnitaireGrammes));
    }

    private long fallbackEnUnitesBase(String codeProduit, double quantiteKg) {
        Optional<Produit> produitOpt = produitRepository.trouverParCode(CodeProduit.de(codeProduit));
        if (produitOpt.isEmpty()) {
            return Math.max(1, Math.round(quantiteKg));
        }
        Produit produit = produitOpt.get();
        try {
            Conditionnement base = produit.trouverParNiveau(com.NTFOODS.Api_tanty.modules.stock.domain.catalogue.enums.NiveauConditionnement.UNITE_BASE);
            if (base.getPoidsUnitaireGrammes() > 0) {
                return Math.max(1, Math.round((quantiteKg * 1000.0) / base.getPoidsUnitaireGrammes()));
            }
        } catch (IllegalArgumentException ignored) {
            // pas d'unité de base
        }
        return Math.max(1, Math.round(quantiteKg));
    }
}
