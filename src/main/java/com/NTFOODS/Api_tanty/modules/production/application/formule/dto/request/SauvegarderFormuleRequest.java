package com.NTFOODS.Api_tanty.modules.production.application.formule.dto.request;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.PositiveOrZero;

import java.math.BigDecimal;
import java.util.List;

/** Création ou remplacement de la formule d'un produit. La somme des proportions doit égaler la base (vérifié par le domaine). */
public record SauvegarderFormuleRequest(
    @NotNull @Positive BigDecimal baseKg,
    @NotEmpty @Valid List<LigneRequest> lignes
) {
    public record LigneRequest(
        @NotBlank String matierePremiere,
        @NotNull @PositiveOrZero BigDecimal proportionKg
    ) {}
}
