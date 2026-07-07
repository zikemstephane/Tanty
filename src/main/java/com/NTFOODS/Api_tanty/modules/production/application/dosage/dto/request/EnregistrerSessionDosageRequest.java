package com.NTFOODS.Api_tanty.modules.production.application.dosage.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.time.LocalDate;

public record EnregistrerSessionDosageRequest(
    @NotBlank String referencePPH,
    @NotNull LocalDate date,
    @NotBlank String matriculeAgentDoseur
) {}
