package com.NTFOODS.Api_tanty.modules.stock.application.mouvement.dto.response;

import java.time.LocalDateTime;

public record MouvementResponse(
    Long id,
    String reference,
    String type,
    String typeLibelle,
    String sens,
    String codeProduit,
    String sku,
    long quantiteEnBase,
    String codeEntrepotSource,
    String codeEntrepotCible,
    String matriculeActeur,
    String referenceDocument,
    String motif,
    LocalDateTime horodatage
) {}
