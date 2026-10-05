package com.NTFOODS.Api_tanty.modules.stock.application.niveau.dto.response;

import java.util.List;

/**
 * État de stock d'un produit dans un entrepôt, avec décomposition de la
 * quantité (exprimée en unités de base) dans tous les conditionnements.
 */
public record NiveauStockResponse(
    String codeProduit,
    String designation,
    String marque,
    String gamme,
    String codeEntrepot,
    String typeEntrepot,
    long quantiteEnBase,
    List<LigneDecomposition> decomposition
) {
    /** Ex : 2 palettes, 5 cartons, 3 boîtes, 7 sachets. */
    public record LigneDecomposition(String niveau, String libelle, long quantite) {}
}
