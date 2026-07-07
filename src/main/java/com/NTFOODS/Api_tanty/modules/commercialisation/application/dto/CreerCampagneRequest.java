package com.NTFOODS.Api_tanty.modules.commercialisation.application.dto;

import com.NTFOODS.Api_tanty.modules.commercialisation.domain.enums.TypeCampagne;
import jakarta.validation.constraints.*;

import java.time.LocalDate;

public record CreerCampagneRequest(
    @NotBlank String code,
    @NotBlank String libelle,
    String description,
    @NotNull TypeCampagne type,
    @NotNull LocalDate dateDebut,
    LocalDate dateFin,
    String zoneCible,
    @DecimalMin(value = "0.0") double budgetPrevuFCFA
) {}
