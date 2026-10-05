package com.NTFOODS.Api_tanty.modules.production.application.broyage.dto.request;

import com.NTFOODS.Api_tanty.modules.production.domain.broyage.enums.OperationBroyage;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

public record ObjectifMachinisteRequest(
    @NotBlank String machinisteMatricule,
    @NotBlank String nomMachiniste,
    @NotNull OperationBroyage operation,
    @Positive double objectifKg
) {}
