package com.NTFOODS.Api_tanty.modules.auth.domain.rh.model;

import com.NTFOODS.Api_tanty.modules.auth.domain.utilisateur.enums.Role;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Objects;
import java.util.UUID;

/**
 * Employé NTFOODS TANTY — modèle RH partagé.
 *
 * Distinction responsable (avec compte utilisateur) vs exécutant (sans compte).
 * Le matricule est généré selon la règle métier : début du nom + zone de travail + 3 chiffres aléatoires.
 */
public class Employe {

    private Long id;
    private final String matricule;
    private final String nom;
    private final String prenom;
    private final Role role;
    private final String zoneTravail;
    private final String superviseurMatricule;
    private final boolean avecCompte;
    private boolean actif;
    private final LocalDate dateEmbauche;
    private final LocalDateTime dateCreation;
    private LocalDateTime dateModification;
    private Long version;

    private Employe(String matricule, String nom, String prenom, Role role, String zoneTravail,
                    String superviseurMatricule, boolean avecCompte, LocalDate dateEmbauche,
                    LocalDateTime dateCreation) {
        this.matricule = Objects.requireNonNull(matricule, "Le matricule est obligatoire");
        this.nom = Objects.requireNonNull(nom, "Le nom est obligatoire");
        this.prenom = Objects.requireNonNull(prenom, "Le prénom est obligatoire");
        this.role = Objects.requireNonNull(role, "Le rôle est obligatoire");
        this.zoneTravail = Objects.requireNonNull(zoneTravail, "La zone de travail est obligatoire");
        this.superviseurMatricule = superviseurMatricule;
        this.avecCompte = avecCompte;
        this.actif = true;
        this.dateEmbauche = dateEmbauche;
        this.dateCreation = dateCreation;
        this.version = 0L;
    }

    public static Employe creer(String matricule, String nom, String prenom, Role role,
                                 String zoneTravail, String superviseurMatricule,
                                 boolean avecCompte, LocalDate dateEmbauche) {
        return new Employe(matricule, nom, prenom, role, zoneTravail, superviseurMatricule,
            avecCompte, dateEmbauche, LocalDateTime.now());
    }

    public static Employe reconstituer(Long id, String matricule, String nom, String prenom, Role role,
                                        String zoneTravail, String superviseurMatricule,
                                        boolean avecCompte, boolean actif, LocalDate dateEmbauche,
                                        LocalDateTime dateCreation, LocalDateTime dateModification, Long version) {
        Employe e = new Employe(matricule, nom, prenom, role, zoneTravail, superviseurMatricule,
            avecCompte, dateEmbauche, dateCreation);
        e.id = id;
        e.actif = actif;
        e.dateModification = dateModification;
        e.version = version;
        return e;
    }

    public void desactiver() {
        this.actif = false;
        this.dateModification = LocalDateTime.now();
    }

    public void reactiver() {
        this.actif = true;
        this.dateModification = LocalDateTime.now();
    }

    public boolean estExecutant() {
        return role == Role.MACHINISTE || role == Role.AGENT_PRODUCTION;
    }

    public boolean estResponsable() {
        return !estExecutant();
    }

    public Long getId() { return id; }
    public String getMatricule() { return matricule; }
    public String getNom() { return nom; }
    public String getPrenom() { return prenom; }
    public String getNomComplet() { return prenom + " " + nom; }
    public Role getRole() { return role; }
    public String getZoneTravail() { return zoneTravail; }
    public String getSuperviseurMatricule() { return superviseurMatricule; }
    public boolean isAvecCompte() { return avecCompte; }
    public boolean isActif() { return actif; }
    public LocalDate getDateEmbauche() { return dateEmbauche; }
    public LocalDateTime getDateCreation() { return dateCreation; }
    public LocalDateTime getDateModification() { return dateModification; }
    public Long getVersion() { return version; }

    @Override public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Employe)) return false;
        return Objects.equals(matricule, ((Employe) o).matricule);
    }
    @Override public int hashCode() { return Objects.hash(matricule); }
    @Override public String toString() {
        return "Employe{" + matricule + ", " + getNomComplet() + ", " + role + "}";
    }
}
