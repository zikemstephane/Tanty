package com.NTFOODS.Api_tanty.modules.auth.domain.utilisateur.aggregate;

import com.NTFOODS.Api_tanty.modules.auth.domain.utilisateur.enums.Role;
import com.NTFOODS.Api_tanty.modules.auth.domain.utilisateur.events.CompteBloqueEvent;
import com.NTFOODS.Api_tanty.modules.auth.domain.utilisateur.events.UtilisateurConnecteEvent;
import com.NTFOODS.Api_tanty.modules.auth.domain.utilisateur.events.UtilisateurCreeEvent;
import com.NTFOODS.Api_tanty.modules.auth.domain.utilisateur.exceptions.CompteBloqueException;
import com.NTFOODS.Api_tanty.modules.auth.domain.utilisateur.exceptions.CompteDesactiveException;
import com.NTFOODS.Api_tanty.modules.auth.domain.utilisateur.valueobject.Email;
import com.NTFOODS.Api_tanty.modules.auth.domain.utilisateur.valueobject.Matricule;
import com.NTFOODS.Api_tanty.modules.auth.domain.utilisateur.valueobject.MotDePasse;
import com.NTFOODS.Api_tanty.modules.auth.domain.utilisateur.valueobject.NomComplet;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

/**
 * Agrégat Racine — Utilisateur NTFOODS TANTY.
 * Identité métier : Matricule (immuable).
 * Le rôle détermine les droits d'accès sur tous les modules.
 *
 * Sécurité anti-forçage (Pilier 3) :
 * - Blocage automatique après 3 tentatives de connexion erronées (comme une carte bancaire) ;
 *   déblocage manuel réservé à l'administrateur.
 * - Session unique : toute nouvelle connexion réussie invalide la session précédente
 *   (le jeton JWT embarque un identifiant de session comparé à sessionActuelle).
 */
public class Utilisateur {

    private static final int MAX_TENTATIVES = 3;

    private Long id;
    private final Matricule matricule;
    private NomComplet nomComplet;
    private Email email;
    private MotDePasse motDePasse;
    private Role role;
    private boolean actif;
    private int tentativesEchouees;
    private boolean compteBloque;
    private String sessionActuelle;
    private String cni;
    private String telephone;
    private String residence;
    private String photo;
    private LocalDate dateNaissance;
    private String lieuNaissance;
    private LocalDate dateEmbauche;
    private String sexe;
    private final LocalDateTime dateCreation;
    private LocalDateTime dateModification;
    private Long version;

    private final List<Object> domainEvents = new ArrayList<>();

    private Utilisateur(Matricule matricule, NomComplet nomComplet, Email email,
                        MotDePasse motDePasse, Role role, LocalDateTime dateCreation) {
        this.matricule = Objects.requireNonNull(matricule);
        this.nomComplet = Objects.requireNonNull(nomComplet);
        this.email = Objects.requireNonNull(email);
        this.motDePasse = Objects.requireNonNull(motDePasse);
        this.role = Objects.requireNonNull(role);
        this.dateCreation = Objects.requireNonNull(dateCreation);
        this.actif = true;
        this.tentativesEchouees = 0;
        this.compteBloque = false;
        this.version = 0L;
    }

    public static Utilisateur creer(Matricule matricule, NomComplet nomComplet,
                                    Email email, MotDePasse motDePasse, Role role) {
        Utilisateur u = new Utilisateur(matricule, nomComplet, email, motDePasse, role, LocalDateTime.now());
        u.domainEvents.add(new UtilisateurCreeEvent(
            matricule.getValeur(), nomComplet.getNomComplet(), email.getValeur(), role
        ));
        return u;
    }

    public static Utilisateur reconstituer(Long id, Matricule matricule, NomComplet nomComplet,
                                           Email email, MotDePasse motDePasse, Role role,
                                           boolean actif, int tentativesEchouees, boolean compteBloque,
                                           String sessionActuelle, LocalDateTime dateCreation,
                                           LocalDateTime dateModification, Long version,
                                           String cni, String telephone, String residence, String photo,
                                           LocalDate dateNaissance, String lieuNaissance,
                                           LocalDate dateEmbauche, String sexe) {
        Utilisateur u = new Utilisateur(matricule, nomComplet, email, motDePasse, role, dateCreation);
        u.id = id;
        u.actif = actif;
        u.tentativesEchouees = tentativesEchouees;
        u.compteBloque = compteBloque;
        u.sessionActuelle = sessionActuelle;
        u.dateModification = dateModification;
        u.version = version;
        u.cni = cni;
        u.telephone = telephone;
        u.residence = residence;
        u.photo = photo;
        u.dateNaissance = dateNaissance;
        u.lieuNaissance = lieuNaissance;
        u.dateEmbauche = dateEmbauche;
        u.sexe = sexe;
        return u;
    }

    /**
     * Ouvre une nouvelle session (connexion réussie) : réinitialise le compteur
     * d'échecs et génère un nouvel identifiant de session qui invalide,
     * de fait, tout jeton précédemment émis pour cet utilisateur.
     */
    public String ouvrirNouvelleSession() {
        if (!actif) throw new CompteDesactiveException(matricule.getValeur());
        if (compteBloque) throw new CompteBloqueException(matricule.getValeur());
        this.tentativesEchouees = 0;
        this.sessionActuelle = UUID.randomUUID().toString();
        this.dateModification = LocalDateTime.now();
        domainEvents.add(new UtilisateurConnecteEvent(
            matricule.getValeur(), nomComplet.getNomComplet(), role
        ));
        return this.sessionActuelle;
    }

    /** Conservé pour compatibilité ; délègue à ouvrirNouvelleSession(). */
    public void enregistrerConnexion() {
        ouvrirNouvelleSession();
    }

    /**
     * Enregistre un échec d'authentification. Après 3 échecs consécutifs,
     * le compte est automatiquement bloqué (comme une carte bancaire) et
     * seul un administrateur peut le débloquer.
     */
    public void enregistrerEchecConnexion() {
        if (compteBloque) return;
        this.tentativesEchouees++;
        this.dateModification = LocalDateTime.now();
        if (this.tentativesEchouees >= MAX_TENTATIVES) {
            this.compteBloque = true;
            domainEvents.add(new CompteBloqueEvent(matricule.getValeur(), nomComplet.getNomComplet()));
        }
    }

    /** Déblocage manuel réservé à l'administrateur système / DG. */
    public void debloquer() {
        this.compteBloque = false;
        this.tentativesEchouees = 0;
        this.dateModification = LocalDateTime.now();
    }

    /** Invalide la session active (déconnexion forcée / précaution sécurité). */
    public void cloreSession() {
        this.sessionActuelle = null;
        this.dateModification = LocalDateTime.now();
    }

    public void desactiver() {
        this.actif = false;
        this.sessionActuelle = null;
        this.dateModification = LocalDateTime.now();
    }

    public void reactiver() {
        this.actif = true;
        this.dateModification = LocalDateTime.now();
    }

    public void changerMotDePasse(MotDePasse nouveau) {
        this.motDePasse = Objects.requireNonNull(nouveau);
        this.dateModification = LocalDateTime.now();
    }

    public void changerRole(Role nouveauRole) {
        this.role = Objects.requireNonNull(nouveauRole);
        this.dateModification = LocalDateTime.now();
    }

    public void modifierProfil(String telephone, String residence, String photo,
                                String cni, LocalDate dateNaissance, String lieuNaissance,
                                String sexe) {
        this.telephone = telephone;
        this.residence = residence;
        this.photo = photo;
        this.cni = cni;
        this.dateNaissance = dateNaissance;
        this.lieuNaissance = lieuNaissance;
        this.sexe = sexe;
        this.dateModification = LocalDateTime.now();
    }

    public void setDateEmbauche(LocalDate dateEmbauche) {
        this.dateEmbauche = dateEmbauche;
        this.dateModification = LocalDateTime.now();
    }

    public List<Object> getDomainEvents() { return Collections.unmodifiableList(domainEvents); }
    public void clearDomainEvents() { domainEvents.clear(); }

    public Long getId() { return id; }
    public Matricule getMatricule() { return matricule; }
    public NomComplet getNomComplet() { return nomComplet; }
    public Email getEmail() { return email; }
    public MotDePasse getMotDePasse() { return motDePasse; }
    public Role getRole() { return role; }
    public boolean isActif() { return actif; }
    public int getTentativesEchouees() { return tentativesEchouees; }
    public boolean isCompteBloque() { return compteBloque; }
    public String getSessionActuelle() { return sessionActuelle; }
    public LocalDateTime getDateCreation() { return dateCreation; }
    public LocalDateTime getDateModification() { return dateModification; }
    public Long getVersion() { return version; }
    public String getCni() { return cni; }
    public String getTelephone() { return telephone; }
    public String getResidence() { return residence; }
    public String getPhoto() { return photo; }
    public LocalDate getDateNaissance() { return dateNaissance; }
    public String getLieuNaissance() { return lieuNaissance; }
    public LocalDate getDateEmbauche() { return dateEmbauche; }
    public String getSexe() { return sexe; }

    @Override public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Utilisateur)) return false;
        return Objects.equals(matricule, ((Utilisateur) o).matricule);
    }
    @Override public int hashCode() { return Objects.hash(matricule); }
    @Override public String toString() {
        return "Utilisateur{matricule=" + matricule + ", role=" + role + ", actif=" + actif + "}";
    }
}
