package com.NTFOODS.Api_tanty.modules.production.application.fiche.dto.response;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;

public record FicheResponse(
    Long id,
    String referencePPH,
    String semaine,
    LocalDate date,
    String matriculeAgentProduction,
    String justificationEcart,
    boolean verrouillee,
    Map<String, Integer> predictionDosage,
    Map<String, Integer> predictionPostes,
    List<LigneResponse> lignes
) {
    public record LigneResponse(String codeProduit, String nomProduit, double quantiteProduite,
                                 Integer quantitePrediteDosage, Integer quantitePreditePostes, Integer ecart) {}
}
