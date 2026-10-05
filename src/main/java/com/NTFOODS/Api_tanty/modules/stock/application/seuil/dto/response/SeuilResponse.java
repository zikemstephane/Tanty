package com.NTFOODS.Api_tanty.modules.stock.application.seuil.dto.response;

public record SeuilResponse(
    Long id,
    String codeProduit,
    String typeEntrepot,
    long seuilReapprovisionnement,
    boolean actif
) {}
