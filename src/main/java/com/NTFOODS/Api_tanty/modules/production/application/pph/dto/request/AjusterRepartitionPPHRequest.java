package com.NTFOODS.Api_tanty.modules.production.application.pph.dto.request;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;

import java.time.LocalDate;
import java.util.List;

public record AjusterRepartitionPPHRequest(
    @NotEmpty @Valid List<LigneRepartitionDto> repartition
) {
    public record LigneRepartitionDto(
        @NotNull LocalDate dateJour,
        @PositiveOrZero double poudreMaisKg,
        @PositiveOrZero double poudreSojaKg,
        @PositiveOrZero double poudreArachideKg,
        @PositiveOrZero int futsPrevus,
        @PositiveOrZero int sachetsPrevus,
        @PositiveOrZero int couverclesPrevus,
        @PositiveOrZero int scellesPrevus
    ) {}
}
