package com.NTFOODS.Api_tanty.modules.auth.application.auth.dto;

import com.NTFOODS.Api_tanty.modules.auth.domain.utilisateur.enums.Role;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDate;

public record RegisterRequest(
    @NotBlank String prenom,
    @NotBlank String nom,
    @NotBlank @Email String email,
    @NotBlank String motDePasse,
    @NotNull Role role,
    String telephone,
    String residence,
    String cni,
    String photo,
    LocalDate dateNaissance,
    String lieuNaissance,
    LocalDate dateEmbauche,
    String sexe
) {}
