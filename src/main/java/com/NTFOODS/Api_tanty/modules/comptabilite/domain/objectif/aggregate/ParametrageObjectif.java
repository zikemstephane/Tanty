package com.NTFOODS.Api_tanty.modules.comptabilite.domain.objectif.aggregate;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Objects;

/**
 * Agrégat Racine — Paramétrage des objectifs commerciaux.
 *
 * <p>Porte le nombre de jours ouvrés servant à répartir un objectif hebdomadaire
 * en objectifs quotidiens. La valeur était compilée dans
 * {@code ObjectifCommercial} alors qu'elle dépend du calendrier de l'entreprise —
 * une semaine de six jours ouvrés n'a rien d'universel et peut évoluer.</p>
 *
 * <p>Chaque objectif conserve le nombre de jours qui lui a été appliqué : modifier
 * ce paramétrage n'altère donc aucun objectif déjà défini.</p>
 */
public class ParametrageObjectif {

    private Long id;
    private final String reference;
    private final int nombreJoursOuvres;
    private final LocalDate dateDebut;
    private LocalDate dateFin;
    private final String matriculeAuteur;
    private final String motif;
    private final LocalDateTime dateCreation;
    private Long version;

    private ParametrageObjectif(String reference, int nombreJoursOuvres, LocalDate dateDebut,
                                 String matriculeAuteur, String motif, LocalDateTime dateCreation) {
        this.reference = Objects.requireNonNull(reference, "La référence du paramétrage est obligatoire");
        if (nombreJoursOuvres <= 0 || nombreJoursOuvres > 7) {
            throw new IllegalArgumentException(
                "Le nombre de jours ouvrés doit être compris entre 1 et 7, reçu : " + nombreJoursOuvres);
        }
        this.nombreJoursOuvres = nombreJoursOuvres;
        this.dateDebut = Objects.requireNonNull(dateDebut, "La date de prise d'effet est obligatoire");
        this.matriculeAuteur = Objects.requireNonNull(matriculeAuteur, "L'auteur est obligatoire");
        this.motif = (motif != null && !motif.isBlank()) ? motif : null;
        this.dateCreation = dateCreation;
        this.version = 0L;
    }

    public static ParametrageObjectif creer(String reference, int nombreJoursOuvres, LocalDate dateDebut,
                                             String matriculeAuteur, String motif) {
        return new ParametrageObjectif(reference, nombreJoursOuvres, dateDebut, matriculeAuteur, motif,
            LocalDateTime.now());
    }

    public static ParametrageObjectif reconstituer(Long id, String reference, int nombreJoursOuvres,
                                                    LocalDate dateDebut, LocalDate dateFin,
                                                    String matriculeAuteur, String motif,
                                                    LocalDateTime dateCreation, Long version) {
        ParametrageObjectif p = new ParametrageObjectif(reference, nombreJoursOuvres, dateDebut,
            matriculeAuteur, motif, dateCreation);
        p.id = id;
        p.dateFin = dateFin;
        p.version = version;
        return p;
    }

    /** Valeur appliquée avant la mise en place du paramétrage : six jours ouvrés. */
    public static ParametrageObjectif reglesHistoriques() {
        return new ParametrageObjectif("PARAM-OBJECTIF-HISTORIQUE", 6, LocalDate.of(2000, 1, 1),
            "SYSTEME", "Valeur appliquée avant la mise en place du paramétrage", LocalDateTime.now());
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

    public Long getId() { return id; }
    public String getReference() { return reference; }
    public int getNombreJoursOuvres() { return nombreJoursOuvres; }
    public LocalDate getDateDebut() { return dateDebut; }
    public LocalDate getDateFin() { return dateFin; }
    public String getMatriculeAuteur() { return matriculeAuteur; }
    public String getMotif() { return motif; }
    public LocalDateTime getDateCreation() { return dateCreation; }
    public Long getVersion() { return version; }

    @Override public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof ParametrageObjectif that)) return false;
        return Objects.equals(reference, that.reference);
    }
    @Override public int hashCode() { return Objects.hash(reference); }
}
