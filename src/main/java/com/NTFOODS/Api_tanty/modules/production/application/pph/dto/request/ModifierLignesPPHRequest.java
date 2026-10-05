package com.NTFOODS.Api_tanty.modules.production.application.pph.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.util.List;

public record ModifierLignesPPHRequest(
    @NotNull List<LigneDto> lignes
) {
    public record LigneDto(
        @NotBlank String codeProduit,
        @NotNull Integer objectifSemaine
    ) {}
}
