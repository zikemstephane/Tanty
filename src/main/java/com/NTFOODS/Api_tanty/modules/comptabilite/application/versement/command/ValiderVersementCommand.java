package com.NTFOODS.Api_tanty.modules.comptabilite.application.versement.command;

/**
 * @param codeOtp Code OTP reçu par SMS par le comptable (Pilier 3 — opérations
 *                financières sensibles). Obligatoire pour valider un versement.
 */
public record ValiderVersementCommand(
        String referenceVersement,
        String matriculeValidateur,
        String codeOtp
) {}
