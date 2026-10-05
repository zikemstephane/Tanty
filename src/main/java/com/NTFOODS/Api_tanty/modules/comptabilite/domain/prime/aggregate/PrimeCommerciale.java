package com.NTFOODS.Api_tanty.modules.comptabilite.domain.prime.aggregate;

import com.NTFOODS.Api_tanty.modules.comptabilite.domain.prime.enums.StatutPrime;
import com.NTFOODS.Api_tanty.modules.comptabilite.domain.prime.events.PrimeValideeEvent;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.*;

/**
 * Agrégat Racine — Prime Commerciale hebdomadaire.
 *
 * RÈGLE RÉELLE NTFOODS : la prime hebdomadaire n'est débloquée que si le
 * commercial atteint AU MOINS 80 % de son objectif de chiffre d'affaires
 * global (sinon montant = 0, quel que soit le détail par gamme). Si cette
 * condition est remplie, 3 000 FCFA sont attribués par gamme de produits
 * (Bouillies, À Grignoter, Choco, Ingrédients) ayant atteint AU MOINS 75 %
 * de son objectif — plafonné à {@link #MONTANT_MAX_FCFA} (12 000 FCFA pour
 * les 4 gammes). Le paiement se fait moitié en cash, moitié en nature.
 *
 * Le détail par gamme reste toujours conservé pour la transparence envers
 * le commercial, y compris quand la prime est nulle.
 */
public class PrimeCommerciale {

    private Long id;
    /** Barème appliqué lors du calcul, conservé pour pouvoir justifier le montant a posteriori. */
    private String referenceBareme;
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
                              Double montantPrimeImpose, BaremePrime bareme, LocalDateTime dateCreation) {
        if (!semaineFin.isAfter(semaineDebut)) {
            throw new IllegalArgumentException("La fin de semaine doit être postérieure au début");
        }
        Objects.requireNonNull(bareme, "Un barème est requis pour calculer une prime");
        this.referenceBareme = bareme.getReference();
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
        int gammesValidees = 0;
        for (Map.Entry<String, Double> entry : this.objectifsParGammeFCFA.entrySet()) {
            double objectifGamme = entry.getValue() != null ? entry.getValue() : 0.0;
            double realiseGamme = this.realisationsParGammeFCFA.getOrDefault(entry.getKey(), 0.0);
            double tauxGamme = objectifGamme > 0 ? (realiseGamme / objectifGamme) * 100.0 : 100.0;
            this.tauxParGammePourcent.put(entry.getKey(), tauxGamme);
            if (tauxGamme >= bareme.getTauxGammeMinimumPourcent()) gammesValidees++;
        }

        boolean conditionGlobale =
            this.tauxRealisationGlobalPourcent >= bareme.getTauxGlobalMinimumPourcent();
        // Montant modulaire par gamme validée, plafonné, selon le barème en vigueur la
        // semaine concernée ; montantPrimeImpose permet à reconstituer() de restaurer un
        // montant persisté tel quel sans le recalculer.
        double montantCalcule = conditionGlobale ? bareme.montantPour(gammesValidees) : 0.0;
        this.montantPrimeFCFA = montantPrimeImpose != null ? Math.max(0, montantPrimeImpose) : montantCalcule;
        this.eligible = conditionGlobale && this.montantPrimeFCFA > 0;
        this.statut = StatutPrime.CALCULEE;
        this.dateCreation = dateCreation;
        this.version = 0L;
    }

    /**
     * Calcule une prime selon le barème fourni.
     *
     * <p>Le barème est passé explicitement plutôt que résolu ici : le domaine reste
     * ainsi dépourvu de dépendance à la persistance, et un recalcul de contrôle peut
     * rejouer une semaine passée avec le barème qui s'y appliquait.</p>
     */
    public static PrimeCommerciale calculer(String referencePrime, String matriculeCommercial,
                                             LocalDate semaineDebut, LocalDate semaineFin,
                                             double objectifGlobalFCFA, double totalVentesGlobalFCFA,
                                             Map<String, Double> objectifsParGammeFCFA,
                                             Map<String, Double> realisationsParGammeFCFA,
                                             BaremePrime bareme) {
        return new PrimeCommerciale(referencePrime, matriculeCommercial, semaineDebut, semaineFin,
            objectifGlobalFCFA, totalVentesGlobalFCFA, objectifsParGammeFCFA, realisationsParGammeFCFA,
            null, bareme, LocalDateTime.now());
    }

    public static PrimeCommerciale reconstituer(Long id, String referencePrime, String matriculeCommercial,
                                                 LocalDate semaineDebut, LocalDate semaineFin,
                                                 double objectifGlobalFCFA, double totalVentesGlobalFCFA,
                                                 Map<String, Double> objectifsParGammeFCFA,
                                                 Map<String, Double> realisationsParGammeFCFA,
                                                 double montantPrimeFCFA, StatutPrime statut,
                                                 String matriculeValidateur, LocalDateTime dateCreation,
                                                 LocalDateTime dateModification, Long version) {
        return reconstituer(id, referencePrime, matriculeCommercial, semaineDebut, semaineFin,
            objectifGlobalFCFA, totalVentesGlobalFCFA, objectifsParGammeFCFA, realisationsParGammeFCFA,
            montantPrimeFCFA, statut, matriculeValidateur, null, dateCreation, dateModification, version);
    }

    public static PrimeCommerciale reconstituer(Long id, String referencePrime, String matriculeCommercial,
                                                 LocalDate semaineDebut, LocalDate semaineFin,
                                                 double objectifGlobalFCFA, double totalVentesGlobalFCFA,
                                                 Map<String, Double> objectifsParGammeFCFA,
                                                 Map<String, Double> realisationsParGammeFCFA,
                                                 double montantPrimeFCFA, StatutPrime statut,
                                                 String matriculeValidateur, String referenceBareme,
                                                 LocalDateTime dateCreation,
                                                 LocalDateTime dateModification, Long version) {
        // Le montant persisté est restauré tel quel : une prime déjà calculée ne doit
        // jamais changer de valeur parce qu'un nouveau barème est entré en vigueur.
        // Le barème de repli ne sert ici qu'à reconstruire les taux affichés.
        PrimeCommerciale p = new PrimeCommerciale(referencePrime, matriculeCommercial, semaineDebut, semaineFin,
            objectifGlobalFCFA, totalVentesGlobalFCFA, objectifsParGammeFCFA, realisationsParGammeFCFA,
            montantPrimeFCFA, BaremePrime.reglesHistoriques(), dateCreation);
        p.id = id;
        p.statut = statut;
        p.matriculeValidateur = matriculeValidateur;
        p.referenceBareme = referenceBareme;
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
    /** Moitié cash de la prime (règle de paiement 50/50). */
    public double getMontantCashFCFA() { return montantPrimeFCFA / 2.0; }
    /** Moitié en nature (produits) de la prime (règle de paiement 50/50). */
    public double getMontantNatureFCFA() { return montantPrimeFCFA - getMontantCashFCFA(); }
    public StatutPrime getStatut() { return statut; }
    public String getMatriculeValidateur() { return matriculeValidateur; }
    /** Barème ayant servi au calcul, ou {@code null} pour une prime antérieure au paramétrage. */
    public String getReferenceBareme() { return referenceBareme; }
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
