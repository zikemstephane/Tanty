package com.NTFOODS.Api_tanty.modules.production.domain.prediction.aggregate;

import com.NTFOODS.Api_tanty.modules.production.domain.prediction.service.RepartitionPoudre;

import java.time.LocalDateTime;
import java.util.List;

/**
 * Prédiction de la production d'un plan PPH à partir du nombre de fûts : la poudre est répartie
 * entre les produits du bon de commande. C'est la prédiction de référence du plan, lisible par les autres rôles.
 */
public record PredictionPPH(
    String referencePPH,
    int nbFuts,
    double poudreKg,
    LocalDateTime dateCalcul,
    String matricule,
    List<RepartitionPoudre.Ligne> lignes,
    List<RepartitionPoudre.Exclusion> exclusions
) {}
