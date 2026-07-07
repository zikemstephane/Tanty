package com.NTFOODS.Api_tanty.modules.dg.application.dto.response;

import com.NTFOODS.Api_tanty.modules.auth.domain.utilisateur.enums.Role;

import java.time.LocalDate;
import java.time.LocalDateTime;

public record UtilisateurDgResponse(
    Long id,
    String matricule,
    String prenom,
    String nom,
    String email,
    Role role,
    boolean actif,
    boolean compteBloque,
    int tentativesEchouees,
    String telephone,
    String residence,
    String cni,
    String photo,
    LocalDate dateNaissance,
    String lieuNaissance,
    LocalDate dateEmbauche,
    String sexe,
    LocalDateTime dateCreation
) {}
