package com.NTFOODS.Api_tanty.modules.commercial.application.tarification.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

/**
 * @param codeOtp Code OTP reçu par SMS par le DG / Directeur Commercial —
 *                obligatoire (Pilier 3 : opération financière sensible).
 */
public record AccorderPrixExceptionnelRequest(
    @NotBlank String codeProduit,
    @NotBlank String codeClient,
    @NotNull @Positive Double prixAppliqueFCFA,
    @NotBlank String motif,
    String referenceVenteCible,
    @NotBlank String codeOtp
) {}
