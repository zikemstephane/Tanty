package com.NTFOODS.Api_tanty.modules.stock.application.transfert.dto.response;

import java.time.LocalDateTime;
import java.util.List;

public record TransfertResponse(
    Long id,
    String numero,
    String codeEntrepotSource,
    String codeEntrepotCible,
    String statut,
    String statutLibelle,
    boolean declencheParSeuil,
    String matriculeGestionnaire,
    LocalDateTime dateExecution,
    List<LigneResponse> lignes
) {
    public record LigneResponse(String id, String codeProduit, String sku, long quantite) {}
}
