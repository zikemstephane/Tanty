package com.NTFOODS.Api_tanty.modules.comptabilite.domain.decaissement.aggregate;

import com.NTFOODS.Api_tanty.modules.comptabilite.domain.decaissement.enums.StatutDecaissement;
import com.NTFOODS.Api_tanty.modules.comptabilite.domain.decaissement.enums.TypeDepense;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Objects;

/**
 * Agrégat Racine — Décaissement (§5.11).
 *
 * Workflow à 3 étapes avec séparation des fonctions :
 * 1. PROPOSE → le Contrôleur Général propose la dépense
 * 2. VALIDEE_CG → le DG valide la proposition
 * 3. VALIDEE_DG → la Secrétaire exécute le paiement
 *
 * Aucune étape ne peut être sautée. L'acteur qui propose ne peut pas
 * valider ni exécuter (séparation des fonctions — §4.2).
 *
 * Numérotation : DEC-YYYYMMDD-NNN
 */
public class Decaissement {

    private Long id;
    private final String numeroDecaissement;
    private final TypeDepense typeDepense;
    private final String matriculeProposant;
    private String matriculeValidateurCG;
    private String matriculeValidateurDG;
    private String matriculeExecuteur;
    private final double montantFCFA;
    private final String beneficiaire;
    private final LocalDate dateDepense;
    private String motif;
    private String motifAnnulation;
    private StatutDecaissement statut;
    private final LocalDateTime dateCreation;
    private LocalDateTime dateModification;
    private Long version;

    private Decaissement(String numeroDecaissement, TypeDepense typeDepense, String matriculeProposant,
                         double montantFCFA, String beneficiaire, LocalDate dateDepense,
                         String motif, LocalDateTime dateCreation) {
        this.numeroDecaissement = Objects.requireNonNull(numeroDecaissement);
        this.typeDepense = Objects.requireNonNull(typeDepense);
        this.matriculeProposant = Objects.requireNonNull(matriculeProposant);
        if (montantFCFA <= 0) throw new IllegalArgumentException("Le montant doit être positif");
        this.montantFCFA = montantFCFA;
        this.beneficiaire = Objects.requireNonNull(beneficiaire);
        this.dateDepense = Objects.requireNonNull(dateDepense);
        this.motif = motif;
        this.dateCreation = dateCreation;
        this.statut = StatutDecaissement.PROPOSE;
        this.version = 0L;
    }

    public static Decaissement proposer(String numeroDecaissement, TypeDepense typeDepense,
                                         String matriculeProposant, double montantFCFA,
                                         String beneficiaire, LocalDate dateDepense, String motif) {
        return new Decaissement(numeroDecaissement, typeDepense, matriculeProposant,
            montantFCFA, beneficiaire, dateDepense, motif, LocalDateTime.now());
    }

    public static Decaissement reconstituer(Long id, String numeroDecaissement, TypeDepense typeDepense,
                                             String matriculeProposant, String matriculeValidateurCG,
                                             String matriculeValidateurDG, String matriculeExecuteur,
                                             double montantFCFA, String beneficiaire, LocalDate dateDepense,
                                             String motif, String motifAnnulation, StatutDecaissement statut,
                                             LocalDateTime dateCreation, LocalDateTime dateModification, Long version) {
        Decaissement d = new Decaissement(numeroDecaissement, typeDepense, matriculeProposant,
            montantFCFA, beneficiaire, dateDepense, motif, dateCreation);
        d.id = id;
        d.matriculeValidateurCG = matriculeValidateurCG;
        d.matriculeValidateurDG = matriculeValidateurDG;
        d.matriculeExecuteur = matriculeExecuteur;
        d.motifAnnulation = motifAnnulation;
        d.statut = statut;
        d.dateModification = dateModification;
        d.version = version;
        return d;
    }

    public void validerParDG(String matriculeDG) {
        if (statut != StatutDecaissement.PROPOSE) {
            throw new IllegalStateException("Seul un décaissement PROPOSE peut être validé par le DG");
        }
        if (matriculeProposant.equals(matriculeDG)) {
            throw new IllegalStateException("Séparation des fonctions (§4.2) : l'acteur qui propose ne peut pas valider");
        }
        this.matriculeValidateurDG = Objects.requireNonNull(matriculeDG);
        this.statut = StatutDecaissement.VALIDEE_CG;
        this.dateModification = LocalDateTime.now();
    }

    public void approuverParDG(String matriculeDG) {
        if (statut != StatutDecaissement.VALIDEE_CG) {
            throw new IllegalStateException("Le décaissement doit être VALIDEE_CG avant approbation finale DG");
        }
        this.matriculeValidateurDG = Objects.requireNonNull(matriculeDG);
        this.statut = StatutDecaissement.VALIDEE_DG;
        this.dateModification = LocalDateTime.now();
    }

    public void executer(String matriculeExecuteur) {
        if (statut != StatutDecaissement.VALIDEE_DG) {
            throw new IllegalStateException("Seul un décaissement VALIDEE_DG peut être exécuté");
        }
        if (matriculeProposant.equals(matriculeExecuteur)) {
            throw new IllegalStateException("Séparation des fonctions (§4.2) : l'acteur qui propose ne peut pas exécuter");
        }
        this.matriculeExecuteur = Objects.requireNonNull(matriculeExecuteur);
        this.statut = StatutDecaissement.EXECUTE;
        this.dateModification = LocalDateTime.now();
    }

    public void annuler(String motif, String matricule) {
        if (statut == StatutDecaissement.EXECUTE) {
            throw new IllegalStateException("Un décaissement exécuté ne peut pas être annulé (§4.3 — contre-écriture)");
        }
        this.motifAnnulation = motif;
        this.statut = StatutDecaissement.ANNULE;
        this.dateModification = LocalDateTime.now();
    }

    public Long getId() { return id; }
    public String getNumeroDecaissement() { return numeroDecaissement; }
    public TypeDepense getTypeDepense() { return typeDepense; }
    public String getMatriculeProposant() { return matriculeProposant; }
    public String getMatriculeValidateurCG() { return matriculeValidateurCG; }
    public String getMatriculeValidateurDG() { return matriculeValidateurDG; }
    public String getMatriculeExecuteur() { return matriculeExecuteur; }
    public double getMontantFCFA() { return montantFCFA; }
    public String getBeneficiaire() { return beneficiaire; }
    public LocalDate getDateDepense() { return dateDepense; }
    public String getMotif() { return motif; }
    public String getMotifAnnulation() { return motifAnnulation; }
    public StatutDecaissement getStatut() { return statut; }
    public LocalDateTime getDateCreation() { return dateCreation; }
    public LocalDateTime getDateModification() { return dateModification; }
    public Long getVersion() { return version; }

    @Override public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Decaissement)) return false;
        return Objects.equals(numeroDecaissement, ((Decaissement) o).numeroDecaissement);
    }
    @Override public int hashCode() { return Objects.hash(numeroDecaissement); }
}
