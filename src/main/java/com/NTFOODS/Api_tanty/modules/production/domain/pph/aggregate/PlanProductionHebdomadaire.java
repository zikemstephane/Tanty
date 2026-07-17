package com.NTFOODS.Api_tanty.modules.production.domain.pph.aggregate;

import com.NTFOODS.Api_tanty.modules.production.domain.pph.entity.LignePlanDetail;
import com.NTFOODS.Api_tanty.modules.production.domain.pph.entity.LignePPH;
import com.NTFOODS.Api_tanty.modules.production.domain.pph.entity.RepartitionJournaliere;
import com.NTFOODS.Api_tanty.modules.production.domain.pph.enums.StatutPPH;
import com.NTFOODS.Api_tanty.modules.production.domain.pph.events.PPHValideEvent;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.*;

/**
 * Agrégat Racine — Plan de Production Hebdomadaire (PPH).
 *
 * Document de référence de la semaine de production : liste les produits à fabriquer,
 * les objectifs par produit, et suit l'avancement cumulé via les Fiches Journalières.
 *
 * Invariants :
 * - Une seule fiche journalière par (PPH, date)
 * - La surproduction n'est pas bloquante mais tracée
 * - Le PPH passe de BROUILLON → VALIDE avant d'accepter des fiches journalières
 */
public class PlanProductionHebdomadaire {

    private Long id;
    private final String referencePPH;
    private final String referenceBC;
    private final String semaine;
    private final LocalDate dateDebut;
    private final LocalDate dateFin;
    private final String matriculeChefProduction;
    private final List<LignePPH> lignes;
    private final List<LignePlanDetail> lignesDetaillees;
    private final List<RepartitionJournaliere> repartitionJours;
    private LocalDate dateSoumission;
    private StatutPPH statut;
    private final LocalDateTime dateCreation;
    private LocalDateTime dateModification;
    private Long version;

    private final List<Object> domainEvents = new ArrayList<>();

    private PlanProductionHebdomadaire(String referencePPH, String referenceBC, String semaine, LocalDate dateDebut,
                                        LocalDate dateFin, String matriculeChefProduction,
                                        LocalDateTime dateCreation) {
        this.referencePPH = Objects.requireNonNull(referencePPH);
        this.referenceBC = referenceBC;
        this.semaine = Objects.requireNonNull(semaine);
        this.dateDebut = Objects.requireNonNull(dateDebut);
        this.dateFin = Objects.requireNonNull(dateFin);
        this.matriculeChefProduction = Objects.requireNonNull(matriculeChefProduction);
        this.dateCreation = dateCreation;
        this.lignes = new ArrayList<>();
        this.lignesDetaillees = new ArrayList<>();
        this.repartitionJours = new ArrayList<>();
        this.statut = StatutPPH.BROUILLON;
        this.version = 0L;
    }

    public static PlanProductionHebdomadaire creer(String referencePPH, String semaine,
                                                    LocalDate dateDebut, LocalDate dateFin,
                                                    String matriculeChefProduction,
                                                    List<LignePPH> lignesProduits) {
        return creerAvecBC(referencePPH, null, semaine, dateDebut, dateFin, matriculeChefProduction, lignesProduits);
    }

    public static PlanProductionHebdomadaire creerAvecBC(String referencePPH, String referenceBC, String semaine,
                                                    LocalDate dateDebut, LocalDate dateFin,
                                                    String matriculeChefProduction,
                                                    List<LignePPH> lignesProduits) {
        if (dateDebut.isAfter(dateFin)) {
            throw new IllegalArgumentException("La date de début doit être avant la date de fin");
        }
        if (lignesProduits == null || lignesProduits.isEmpty()) {
            throw new IllegalArgumentException("Le PPH doit contenir au moins un produit planifié");
        }
        PlanProductionHebdomadaire pph = new PlanProductionHebdomadaire(
            referencePPH, referenceBC, semaine, dateDebut, dateFin, matriculeChefProduction, LocalDateTime.now()
        );
        pph.lignes.addAll(lignesProduits);
        return pph;
    }

    public static PlanProductionHebdomadaire creerAvecDetails(String referencePPH, String referenceBC, String semaine,
                                                    LocalDate dateDebut, LocalDate dateFin,
                                                    String matriculeChefProduction,
                                                    List<LignePPH> lignesProduits,
                                                    List<LignePlanDetail> lignesDetaillees,
                                                    List<RepartitionJournaliere> repartitionJours,
                                                    LocalDate dateSoumission) {
        PlanProductionHebdomadaire pph = creerAvecBC(referencePPH, referenceBC, semaine, dateDebut, dateFin,
            matriculeChefProduction, lignesProduits);
        if (lignesDetaillees != null) pph.lignesDetaillees.addAll(lignesDetaillees);
        if (repartitionJours != null) pph.repartitionJours.addAll(repartitionJours);
        pph.dateSoumission = dateSoumission;
        return pph;
    }

    public static PlanProductionHebdomadaire reconstituer(Long id, String referencePPH, String semaine,
                                                           LocalDate dateDebut, LocalDate dateFin,
                                                           String matriculeChefProduction,
                                                           List<LignePPH> lignes, StatutPPH statut,
                                                           LocalDateTime dateCreation,
                                                           LocalDateTime dateModification, Long version) {
        return reconstituerAvecBC(id, referencePPH, null, semaine, dateDebut, dateFin,
            matriculeChefProduction, lignes, statut, dateCreation, dateModification, version);
    }

    public static PlanProductionHebdomadaire reconstituerAvecBC(Long id, String referencePPH, String referenceBC,
                                                           String semaine,
                                                           LocalDate dateDebut, LocalDate dateFin,
                                                           String matriculeChefProduction,
                                                           List<LignePPH> lignes, StatutPPH statut,
                                                           LocalDateTime dateCreation,
                                                           LocalDateTime dateModification, Long version) {
        return reconstituerComplet(id, referencePPH, referenceBC, semaine, dateDebut, dateFin,
            matriculeChefProduction, lignes, List.of(), List.of(), null, statut,
            dateCreation, dateModification, version);
    }

    public static PlanProductionHebdomadaire reconstituerComplet(Long id, String referencePPH, String referenceBC,
                                                           String semaine,
                                                           LocalDate dateDebut, LocalDate dateFin,
                                                           String matriculeChefProduction,
                                                           List<LignePPH> lignes,
                                                           List<LignePlanDetail> lignesDetaillees,
                                                           List<RepartitionJournaliere> repartitionJours,
                                                           LocalDate dateSoumission,
                                                           StatutPPH statut,
                                                           LocalDateTime dateCreation,
                                                           LocalDateTime dateModification, Long version) {
        PlanProductionHebdomadaire pph = new PlanProductionHebdomadaire(
            referencePPH, referenceBC, semaine, dateDebut, dateFin, matriculeChefProduction, dateCreation
        );
        pph.id = id;
        pph.lignes.addAll(lignes != null ? lignes : List.of());
        pph.lignesDetaillees.addAll(lignesDetaillees != null ? lignesDetaillees : List.of());
        pph.repartitionJours.addAll(repartitionJours != null ? repartitionJours : List.of());
        pph.dateSoumission = dateSoumission;
        pph.statut = statut;
        pph.dateModification = dateModification;
        pph.version = version;
        return pph;
    }

    public void valider() {
        if (statut != StatutPPH.BROUILLON) {
            throw new IllegalStateException("Seul un PPH en BROUILLON peut être validé");
        }
        this.statut = StatutPPH.VALIDE;
        this.dateModification = LocalDateTime.now();
        domainEvents.add(new PPHValideEvent(referencePPH, semaine, dateDebut, matriculeChefProduction));
    }

    public void demarrer() {
        if (statut != StatutPPH.VALIDE) {
            throw new IllegalStateException("Le PPH doit être validé avant de démarrer");
        }
        this.statut = StatutPPH.EN_COURS;
        this.dateModification = LocalDateTime.now();
    }

    public void cloturer() {
        if (statut != StatutPPH.EN_COURS) {
            throw new IllegalStateException("Seul un PPH EN_COURS peut être clôturé");
        }
        this.statut = StatutPPH.CLOTURE;
        this.dateModification = LocalDateTime.now();
    }

    /**
     * Ajuste manuellement la répartition journalière des ressources.
     * Permet au Responsable de Salle de corriger les quantités de poudre,
     * fûts, emballages selon les contraintes opérationnelles réelles.
     * Autorisé uniquement sur un PPH BROUILLON ou VALIDE.
     */
    public void ajusterRepartitionJours(List<RepartitionJournaliere> nouvelleRepartition) {
        if (statut == StatutPPH.CLOTURE) {
            throw new IllegalStateException("Impossible d'ajuster la répartition d'un PPH clôturé");
        }
        this.repartitionJours.clear();
        if (nouvelleRepartition != null) {
            this.repartitionJours.addAll(nouvelleRepartition);
        }
        this.dateModification = LocalDateTime.now();
    }

    public void cumulerProductionLigne(String codeProduit, int quantite) {
        if (statut != StatutPPH.EN_COURS) {
            throw new IllegalStateException("La production ne peut être cumulée que pour un PPH EN_COURS");
        }
        lignes.stream()
            .filter(l -> l.getCodeProduit().equals(codeProduit))
            .findFirst()
            .orElseThrow(() -> new IllegalArgumentException("Produit " + codeProduit + " non trouvé dans le PPH"))
            .cumulerProduction(quantite);
        this.dateModification = LocalDateTime.now();
    }

    public void ajusterProductionLigne(String codeProduit, int delta) {
        if (statut != StatutPPH.EN_COURS) {
            throw new IllegalStateException("La production ne peut être ajustée que pour un PPH EN_COURS");
        }
        lignes.stream()
            .filter(l -> l.getCodeProduit().equals(codeProduit))
            .findFirst()
            .orElseThrow(() -> new IllegalArgumentException("Produit " + codeProduit + " non trouvé dans le PPH"))
            .ajusterProduction(delta);
        this.dateModification = LocalDateTime.now();
    }

    public int tauxAvancementGlobal() {
        if (lignes.isEmpty()) return 0;
        return (int) lignes.stream().mapToInt(LignePPH::tauxAvancement).average().orElse(0);
    }

    public List<Object> getDomainEvents() { return Collections.unmodifiableList(domainEvents); }
    public void clearDomainEvents() { domainEvents.clear(); }

    public Long getId() { return id; }
    public String getReferencePPH() { return referencePPH; }
    public String getReferenceBC() { return referenceBC; }
    public String getSemaine() { return semaine; }
    public LocalDate getDateDebut() { return dateDebut; }
    public LocalDate getDateFin() { return dateFin; }
    public String getMatriculeChefProduction() { return matriculeChefProduction; }
    public List<LignePPH> getLignes() { return Collections.unmodifiableList(lignes); }
    public List<LignePlanDetail> getLignesDetaillees() { return Collections.unmodifiableList(lignesDetaillees); }
    public List<RepartitionJournaliere> getRepartitionJours() { return Collections.unmodifiableList(repartitionJours); }
    public LocalDate getDateSoumission() { return dateSoumission; }
    public StatutPPH getStatut() { return statut; }
    public LocalDateTime getDateCreation() { return dateCreation; }
    public LocalDateTime getDateModification() { return dateModification; }
    public Long getVersion() { return version; }

    @Override public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof PlanProductionHebdomadaire)) return false;
        return Objects.equals(referencePPH, ((PlanProductionHebdomadaire) o).referencePPH);
    }
    @Override public int hashCode() { return Objects.hash(referencePPH); }
    @Override public String toString() {
        return "PPH{ref=" + referencePPH + ", semaine=" + semaine + ", statut=" + statut + "}";
    }
}
