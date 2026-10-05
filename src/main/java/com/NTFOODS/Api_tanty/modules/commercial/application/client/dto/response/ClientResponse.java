package com.NTFOODS.Api_tanty.modules.commercial.application.client.dto.response;

public record ClientResponse(
    Long id,
    String codeClient,
    String nom,
    String telephone,
    String adresse,
    String localite,
    String type,
    String matriculeCommercialReferent,
    boolean actif,
    Double latitude,
    Double longitude,
    double dette
) {}
