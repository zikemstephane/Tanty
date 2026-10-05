package com.NTFOODS.Api_tanty.modules.production.application.of.dto.response;

import java.time.LocalDate;

public record OFResponse(
    Long id,
    String idOF,
    String referencePPH,
    String codeProduit,
    String designationProduit,
    int qteDemandee,
    int qteRealisee,
    LocalDate dateButoir,
    String statut
) {}
