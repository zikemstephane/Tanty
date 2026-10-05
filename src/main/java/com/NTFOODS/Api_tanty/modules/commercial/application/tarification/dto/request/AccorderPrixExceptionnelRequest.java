package com.NTFOODS.Api_tanty.modules.commercial.application.tarification.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

public record AccorderPrixExceptionnelRequest(
    @NotBlank String codeProduit,
    @NotBlank String codeClient,
    @NotNull @Positive Double prixAppliqueFCFA,
    @NotBlank String motif,
    String referenceVenteCible
) {}
