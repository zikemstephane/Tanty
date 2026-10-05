package com.NTFOODS.Api_tanty.modules.stock.application.entrepot.dto.response;

public record EntrepotResponse(
    Long id,
    String code,
    String libelle,
    String type,
    String typeLibelle,
    String matriculeProprietaire,
    boolean actif
) {}
