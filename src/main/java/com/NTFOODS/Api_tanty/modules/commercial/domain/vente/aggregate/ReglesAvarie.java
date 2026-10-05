package com.NTFOODS.Api_tanty.modules.commercial.domain.vente.aggregate;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Objects;

/**
 * Agrégat Racine — Règles de déclaration d'avarie.
 *
 * <p>Porte la quantité <strong>en unités de base</strong> à partir de laquelle une
 * preuve photographique devient obligatoire. Cette règle n'existait que dans
 * l'application mobile, sous forme d'un test compilé : le serveur acceptait donc
 * une déclaration sans preuve quel qu'en soit le volume, et la règle ne pouvait
 * être ajustée sans livrer une nouvelle version de l'application.</p>
 *
 * <p>Le mobile affichait par ailleurs « seuil &gt; 5 % du chargement » alors qu'il
 * appliquait un seuil en quantité. C'est la quantité qui fait foi ici, et le
 * libellé doit désormais l'énoncer telle quelle.</p>
 */
public class ReglesAvarie {

    private Long id;
    private final String reference;
    private final int quantiteSeuilPhotoObligatoire;
    private final LocalDate dateDebut;
    private LocalDate dateFin;
    private final String matriculeAuteur;
    private final String motif;
    private final LocalDateTime dateCreation;
    private Long version;

    private ReglesAvarie(String reference, int quantiteSeuilPhotoObligatoire, LocalDate dateDebut,
                          String matriculeAuteur, String motif, LocalDateTime dateCreation) {
        this.reference = Objects.requireNonNull(reference, "La référence des règles est obligatoire");
        if (quantiteSeuilPhotoObligatoire < 0) {
            throw new IllegalArgumentException("Le seuil de photo obligatoire ne peut pas être négatif");
        }
        this.quantiteSeuilPhotoObligatoire = quantiteSeuilPhotoObligatoire;
        this.dateDebut = Objects.requireNonNull(dateDebut, "La date de prise d'effet est obligatoire");
        this.matriculeAuteur = Objects.requireNonNull(matriculeAuteur, "L'auteur est obligatoire");
        this.motif = (motif != null && !motif.isBlank()) ? motif : null;
        this.dateCreation = dateCreation;
        this.version = 0L;
    }

    public static ReglesAvarie creer(String reference, int quantiteSeuilPhotoObligatoire,
                                      LocalDate dateDebut, String matriculeAuteur, String motif) {
        return new ReglesAvarie(reference, quantiteSeuilPhotoObligatoire, dateDebut, matriculeAuteur,
            motif, LocalDateTime.now());
    }

    public static ReglesAvarie reconstituer(Long id, String reference, int quantiteSeuilPhotoObligatoire,
                                             LocalDate dateDebut, LocalDate dateFin, String matriculeAuteur,
                                             String motif, LocalDateTime dateCreation, Long version) {
        ReglesAvarie r = new ReglesAvarie(reference, quantiteSeuilPhotoObligatoire, dateDebut,
            matriculeAuteur, motif, dateCreation);
        r.id = id;
        r.dateFin = dateFin;
        r.version = version;
        return r;
    }

    /**
     * Seuil de repli appliqué tant qu'aucune règle datée n'a été enregistrée :
     * <strong>zéro</strong>, c'est-à-dire une preuve photographique pour toute
     * avarie, quelle qu'en soit la quantité.
     *
     * <p>La valeur était auparavant de trois. Elle avait été retenue quand le
     * seuil comptait des colis ; exprimée en unités de base elle mordait bien
     * plus tôt sans que personne l'ait décidé. Le commanditaire a tranché le
     * 18/08/2026 : la photo est obligatoire quelle que soit la quantité.</p>
     *
     * <p>Ce n'est qu'un repli. Le seuil reste administrable et daté : il se
     * règle par {@code POST /commercial/regles-avarie}, et toute règle
     * enregistrée l'emporte sur cette valeur.</p>
     */
    public static ReglesAvarie reglesHistoriques() {
        return new ReglesAvarie("REGLES-AVARIE-HISTORIQUE", 0, LocalDate.of(2000, 1, 1), "SYSTEME",
            "Photo obligatoire quelle que soit la quantité (décision du 18/08/2026)", LocalDateTime.now());
    }

    public void cloturerAvant(LocalDate dateEffetSuccesseur) {
        if (dateFin != null) {
            throw new IllegalStateException("Les règles " + reference + " sont déjà clôturées au " + dateFin);
        }
        LocalDate nouvelleFin = dateEffetSuccesseur.minusDays(1);
        if (nouvelleFin.isBefore(dateDebut)) {
            throw new IllegalArgumentException(
                "Des règles ne peuvent pas être clôturées avant leur prise d'effet du " + dateDebut);
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

    /**
     * Vrai si une déclaration portant cette quantité exige une preuve photographique.
     *
     * <p>La quantité est attendue <strong>en unités de base</strong>, jamais dans le
     * conditionnement déclaré. Le seuil portait auparavant sur le nombre de colis
     * saisis : une même perte échappait alors à la preuve selon la façon dont elle
     * était énoncée — deux cartons de vingt-cinq passaient sous un seuil de trois,
     * cinquante unités non. Le conditionnement est une façon de compter, pas une
     * façon de perdre.</p>
     */
    public boolean photoObligatoirePour(long quantiteEnUnitesBase) {
        return quantiteEnUnitesBase >= quantiteSeuilPhotoObligatoire;
    }

    public Long getId() { return id; }
    public String getReference() { return reference; }
    public int getQuantiteSeuilPhotoObligatoire() { return quantiteSeuilPhotoObligatoire; }
    public LocalDate getDateDebut() { return dateDebut; }
    public LocalDate getDateFin() { return dateFin; }
    public String getMatriculeAuteur() { return matriculeAuteur; }
    public String getMotif() { return motif; }
    public LocalDateTime getDateCreation() { return dateCreation; }
    public Long getVersion() { return version; }

    @Override public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof ReglesAvarie that)) return false;
        return Objects.equals(reference, that.reference);
    }
    @Override public int hashCode() { return Objects.hash(reference); }
}
