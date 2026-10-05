package com.NTFOODS.Api_tanty.modules.auth.infrastructure.persistence.jpa;

import com.NTFOODS.Api_tanty.modules.auth.domain.utilisateur.enums.Role;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Entity
@Table(name = "utilisateurs", indexes = {
    @Index(name = "idx_utilisateur_matricule", columnList = "matricule", unique = true),
    @Index(name = "idx_utilisateur_email", columnList = "email", unique = true)
})
@Getter @Setter @NoArgsConstructor
public class UtilisateurJpaEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true, nullable = false, length = 20)
    private String matricule;

    @Column(nullable = false, length = 100)
    private String prenom;

    @Column(nullable = false, length = 100)
    private String nom;

    @Column(unique = true, nullable = false, length = 150)
    private String email;

    @Column(name = "mot_de_passe_hash", nullable = false)
    private String motDePasseHash;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 30)
    private Role role;

    @Column(nullable = false)
    private boolean actif = true;

    // ─── Sécurité anti-forçage (Pilier 3) ──────────────────────────────────
    @Column(name = "tentatives_echouees", nullable = false)
    private int tentativesEchouees = 0;

    @Column(name = "compte_bloque", nullable = false)
    private boolean compteBloque = false;

    // ─── Session unique : un nouveau login invalide l'ancien token ─────────
    @Column(name = "session_actuelle", length = 40)
    private String sessionActuelle;

    @Column(name = "date_creation", nullable = false)
    private LocalDateTime dateCreation;

    @Column(name = "date_modification")
    private LocalDateTime dateModification;

    @Column(name = "cni", length = 30)
    private String cni;

    @Column(name = "telephone", length = 20)
    private String telephone;

    @Column(name = "residence", length = 150)
    private String residence;

    @Column(name = "photo", length = 500)
    private String photo;

    @Column(name = "date_naissance")
    private java.time.LocalDate dateNaissance;

    @Column(name = "lieu_naissance", length = 100)
    private String lieuNaissance;

    @Column(name = "date_embauche")
    private java.time.LocalDate dateEmbauche;

    @Column(name = "sexe", length = 10)
    private String sexe;

    @Version
    private Long version;
}
