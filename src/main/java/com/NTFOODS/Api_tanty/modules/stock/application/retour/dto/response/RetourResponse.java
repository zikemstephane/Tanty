package com.NTFOODS.Api_tanty.modules.stock.application.retour.dto.response;

import java.time.LocalDate;
import java.util.List;

public record RetourResponse(
    Long id,
    String numero,
    String matriculeCommercial,
    String matriculeGestionnaire,
    LocalDate dateRetour,
    List<LigneResponse> lignes
) {
    public record LigneResponse(String id, String codeProduit, String sku, long quantite, String motif) {}
}
