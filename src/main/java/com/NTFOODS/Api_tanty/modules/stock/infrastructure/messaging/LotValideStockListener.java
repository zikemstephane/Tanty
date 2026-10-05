package com.NTFOODS.Api_tanty.modules.stock.infrastructure.messaging;

import com.NTFOODS.Api_tanty.modules.production.domain.lot.events.LotValideParStockEvent;
import com.NTFOODS.Api_tanty.modules.stock.domain.catalogue.enums.NiveauConditionnement;
import com.NTFOODS.Api_tanty.modules.stock.domain.catalogue.model.Conditionnement;
import com.NTFOODS.Api_tanty.modules.stock.domain.catalogue.model.Produit;
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
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
public class LotValideStockListener {

    private static final Logger log = LoggerFactory.getLogger(LotValideStockListener.class);

    private final EntrepotRepository entrepotRepository;
    private final NiveauStockRepository niveauStockRepository;
    private final MouvementStockRepository mouvementStockRepository;
    private final ProduitRepository produitRepository;

    public LotValideStockListener(EntrepotRepository entrepotRepository,
                                  NiveauStockRepository niveauStockRepository,
                                  MouvementStockRepository mouvementStockRepository,
                                  ProduitRepository produitRepository) {
        this.entrepotRepository = entrepotRepository;
        this.niveauStockRepository = niveauStockRepository;
        this.mouvementStockRepository = mouvementStockRepository;
        this.produitRepository = produitRepository;
    }

    @EventListener
    @Transactional
    public void surLotValideParStock(LotValideParStockEvent event) {
        log.info("[LOT-STOCK] Validation lot {} — entrée en stock central: {} kg de {}",
            event.getNumeroLot(), event.getDeclaredQuantityKg(), event.getProductSku());

        Entrepot central = entrepotRepository.trouverUniqueParType(TypeEntrepot.CENTRAL)
            .orElseThrow(() -> new IllegalStateException("Aucun entrepôt central trouvé"));

        long quantiteEnBase = convertirKgEnUnitesBase(event.getProductSku(), event.getDeclaredQuantityKg());
        String skuBase = recupererSkuBase(event.getProductSku());

        NiveauStock niveau = niveauStockRepository.trouver(event.getProductSku(), central.getCode())
            .orElseGet(() -> NiveauStock.creer(event.getProductSku(), central.getCode(), TypeEntrepot.CENTRAL));

        niveau.entrer(quantiteEnBase);
        niveauStockRepository.sauvegarder(niveau);

        MouvementStock mouvement = MouvementStock.enregistrer(
            "MV-LOT-" + event.getNumeroLot(),
            TypeMouvement.ENTREE_RECEPTION_PRODUCTION,
            event.getProductSku(),
            skuBase,
            quantiteEnBase,
            null,
            central.getCode(),
            event.getMatriculeGestionnaire(),
            event.getNumeroLot(),
            "Validation lot production — " + event.getDeclaredQuantityKg() + " kg"
        );
        mouvementStockRepository.enregistrer(mouvement);

        log.info("[LOT-STOCK] Stock central mis à jour: {} +{} unités de base (lot {})",
            central.getCode(), quantiteEnBase, event.getNumeroLot());
    }

    /**
     * Convertit le poids déclaré en unités de base.
     *
     * <p>Les trois replis qui précédaient — produit inconnu, unité de base absente,
     * poids unitaire nul — retournaient tous le poids arrondi, c'est-à-dire
     * comptaient un kilogramme pour une unité. Pour un sachet de 250 g, le stock
     * central était minoré de trois quarts sans que rien ne le signale hors des
     * journaux. Un lot qui ne peut pas être converti n'entre plus en stock.</p>
     */
    private long convertirKgEnUnitesBase(String codeProduit, double quantiteKg) {
        Conditionnement base = exigerUniteBase(codeProduit);
        if (quantiteKg <= 0) {
            throw new IllegalArgumentException("Le lot de " + codeProduit
                + " est déclaré sans poids : la quantité entrant en stock central serait arbitraire");
        }
        if (base.getPoidsUnitaireGrammes() <= 0) {
            throw new IllegalArgumentException("Le poids unitaire de " + base.getSku()
                + " n'est pas renseigné au catalogue : un lot déclaré au poids ne peut pas être "
                + "converti en unités de base");
        }
        return Math.round((quantiteKg * 1000.0) / base.getPoidsUnitaireGrammes());
    }

    /**
     * SKU de l'unité de base, sous lequel le mouvement est tracé.
     *
     * <p>Le repli {@code <code>-BASE} fabriquait un SKU absent du catalogue —
     * « BASE » n'est pas un niveau — et l'inscrivait dans les mouvements de stock,
     * où plus rien ne permettait ensuite de le rattacher à un conditionnement.</p>
     */
    private String recupererSkuBase(String codeProduit) {
        return exigerUniteBase(codeProduit).getSku();
    }

    private Conditionnement exigerUniteBase(String codeProduit) {
        Produit produit = produitRepository.trouverParCode(CodeProduit.de(codeProduit))
            .orElseThrow(() -> new IllegalArgumentException(
                "Produit inconnu au catalogue : " + codeProduit
                    + " — le lot ne peut pas entrer en stock central"));
        try {
            return produit.trouverParNiveau(NiveauConditionnement.UNITE_BASE);
        } catch (IllegalArgumentException absent) {
            throw new IllegalArgumentException("Le produit " + codeProduit
                + " n'a pas d'unité de base au catalogue : son stock ne peut pas être tenu");
        }
    }
}
