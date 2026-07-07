package com.NTFOODS.Api_tanty.modules.production.application.broyage.dto.request;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;

public record AutoriserDepassementBroyageRequest(
    @NotNull Long sessionId,
    @Min(0) double quantiteSupplementaireKg
) {}
