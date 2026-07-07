package com.NTFOODS.Api_tanty.modules.auth.application.auth.dto;

import jakarta.validation.constraints.NotBlank;

public record DemanderOtpRequest(
    @NotBlank(message = "Le matricule est obligatoire") String matricule,
    String raison
) {}
