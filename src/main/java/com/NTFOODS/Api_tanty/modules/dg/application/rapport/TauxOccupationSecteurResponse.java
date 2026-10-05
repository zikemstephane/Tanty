package com.NTFOODS.Api_tanty.modules.dg.application.rapport;

public record TauxOccupationSecteurResponse(
    String secteur,
    long boutiquesRecensees,
    long clientsLivres,
    double tauxOccupationPourcent
) {}
