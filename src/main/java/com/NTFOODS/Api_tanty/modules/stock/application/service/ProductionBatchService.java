package com.NTFOODS.Api_tanty.modules.stock.application.service;

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
import com.NTFOODS.Api_tanty.modules.stock.infrastructure.persistence.productionbatch.jpa.ProductionBatchJpaEntity;
import com.NTFOODS.Api_tanty.modules.stock.infrastructure.persistence.productionbatch.repository.ProductionBatchRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Service
@Transactional
public class ProductionBatchService {

    private static final Logger log = LoggerFactory.getLogger(ProductionBatchService.class);

    private final ProductionBatchRepository batchRepository;
    private final EntrepotRepository entrepotRepository;
    private final NiveauStockRepository niveauStockRepository;
    private final MouvementStockRepository mouvementStockRepository;
    private final ProduitRepository produitRepository;

    public ProductionBatchService(ProductionBatchRepository batchRepository,
                                  EntrepotRepository entrepotRepository,
                                  NiveauStockRepository niveauStockRepository,
                                  MouvementStockRepository mouvementStockRepository,
                                  ProduitRepository produitRepository) {
        this.batchRepository = batchRepository;
        this.entrepotRepository = entrepotRepository;
        this.niveauStockRepository = niveauStockRepository;
        this.mouvementStockRepository = mouvementStockRepository;
        this.produitRepository = produitRepository;
    }

    @Transactional(readOnly = true)
    public List<ProductionBatchJpaEntity> getAllBatches() {
        return batchRepository.findAllByOrderByCreatedAtDesc();
    }

    @Transactional(readOnly = true)
    public List<ProductionBatchJpaEntity> getPendingBatches() {
        return batchRepository.findByStatusOrderByCreatedAtDesc("DECLARED_BY_PRODUCTION");
    }

    @Transactional(readOnly = true)
    public ProductionBatchJpaEntity getBatchById(Long id) {
        return batchRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Lot de production introuvable: " + id));
    }

    public ProductionBatchJpaEntity declareBatch(Long productId, String productSku, String productName,
                                                  String productUnit, BigDecimal declaredQuantityKg,
                                                  BigDecimal equivalentUnits, LocalDate productionDate,
                                                  String declaredBy, String declaredByName, String notes,
                                                  String conditioningType, BigDecimal conditioningQty) {
        ProductionBatchJpaEntity batch = new ProductionBatchJpaEntity();
        batch.setProductId(productId);
        batch.setProductSku(productSku);
        batch.setProductName(productName);
        batch.setProductUnit(productUnit);
        batch.setDeclaredQuantityKg(declaredQuantityKg);
        batch.setEquivalentUnits(equivalentUnits);
        batch.setProductionDate(productionDate);
        batch.setBatchDate(productionDate);
        batch.setStatus("DECLARED_BY_PRODUCTION");
        batch.setDeclaredBy(declaredBy);
        batch.setDeclaredByName(declaredByName);
        batch.setCreatedAt(LocalDateTime.now());
        batch.setNotes(notes);
        batch.setConditioningType(conditioningType);
        batch.setConditioningQty(conditioningQty);

        ProductionBatchJpaEntity saved = batchRepository.save(batch);
        log.info("Lot de production déclaré: {} par {}", saved.getId(), declaredBy);
        return saved;
    }

    public ProductionBatchJpaEntity validateBatch(Long id, String validatorMatricule, String validatorName, String notes) {
        ProductionBatchJpaEntity batch = getBatchById(id);
        if (!"DECLARED_BY_PRODUCTION".equals(batch.getStatus())) {
            throw new IllegalStateException("Seul un lot déclaré peut être validé (statut actuel: " + batch.getStatus() + ")");
        }

        batch.setStatus("VALIDATED_BY_STOCK");
        batch.setStockValidator(validatorMatricule);
        batch.setStockValidatorName(validatorName);
        batch.setValidatedAt(LocalDateTime.now());
        if (notes != null && !notes.isBlank()) {
            batch.setNotes((batch.getNotes() != null ? batch.getNotes() + " | " : "") + notes);
        }

        ProductionBatchJpaEntity saved = batchRepository.save(batch);

        // Entrée en stock central
        Entrepot central = entrepotRepository.trouverUniqueParType(TypeEntrepot.CENTRAL)
            .orElseThrow(() -> new IllegalStateException("Aucun entrepôt central trouvé"));

        String codeProduit = batch.getProductSku() != null ? batch.getProductSku() : String.valueOf(batch.getProductId());
        long quantiteEnBase = convertirConditionnementEnBase(codeProduit, batch.getConditioningType(),
            batch.getConditioningQty() != null ? batch.getConditioningQty().longValue() : 0L,
            batch.getDeclaredQuantityKg() != null ? batch.getDeclaredQuantityKg().doubleValue() : 0.0);
        String skuBase = recupererSkuBase(codeProduit);

        NiveauStock niveau = niveauStockRepository.trouver(codeProduit, central.getCode())
            .orElseGet(() -> NiveauStock.creer(codeProduit, central.getCode(), TypeEntrepot.CENTRAL));

        niveau.entrer(quantiteEnBase);
        niveauStockRepository.sauvegarder(niveau);

        MouvementStock mouvement = MouvementStock.enregistrer(
            "MV-PROD-" + batch.getId(),
            TypeMouvement.ENTREE_RECEPTION_PRODUCTION,
            codeProduit,
            skuBase,
            quantiteEnBase,
            null,
            central.getCode(),
            validatorMatricule,
            "LOT-PROD-" + batch.getId(),
            "Validation lot de production " + batch.getId() + " — " + batch.getConditioningQty() + " " + batch.getConditioningType()
        );
        mouvementStockRepository.enregistrer(mouvement);

        log.info("Lot de production {} validé par {} — stock central mis à jour: +{} unités de base",
            id, validatorMatricule, quantiteEnBase);
        return saved;
    }

    public ProductionBatchJpaEntity rejectBatch(Long id, String rejectedByMatricule, String reason) {
        ProductionBatchJpaEntity batch = getBatchById(id);
        if (!"DECLARED_BY_PRODUCTION".equals(batch.getStatus())) {
            throw new IllegalStateException("Seul un lot déclaré peut être rejeté (statut actuel: " + batch.getStatus() + ")");
        }

        batch.setStatus("REJECTED");
        batch.setNotes((batch.getNotes() != null ? batch.getNotes() + " | " : "") + "REJETÉ: " + reason);

        ProductionBatchJpaEntity saved = batchRepository.save(batch);
        log.info("Lot de production {} rejeté par {}", id, rejectedByMatricule);
        return saved;
    }

    /**
     * Convertit la quantité déclarée en unités de base.
     *
     * <p>Le repli qui précédait — retourner la quantité brute quand le
     * conditionnement ne résolvait pas — faisait entrer dix cartons de vingt-cinq
     * en stock central comme dix unités, sans autre trace qu'un avertissement dans
     * les journaux. Une déclaration qui ne peut pas être convertie est désormais
     * refusée : une saisie à reprendre coûte moins cher qu'un stock faux dont
     * personne ne connaît la date d'apparition.</p>
     */
    private long convertirConditionnementEnBase(String codeProduit, String conditioningType,
                                                long conditioningQty, double declaredQuantityKg) {
        Produit produit = produitRepository.trouverParCode(CodeProduit.de(codeProduit))
            .orElseThrow(() -> new IllegalArgumentException(
                "Produit inconnu au catalogue : " + codeProduit
                    + " — le lot ne peut pas entrer en stock central"));

        // Sans conditionnement, le lot n'est déclaré qu'au poids. Le convertir
        // relève de la même exigence : arrondir des kilogrammes en unités, comme
        // on le faisait ici, revient à décider qu'un sachet pèse un kilo.
        if (conditioningType == null || conditioningType.isBlank() || conditioningQty <= 0) {
            return convertirPoidsEnUnitesBase(produit, declaredQuantityKg);
        }
        return exigerConditionnement(produit, conditioningType).versUnitesBase(conditioningQty);
    }

    /**
     * Résout le conditionnement déclaré par son <em>niveau</em>.
     *
     * <p>Le champ porte un niveau — {@code CARTON}, {@code UNITE_BASE} — et jamais
     * un libellé commercial : « Carton de 25 » désigne un carton pour un lecteur
     * humain, mais ne résout aucun conditionnement. La résolution passait
     * auparavant par un SKU reconstruit à la main, ce qui la liait à une
     * convention de nommage plutôt qu'au catalogue.</p>
     */
    private Conditionnement exigerConditionnement(Produit produit, String conditioningType) {
        NiveauConditionnement niveau;
        try {
            niveau = NiveauConditionnement.valueOf(conditioningType.trim().toUpperCase());
        } catch (IllegalArgumentException inconnu) {
            throw new IllegalArgumentException("Conditionnement « " + conditioningType
                + " » inconnu : un niveau est attendu. " + conditionnementsAuCatalogue(produit));
        }
        try {
            return produit.trouverParNiveau(niveau);
        } catch (IllegalArgumentException absent) {
            throw new IllegalArgumentException("Le produit " + produit.getCode().getValeur()
                + " n'existe pas en " + niveau.getLibelle() + ". "
                + conditionnementsAuCatalogue(produit));
        }
    }

    private long convertirPoidsEnUnitesBase(Produit produit, double quantiteKg) {
        Conditionnement base = exigerUniteBase(produit);
        if (quantiteKg <= 0) {
            throw new IllegalArgumentException("Le lot " + produit.getCode().getValeur()
                + " ne porte ni conditionnement ni poids déclaré : la quantité entrant en stock "
                + "central serait arbitraire");
        }
        if (base.getPoidsUnitaireGrammes() <= 0) {
            throw new IllegalArgumentException("Le poids unitaire de " + base.getSku()
                + " n'est pas renseigné au catalogue : un lot déclaré au poids ne peut pas être "
                + "converti en unités de base");
        }
        return Math.round((quantiteKg * 1000.0) / base.getPoidsUnitaireGrammes());
    }

    /**
     * SKU de l'unité de base, sous lequel le mouvement de stock est tracé.
     *
     * <p>Le repli {@code <code>-BASE} qui précédait fabriquait un SKU absent du
     * catalogue — « BASE » n'est pas un niveau — et l'inscrivait dans les
     * mouvements, où plus rien ne permettait ensuite de le rattacher au produit.</p>
     */
    private String recupererSkuBase(String codeProduit) {
        Produit produit = produitRepository.trouverParCode(CodeProduit.de(codeProduit))
            .orElseThrow(() -> new IllegalArgumentException(
                "Produit inconnu au catalogue : " + codeProduit));
        return exigerUniteBase(produit).getSku();
    }

    private Conditionnement exigerUniteBase(Produit produit) {
        try {
            return produit.trouverParNiveau(NiveauConditionnement.UNITE_BASE);
        } catch (IllegalArgumentException absent) {
            throw new IllegalArgumentException("Le produit " + produit.getCode().getValeur()
                + " n'a pas d'unité de base au catalogue : son stock ne peut pas être tenu");
        }
    }

    private String conditionnementsAuCatalogue(Produit produit) {
        if (produit.getConditionnements().isEmpty()) {
            return "Aucun conditionnement n'est déclaré pour ce produit.";
        }
        return "Conditionnements déclarés : " + produit.getConditionnements().stream()
            .map(c -> c.getNiveau().name() + " (" + c.getSku() + ")")
            .collect(java.util.stream.Collectors.joining(", "));
    }
}
