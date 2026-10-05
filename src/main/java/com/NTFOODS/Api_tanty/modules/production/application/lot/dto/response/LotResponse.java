package com.NTFOODS.Api_tanty.modules.production.application.lot.dto.response;

import java.time.LocalDate;

public record LotResponse(
    Long id,
    String numeroLot,
    String referencePPH,
    String productSku,
    String productName,
    String productUnit,
    double declaredQuantityKg,
    int equivalentUnits,
    LocalDate dateProduction,
    LocalDate dlc,
    String matriculeChefProduction,
    String declaredBy,
    String statut,
    String matriculeGestionnaireValidation,
    String motifRejet,
    String conditioningType,
    Integer conditioningQty,
    String notes,
    LocalDate createdAt,
    LocalDate validatedAt
) {}
