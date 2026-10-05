package com.NTFOODS.Api_tanty.modules.dg.application.rapport;

import java.time.LocalDate;

public record RapportComparatifResponse(
    PeriodeComparaison periode,
    Tranche actuelle,
    Tranche precedente,
    double variationAbsolueFCFA,
    double variationPourcent
) {
    public record Tranche(LocalDate debut, LocalDate fin, double caFCFA, long nombreVentes) {}
}
