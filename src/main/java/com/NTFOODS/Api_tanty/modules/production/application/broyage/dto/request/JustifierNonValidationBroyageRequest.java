package com.NTFOODS.Api_tanty.modules.production.application.broyage.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record JustifierNonValidationBroyageRequest(
    @NotNull Long sessionId,
    @NotBlank String justificatif
) {}
