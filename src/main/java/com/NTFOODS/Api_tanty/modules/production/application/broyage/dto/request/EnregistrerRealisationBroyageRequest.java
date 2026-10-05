package com.NTFOODS.Api_tanty.modules.production.application.broyage.dto.request;

import com.NTFOODS.Api_tanty.modules.production.domain.broyage.enums.OperationBroyage;
import com.NTFOODS.Api_tanty.modules.production.domain.broyage.enums.TypePoudre;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record EnregistrerRealisationBroyageRequest(
    @NotNull Long sessionId,
    @NotBlank String machinisteMatricule,
    @NotBlank String nomMachiniste,
    @NotNull OperationBroyage operation,
    @NotNull TypePoudre typePoudre,
    @Min(0) double objectifKg,
    @Min(0) double realiseKg
) {}
