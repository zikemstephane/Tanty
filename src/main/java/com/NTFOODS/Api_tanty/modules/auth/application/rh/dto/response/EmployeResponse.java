package com.NTFOODS.Api_tanty.modules.auth.application.rh.dto.response;

import com.NTFOODS.Api_tanty.modules.auth.domain.utilisateur.enums.Role;

import java.time.LocalDate;
import java.time.LocalDateTime;

public record EmployeResponse(
    Long id,
    String matricule,
    String nom,
    String prenom,
    String nomComplet,
    Role role,
    String zoneTravail,
    String superviseurMatricule,
    boolean avecCompte,
    boolean actif,
    LocalDate dateEmbauche,
    LocalDateTime dateCreation,
    LocalDateTime dateModification
) {}
