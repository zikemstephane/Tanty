package com.NTFOODS.Api_tanty.modules.stock.application.dotation.dto.response;

import java.time.LocalDate;
import java.util.List;

public record DotationResponse(
    Long id,
    String numero,
    String matriculeCommercial,
    LocalDate dateDotation,
    LocalDate dateEffet,
    String statut,
    String statutLibelle,
    String matriculeGestionnaire,
    String motifRefus,
    List<LigneResponse> lignes
) {
    public record LigneResponse(
        String id,
        String codeProduit,
        String sku,
        long quantiteDemandee,
        long quantiteApprouvee,
        String motifModification
    ) {}
}
