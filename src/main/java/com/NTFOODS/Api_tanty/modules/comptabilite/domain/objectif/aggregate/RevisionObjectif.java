package com.NTFOODS.Api_tanty.modules.comptabilite.domain.objectif.aggregate;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

/**
 * Entité — trace d'une révision d'objectif commercial.
 *
 * <p>Une révision conserve la valeur <strong>antérieure</strong>, son auteur, sa
 * date et son motif. Sans ce journal, abaisser un objectif en fin de semaine
 * rendait éligible à la prime un commercial qui ne l'était pas, sans qu'aucune
 * trace ne subsiste de l'objectif initialement fixé.</p>
 *
 * <p>Le journal est immuable : une révision, une fois inscrite, n'est jamais
 * modifiée ni supprimée.</p>
 */
public class RevisionObjectif {

    private Long id;
    private final double objectifGlobalAvantFCFA;
    private final Map<String, Double> objectifsParGammeAvantFCFA;
    private final double objectifGlobalApresFCFA;
    private final String matriculeAuteur;
    private final String motif;
    private final LocalDateTime dateRevision;

    private RevisionObjectif(Long id, double objectifGlobalAvantFCFA,
                             Map<String, Double> objectifsParGammeAvantFCFA,
                             double objectifGlobalApresFCFA, String matriculeAuteur,
                             String motif, LocalDateTime dateRevision) {
        this.id = id;
        this.objectifGlobalAvantFCFA = objectifGlobalAvantFCFA;
        this.objectifsParGammeAvantFCFA = new HashMap<>(
            objectifsParGammeAvantFCFA != null ? objectifsParGammeAvantFCFA : Map.of());
        this.objectifGlobalApresFCFA = objectifGlobalApresFCFA;
        this.matriculeAuteur = Objects.requireNonNull(matriculeAuteur, "L'auteur d'une révision est obligatoire");
        this.motif = motif;
        this.dateRevision = dateRevision;
    }

    static RevisionObjectif inscrire(double objectifGlobalAvantFCFA,
                                     Map<String, Double> objectifsParGammeAvantFCFA,
                                     double objectifGlobalApresFCFA, String matriculeAuteur, String motif) {
        return new RevisionObjectif(null, objectifGlobalAvantFCFA, objectifsParGammeAvantFCFA,
            objectifGlobalApresFCFA, matriculeAuteur, motif, LocalDateTime.now());
    }

    public static RevisionObjectif reconstituer(Long id, double objectifGlobalAvantFCFA,
                                                 Map<String, Double> objectifsParGammeAvantFCFA,
                                                 double objectifGlobalApresFCFA, String matriculeAuteur,
                                                 String motif, LocalDateTime dateRevision) {
        return new RevisionObjectif(id, objectifGlobalAvantFCFA, objectifsParGammeAvantFCFA,
            objectifGlobalApresFCFA, matriculeAuteur, motif, dateRevision);
    }

    /** Négatif lorsque l'objectif a été abaissé — le cas qui mérite attention. */
    public double variationFCFA() {
        return objectifGlobalApresFCFA - objectifGlobalAvantFCFA;
    }

    public boolean estUnAbaissement() {
        return variationFCFA() < 0;
    }

    public Long getId() { return id; }
    public double getObjectifGlobalAvantFCFA() { return objectifGlobalAvantFCFA; }
    public Map<String, Double> getObjectifsParGammeAvantFCFA() {
        return Collections.unmodifiableMap(objectifsParGammeAvantFCFA);
    }
    public double getObjectifGlobalApresFCFA() { return objectifGlobalApresFCFA; }
    public String getMatriculeAuteur() { return matriculeAuteur; }
    public String getMotif() { return motif; }
    public LocalDateTime getDateRevision() { return dateRevision; }
}
