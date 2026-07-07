package com.NTFOODS.Api_tanty.modules.production.application.fiche.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;

public record EnregistrerFicheRequest(
    @NotBlank String referencePPH,
    @NotBlank String semaine,
    LocalDate date,
    @NotBlank String matriculeAgentProduction,
    @NotEmpty List<LigneFicheDto> lignes,
    Map<String, Integer> predictionDosage,
    Map<String, Integer> predictionPostes
) {
    public record LigneFicheDto(String codeProduit, String nomProduit, double quantiteProduite) {}
}
