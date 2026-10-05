package com.NTFOODS.Api_tanty.modules.stock.domain.inventaire.model;

import com.NTFOODS.Api_tanty.modules.auth.domain.utilisateur.enums.Role;
import com.NTFOODS.Api_tanty.modules.stock.domain.inventaire.enums.StatutInventaire;
import com.NTFOODS.Api_tanty.modules.stock.domain.inventaire.enums.TypeInventaire;
import com.NTFOODS.Api_tanty.modules.stock.domain.inventaire.event.InventaireValideEvent;
import com.NTFOODS.Api_tanty.shared.kernel.exception.AccesRefuseException;
import com.NTFOODS.Api_tanty.shared.kernel.exception.RegleMetierException;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.*;

/**
 * Agrégat Racine — Session d'inventaire.
 *
 * Types : complet, tournant, ciblé, surprise (DG). Le comptage est saisi par
 * conditionnement/emplacement, l'écart calculé automatiquement, puis le DG valide
 * (dérogation au-delà du seuil). À la validation, le stock est ajusté.
 */
public class SessionInventaire {

    private static final double SEUIL_ECART_DEROGATION = 5.0; // % au-delà duquel une justification DG est requise

    private Long id;
    private final String numero;
    private final TypeInventaire type;
    private final String codeEntrepot;
    private final String perimetre;          // gamme/zone/produit selon le type
    private StatutInventaire statut;
    private final List<LigneInventaire> lignes;
    private final String matriculeInitiateur;
    private String matriculeValidateurDG;
    private String justificationEcarts;
    private final LocalDate dateInventaire;
    private final LocalDateTime dateCreation;
    private LocalDateTime dateValidation;
    private Long version;

    private final List<Object> domainEvents = new ArrayList<>();

    private SessionInventaire(String numero, TypeInventaire type, String codeEntrepot, String perimetre,
                             String matriculeInitiateur, LocalDate dateInventaire, LocalDateTime dateCreation) {
        this.numero = Objects.requireNonNull(numero);
        this.type = Objects.requireNonNull(type);
        this.codeEntrepot = Objects.requireNonNull(codeEntrepot);
        this.perimetre = perimetre;
        this.matriculeInitiateur = Objects.requireNonNull(matriculeInitiateur);
        this.dateInventaire = Objects.requireNonNull(dateInventaire);
        this.dateCreation = dateCreation;
        this.statut = StatutInventaire.OUVERT;
        this.lignes = new ArrayList<>();
        this.version = 0L;
    }

    public static SessionInventaire ouvrir(String numero, TypeInventaire type, String codeEntrepot, String perimetre,
                                           String matriculeInitiateur, Role role) {
        if (type == TypeInventaire.SURPRISE && role != Role.DIRECTEUR_GENERAL && role != Role.ADMIN) {
            throw new AccesRefuseException("Seul le Directeur Général peut déclencher un inventaire surprise");
        }
        if (role != Role.GESTIONNAIRE_STOCK && role != Role.DIRECTEUR_GENERAL && role != Role.ADMIN) {
            throw new AccesRefuseException("Le rôle '" + role.getLibelle() + "' ne peut pas ouvrir un inventaire");
        }
        return new SessionInventaire(numero, type, codeEntrepot, perimetre, matriculeInitiateur,
            LocalDate.now(), LocalDateTime.now());
    }

    public static SessionInventaire reconstituer(Long id, String numero, TypeInventaire type, String codeEntrepot,
                                                 String perimetre, StatutInventaire statut, List<LigneInventaire> lignes,
                                                 String matriculeInitiateur, String matriculeValidateurDG,
                                                 String justificationEcarts, LocalDate dateInventaire,
                                                 LocalDateTime dateCreation, LocalDateTime dateValidation, Long version) {
        SessionInventaire s = new SessionInventaire(numero, type, codeEntrepot, perimetre, matriculeInitiateur,
            dateInventaire, dateCreation);
        s.id = id;
        s.statut = statut;
        if (lignes != null) s.lignes.addAll(lignes);
        s.matriculeValidateurDG = matriculeValidateurDG;
        s.justificationEcarts = justificationEcarts;
        s.dateValidation = dateValidation;
        s.version = version;
        return s;
    }

    public LigneInventaire ajouterLigne(String codeProduit, String emplacement, long quantiteTheorique) {
        if (statut != StatutInventaire.OUVERT) {
            throw new RegleMetierException("Les lignes ne peuvent être ajoutées qu'à un inventaire ouvert");
        }
        LigneInventaire ligne = LigneInventaire.creer(codeProduit, emplacement, quantiteTheorique);
        lignes.add(ligne);
        return ligne;
    }

    public void saisirComptage(String idLigne, long quantiteComptee) {
        if (statut != StatutInventaire.OUVERT) {
            throw new RegleMetierException("Le comptage n'est possible que sur un inventaire ouvert");
        }
        trouverLigne(idLigne).saisirComptage(quantiteComptee);
    }

    public void cloturerComptage() {
        if (statut != StatutInventaire.OUVERT) {
            throw new RegleMetierException("Seul un inventaire ouvert peut passer à l'état compté");
        }
        if (lignes.stream().noneMatch(LigneInventaire::estComptee)) {
            throw new RegleMetierException("Aucune ligne comptée");
        }
        this.statut = StatutInventaire.COMPTE;
    }

    /** Validation par le DG. Justification requise si un écart dépasse le seuil de dérogation. */
    public void valider(String matriculeDG, Role role, String justification) {
        if (role != Role.DIRECTEUR_GENERAL && role != Role.ADMIN) {
            throw new AccesRefuseException("Seul le Directeur Général peut valider un inventaire");
        }
        if (statut != StatutInventaire.COMPTE) {
            throw new RegleMetierException("L'inventaire doit être compté avant validation");
        }
        if (aEcartSignificatif() && (justification == null || justification.isBlank())) {
            throw new RegleMetierException("Une justification est requise : écart au-delà du seuil de dérogation");
        }
        this.matriculeValidateurDG = matriculeDG;
        this.justificationEcarts = justification;
        this.statut = StatutInventaire.VALIDE;
        this.dateValidation = LocalDateTime.now();
        domainEvents.add(new InventaireValideEvent(numero, codeEntrepot, type));
    }

    public boolean aEcartSignificatif() {
        return lignes.stream().filter(LigneInventaire::estComptee).anyMatch(l -> {
            if (l.getQuantiteTheorique() == 0) return l.getEcart() != 0;
            return Math.abs(l.getEcart()) * 100.0 / l.getQuantiteTheorique() > SEUIL_ECART_DEROGATION;
        });
    }

    private LigneInventaire trouverLigne(String idLigne) {
        return lignes.stream().filter(l -> l.getId().equals(idLigne)).findFirst()
            .orElseThrow(() -> new IllegalArgumentException("Ligne introuvable : " + idLigne));
    }

    public List<Object> getDomainEvents() { return Collections.unmodifiableList(domainEvents); }
    public void clearDomainEvents() { domainEvents.clear(); }

    public Long getId() { return id; }
    public String getNumero() { return numero; }
    public TypeInventaire getType() { return type; }
    public String getCodeEntrepot() { return codeEntrepot; }
    public String getPerimetre() { return perimetre; }
    public StatutInventaire getStatut() { return statut; }
    public List<LigneInventaire> getLignes() { return Collections.unmodifiableList(lignes); }
    public String getMatriculeInitiateur() { return matriculeInitiateur; }
    public String getMatriculeValidateurDG() { return matriculeValidateurDG; }
    public String getJustificationEcarts() { return justificationEcarts; }
    public LocalDate getDateInventaire() { return dateInventaire; }
    public LocalDateTime getDateCreation() { return dateCreation; }
    public LocalDateTime getDateValidation() { return dateValidation; }
    public Long getVersion() { return version; }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof SessionInventaire that)) return false;
        return Objects.equals(numero, that.numero);
    }

    @Override
    public int hashCode() { return Objects.hash(numero); }
}
