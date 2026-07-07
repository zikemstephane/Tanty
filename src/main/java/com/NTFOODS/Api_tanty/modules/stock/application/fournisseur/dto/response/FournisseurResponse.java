package com.NTFOODS.Api_tanty.modules.stock.application.fournisseur.dto.response;

public record FournisseurResponse(
    Long id,
    String code,
    String raisonSociale,
    String telephone,
    String email,
    String adresse,
    boolean actif
) {}
