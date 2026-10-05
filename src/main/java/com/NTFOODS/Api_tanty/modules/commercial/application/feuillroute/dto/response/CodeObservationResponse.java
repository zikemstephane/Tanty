package com.NTFOODS.Api_tanty.modules.commercial.application.feuillroute.dto.response;

public record CodeObservationResponse(
    Long id,
    String code,
    String libelle,
    boolean requireQuantity,
    boolean actif,
    int ordre
) {}
