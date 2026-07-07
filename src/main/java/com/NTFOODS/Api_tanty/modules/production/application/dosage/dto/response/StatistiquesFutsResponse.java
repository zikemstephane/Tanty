package com.NTFOODS.Api_tanty.modules.production.application.dosage.dto.response;

import java.time.LocalDate;

/**
 * Statistiques de fûts produits sur une période, avec comparaison N-1.
 */
public record StatistiquesFutsResponse(
    LocalDate debut,
    LocalDate fin,
    String periodeLibelle,
    int nbFutsProduits,
    int nbFutsCasses,
    int nbFutsNets,
    double poudreMaisKg,
    double poudreSojaKg,
    double poudreArachideKg,
    ComparaisonPeriode comparaison,
    int nbSessions
) {
    public record ComparaisonPeriode(
        LocalDate debutPeriodePrecedente,
        LocalDate finPeriodePrecedente,
        int nbFutsNetsPeriodePrecedente,
        double variationPourcentage
    ) {}
}
