package com.NTFOODS.Api_tanty.modules.auth.application.rh.dto.request;

import com.NTFOODS.Api_tanty.modules.auth.domain.utilisateur.enums.Role;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDate;

public record CreerEmployeRequest(
    @NotBlank String prenom,
    @NotBlank String nom,
    @NotNull Role role,
    @NotBlank String zoneTravail,
    String superviseurMatricule,
    LocalDate dateEmbauche,
    @Email String email,
    String motDePasse
) {}
