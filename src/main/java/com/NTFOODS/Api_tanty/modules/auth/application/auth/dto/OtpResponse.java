package com.NTFOODS.Api_tanty.modules.auth.application.auth.dto;

public record OtpResponse(
    String matricule,
    boolean otpEnvoye,
    String telephoneMasque,
    int dureeValiditeMinutes
) {}
