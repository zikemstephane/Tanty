package com.NTFOODS.Api_tanty.modules.comptabilite.domain.caisse.aggregate;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Objects;

/**
 * Agrégat Racine — Paramétrage de la caisse et des versements.
 *
 * <p>Réunit deux seuils qui gouvernaient jusqu'ici des comportements automatiques
 * depuis des constantes compilées : celui au-delà duquel le solde de caisse doit
 * être sécurisé, et l'écart de versement au-delà duquel une alerte rouge est
 * levée.</p>
 *
 * <p>Le seuil de sécurisation était écrit à trois endroits — l'agrégat
 * {@code JournalCaisse}, le tableau de bord de la secrétaire et le fichier de
 * configuration — sans qu'aucun ne fasse autorité. Ce paramétrage devient la
 * source unique.</p>
 *
 * <p>Comme les barèmes de prime, les paramétrages se succèdent par périodes :
 * une caisse ouverte hier conserve le seuil qui s'appliquait hier.</p>
 */
public class ParametrageCaisse {

    private Long id;
    private final String reference;
    private final double seuilSecurisationFCFA;
    private final double seuilAlerteRougeVersementFCFA;
    private final LocalDate dateDebut;
    private LocalDate dateFin;
    private final String matriculeAuteur;
    private final String motif;
    private final LocalDateTime dateCreation;
    private Long version;

    private ParametrageCaisse(String reference, double seuilSecurisationFCFA,
                               double seuilAlerteRougeVersementFCFA, LocalDate dateDebut,
                               String matriculeAuteur, String motif, LocalDateTime dateCreation) {
        this.reference = Objects.requireNonNull(reference, "La référence du paramétrage est obligatoire");
        if (seuilSecurisationFCFA <= 0) {
            throw new IllegalArgumentException("Le seuil de sécurisation doit être strictement positif");
        }
        if (seuilAlerteRougeVersementFCFA < 0) {
            throw new IllegalArgumentException("Le seuil d'alerte rouge ne peut pas être négatif");
        }
        this.seuilSecurisationFCFA = seuilSecurisationFCFA;
        this.seuilAlerteRougeVersementFCFA = seuilAlerteRougeVersementFCFA;
        this.dateDebut = Objects.requireNonNull(dateDebut, "La date de prise d'effet est obligatoire");
        this.matriculeAuteur = Objects.requireNonNull(matriculeAuteur, "L'auteur est obligatoire");
        this.motif = (motif != null && !motif.isBlank()) ? motif : null;
        this.dateCreation = dateCreation;
        this.version = 0L;
    }

    public static ParametrageCaisse creer(String reference, double seuilSecurisationFCFA,
                                           double seuilAlerteRougeVersementFCFA, LocalDate dateDebut,
                                           String matriculeAuteur, String motif) {
        return new ParametrageCaisse(reference, seuilSecurisationFCFA, seuilAlerteRougeVersementFCFA,
            dateDebut, matriculeAuteur, motif, LocalDateTime.now());
    }

    public static ParametrageCaisse reconstituer(Long id, String reference, double seuilSecurisationFCFA,
                                                  double seuilAlerteRougeVersementFCFA, LocalDate dateDebut,
                                                  LocalDate dateFin, String matriculeAuteur, String motif,
                                                  LocalDateTime dateCreation, Long version) {
        ParametrageCaisse p = new ParametrageCaisse(reference, seuilSecurisationFCFA,
            seuilAlerteRougeVersementFCFA, dateDebut, matriculeAuteur, motif, dateCreation);
        p.id = id;
        p.dateFin = dateFin;
        p.version = version;
        return p;
    }

    /**
     * Valeurs appliquées avant la mise en place du paramétrage.
     *
     * <p>Reprend les constantes qui étaient compilées, afin que son introduction
     * ne modifie aucun comportement existant.</p>
     */
    public static ParametrageCaisse reglesHistoriques() {
        return new ParametrageCaisse("PARAM-CAISSE-HISTORIQUE", 500_000.0, 5_000.0,
            LocalDate.of(2000, 1, 1), "SYSTEME",
            "Valeurs appliquées avant la mise en place du paramétrage", LocalDateTime.now());
    }

    public void cloturerAvant(LocalDate dateEffetSuccesseur) {
        if (dateFin != null) {
            throw new IllegalStateException("Le paramétrage " + reference + " est déjà clôturé au " + dateFin);
        }
        LocalDate nouvelleFin = dateEffetSuccesseur.minusDays(1);
        if (nouvelleFin.isBefore(dateDebut)) {
            throw new IllegalArgumentException(
                "Un paramétrage ne peut pas être clôturé avant sa prise d'effet du " + dateDebut);
        }
        this.dateFin = nouvelleFin;
    }

    public boolean estApplicableA(LocalDate date) {
        if (date.isBefore(dateDebut)) return false;
        return dateFin == null || !date.isAfter(dateFin);
    }

    public boolean estCourant() {
        return dateFin == null;
    }

    /** Vrai si un écart de versement justifie une alerte rouge. */
    public boolean declencheAlerteRouge(double montantAttendu, double cashVerse) {
        double ecart = cashVerse - montantAttendu;
        return Math.abs(ecart) > seuilAlerteRougeVersementFCFA && cashVerse < montantAttendu;
    }

    public Long getId() { return id; }
    public String getReference() { return reference; }
    public double getSeuilSecurisationFCFA() { return seuilSecurisationFCFA; }
    public double getSeuilAlerteRougeVersementFCFA() { return seuilAlerteRougeVersementFCFA; }
    public LocalDate getDateDebut() { return dateDebut; }
    public LocalDate getDateFin() { return dateFin; }
    public String getMatriculeAuteur() { return matriculeAuteur; }
    public String getMotif() { return motif; }
    public LocalDateTime getDateCreation() { return dateCreation; }
    public Long getVersion() { return version; }

    @Override public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof ParametrageCaisse that)) return false;
        return Objects.equals(reference, that.reference);
    }
    @Override public int hashCode() { return Objects.hash(reference); }
}
