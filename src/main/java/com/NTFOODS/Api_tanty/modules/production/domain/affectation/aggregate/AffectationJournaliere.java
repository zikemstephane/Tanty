package com.NTFOODS.Api_tanty.modules.production.domain.affectation.aggregate;

import com.NTFOODS.Api_tanty.modules.production.domain.affectation.entity.LigneAffectationPoste;
import com.NTFOODS.Api_tanty.modules.production.domain.affectation.entity.SaisieRealisationPoste;
import com.NTFOODS.Api_tanty.modules.production.domain.affectation.enums.StatutAffectation;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.*;

/**
 * Agrégat Racine — Affectation Journalière des postes de production (responsable de salle).
 *
 * Créée chaque matin : l'agent affecte chaque employé à un poste pour la journée
 * (demi-journée ou journée complète). L'objectif ajusté est calculé automatiquement.
 * En fin de journée, il saisit les réalisations par poste + par employé.
 * Le système calcule le % de réalisation et le classement des postes.
 */
public class AffectationJournaliere {

    private Long id;
    private final String referencePPH;
    private final LocalDate date;
    private final String matriculeAgent;
    private final List<LigneAffectationPoste> lignes;
    private final List<SaisieRealisationPoste> saisies;
    private StatutAffectation statut;
    private final LocalDateTime dateCreation;
    private LocalDateTime dateModification;
    private Long version;

    private AffectationJournaliere(String referencePPH, LocalDate date,
                                    String matriculeAgent, LocalDateTime dateCreation) {
        this.referencePPH = Objects.requireNonNull(referencePPH);
        this.date = Objects.requireNonNull(date);
        this.matriculeAgent = Objects.requireNonNull(matriculeAgent);
        this.lignes = new ArrayList<>();
        this.saisies = new ArrayList<>();
        this.statut = StatutAffectation.BROUILLON;
        this.dateCreation = dateCreation;
        this.version = 0L;
    }

    public static AffectationJournaliere creer(String referencePPH, LocalDate date,
                                                String matriculeAgent,
                                                List<LigneAffectationPoste> lignes) {
        if (lignes == null || lignes.isEmpty()) {
            throw new IllegalArgumentException("L'affectation doit comporter au moins un poste");
        }
        AffectationJournaliere aff = new AffectationJournaliere(referencePPH, date, matriculeAgent, LocalDateTime.now());
        aff.lignes.addAll(lignes);
        return aff;
    }

    public static AffectationJournaliere reconstituer(Long id, String referencePPH, LocalDate date,
                                                       String matriculeAgent,
                                                       List<LigneAffectationPoste> lignes,
                                                       List<SaisieRealisationPoste> saisies,
                                                       StatutAffectation statut,
                                                       LocalDateTime dateCreation,
                                                       LocalDateTime dateModification, Long version) {
        AffectationJournaliere a = new AffectationJournaliere(referencePPH, date, matriculeAgent, dateCreation);
        a.id = id;
        a.lignes.addAll(lignes != null ? lignes : List.of());
        a.saisies.addAll(saisies != null ? saisies : List.of());
        a.statut = statut;
        a.dateModification = dateModification;
        a.version = version;
        return a;
    }

    /**
     * Enregistre ou remplace la saisie de réalisation pour un poste.
     * "LOCK NUMÉRIQUE" (Pilier 1) : refusé une fois l'affectation validée.
     */
    public void saisirRealisation(SaisieRealisationPoste saisie) {
        Objects.requireNonNull(saisie);
        saisies.removeIf(s -> s.getPoste().equals(saisie.getPoste()));
        saisies.add(saisie);
        this.dateModification = LocalDateTime.now();
    }

    /**
     * Modifie les lignes d'affectation (seulement si l'affectation est en BROUILLON).
     */
    public void modifierLignes(List<LigneAffectationPoste> nouvellesLignes) {
        if (statut == StatutAffectation.VALIDEE) {
            throw new IllegalStateException(
                "Affectation verrouillée : impossible de modifier une affectation validée.");
        }
        if (nouvellesLignes == null || nouvellesLignes.isEmpty()) {
            throw new IllegalArgumentException("L'affectation doit comporter au moins un poste");
        }
        this.lignes.clear();
        this.lignes.addAll(nouvellesLignes);
        this.dateModification = LocalDateTime.now();
    }

    /** Valide l'affectation : verrouille le planning (lignes) — les saisies de réalisations restent possibles. */
    public void valider() {
        if (statut == StatutAffectation.VALIDEE) {
            throw new IllegalStateException("L'affectation est déjà validée");
        }
        this.statut = StatutAffectation.VALIDEE;
        this.dateModification = LocalDateTime.now();
    }

    /**
     * Retourne le classement des postes par % de réalisation décroissant.
     * Seuls les postes ayant une saisie ET un objectif > 0 sont classés.
     */
    public List<SaisieRealisationPoste> getClassementPostes() {
        return saisies.stream()
            .filter(s -> s.getObjectifAjuste() > 0)
            .sorted(Comparator.comparingDouble(SaisieRealisationPoste::getPctRealisation).reversed())
            .toList();
    }

    public Optional<LigneAffectationPoste> getLignePourPoste(String poste) {
        return lignes.stream().filter(l -> l.getPoste().equals(poste)).findFirst();
    }

    public Long getId() { return id; }
    public String getReferencePPH() { return referencePPH; }
    public LocalDate getDate() { return date; }
    public String getMatriculeAgent() { return matriculeAgent; }
    public List<LigneAffectationPoste> getLignes() { return Collections.unmodifiableList(lignes); }
    public List<SaisieRealisationPoste> getSaisies() { return Collections.unmodifiableList(saisies); }
    public StatutAffectation getStatut() { return statut; }
    public LocalDateTime getDateCreation() { return dateCreation; }
    public LocalDateTime getDateModification() { return dateModification; }
    public Long getVersion() { return version; }

    @Override public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof AffectationJournaliere)) return false;
        AffectationJournaliere that = (AffectationJournaliere) o;
        return Objects.equals(referencePPH, that.referencePPH) && Objects.equals(date, that.date);
    }
    @Override public int hashCode() { return Objects.hash(referencePPH, date); }
    @Override public String toString() {
        return "AffectationJournaliere{" + referencePPH + ", " + date + ", " + statut + "}";
    }
}
