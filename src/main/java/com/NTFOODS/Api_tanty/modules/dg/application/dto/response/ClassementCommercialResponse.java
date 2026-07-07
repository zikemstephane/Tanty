package com.NTFOODS.Api_tanty.modules.dg.application.dto.response;

public record ClassementCommercialResponse(
    int rang,
    String matricule,
    String nomComplet,
    double caRealise,
    long nbVentes
) {}
