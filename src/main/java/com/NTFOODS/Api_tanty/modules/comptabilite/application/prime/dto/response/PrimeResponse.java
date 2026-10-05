package com.NTFOODS.Api_tanty.modules.comptabilite.application.prime.dto.response;

import java.time.LocalDate;
import java.util.Map;

public record PrimeResponse(
    Long id,
    String referencePrime,
    String matriculeCommercial,
    LocalDate semaineDebut,
    LocalDate semaineFin,
    double objectifGlobalFCFA,
    double totalVentesGlobalFCFA,
    double tauxRealisationGlobalPourcent,
    Map<String, Double> objectifsParGammeFCFA,
    Map<String, Double> realisationsParGammeFCFA,
    Map<String, Double> tauxParGammePourcent,
    boolean eligible,
    double montantPrimeFCFA,
    double montantCashFCFA,
    double montantNatureFCFA,
    String statut,
    String matriculeValidateur
) {}
