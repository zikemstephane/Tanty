package com.NTFOODS.Api_tanty.modules.production.application.lot.dto.response;

import java.time.LocalDate;

public record LotResponse(
    Long id,
    String numeroLot,
    String referencePPH,
    String codeProduit,
    String designationProduit,
    double quantiteKg,
    int nbCartons,
    LocalDate dateProduction,
    LocalDate dlc,
    String matriculeChefProduction,
    String statut,
    String matriculeGestionnaireValidation,
    String motifRejet
) {}
