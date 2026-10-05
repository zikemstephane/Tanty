package com.NTFOODS.Api_tanty.modules.production.domain.pph.util;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Répartition proportionnelle au plus grand reste — l'invariant protégé est
 * que la somme des parts renvoyées est TOUJOURS exactement égale au total
 * fourni, y compris quand aucune répartition « ronde » n'existe.
 */
class RepartitionExacteTest {

    @Test
    @DisplayName("100 réparti sur 3 poids égaux : 34/33/33, jamais 34/34/34 ni 33/33/33")
    void entiers_poids_egaux_non_divisible() {
        List<Long> parts = RepartitionExacte.repartirEntiers(100, List.of(1L, 1L, 1L));

        assertEquals(100L, parts.stream().mapToLong(Long::longValue).sum());
        assertEquals(List.of(34L, 33L, 33L), parts, "le reliquat va aux premiers indices, à réste égal");
    }

    @Test
    @DisplayName("Entiers à poids inégaux : la somme reste exacte quel que soit le déséquilibre")
    void entiers_poids_inegaux() {
        List<Long> parts = RepartitionExacte.repartirEntiers(17, List.of(3L, 3L, 3L, 2L, 2L));

        assertEquals(17L, parts.stream().mapToLong(Long::longValue).sum());
        assertTrue(parts.stream().allMatch(p -> p >= 0));
    }

    @Test
    @DisplayName("Décimal à poids inégaux (kg) : la somme des jours reproduit exactement le total, malgré des tiers non ronds")
    void decimal_poids_inegaux_non_divisible() {
        // 10 kg répartis au prorata de [1, 1, 1] : 3.33 + 3.33 + 3.34 = 10.00 exactement.
        List<BigDecimal> parts = RepartitionExacte.repartirDecimal(BigDecimal.TEN, List.of(1L, 1L, 1L), 2);

        BigDecimal somme = parts.stream().reduce(BigDecimal.ZERO, BigDecimal::add);
        assertEquals(0, BigDecimal.valueOf(10.00).setScale(2).compareTo(somme));
        assertEquals(3, parts.size());
    }

    @Test
    @DisplayName("Total nul : toutes les parts sont nulles, sans erreur")
    void total_nul() {
        assertEquals(List.of(0L, 0L), RepartitionExacte.repartirEntiers(0, List.of(1L, 1L)));
        List<BigDecimal> parts = RepartitionExacte.repartirDecimal(BigDecimal.ZERO, List.of(1L, 1L), 2);
        assertTrue(parts.stream().allMatch(p -> p.compareTo(BigDecimal.ZERO) == 0));
    }

    @Test
    @DisplayName("Un poids nul ne reçoit aucune part, mais la somme reste exacte")
    void poids_nul_ne_recoit_rien() {
        List<Long> parts = RepartitionExacte.repartirEntiers(10, List.of(0L, 5L, 0L));

        assertEquals(0L, parts.get(0));
        assertEquals(0L, parts.get(2));
        assertEquals(10L, parts.stream().mapToLong(Long::longValue).sum());
    }
}
