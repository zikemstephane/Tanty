package com.NTFOODS.Api_tanty.modules.commercial.application.tarification.dto.request;

import com.NTFOODS.Api_tanty.modules.commercial.domain.client.enums.TypeClient;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

public record DefinirGrilleTarifaireRequest(
    @NotBlank String codeProduit,
    @NotNull TypeClient segment,
    @NotNull @Positive Double prixHomologueFCFA
) {}
