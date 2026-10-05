package com.NTFOODS.Api_tanty.modules.production.application.of.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Positive;
import java.time.LocalDate;

public record CreerOFRequest(
    @NotBlank String idOF,
    @NotBlank String referencePPH,
    @NotBlank String codeProduit,
    @NotBlank String designationProduit,
    @Positive int qteDemandee,
    LocalDate dateButoir
) {}
