package com.NTFOODS.Api_tanty.modules.auth.application.auth.dto;

import jakarta.validation.constraints.NotBlank;

public record VerifierOtpRequest(
    @NotBlank(message = "Le matricule est obligatoire") String matricule,
    @NotBlank(message = "Le code OTP est obligatoire") String codeOtp
) {}
