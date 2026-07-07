package com.NTFOODS.Api_tanty.modules.production.application.broyage.dto.request;

import com.NTFOODS.Api_tanty.modules.production.domain.broyage.enums.TypePoudre;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

import java.time.LocalDate;
import java.util.List;

public record OuvrirSessionBroyageRequest(
    @NotBlank String referencePPH,
    @NotNull LocalDate date,
    @NotNull TypePoudre typePoudre,
    @Positive double objectifJournalierKg,
    @NotEmpty @Valid List<ObjectifMachinisteRequest> objectifsMachinistes
) {}
