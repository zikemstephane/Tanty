package com.NTFOODS.Api_tanty.modules.production.application.affectation.dto.request;

import jakarta.validation.constraints.Min;

/**
 * Détails optionnels de saisie d'une réalisation par poste.
 */
public record DetailsRealisationPosteRequest(
    @Min(0) Integer nbRegimesEpluches,
    @Min(0) Integer nbRegimesDecoupes,
    @Min(0) Integer nbSceauxEtiquetes,
    @Min(0) Integer nbEtiquettesDatees,
    @Min(0) Integer nbSachetsDates,
    @Min(0) Integer nbCartonsProduits,
    @Min(0) Double kgChipsProduits
) {}
