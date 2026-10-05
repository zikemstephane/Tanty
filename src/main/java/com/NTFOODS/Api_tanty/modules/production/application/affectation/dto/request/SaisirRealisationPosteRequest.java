package com.NTFOODS.Api_tanty.modules.production.application.affectation.dto.request;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.util.List;

public record SaisirRealisationPosteRequest(
    @NotNull Long affectationId,
    @NotBlank String poste,
    @Min(0) double quantiteRealisee,
    @Valid List<ProductionIndividuelleRequest> productionsIndividuelles,
    @Valid DetailsRealisationPosteRequest details
) {}
