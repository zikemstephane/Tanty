package com.NTFOODS.Api_tanty.modules.production.application.pph.dto.request;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

public record AjusterRepartitionPPHRequest(
    @NotEmpty @Valid List<LigneRepartitionDto> repartition
) {
    public record LigneRepartitionDto(
        @NotNull LocalDate dateJour,
        @PositiveOrZero double futsPrevus,
        @Valid List<BesoinMatierePremiereDto> besoinsMatieresPremiere,
        @Valid List<BesoinEmballageDto> besoinsEmballage
    ) {}

    public record BesoinMatierePremiereDto(
        @NotNull String matierePremiere,
        @NotNull @PositiveOrZero BigDecimal quantiteKg
    ) {}

    public record BesoinEmballageDto(
        @NotNull String type,
        @PositiveOrZero long quantite
    ) {}
}
