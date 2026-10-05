package com.NTFOODS.Api_tanty.modules.production.application.affectation.dto.response;

/**
 * Détails de réalisation par poste dans la réponse.
 */
public record DetailsRealisationPosteResponse(
    Integer nbRegimesEpluches,
    Integer nbRegimesDecoupes,
    Integer nbSceauxEtiquetes,
    Integer nbEtiquettesDatees,
    Integer nbSachetsDates,
    Integer nbCartonsProduits,
    Double kgChipsProduits
) {}
