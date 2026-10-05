package com.NTFOODS.Api_tanty.modules.commercial.application.tarification.dto.response;

import java.time.LocalDateTime;

public record GrilleTarifaireResponse(
    Long id,
    String codeProduit,
    String segment,
    double prixHomologueFCFA,
    boolean actif,
    String matriculeAuteur,
    LocalDateTime dateModification
) {}
