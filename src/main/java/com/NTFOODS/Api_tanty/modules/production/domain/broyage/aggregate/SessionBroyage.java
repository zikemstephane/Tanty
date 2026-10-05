package com.NTFOODS.Api_tanty.modules.production.domain.broyage.aggregate;

import com.NTFOODS.Api_tanty.modules.production.domain.broyage.entity.ObjectifMachiniste;
import com.NTFOODS.Api_tanty.modules.production.domain.broyage.entity.RealisationMachiniste;
import com.NTFOODS.Api_tanty.modules.production.domain.broyage.enums.OperationBroyage;
import com.NTFOODS.Api_tanty.modules.production.domain.broyage.enums.StatutSessionBroyage;
import com.NTFOODS.Api_tanty.modules.production.domain.broyage.enums.TypePoudre;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.*;

/**
 * Agrégat Racine — Session Broyage Journalière (Chef Machiniste).
 *
 * Une session couvre une journée de broyage pour un type de poudre (maïs, soja ou arachide).
 * Le Chef Machiniste y définit les objectifs par machiniste et enregistre les réalisations.
 * La clôture de fin de journée saisit les indicateurs globaux (quantité nette, pertes).
 */
public class SessionBroyage {

    private Long id;
    private final String referencePPH;
    private final LocalDate date;
    private final TypePoudre typePoudre;
    private final double objectifJournalierKg;
    private final String matriculeChefMachiniste;
    private final List<ObjectifMachiniste> objectifsMachinistes;
    private final List<RealisationMachiniste> realisations;
    private Double quantiteNetteBroyeeKg;
    private Double pertesKg;
    private double depassementAutoriseKg;
    private String justificatifNonValidation;
    private StatutSessionBroyage statut;
    private final LocalDateTime dateCreation;
    private LocalDateTime dateModification;
    private Long version;

    private SessionBroyage(String referencePPH, LocalDate date, TypePoudre typePoudre,
                            double objectifJournalierKg, String matriculeChefMachiniste,
                            LocalDateTime dateCreation) {
        this.referencePPH = Objects.requireNonNull(referencePPH);
        this.date = Objects.requireNonNull(date);
        this.typePoudre = Objects.requireNonNull(typePoudre);
        if (objectifJournalierKg <= 0) throw new IllegalArgumentException("L'objectif journalier doit être positif");
        this.objectifJournalierKg = objectifJournalierKg;
        this.matriculeChefMachiniste = Objects.requireNonNull(matriculeChefMachiniste);
        this.objectifsMachinistes = new ArrayList<>();
        this.realisations = new ArrayList<>();
        this.statut = StatutSessionBroyage.OUVERTE;
        this.dateCreation = dateCreation;
        this.version = 0L;
    }

    public static SessionBroyage ouvrir(String referencePPH, LocalDate date, TypePoudre typePoudre,
                                         double objectifJournalierKg, String matriculeChefMachiniste,
                                         List<ObjectifMachiniste> objectifs) {
        if (objectifs == null || objectifs.isEmpty()) {
            throw new IllegalArgumentException("La session doit comporter au moins un objectif machiniste");
        }
        SessionBroyage session = new SessionBroyage(referencePPH, date, typePoudre,
            objectifJournalierKg, matriculeChefMachiniste, LocalDateTime.now());
        session.objectifsMachinistes.addAll(objectifs);
        return session;
    }

    public static SessionBroyage reconstituer(Long id, String referencePPH, LocalDate date,
                                               TypePoudre typePoudre, double objectifJournalierKg,
                                               String matriculeChefMachiniste,
                                               List<ObjectifMachiniste> objectifs,
                                               List<RealisationMachiniste> realisations,
                                               Double quantiteNetteBroyeeKg, Double pertesKg,
                                               double depassementAutoriseKg, String justificatifNonValidation,
                                               StatutSessionBroyage statut, LocalDateTime dateCreation,
                                               LocalDateTime dateModification, Long version) {
        SessionBroyage s = new SessionBroyage(referencePPH, date, typePoudre, objectifJournalierKg,
            matriculeChefMachiniste, dateCreation);
        s.id = id;
        s.objectifsMachinistes.addAll(objectifs != null ? objectifs : List.of());
        s.realisations.addAll(realisations != null ? realisations : List.of());
        s.quantiteNetteBroyeeKg = quantiteNetteBroyeeKg;
        s.pertesKg = pertesKg;
        s.depassementAutoriseKg = depassementAutoriseKg;
        s.justificatifNonValidation = justificatifNonValidation;
        s.statut = statut;
        s.dateModification = dateModification;
        s.version = version;
        return s;
    }

    /** Enregistre ou met à jour la réalisation d'un machiniste pour une opération. */
    public void enregistrerRealisation(RealisationMachiniste realisation) {
        if (statut == StatutSessionBroyage.CLOTUREE) {
            throw new IllegalStateException("Impossible de modifier une session clôturée");
        }
        realisations.removeIf(r ->
            r.getMachinisteMatricule().equals(realisation.getMachinisteMatricule()) &&
            r.getOperation() == realisation.getOperation() &&
            r.getTypePoudre() == realisation.getTypePoudre());
        realisations.add(realisation);
        this.dateModification = LocalDateTime.now();
    }

    /** Clôture la session avec les indicateurs globaux de fin de journée. */
    public void cloturer(double quantiteNetteBroyeeKg, double pertesKg) {
        if (statut == StatutSessionBroyage.CLOTUREE) {
            throw new IllegalStateException("La session est déjà clôturée");
        }
        if (quantiteNetteBroyeeKg < 0) throw new IllegalArgumentException("La quantité nette doit être positive");
        if (pertesKg < 0) throw new IllegalArgumentException("Les pertes ne peuvent être négatives");
        double totalAutorise = objectifJournalierKg + depassementAutoriseKg;
        if (quantiteNetteBroyeeKg > totalAutorise) {
            throw new IllegalStateException(
                "Dépassement non autorisé : quantité nette " + quantiteNetteBroyeeKg +
                " kg > objectif + dépassement autorisé (" + totalAutorise + " kg). " +
                "Le Chef Machiniste doit autoriser le dépassement.");
        }
        this.quantiteNetteBroyeeKg = quantiteNetteBroyeeKg;
        this.pertesKg = pertesKg;
        this.statut = StatutSessionBroyage.CLOTUREE;
        this.dateModification = LocalDateTime.now();
    }

    /**
     * Autorise un dépassement de quantité par rapport à l'objectif journalier.
     */
    public void autoriserDepassement(double quantiteSupplementaireKg) {
        if (quantiteSupplementaireKg <= 0) throw new IllegalArgumentException("Le dépassement doit être positif");
        this.depassementAutoriseKg += quantiteSupplementaireKg;
        this.dateModification = LocalDateTime.now();
    }

    /**
     * Justifie une journée en dessous du seuil de validation (85 %).
     */
    public void justifierNonValidation(String justificatif) {
        if (justificatif == null || justificatif.isBlank()) {
            throw new IllegalArgumentException("Le justificatif est obligatoire pour valider une journée sous le seuil");
        }
        this.justificatifNonValidation = justificatif;
        this.dateModification = LocalDateTime.now();
    }

    /**
     * Seuil de validation : 85 % de l'objectif journalier.
     * La journée est validée si le total réalisé atteint au moins 85 %,
     * ou si un justificatif a été enregistré.
     */
    public boolean estJourneeValidee() {
        if (justificatifNonValidation != null && !justificatifNonValidation.isBlank()) {
            return true;
        }
        if (objectifJournalierKg <= 0) return true;
        return totalRealiseKg() >= objectifJournalierKg * 0.85;
    }

    /** % de pertes sur la quantité brute initiale (nette + pertes). */
    public Double getPctPertes() {
        if (quantiteNetteBroyeeKg == null || pertesKg == null) return null;
        double brut = quantiteNetteBroyeeKg + pertesKg;
        if (brut == 0) return 0.0;
        return (pertesKg / brut) * 100.0;
    }

    /** Total kg réalisés par tous les machinistes. */
    public double totalRealiseKg() {
        return realisations.stream().mapToDouble(RealisationMachiniste::getRealiseKg).sum();
    }

    public Long getId() { return id; }
    public String getReferencePPH() { return referencePPH; }
    public LocalDate getDate() { return date; }
    public TypePoudre getTypePoudre() { return typePoudre; }
    public double getObjectifJournalierKg() { return objectifJournalierKg; }
    public String getMatriculeChefMachiniste() { return matriculeChefMachiniste; }
    public List<ObjectifMachiniste> getObjectifsMachinistes() { return Collections.unmodifiableList(objectifsMachinistes); }
    public List<RealisationMachiniste> getRealisations() { return Collections.unmodifiableList(realisations); }
    public Double getQuantiteNetteBroyeeKg() { return quantiteNetteBroyeeKg; }
    public Double getPertesKg() { return pertesKg; }
    public double getDepassementAutoriseKg() { return depassementAutoriseKg; }
    public String getJustificatifNonValidation() { return justificatifNonValidation; }
    public StatutSessionBroyage getStatut() { return statut; }
    public LocalDateTime getDateCreation() { return dateCreation; }
    public LocalDateTime getDateModification() { return dateModification; }
    public Long getVersion() { return version; }

    @Override public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof SessionBroyage)) return false;
        SessionBroyage that = (SessionBroyage) o;
        return Objects.equals(referencePPH, that.referencePPH) &&
               Objects.equals(date, that.date) && typePoudre == that.typePoudre;
    }
    @Override public int hashCode() { return Objects.hash(referencePPH, date, typePoudre); }
    @Override public String toString() {
        return "SessionBroyage{" + referencePPH + ", " + date + ", " + typePoudre + ", " + statut + "}";
    }
}
