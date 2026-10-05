package com.NTFOODS.Api_tanty.modules.production.application.dosage.dto.response;

import java.util.List;

/**
 * Prédiction de la quantité de produit fini attendue à partir du nombre de fûts nets produits.
 */
public record PredictionProduitFiniResponse(
    int nbFutsNets,
    double poidsTotalPoudreKg,
    List<LignePrediction> predictions
) {
    public record LignePrediction(
        String codeProduit,
        String designation,
        String composition,
        int quantiteUnitesBase,
        int quantiteCartons,
        double poidsUnitaireGrammes,
        int nombreUnitesParCarton
    ) {}
}
