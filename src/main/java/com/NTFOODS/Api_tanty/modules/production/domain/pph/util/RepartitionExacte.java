package com.NTFOODS.Api_tanty.modules.production.domain.pph.util;

import java.math.BigDecimal;
import java.math.MathContext;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

/**
 * Répartition proportionnelle d'un total exact (entier ou décimal) sur N parts
 * pondérées, par arrondi au plus grand reste — garantit que la somme des
 * parts renvoyées est TOUJOURS égale au total fourni, jamais une unité de
 * plus ou de moins, quels que soient les poids.
 *
 * <p>Utilisée pour répartir la production, les besoins en matières premières
 * et les besoins en composants d'emballage d'une ligne de bon de commande sur
 * les jours ouvrés de sa période de production — chaque jour reçoit une part
 * proportionnelle à sa propre quantité produite plutôt qu'un partage égal ou
 * une valeur figée.</p>
 */
public final class RepartitionExacte {

    private static final MathContext PRECISION_CALCUL = new MathContext(50);

    private RepartitionExacte() {}

    /** Répartit un total entier (ex. un besoin en composants d'emballage) proportionnellement aux poids donnés. */
    public static List<Long> repartirEntiers(long total, List<Long> poids) {
        int n = poids.size();
        if (n == 0) {
            if (total != 0) throw new IllegalArgumentException("Impossible de répartir un total non nul sur zéro part");
            return List.of();
        }
        long sommePoids = poids.stream().mapToLong(Long::longValue).sum();
        if (sommePoids == 0) {
            if (total != 0) throw new IllegalArgumentException("Impossible de répartir un total non nul : tous les poids sont nuls");
            return new ArrayList<>(java.util.Collections.nCopies(n, 0L));
        }
        if (total < 0) throw new IllegalArgumentException("Le total à répartir ne peut pas être négatif");

        long[] planchers = new long[n];
        BigDecimal[] restes = new BigDecimal[n];
        BigDecimal totalDecimal = BigDecimal.valueOf(total);
        BigDecimal sommePoidsDecimal = BigDecimal.valueOf(sommePoids);
        for (int i = 0; i < n; i++) {
            BigDecimal exact = totalDecimal.multiply(BigDecimal.valueOf(poids.get(i)), PRECISION_CALCUL)
                .divide(sommePoidsDecimal, PRECISION_CALCUL);
            BigDecimal plancher = exact.setScale(0, RoundingMode.FLOOR);
            planchers[i] = plancher.longValueExact();
            restes[i] = exact.subtract(plancher);
        }

        long sommePlanchers = 0;
        for (long p : planchers) sommePlanchers += p;
        long ecart = total - sommePlanchers;

        List<Integer> indicesTriesParResteDesc = indicesTriesParResteDesc(restes, n);
        long[] resultat = planchers.clone();
        for (int k = 0; k < ecart; k++) {
            resultat[indicesTriesParResteDesc.get(k)]++;
        }

        List<Long> liste = new ArrayList<>(n);
        for (long v : resultat) liste.add(v);
        return liste;
    }

    /** Répartit un total décimal (ex. un besoin en matière première, en kg) proportionnellement aux poids donnés. */
    public static List<BigDecimal> repartirDecimal(BigDecimal total, List<Long> poids, int scale) {
        int n = poids.size();
        if (n == 0) {
            if (total.signum() != 0) throw new IllegalArgumentException("Impossible de répartir un total non nul sur zéro part");
            return List.of();
        }
        long sommePoids = poids.stream().mapToLong(Long::longValue).sum();
        BigDecimal totalCible = total.setScale(scale, RoundingMode.HALF_UP);
        if (sommePoids == 0) {
            if (total.signum() != 0) throw new IllegalArgumentException("Impossible de répartir un total non nul : tous les poids sont nuls");
            return new ArrayList<>(java.util.Collections.nCopies(n, BigDecimal.ZERO.setScale(scale)));
        }
        if (total.signum() < 0) throw new IllegalArgumentException("Le total à répartir ne peut pas être négatif");

        BigDecimal[] planchers = new BigDecimal[n];
        BigDecimal[] restes = new BigDecimal[n];
        BigDecimal sommePoidsDecimal = BigDecimal.valueOf(sommePoids);
        for (int i = 0; i < n; i++) {
            BigDecimal exact = total.multiply(BigDecimal.valueOf(poids.get(i)), PRECISION_CALCUL)
                .divide(sommePoidsDecimal, PRECISION_CALCUL);
            planchers[i] = exact.setScale(scale, RoundingMode.FLOOR);
            restes[i] = exact.subtract(planchers[i]);
        }

        BigDecimal sommePlanchers = BigDecimal.ZERO;
        for (BigDecimal p : planchers) sommePlanchers = sommePlanchers.add(p);
        BigDecimal ecartDecimal = totalCible.subtract(sommePlanchers);
        BigDecimal uniteArrondi = BigDecimal.ONE.movePointLeft(scale);
        int unitesARepartir = Math.max(0, Math.min(n,
            ecartDecimal.divide(uniteArrondi, 0, RoundingMode.HALF_UP).intValue()));

        List<Integer> indicesTriesParResteDesc = indicesTriesParResteDesc(restes, n);
        BigDecimal[] resultat = planchers.clone();
        for (int k = 0; k < unitesARepartir; k++) {
            int i = indicesTriesParResteDesc.get(k);
            resultat[i] = resultat[i].add(uniteArrondi);
        }

        return new ArrayList<>(List.of(resultat));
    }

    private static List<Integer> indicesTriesParResteDesc(BigDecimal[] restes, int n) {
        List<Integer> indices = new ArrayList<>(n);
        for (int i = 0; i < n; i++) indices.add(i);
        indices.sort(Comparator.<Integer, BigDecimal>comparing(i -> restes[i]).reversed());
        return indices;
    }
}
