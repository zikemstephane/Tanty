package com.NTFOODS.Api_tanty.modules.stock.application.seuil.dto.response;

import java.time.LocalDateTime;

public record HistoriqueSeuilResponse(
    Long id,
    String codeProduit,
    String typeEntrepot,
    long ancienneValeur,
    long nouvelleValeur,
    String matriculeAuteur,
    LocalDateTime dateModification
) {}
