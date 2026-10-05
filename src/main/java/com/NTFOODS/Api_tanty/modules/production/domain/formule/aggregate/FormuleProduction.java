package com.NTFOODS.Api_tanty.modules.production.domain.formule.aggregate;

import com.NTFOODS.Api_tanty.modules.production.domain.formule.entity.BesoinMatierePremiere;
import com.NTFOODS.Api_tanty.modules.production.domain.formule.entity.LigneFormule;

import java.math.BigDecimal;
import java.math.MathContext;
import java.math.RoundingMode;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Objects;

/**
 * Agrégat Racine — Formule de production d'un produit.
 *
 * Remplace l'ancienne recette figée à deux variantes (RecetteBouillie,
 * retirée) par une donnée
 * propre à chaque produit : une liste de matières premières et, pour
 * chacune, la quantité (en kg) qu'elle représente pour une base de
 * référence exprimée elle aussi en kg de produit fini.
 *
 * Une formule est rattachée à EXACTEMENT un produit (par son code) : pas de
 * repli sur la catégorie métier. Un produit sans formule est un état valide
 * — {@link com.NTFOODS.Api_tanty.modules.production.domain.formule.repository.FormuleProductionRepository}
 * renvoie alors {@code Optional.empty()}, à charge de l'appelant de le
 * signaler explicitement plutôt que de produire des besoins à zéro en silence.
 *
 * Invariant : la somme des proportions des lignes doit être égale à la base
 * de la formule. Sans cette égalité, {@link #calculerBesoins} ne pourrait
 * pas garantir que la somme des besoins calculés égale le poids total fourni
 * — précisément l'invariant que cette classe existe pour protéger.
 */
public class FormuleProduction {

    /** Échelle de calcul et d'arrondi des besoins, en kg (0,01 kg = 10 g). */
    private static final int ECHELLE_KG = 2;
    private static final MathContext PRECISION_CALCUL = new MathContext(50);

    private Long id;
    private final String codeProduit;
    private final BigDecimal baseKg;
    private final List<LigneFormule> lignes;
    private final LocalDateTime dateCreation;
    private LocalDateTime dateModification;
    private Long version;

    private FormuleProduction(String codeProduit, BigDecimal baseKg, List<LigneFormule> lignes,
                              LocalDateTime dateCreation) {
        this.codeProduit = Objects.requireNonNull(codeProduit, "Le code produit est obligatoire").trim();
        if (this.codeProduit.isBlank()) {
            throw new IllegalArgumentException("Le code produit est obligatoire");
        }
        Objects.requireNonNull(baseKg, "La base de la formule est obligatoire");
        if (baseKg.signum() <= 0) {
            throw new IllegalArgumentException("La base de la formule doit être strictement positive (produit " + codeProduit + ")");
        }
        if (lignes == null || lignes.isEmpty()) {
            throw new IllegalArgumentException("Une formule doit contenir au moins une matière première (produit " + codeProduit + ")");
        }
        long nomsDistincts = lignes.stream().map(l -> l.getMatierePremiere().toUpperCase()).distinct().count();
        if (nomsDistincts != lignes.size()) {
            throw new IllegalArgumentException("Une même matière première ne peut pas apparaître deux fois dans la formule du produit " + codeProduit);
        }
        BigDecimal sommeProportions = lignes.stream().map(LigneFormule::getProportionKg)
            .reduce(BigDecimal.ZERO, BigDecimal::add);
        if (sommeProportions.compareTo(baseKg) != 0) {
            throw new IllegalArgumentException(
                "La somme des proportions (" + sommeProportions + " kg) doit être égale à la base de la formule ("
                    + baseKg + " kg) pour le produit " + codeProduit
                    + " — sans quoi la somme des besoins calculés ne peut pas égaler le poids total.");
        }
        this.baseKg = baseKg;
        this.lignes = List.copyOf(lignes);
        this.dateCreation = dateCreation;
        this.version = 0L;
    }

    public static FormuleProduction creer(String codeProduit, BigDecimal baseKg, List<LigneFormule> lignes) {
        return new FormuleProduction(codeProduit, baseKg, lignes, LocalDateTime.now());
    }

    public static FormuleProduction reconstituer(Long id, String codeProduit, BigDecimal baseKg,
                                                 List<LigneFormule> lignes, LocalDateTime dateCreation,
                                                 LocalDateTime dateModification, Long version) {
        FormuleProduction formule = new FormuleProduction(codeProduit, baseKg, lignes, dateCreation);
        formule.id = id;
        formule.dateModification = dateModification;
        formule.version = version;
        return formule;
    }

    /**
     * Calcule, pour un poids total de produit fini donné, le besoin en
     * chaque matière première de la formule — proportionnellement à la
     * base, en garantissant que la somme des besoins retournés est
     * exactement égale à {@code poidsTotalKg} (méthode du plus grand
     * reste : les arrondis à {@value #ECHELLE_KG} décimales sont d'abord
     * pris par défaut, puis l'écart résiduel est réparti, un incrément à la
     * fois, sur les lignes dont la partie tronquée était la plus grande).
     */
    public List<BesoinMatierePremiere> calculerBesoins(BigDecimal poidsTotalKg) {
        Objects.requireNonNull(poidsTotalKg, "Le poids total est obligatoire");
        if (poidsTotalKg.signum() < 0) {
            throw new IllegalArgumentException("Le poids total ne peut pas être négatif (produit " + codeProduit + ")");
        }
        BigDecimal poidsCible = poidsTotalKg.setScale(ECHELLE_KG, RoundingMode.HALF_UP);

        int n = lignes.size();
        BigDecimal[] exacts = new BigDecimal[n];
        BigDecimal[] planchers = new BigDecimal[n];
        BigDecimal[] restes = new BigDecimal[n];

        for (int i = 0; i < n; i++) {
            // exact = proportion * poidsTotal / base, en haute précision pour ne perdre
            // aucune information avant l'arrondi final.
            exacts[i] = lignes.get(i).getProportionKg()
                .multiply(poidsTotalKg, PRECISION_CALCUL)
                .divide(baseKg, PRECISION_CALCUL);
            planchers[i] = exacts[i].setScale(ECHELLE_KG, RoundingMode.FLOOR);
            restes[i] = exacts[i].subtract(planchers[i]);
        }

        BigDecimal sommePlanchers = java.util.Arrays.stream(planchers).reduce(BigDecimal.ZERO, BigDecimal::add);
        BigDecimal ecart = poidsCible.subtract(sommePlanchers);
        BigDecimal uniteArrondi = BigDecimal.ONE.movePointLeft(ECHELLE_KG); // 0.01
        int unitesARepartir = Math.max(0, Math.min(n,
            ecart.divide(uniteArrondi, 0, RoundingMode.HALF_UP).intValue()));

        List<Integer> indicesTriesParResteDesc = new ArrayList<>();
        for (int i = 0; i < n; i++) indicesTriesParResteDesc.add(i);
        indicesTriesParResteDesc.sort(Comparator.<Integer, BigDecimal>comparing(i -> restes[i]).reversed());

        BigDecimal[] besoins = planchers.clone();
        for (int k = 0; k < unitesARepartir; k++) {
            int i = indicesTriesParResteDesc.get(k);
            besoins[i] = besoins[i].add(uniteArrondi);
        }

        List<BesoinMatierePremiere> resultat = new ArrayList<>(n);
        for (int i = 0; i < n; i++) {
            resultat.add(new BesoinMatierePremiere(lignes.get(i).getMatierePremiere(), besoins[i]));
        }
        return Collections.unmodifiableList(resultat);
    }

    public Long getId() { return id; }
    public String getCodeProduit() { return codeProduit; }
    public BigDecimal getBaseKg() { return baseKg; }
    public List<LigneFormule> getLignes() { return Collections.unmodifiableList(lignes); }
    public LocalDateTime getDateCreation() { return dateCreation; }
    public LocalDateTime getDateModification() { return dateModification; }
    public Long getVersion() { return version; }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof FormuleProduction that)) return false;
        return Objects.equals(codeProduit, that.codeProduit);
    }

    @Override
    public int hashCode() { return Objects.hash(codeProduit); }
}
