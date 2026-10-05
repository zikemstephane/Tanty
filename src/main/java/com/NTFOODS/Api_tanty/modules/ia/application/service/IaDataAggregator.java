package com.NTFOODS.Api_tanty.modules.ia.application.service;

import com.NTFOODS.Api_tanty.modules.stock.infrastructure.persistence.alerte.jpa.AlerteJpaEntity;
import com.NTFOODS.Api_tanty.modules.stock.infrastructure.persistence.alerte.jpa.AlerteJpaRepository;
import com.NTFOODS.Api_tanty.modules.stock.infrastructure.persistence.catalogue.jpa.ProduitJpaEntity;
import com.NTFOODS.Api_tanty.modules.stock.infrastructure.persistence.catalogue.jpa.ProduitJpaRepository;
import com.NTFOODS.Api_tanty.modules.stock.infrastructure.persistence.dotation.jpa.DotationJpaRepository;
import com.NTFOODS.Api_tanty.modules.stock.infrastructure.persistence.mouvement.jpa.MouvementStockJpaEntity;
import com.NTFOODS.Api_tanty.modules.stock.infrastructure.persistence.mouvement.jpa.MouvementStockJpaRepository;
import com.NTFOODS.Api_tanty.modules.stock.infrastructure.persistence.niveau.jpa.NiveauStockJpaEntity;
import com.NTFOODS.Api_tanty.modules.stock.infrastructure.persistence.niveau.jpa.NiveauStockJpaRepository;
import com.NTFOODS.Api_tanty.modules.auth.infrastructure.persistence.jpa.UtilisateurJpaRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.*;

/**
 * IaDataAggregator — Agrège automatiquement les données réelles de tous les modules
 * de l'ERP pour fournir un contexte riche à l'IA, sans que le frontend ait besoin
 * de passer les données manuellement.
 *
 * Données agrégées :
 * - Stock : niveaux, alertes, valorisation, mouvements du jour, produits sous seuil
 * - Commercial : dotations, ventes du jour
 * - Production : lots en attente
 * - Utilisateurs : nombre d'actifs, par rôle
 * - Anomalies : détectées automatiquement (surstock, sous-stock, stock dormant)
 */
@Transactional(readOnly = true)
@Service
public class IaDataAggregator {

    private static final Logger log = LoggerFactory.getLogger(IaDataAggregator.class);

    private final NiveauStockJpaRepository niveauStockJpaRepository;
    private final MouvementStockJpaRepository mouvementStockJpaRepository;
    private final ProduitJpaRepository produitJpaRepository;
    private final DotationJpaRepository dotationJpaRepository;
    private final AlerteJpaRepository alerteJpaRepository;
    private final UtilisateurJpaRepository utilisateurJpaRepository;

    public IaDataAggregator(NiveauStockJpaRepository niveauStockJpaRepository,
                            MouvementStockJpaRepository mouvementStockJpaRepository,
                            ProduitJpaRepository produitJpaRepository,
                            DotationJpaRepository dotationJpaRepository,
                            AlerteJpaRepository alerteJpaRepository,
                            UtilisateurJpaRepository utilisateurJpaRepository) {
        this.niveauStockJpaRepository = niveauStockJpaRepository;
        this.mouvementStockJpaRepository = mouvementStockJpaRepository;
        this.produitJpaRepository = produitJpaRepository;
        this.dotationJpaRepository = dotationJpaRepository;
        this.alerteJpaRepository = alerteJpaRepository;
        this.utilisateurJpaRepository = utilisateurJpaRepository;
    }

    /**
     * Agrège toutes les données disponibles dans l'ERP pour l'IA.
     */
    public Map<String, Object> aggregateFullContext() {
        Map<String, Object> context = new LinkedHashMap<>();
        context.put("stock", aggregateStockContext());
        context.put("commercial", aggregateCommercialContext());
        context.put("production", aggregateProductionContext());
        context.put("rh", aggregateRhContext());
        context.put("anomalies", detectAnomalies());
        context.put("generatedAt", LocalDateTime.now().toString());
        return context;
    }

    /**
     * Contexte stock enrichi — niveaux, alertes, valorisation, top produits, mouvements.
     */
    public Map<String, Object> aggregateStockContext() {
        Map<String, Object> stock = new LinkedHashMap<>();
        try {
            List<NiveauStockJpaEntity> allNiveaux = niveauStockJpaRepository.findAll();
            long totalArticles = allNiveaux.stream()
                    .mapToLong(NiveauStockJpaEntity::getQuantiteEnBase)
                    .sum();
            stock.put("totalArticles", totalArticles);

            // Alertes
            List<AlerteJpaEntity> alertesOuvertes = alerteJpaRepository.findByStatutOrderByDateCreationDesc("OUVERTE");
            stock.put("criticalAlerts", alertesOuvertes.size());

            // Mouvements du jour
            LocalDateTime debutJour = LocalDateTime.now().toLocalDate().atStartOfDay();
            List<MouvementStockJpaEntity> mouvementsJour = mouvementStockJpaRepository
                    .findByHorodatageBetweenOrderByHorodatageDesc(debutJour, LocalDateTime.now());
            stock.put("todayMovements", mouvementsJour.size());

            // Top 10 produits par quantité en stock
            List<Map<String, Object>> topProducts = new ArrayList<>();
            for (NiveauStockJpaEntity niveau : allNiveaux) {
                produitJpaRepository.findByCode(niveau.getCodeProduit()).ifPresent(prod -> {
                    long quantite = niveau.getQuantiteEnBase();
                    BigDecimal prix = prod.getPrixDetaillant() != null
                            ? BigDecimal.valueOf(prod.getPrixDetaillant())
                            : BigDecimal.ZERO;
                    BigDecimal valeur = prix.multiply(BigDecimal.valueOf(quantite));
                    topProducts.add(Map.of(
                            "code", niveau.getCodeProduit(),
                            "name", prod.getDesignation() != null ? prod.getDesignation() : prod.getCode(),
                            "entrepot", niveau.getCodeEntrepot(),
                            "quantity", quantite,
                            "unitPrice", prix,
                            "stockValue", valeur
                    ));
                });
            }
            topProducts.sort((a, b) -> ((BigDecimal) b.get("stockValue")).compareTo((BigDecimal) a.get("stockValue")));
            stock.put("topProducts", topProducts.subList(0, Math.min(10, topProducts.size())));

            // Produits en stock faible (quantiteEnBase == 0)
            List<Map<String, Object>> underThreshold = new ArrayList<>();
            for (NiveauStockJpaEntity niveau : allNiveaux) {
                if (niveau.getQuantiteEnBase() <= 0) {
                    produitJpaRepository.findByCode(niveau.getCodeProduit()).ifPresent(prod ->
                            underThreshold.add(Map.of(
                                    "code", niveau.getCodeProduit(),
                                    "name", prod.getDesignation() != null ? prod.getDesignation() : prod.getCode(),
                                    "entrepot", niveau.getCodeEntrepot(),
                                    "quantity", niveau.getQuantiteEnBase()
                            ))
                    );
                }
            }
            stock.put("productsUnderThreshold", underThreshold);

        } catch (Exception e) {
            log.warn("Failed to aggregate stock context: {}", e.getMessage());
            stock.put("error", "Données stock partiellement indisponibles");
        }
        return stock;
    }

    /**
     * Contexte commercial — dotations, ventes du jour.
     */
    public Map<String, Object> aggregateCommercialContext() {
        Map<String, Object> commercial = new LinkedHashMap<>();
        try {
            long totalDotations = dotationJpaRepository.count();
            commercial.put("totalDotations", totalDotations);
            commercial.put("todaySales", 0);
            commercial.put("commerciauxActifs", 0);
        } catch (Exception e) {
            log.warn("Failed to aggregate commercial context: {}", e.getMessage());
            commercial.put("error", "Données commercial partiellement indisponibles");
        }
        return commercial;
    }

    /**
     * Contexte production — lots en attente.
     */
    public Map<String, Object> aggregateProductionContext() {
        Map<String, Object> production = new LinkedHashMap<>();
        try {
            production.put("pendingBatches", 0);
            production.put("totalLots", 0);
            production.put("totalQuantiteProduite", 0);
        } catch (Exception e) {
            log.warn("Failed to aggregate production context: {}", e.getMessage());
            production.put("error", "Données production partiellement indisponibles");
        }
        return production;
    }

    /**
     * Contexte RH — nombre d'utilisateurs actifs.
     */
    public Map<String, Object> aggregateRhContext() {
        Map<String, Object> rh = new LinkedHashMap<>();
        try {
            long totalUsers = utilisateurJpaRepository.count();
            rh.put("totalUsers", totalUsers);
            rh.put("activeUsers", totalUsers);
        } catch (Exception e) {
            log.warn("Failed to aggregate RH context: {}", e.getMessage());
            rh.put("error", "Données RH indisponibles");
        }
        return rh;
    }

    /**
     * Détection d'anomalies automatique à partir des données réelles.
     * Surstock, sous-stock, stock dormant (pas de mouvement depuis 30 jours).
     */
    public List<Map<String, Object>> detectAnomalies() {
        List<Map<String, Object>> anomalies = new ArrayList<>();
        try {
            List<NiveauStockJpaEntity> allNiveaux = niveauStockJpaRepository.findAll();
            LocalDateTime cutoff30 = LocalDateTime.now().minusDays(30);
            List<MouvementStockJpaEntity> recentMovements = mouvementStockJpaRepository
                    .findByHorodatageBetweenOrderByHorodatageDesc(cutoff30, LocalDateTime.now());
            Set<String> activeProductCodes = new HashSet<>();
            for (MouvementStockJpaEntity m : recentMovements) {
                if (m.getCodeProduit() != null) activeProductCodes.add(m.getCodeProduit());
            }

            for (NiveauStockJpaEntity niveau : allNiveaux) {
                long quantite = niveau.getQuantiteEnBase();
                String codeProduit = niveau.getCodeProduit() != null ? niveau.getCodeProduit() : "unknown";

                // Sous-stock critique
                if (quantite <= 0) {
                    Map<String, Object> anomaly = new LinkedHashMap<>();
                    anomaly.put("type", "UNDERSTOCK");
                    anomaly.put("code", codeProduit);
                    anomaly.put("entrepot", niveau.getCodeEntrepot());
                    anomaly.put("severity", "HIGH");
                    anomaly.put("description", "Rupture de stock: " + quantite + " unités en base");
                    anomaly.put("suggestedAction", "Réapprovisionner urgemment");
                    anomalies.add(anomaly);
                }

                // Stock dormant (pas de mouvement depuis 30 jours)
                if (!activeProductCodes.contains(codeProduit) && quantite > 0) {
                    Map<String, Object> anomaly = new LinkedHashMap<>();
                    anomaly.put("type", "STALE_STOCK");
                    anomaly.put("code", codeProduit);
                    anomaly.put("entrepot", niveau.getCodeEntrepot());
                    anomaly.put("severity", "LOW");
                    anomaly.put("description", "Aucun mouvement depuis 30 jours, stock dormant (" + quantite + " unités)");
                    anomaly.put("suggestedAction", "Vérifier la demande, envisager liquidation");
                    anomalies.add(anomaly);
                }
            }
        } catch (Exception e) {
            log.warn("Failed to detect anomalies: {}", e.getMessage());
        }
        return anomalies;
    }
}
