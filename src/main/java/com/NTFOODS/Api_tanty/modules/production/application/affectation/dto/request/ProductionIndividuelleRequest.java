package com.NTFOODS.Api_tanty.modules.production.application.affectation.dto.request;

import com.NTFOODS.Api_tanty.modules.production.domain.affectation.enums.CodePoste;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record ProductionIndividuelleRequest(
    @NotBlank String matriculeEmploye,
    @NotBlank String nomEmploye,
    @NotNull CodePoste poste,
    @Min(0) double quantiteRealisee
) {}
