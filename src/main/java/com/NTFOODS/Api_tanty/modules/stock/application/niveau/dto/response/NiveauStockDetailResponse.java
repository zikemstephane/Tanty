package com.NTFOODS.Api_tanty.modules.stock.application.niveau.dto.response;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * Réponse enrichie — niveau de stock avec produit, entrepôt, seuil, niveau
 * d'alerte et valorisation. Destinée aux écrans « Inventaire / Alertes » du web.
 */
public record NiveauStockDetailResponse(
    Long id,
    Long produitId,
    String codeProduit,
    String sku,
    String designation,
    String marque,
    String gamme,
    String categorie,
    Long entrepotId,
    String codeEntrepot,
    String libelleEntrepot,
    String typeEntrepot,
    long quantite,
    long seuilReapprovisionnement,
    long stockSecurite,
    String niveauAlerte,
    BigDecimal valeurStock,
    LocalDateTime dateModification
) {}
