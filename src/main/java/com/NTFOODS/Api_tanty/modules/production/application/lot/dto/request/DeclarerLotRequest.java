package com.NTFOODS.Api_tanty.modules.production.application.lot.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Positive;

import java.time.LocalDate;

public record DeclarerLotRequest(
    String referencePPH,
    @NotBlank String productSku,
    @NotBlank String productName,
    String productUnit,
    @Positive double declaredQuantityKg,
    int equivalentUnits,
    LocalDate dateProduction,
    LocalDate dlc,
    String conditioningType,
    Integer conditioningQty,
    String notes,
    String declaredBy
) {}
