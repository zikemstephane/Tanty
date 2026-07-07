package com.NTFOODS.Api_tanty.modules.stock.application.alerte.dto.response;

import java.time.LocalDateTime;

public record AlerteResponse(
    Long id,
    String reference,
    String type,
    String typeLibelle,
    String message,
    String destinataireRole,
    String actionSuggeree,
    String priorite,
    String codeProduit,
    String codeEntrepot,
    String referenceLiee,
    String statut,
    LocalDateTime dateCreation,
    LocalDateTime dateTraitement
) {}
