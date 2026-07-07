package com.NTFOODS.Api_tanty.modules.comptabilite.domain.prime.aggregate;

import com.NTFOODS.Api_tanty.modules.comptabilite.domain.prime.enums.StatutPrime;
import com.NTFOODS.Api_tanty.modules.comptabilite.domain.prime.events.PrimeValideeEvent;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.*;

/**
 * Agrégat Racine — Prime Commerciale hebdomadaire.
 *
 * RÈGLE D'OR DES PRIMES (80/75) : la prime hebdomadaire fixe n'est débloquée
 * que si le commercial atteint AU MOINS 80 % de son objectif de chiffre
 * d'affaires global ET AU MOINS 75 % de son objectif sur CHAQUE gamme de
 * produits (Bouillies, Snacks, Choco, Ingrédients...). Un seul critère non
 * atteint annule la totalité de la prime (montant = 0), mais le détail par
 * gamme reste toujours conservé pour la transparence envers le commercial.
 *
 * Le montant de la prime est FIXE (ex: 12 000 FCFA), pas proportionnel — ce
 * n'est donc plus une commission mais un bonus de méritocratie tout-ou-rien.
 */
public class PrimeCommerciale {

    private static final double SEUIL_GLOBAL_POURCENT = 80.0;
    private static final double SEUIL_GAMME_POURCENT = 75.0;

    private Long id;
    private final String referencePrime;
    private final String matriculeCommercial;
    private final LocalDate semaineDebut;
    private final LocalDate semaineFin;
    private final double objectifGlobalFCFA;
    private final double totalVentesGlobalFCFA;
    private final double tauxRealisationGlobalPourcent;
    private final Map<String, Double> objectifsParGammeFCFA;
    private final Map<String, Double> realisationsParGammeFCFA;
    private final Map<String, Double> tauxParGammePourcent;
    private final boolean eligible;
    private final double montantPrimeFCFA;
    private StatutPrime statut;
    private String matriculeValidateur;
    private final LocalDateTime dateCreation;
    private LocalDateTime dateModification;
    private Long version;

    private final List<Object> domainEvents = new ArrayList<>();

    private PrimeCommerciale(String referencePrime, String matriculeCommercial, LocalDate semaineDebut,
                              LocalDate semaineFin, double objectifGlobalFCFA, double totalVentesGlobalFCFA,
                              Map<String, Double> objectifsParGammeFCFA, Map<String, Double> realisationsParGammeFCFA,
                              double montantPrimeSiEligibleFCFA, LocalDateTime dateCreation) {
        if (!semaineFin.isAfter(semaineDebut)) {
            throw new IllegalArgumentException("La fin de semaine doit être postérieure au début");
        }
        this.referencePrime = Objects.requireNonNull(referencePrime);
        this.matriculeCommercial = Objects.requireNonNull(matriculeCommercial);
        this.semaineDebut = semaineDebut;
        this.semaineFin = semaineFin;
        this.objectifGlobalFCFA = Math.max(0, objectifGlobalFCFA);
        this.totalVentesGlobalFCFA = Math.max(0, totalVentesGlobalFCFA);
        this.objectifsParGammeFCFA = new LinkedHashMap<>(objectifsParGammeFCFA != null ? objectifsParGammeFCFA : Map.of());
        this.realisationsParGammeFCFA = new LinkedHashMap<>(realisationsParGammeFCFA != null ? realisationsParGammeFCFA : Map.of());

        this.tauxRealisationGlobalPourcent = this.objectifGlobalFCFA > 0
            ? (this.totalVentesGlobalFCFA / this.objectifGlobalFCFA) * 100.0 : 0.0;

        this.tauxParGammePourcent = new LinkedHashMap<>();
        boolean toutesGammesOk = true;
        for (Map.Entry<String, Double> entry : this.objectifsParGammeFCFA.entrySet()) {
            double objectifGamme = entry.getValue() != null ? entry.getValue() : 0.0;
            double realiseGamme = this.realisationsParGammeFCFA.getOrDefault(entry.getKey(), 0.0);
            double tauxGamme = objectifGamme > 0 ? (realiseGamme / objectifGamme) * 100.0 : 100.0;
            this.tauxParGammePourcent.put(entry.getKey(), tauxGamme);
            if (tauxGamme < SEUIL_GAMME_POURCENT) toutesGammesOk = false;
        }

        this.eligible = this.tauxRealisationGlobalPourcent >= SEUIL_GLOBAL_POURCENT && toutesGammesOk;
        this.montantPrimeFCFA = this.eligible ? Math.max(0, montantPrimeSiEligibleFCFA) : 0.0;
        this.statut = StatutPrime.CALCULEE;
        this.dateCreation = dateCreation;
        this.version = 0L;
    }

    public static PrimeCommerciale calculer(String referencePrime, String matriculeCommercial,
                                             LocalDate semaineDebut, LocalDate semaineFin,
                                             double objectifGlobalFCFA, double totalVentesGlobalFCFA,
                                             Map<String, Double> objectifsParGammeFCFA,
                                             Map<String, Double> realisationsParGammeFCFA,
                                             double montantPrimeSiEligibleFCFA) {
        return new PrimeCommerciale(referencePrime, matriculeCommercial, semaineDebut, semaineFin,
            objectifGlobalFCFA, totalVentesGlobalFCFA, objectifsParGammeFCFA, realisationsParGammeFCFA,
            montantPrimeSiEligibleFCFA, LocalDateTime.now());
    }

    public static PrimeCommerciale reconstituer(Long id, String referencePrime, String matriculeCommercial,
                                                 LocalDate semaineDebut, LocalDate semaineFin,
                                                 double objectifGlobalFCFA, double totalVentesGlobalFCFA,
                                                 Map<String, Double> objectifsParGammeFCFA,
                                                 Map<String, Double> realisationsParGammeFCFA,
                                                 double montantPrimeFCFA, StatutPrime statut,
                                                 String matriculeValidateur, LocalDateTime dateCreation,
                                                 LocalDateTime dateModification, Long version) {
        // montantPrimeFCFA persisté est réappliqué tel quel via le statut/montant stockés ;
        // on reconstruit d'abord l'éligibilité calculée puis on restaure le montant exact.
        PrimeCommerciale p = new PrimeCommerciale(referencePrime, matriculeCommercial, semaineDebut, semaineFin,
            objectifGlobalFCFA, totalVentesGlobalFCFA, objectifsParGammeFCFA, realisationsParGammeFCFA,
            montantPrimeFCFA, dateCreation);
        p.id = id;
        p.statut = statut;
        p.matriculeValidateur = matriculeValidateur;
        p.dateModification = dateModification;
        p.version = version;
        return p;
    }

    public void valider(String matriculeValidateur) {
        if (statut != StatutPrime.CALCULEE) {
            throw new IllegalStateException("Seule une prime calculée peut être validée");
        }
        this.matriculeValidateur = Objects.requireNonNull(matriculeValidateur);
        this.statut = StatutPrime.VALIDEE;
        this.dateModification = LocalDateTime.now();
        domainEvents.add(new PrimeValideeEvent(referencePrime, matriculeCommercial, montantPrimeFCFA));
    }

    public void marquerVersee() {
        if (statut != StatutPrime.VALIDEE) {
            throw new IllegalStateException("Seule une prime validée peut être versée");
        }
        if (montantPrimeFCFA <= 0) {
            throw new IllegalStateException("Aucun versement possible : le commercial n'a pas atteint la règle d'or 80/75");
        }
        this.statut = StatutPrime.VERSEE;
        this.dateModification = LocalDateTime.now();
    }

    public List<Object> getDomainEvents() { return Collections.unmodifiableList(domainEvents); }
    public void clearDomainEvents() { domainEvents.clear(); }

    public Long getId() { return id; }
    public String getReferencePrime() { return referencePrime; }
    public String getMatriculeCommercial() { return matriculeCommercial; }
    public LocalDate getSemaineDebut() { return semaineDebut; }
    public LocalDate getSemaineFin() { return semaineFin; }
    public double getObjectifGlobalFCFA() { return objectifGlobalFCFA; }
    public double getTotalVentesGlobalFCFA() { return totalVentesGlobalFCFA; }
    public double getTauxRealisationGlobalPourcent() { return tauxRealisationGlobalPourcent; }
    public Map<String, Double> getObjectifsParGammeFCFA() { return Collections.unmodifiableMap(objectifsParGammeFCFA); }
    public Map<String, Double> getRealisationsParGammeFCFA() { return Collections.unmodifiableMap(realisationsParGammeFCFA); }
    public Map<String, Double> getTauxParGammePourcent() { return Collections.unmodifiableMap(tauxParGammePourcent); }
    public boolean isEligible() { return eligible; }
    public double getMontantPrimeFCFA() { return montantPrimeFCFA; }
    public StatutPrime getStatut() { return statut; }
    public String getMatriculeValidateur() { return matriculeValidateur; }
    public LocalDateTime getDateCreation() { return dateCreation; }
    public LocalDateTime getDateModification() { return dateModification; }
    public Long getVersion() { return version; }

    @Override public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof PrimeCommerciale)) return false;
        return Objects.equals(referencePrime, ((PrimeCommerciale) o).referencePrime);
    }
    @Override public int hashCode() { return Objects.hash(referencePrime); }
}
