package com.NTFOODS.Api_tanty.modules.production.application.pph.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDate;
import java.util.List;

public record CreerPPHRequest(
    @NotBlank String referencePPH,
    String referenceBC,
    @NotBlank String semaine,
    @NotNull LocalDate dateDebut,
    @NotNull LocalDate dateFin,
    @NotEmpty List<LignePPHDto> lignes
) {
    public record LignePPHDto(String codeProduit, String nomProduit, int objectifSemaine) {}
}
