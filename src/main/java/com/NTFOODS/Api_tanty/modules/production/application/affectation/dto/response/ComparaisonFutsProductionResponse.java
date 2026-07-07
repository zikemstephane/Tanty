package com.NTFOODS.Api_tanty.modules.production.application.affectation.dto.response;

import java.time.LocalDate;

/**
 * Comparaison entre les fûts réalisés par l'Agent Doseur et la production aval (conditionnement).
 */
public record ComparaisonFutsProductionResponse(
    String referencePPH,
    LocalDate date,
    int nbFutsNets,
    double poidsTotalPoudreKg,
    double productionReelleKg,
    double ecartKg,
    double ecartPourcentage,
    String uniteProduction
) {}
