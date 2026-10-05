package com.NTFOODS.Api_tanty.modules.dg.application.rapport;

public record ZonePerformanceResponse(
    String secteur,
    double caMoisCourantFCFA,
    long nombreClients,
    long nombreVentesMoisCourant
) {}
