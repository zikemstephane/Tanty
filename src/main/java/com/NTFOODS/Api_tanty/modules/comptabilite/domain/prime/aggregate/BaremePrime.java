package com.NTFOODS.Api_tanty.modules.comptabilite.domain.prime.aggregate;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Objects;

/**
 * Agrégat Racine — Barème de calcul des primes commerciales.
 *
 * <p>Porte les quatre valeurs qui déterminent une prime hebdomadaire : le taux de
 * réalisation global ouvrant droit à la prime, le taux par gamme validant chaque
 * gamme, le montant accordé par gamme validée, et le plafond total.</p>
 *
 * <p>Ces valeurs étaient auparavant compilées dans {@code PrimeCommerciale}. Les
 * porter dans un agrégat daté répond à une exigence précise : une prime contestée
 * doit pouvoir être recalculée <em>avec le barème en vigueur la semaine
 * concernée</em>, et non avec le barème courant. Un simple couple clé/valeur ne le
 * permettrait pas, puisqu'il ne conserve que la dernière valeur.</p>
 *
 * <p>Les barèmes se succèdent sans se chevaucher : ouvrir un barème clôture le
 * précédent la veille de sa prise d'effet. Le barème courant est celui dont la
 * date de fin est absente.</p>
 */
public class BaremePrime {

    private Long id;
    private final String reference;
    private final double tauxGlobalMinimumPourcent;
    private final double tauxGammeMinimumPourcent;
    private final double montantParGammeFCFA;
    private final double montantMaximumFCFA;
    private final LocalDate dateDebut;
    private LocalDate dateFin;
    private final String matriculeAuteur;
    private final String motif;
    private final LocalDateTime dateCreation;
    private Long version;

    private BaremePrime(String reference, double tauxGlobalMinimumPourcent, double tauxGammeMinimumPourcent,
                         double montantParGammeFCFA, double montantMaximumFCFA, LocalDate dateDebut,
                         String matriculeAuteur, String motif, LocalDateTime dateCreation) {
        this.reference = Objects.requireNonNull(reference, "La référence du barème est obligatoire");
        if (tauxGlobalMinimumPourcent < 0 || tauxGlobalMinimumPourcent > 100) {
            throw new IllegalArgumentException("Le taux global minimum doit être compris entre 0 et 100 %");
        }
        if (tauxGammeMinimumPourcent < 0 || tauxGammeMinimumPourcent > 100) {
            throw new IllegalArgumentException("Le taux par gamme minimum doit être compris entre 0 et 100 %");
        }
        if (montantParGammeFCFA < 0) {
            throw new IllegalArgumentException("Le montant par gamme ne peut pas être négatif");
        }
        if (montantMaximumFCFA < montantParGammeFCFA) {
            throw new IllegalArgumentException(
                "Le plafond (" + montantMaximumFCFA + " FCFA) ne peut pas être inférieur au montant "
                + "accordé pour une seule gamme (" + montantParGammeFCFA + " FCFA)");
        }
        this.tauxGlobalMinimumPourcent = tauxGlobalMinimumPourcent;
        this.tauxGammeMinimumPourcent = tauxGammeMinimumPourcent;
        this.montantParGammeFCFA = montantParGammeFCFA;
        this.montantMaximumFCFA = montantMaximumFCFA;
        this.dateDebut = Objects.requireNonNull(dateDebut, "La date de prise d'effet est obligatoire");
        this.matriculeAuteur = Objects.requireNonNull(matriculeAuteur, "L'auteur du barème est obligatoire");
        this.motif = (motif != null && !motif.isBlank()) ? motif : null;
        this.dateCreation = dateCreation;
        this.version = 0L;
    }

    public static BaremePrime creer(String reference, double tauxGlobalMinimumPourcent,
                                     double tauxGammeMinimumPourcent, double montantParGammeFCFA,
                                     double montantMaximumFCFA, LocalDate dateDebut,
                                     String matriculeAuteur, String motif) {
        return new BaremePrime(reference, tauxGlobalMinimumPourcent, tauxGammeMinimumPourcent,
            montantParGammeFCFA, montantMaximumFCFA, dateDebut, matriculeAuteur, motif, LocalDateTime.now());
    }

    public static BaremePrime reconstituer(Long id, String reference, double tauxGlobalMinimumPourcent,
                                            double tauxGammeMinimumPourcent, double montantParGammeFCFA,
                                            double montantMaximumFCFA, LocalDate dateDebut, LocalDate dateFin,
                                            String matriculeAuteur, String motif,
                                            LocalDateTime dateCreation, Long version) {
        BaremePrime b = new BaremePrime(reference, tauxGlobalMinimumPourcent, tauxGammeMinimumPourcent,
            montantParGammeFCFA, montantMaximumFCFA, dateDebut, matriculeAuteur, motif, dateCreation);
        b.id = id;
        b.dateFin = dateFin;
        b.version = version;
        return b;
    }

    /**
     * Barème de repli, appliqué tant qu'aucun barème n'a été défini.
     *
     * <p>Reprend exactement les valeurs qui étaient compilées jusqu'ici, afin que
     * l'introduction du paramétrage ne modifie aucune prime déjà calculée. Il n'est
     * pas persisté : il ne masque donc jamais un barème réel.</p>
     */
    public static BaremePrime reglesHistoriques() {
        return new BaremePrime("BAREME-HISTORIQUE", 80.0, 75.0, 3_000.0, 12_000.0,
            LocalDate.of(2000, 1, 1), "SYSTEME",
            "Valeurs appliquées avant la mise en place du paramétrage", LocalDateTime.now());
    }

    /**
     * Clôture le barème la veille de la prise d'effet de son successeur.
     *
     * @throws IllegalStateException    si le barème est déjà clôturé
     * @throws IllegalArgumentException si la date de fin précède la prise d'effet
     */
    public void cloturerAvant(LocalDate dateEffetSuccesseur) {
        if (dateFin != null) {
            throw new IllegalStateException("Le barème " + reference + " est déjà clôturé au " + dateFin);
        }
        LocalDate nouvelleFin = dateEffetSuccesseur.minusDays(1);
        if (nouvelleFin.isBefore(dateDebut)) {
            throw new IllegalArgumentException(
                "Un barème ne peut pas être clôturé avant sa prise d'effet : le successeur doit prendre "
                + "effet après le " + dateDebut);
        }
        this.dateFin = nouvelleFin;
    }

    /** Vrai si ce barème régit la date donnée. */
    public boolean estApplicableA(LocalDate date) {
        if (date.isBefore(dateDebut)) return false;
        return dateFin == null || !date.isAfter(dateFin);
    }

    /** Vrai tant qu'aucun successeur ne l'a clôturé. */
    public boolean estCourant() {
        return dateFin == null;
    }

    /** Prime due pour un nombre de gammes validées, plafond compris. */
    public double montantPour(int gammesValidees) {
        if (gammesValidees <= 0) return 0.0;
        return Math.min(montantParGammeFCFA * gammesValidees, montantMaximumFCFA);
    }

    public Long getId() { return id; }
    public String getReference() { return reference; }
    public double getTauxGlobalMinimumPourcent() { return tauxGlobalMinimumPourcent; }
    public double getTauxGammeMinimumPourcent() { return tauxGammeMinimumPourcent; }
    public double getMontantParGammeFCFA() { return montantParGammeFCFA; }
    public double getMontantMaximumFCFA() { return montantMaximumFCFA; }
    public LocalDate getDateDebut() { return dateDebut; }
    public LocalDate getDateFin() { return dateFin; }
    public String getMatriculeAuteur() { return matriculeAuteur; }
    public String getMotif() { return motif; }
    public LocalDateTime getDateCreation() { return dateCreation; }
    public Long getVersion() { return version; }

    @Override public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof BaremePrime that)) return false;
        return Objects.equals(reference, that.reference);
    }
    @Override public int hashCode() { return Objects.hash(reference); }
}
