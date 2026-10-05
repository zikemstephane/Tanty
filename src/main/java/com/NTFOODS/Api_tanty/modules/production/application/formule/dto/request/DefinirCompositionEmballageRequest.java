package com.NTFOODS.Api_tanty.modules.production.application.formule.dto.request;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

import java.util.List;

/**
 * Type et composition d'emballage d'un conditionnement. {@code type} nul avec
 * {@code composants} vide efface la composition (état « non renseigné »).
 */
public record DefinirCompositionEmballageRequest(
    String type,
    @Valid List<ComposantRequest> composants
) {
    public record ComposantRequest(
        @NotNull String type,
        @Positive int quantiteComposant,
        @Positive int quantiteUnitesProduites
    ) {}
}
