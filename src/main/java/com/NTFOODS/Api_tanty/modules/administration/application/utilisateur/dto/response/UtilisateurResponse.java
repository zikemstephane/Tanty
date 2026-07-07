package com.NTFOODS.Api_tanty.modules.administration.application.utilisateur.dto.response;

import java.time.LocalDate;
import java.time.LocalDateTime;

/** Vue administrative d'un utilisateur (sans données sensibles). */
public record UtilisateurResponse(
    Long id,
    String matricule,
    String prenom,
    String nom,
    String nomComplet,
    String email,
    String role,
    String roleLibelle,
    boolean actif,
    boolean compteBloque,
    int tentativesEchouees,
    boolean sessionActive,
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
