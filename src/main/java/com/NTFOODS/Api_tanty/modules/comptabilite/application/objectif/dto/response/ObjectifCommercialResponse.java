package com.NTFOODS.Api_tanty.modules.comptabilite.application.objectif.dto.response;

import java.time.LocalDate;
import java.util.Map;

public record ObjectifCommercialResponse(
    Long id,
    String matriculeCommercial,
    LocalDate semaineDebut,
    LocalDate semaineFin,
    double objectifGlobalFCFA,
    Map<String, Double> objectifsParGammeFCFA,
    String matriculeAuteur
) {}
