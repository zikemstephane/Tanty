package com.NTFOODS.Api_tanty.modules.comptabilite.domain.objectif.aggregate;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Collections;
import java.util.HashMap;
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

    private Long id;
    private final String matriculeCommercial;
    private final LocalDate semaineDebut;
    private final LocalDate semaineFin;
    private double objectifGlobalFCFA;
    private final Map<String, Double> objectifsParGammeFCFA;
    private String matriculeAuteur;
    private final LocalDateTime dateCreation;
    private LocalDateTime dateModification;
    private Long version;

    private ObjectifCommercial(String matriculeCommercial, LocalDate semaineDebut, LocalDate semaineFin,
                                double objectifGlobalFCFA, Map<String, Double> objectifsParGammeFCFA,
                                String matriculeAuteur, LocalDateTime dateCreation) {
        this.matriculeCommercial = Objects.requireNonNull(matriculeCommercial);
        if (!semaineFin.isAfter(semaineDebut)) {
            throw new IllegalArgumentException("La fin de semaine doit être postérieure au début");
        }
        this.semaineDebut = semaineDebut;
        this.semaineFin = semaineFin;
        if (objectifGlobalFCFA <= 0) throw new IllegalArgumentException("L'objectif global doit être positif");
        this.objectifGlobalFCFA = objectifGlobalFCFA;
        this.objectifsParGammeFCFA = new HashMap<>(objectifsParGammeFCFA != null ? objectifsParGammeFCFA : Map.of());
        this.matriculeAuteur = Objects.requireNonNull(matriculeAuteur);
        this.dateCreation = dateCreation;
        this.version = 0L;
    }

    public static ObjectifCommercial definir(String matriculeCommercial, LocalDate semaineDebut, LocalDate semaineFin,
                                              double objectifGlobalFCFA, Map<String, Double> objectifsParGammeFCFA,
                                              String matriculeAuteur) {
        return new ObjectifCommercial(matriculeCommercial, semaineDebut, semaineFin, objectifGlobalFCFA,
            objectifsParGammeFCFA, matriculeAuteur, LocalDateTime.now());
    }

    public static ObjectifCommercial reconstituer(Long id, String matriculeCommercial, LocalDate semaineDebut,
                                                   LocalDate semaineFin, double objectifGlobalFCFA,
                                                   Map<String, Double> objectifsParGammeFCFA, String matriculeAuteur,
                                                   LocalDateTime dateCreation, LocalDateTime dateModification,
                                                   Long version) {
        ObjectifCommercial o = new ObjectifCommercial(matriculeCommercial, semaineDebut, semaineFin,
            objectifGlobalFCFA, objectifsParGammeFCFA, matriculeAuteur, dateCreation);
        o.id = id;
        o.dateModification = dateModification;
        o.version = version;
        return o;
    }

    public void reviser(double objectifGlobalFCFA, Map<String, Double> objectifsParGammeFCFA, String matriculeAuteur) {
        if (objectifGlobalFCFA <= 0) throw new IllegalArgumentException("L'objectif global doit être positif");
        this.objectifGlobalFCFA = objectifGlobalFCFA;
        this.objectifsParGammeFCFA.clear();
        this.objectifsParGammeFCFA.putAll(objectifsParGammeFCFA != null ? objectifsParGammeFCFA : Map.of());
        this.matriculeAuteur = Objects.requireNonNull(matriculeAuteur);
        this.dateModification = LocalDateTime.now();
    }

    public Long getId() { return id; }
    public String getMatriculeCommercial() { return matriculeCommercial; }
    public LocalDate getSemaineDebut() { return semaineDebut; }
    public LocalDate getSemaineFin() { return semaineFin; }
    public double getObjectifGlobalFCFA() { return objectifGlobalFCFA; }
    public Map<String, Double> getObjectifsParGammeFCFA() { return Collections.unmodifiableMap(objectifsParGammeFCFA); }
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
