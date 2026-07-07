package com.NTFOODS.Api_tanty.modules.auth.application.auth.dto;

import jakarta.validation.constraints.NotBlank;

public record LoginRequest(
    @NotBlank(message = "Le matricule est obligatoire") String matricule,
    @NotBlank(message = "Le mot de passe est obligatoire") String motDePasse
) {}
