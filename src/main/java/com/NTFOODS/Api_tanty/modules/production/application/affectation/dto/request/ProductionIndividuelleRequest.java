package com.NTFOODS.Api_tanty.modules.production.application.affectation.dto.request;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;

public record ProductionIndividuelleRequest(
    @NotBlank String matriculeEmploye,
    @NotBlank String nomEmploye,
    @NotBlank String poste,
    @Min(0) double quantiteRealisee
) {}
