package com.NTFOODS.Api_tanty.modules.production.application.lot.dto.request;

import jakarta.validation.constraints.NotBlank;

import java.time.LocalDate;

public record DeclarerLotRequest(
    @NotBlank String referencePPH,
    @NotBlank String codeProduit,
    @NotBlank String designationProduit,
    double quantiteKg,
    int nbCartons,
    LocalDate dateProduction,
    LocalDate dlc
) {}
