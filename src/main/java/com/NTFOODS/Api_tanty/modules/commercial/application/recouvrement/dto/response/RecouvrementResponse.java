package com.NTFOODS.Api_tanty.modules.commercial.application.recouvrement.dto.response;

import java.time.LocalDate;

public record RecouvrementResponse(
    Long id,
    String referenceRecouvrement,
    String codeClient,
    String nomClient,
    double montantDu,
    double montantRembourse,
    double montantRestant,
    LocalDate dateEcheance,
    String statut,
    String matriculeCommercial,
    String observations
) {}
