package com.NTFOODS.Api_tanty.modules.commercial.application.tarification.dto.response;

import java.time.LocalDateTime;

public record PrixExceptionnelResponse(
    Long id,
    String reference,
    String codeProduit,
    String codeClient,
    double prixHomologueFCFA,
    double prixAppliqueFCFA,
    double ecartFCFA,
    String motif,
    String matriculeAutorisateur,
    String referenceVenteCible,
    LocalDateTime dateAutorisation
) {}
