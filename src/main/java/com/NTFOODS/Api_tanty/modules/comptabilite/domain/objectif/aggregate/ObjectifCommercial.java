package com.NTFOODS.Api_tanty.modules.comptabilite.domain.objectif.aggregate;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

/**
 * Agrégat Racine — Objectif Commercial hebdomadaire.
 *
 * Fixé exclusivement par le DG ou le Contrôleur Général, il porte un objectif
 * global (FCFA) et un objectif par gamme de produits (Bouillies, Snacks,
 * Choco, Ingrédients...). Sert de dénominateur à la Règle d'or des Primes
 * (80 % global ET 75 % par gamme).
 */
public class ObjectifCommercial {

    // Aucun nombre de jours ouvrés par défaut ici : il provient du
    // ParametrageObjectif en vigueur et est figé sur chaque objectif à sa création.

    private Long id;
    private final String matriculeCommercial;
    private final LocalDate semaineDebut;
    private final LocalDate semaineFin;
    private double objectifGlobalFCFA;
    private final Map<String, Double> objectifsParGammeFCFA;
    /** Journal immuable des valeurs remplacées. Vide tant qu'aucune révision n'a eu lieu. */
    private final List<RevisionObjectif> revisions = new ArrayList<>();
    private int nombreJoursOuvres;
    private String matriculeAuteur;
    private final LocalDateTime dateCreation;
    private LocalDateTime dateModification;
    private Long version;

    private ObjectifCommercial(String matriculeCommercial, LocalDate semaineDebut, LocalDate semaineFin,
                                double objectifGlobalFCFA, Map<String, Double> objectifsParGammeFCFA,
                                int nombreJoursOuvres, String matriculeAuteur, LocalDateTime dateCreation) {
        this.matriculeCommercial = Objects.requireNonNull(matriculeCommercial);
        if (!semaineFin.isAfter(semaineDebut)) {
            throw new IllegalArgumentException("La fin de semaine doit être postérieure au début");
        }
        this.semaineDebut = semaineDebut;
        this.semaineFin = semaineFin;
        if (objectifGlobalFCFA <= 0) throw new IllegalArgumentException("L'objectif global doit être positif");
        this.objectifGlobalFCFA = objectifGlobalFCFA;
        this.objectifsParGammeFCFA = new HashMap<>(objectifsParGammeFCFA != null ? objectifsParGammeFCFA : Map.of());
        if (nombreJoursOuvres <= 0) throw new IllegalArgumentException("Le nombre de jours ouvrés doit être positif");
        this.nombreJoursOuvres = nombreJoursOuvres;
        this.matriculeAuteur = Objects.requireNonNull(matriculeAuteur);
        this.dateCreation = dateCreation;
        this.version = 0L;
    }

    /**
     * Définit un objectif hebdomadaire.
     *
     * <p>Le nombre de jours ouvrés est transmis par l'appelant, qui le tient du
     * {@code ParametrageObjectif} en vigueur. Il n'existe volontairement pas de
     * surcharge appliquant une valeur par défaut : elle constituerait une seconde
     * source de vérité, concurrente du paramétrage.</p>
     */
    public static ObjectifCommercial definir(String matriculeCommercial, LocalDate semaineDebut, LocalDate semaineFin,
                                              double objectifGlobalFCFA, Map<String, Double> objectifsParGammeFCFA,
                                              int nombreJoursOuvres, String matriculeAuteur) {
        return new ObjectifCommercial(matriculeCommercial, semaineDebut, semaineFin, objectifGlobalFCFA,
            objectifsParGammeFCFA, nombreJoursOuvres, matriculeAuteur, LocalDateTime.now());
    }

    public static ObjectifCommercial reconstituer(Long id, String matriculeCommercial, LocalDate semaineDebut,
                                                   LocalDate semaineFin, double objectifGlobalFCFA,
                                                   Map<String, Double> objectifsParGammeFCFA, int nombreJoursOuvres,
                                                   String matriculeAuteur,
                                                   LocalDateTime dateCreation, LocalDateTime dateModification,
                                                   Long version) {
        return reconstituer(id, matriculeCommercial, semaineDebut, semaineFin, objectifGlobalFCFA,
            objectifsParGammeFCFA, nombreJoursOuvres, matriculeAuteur, dateCreation, dateModification,
            List.of(), version);
    }

    public static ObjectifCommercial reconstituer(Long id, String matriculeCommercial, LocalDate semaineDebut,
                                                   LocalDate semaineFin, double objectifGlobalFCFA,
                                                   Map<String, Double> objectifsParGammeFCFA, int nombreJoursOuvres,
                                                   String matriculeAuteur,
                                                   LocalDateTime dateCreation, LocalDateTime dateModification,
                                                   List<RevisionObjectif> revisions, Long version) {
        ObjectifCommercial o = new ObjectifCommercial(matriculeCommercial, semaineDebut, semaineFin,
            objectifGlobalFCFA, objectifsParGammeFCFA, nombreJoursOuvres, matriculeAuteur, dateCreation);
        o.id = id;
        o.dateModification = dateModification;
        if (revisions != null) o.revisions.addAll(revisions);
        o.version = version;
        return o;
    }

    /**
     * Révise l'objectif en conservant la trace de la valeur remplacée.
     *
     * <p>La prime se calcule sur la valeur courante : une révision prend donc
     * effet immédiatement, y compris sur la semaine en cours. C'est précisément
     * pourquoi le motif est exigé et l'ancienne valeur conservée — un abaissement
     * tardif reste possible mais devient opposable.</p>
     *
     * @param motif justification de la révision, obligatoire
     */
    public void reviser(double objectifGlobalFCFA, Map<String, Double> objectifsParGammeFCFA,
                        String matriculeAuteur, String motif) {
        if (objectifGlobalFCFA <= 0) throw new IllegalArgumentException("L'objectif global doit être positif");
        if (motif == null || motif.isBlank()) {
            throw new IllegalArgumentException(
                "Le motif est obligatoire pour réviser un objectif déjà fixé");
        }
        Objects.requireNonNull(matriculeAuteur);

        revisions.add(RevisionObjectif.inscrire(this.objectifGlobalFCFA, this.objectifsParGammeFCFA,
            objectifGlobalFCFA, matriculeAuteur, motif));

        this.objectifGlobalFCFA = objectifGlobalFCFA;
        this.objectifsParGammeFCFA.clear();
        this.objectifsParGammeFCFA.putAll(objectifsParGammeFCFA != null ? objectifsParGammeFCFA : Map.of());
        this.matriculeAuteur = matriculeAuteur;
        this.dateModification = LocalDateTime.now();
    }

    /** Journal des révisions, de la plus ancienne à la plus récente. */
    public List<RevisionObjectif> getRevisions() {
        return Collections.unmodifiableList(revisions);
    }

    public boolean aEteRevise() {
        return !revisions.isEmpty();
    }

    /**
     * Objectif tel qu'il avait été fixé à l'origine, avant toute révision.
     * Égal à l'objectif courant lorsque aucune révision n'a eu lieu.
     */
    public double objectifGlobalInitialFCFA() {
        return revisions.isEmpty() ? objectifGlobalFCFA : revisions.get(0).getObjectifGlobalAvantFCFA();
    }

    /** Objectif quotidien global = objectif hebdo ÷ nombre de jours ouvrés (règle des 60% journaliers). */
    public double objectifQuotidienGlobal() {
        return objectifGlobalFCFA / nombreJoursOuvres;
    }

    /** Objectif quotidien par gamme = objectif hebdo de la gamme ÷ nombre de jours ouvrés. */
    public Map<String, Double> objectifsQuotidiensParGamme() {
        Map<String, Double> result = new HashMap<>();
        objectifsParGammeFCFA.forEach((gamme, valeur) -> result.put(gamme, valeur / nombreJoursOuvres));
        return result;
    }

    public Long getId() { return id; }
    public String getMatriculeCommercial() { return matriculeCommercial; }
    public LocalDate getSemaineDebut() { return semaineDebut; }
    public LocalDate getSemaineFin() { return semaineFin; }
    public double getObjectifGlobalFCFA() { return objectifGlobalFCFA; }
    public Map<String, Double> getObjectifsParGammeFCFA() { return Collections.unmodifiableMap(objectifsParGammeFCFA); }
    public int getNombreJoursOuvres() { return nombreJoursOuvres; }
    public String getMatriculeAuteur() { return matriculeAuteur; }
    public LocalDateTime getDateCreation() { return dateCreation; }
    public LocalDateTime getDateModification() { return dateModification; }
    public Long getVersion() { return version; }

    @Override public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof ObjectifCommercial that)) return false;
        return Objects.equals(matriculeCommercial, that.matriculeCommercial)
            && Objects.equals(semaineDebut, that.semaineDebut);
    }
    @Override public int hashCode() { return Objects.hash(matriculeCommercial, semaineDebut); }
}
