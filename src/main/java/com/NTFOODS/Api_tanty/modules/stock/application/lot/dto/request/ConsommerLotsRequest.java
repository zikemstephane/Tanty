package com.NTFOODS.Api_tanty.modules.stock.application.lot.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Positive;

/**
 * Requête — consommation d'une quantité, prélevée sur les lots au plus proche
 * de la péremption.
 *
 * Le prélèvement porte sur la traçabilité : il dit de quels lots la
 * marchandise est sortie. Il ne fait pas varier le stock — c'est le mouvement
 * de stock qui s'en charge, sur {@code niveaux_stock}.
 */
public record ConsommerLotsRequest(
    @NotBlank(message = "Le SKU est obligatoire") String sku,
    @NotBlank(message = "L'entrepôt est obligatoire") String codeEntrepot,
    @Positive(message = "La quantité consommée doit être strictement positive") long quantite,
    String motif
) {}
