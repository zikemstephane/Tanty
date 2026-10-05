package com.NTFOODS.Api_tanty.modules.production.application.prediction.dto.request;

import jakarta.validation.constraints.Min;

/** Nombre de fûts à produire, pour calculer la prédiction d'un plan. */
public record CalculerPredictionRequest(
    @Min(value = 1, message = "Le nombre de fûts doit être supérieur à 0") int nbFuts
) {}
