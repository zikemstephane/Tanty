package com.NTFOODS.Api_tanty.modules.production.domain.affectation;

import com.NTFOODS.Api_tanty.modules.production.domain.affectation.service.PerformancesPostes;
import com.NTFOODS.Api_tanty.modules.production.domain.affectation.service.PerformancesPostes.Observation;
import com.NTFOODS.Api_tanty.modules.production.domain.affectation.service.PerformancesPostes.Performance;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

/** Les performances d'un poste sur une période : taux et rendement sur les journées saisies, rangs seulement pour les saisis. */
class PerformancesPostesTest {

    private static Observation saisi(String poste, double realise, double objectif, int effectif) {
        return new Observation(poste, true, realise, objectif, effectif);
    }

    private static Observation nonSaisi(String poste) {
        return new Observation(poste, false, 0, 0, 0);
    }

    @Test
    void tauxEtRendementSurLesJourneesSaisiesSeulement() {
        // Deux journées saisies (objectif 100 + 100, réalisé 80 + 120) et une journée affectée sans saisie
        List<Performance> resultat = PerformancesPostes.calculer(List.of(
            saisi("CARAMEL", 80, 100, 2),
            saisi("CARAMEL", 120, 100, 4),
            nonSaisi("CARAMEL")));

        Performance p = resultat.get(0);
        assertThat(p.joursAffectes()).isEqualTo(3);
        assertThat(p.joursSaisis()).isEqualTo(2);
        assertThat(p.tauxRealisation()).isEqualTo(100.0);
        assertThat(p.effectifMoyen()).isEqualTo(3.0);
        assertThat(p.rendementParPersonne()).isEqualTo(200.0 / 6.0);
        assertThat(p.rang()).isEqualTo(1);
    }

    @Test
    void classeLesPostesSaisisParTauxDecroissant() {
        List<Performance> resultat = PerformancesPostes.calculer(List.of(
            saisi("CROQUETTE", 50, 100, 1),
            saisi("CARAMEL", 110, 100, 1),
            saisi("ETIQUETAGE", 90, 100, 1)));

        assertThat(resultat).extracting(Performance::codePoste)
            .containsExactly("CARAMEL", "ETIQUETAGE", "CROQUETTE");
        assertThat(resultat).extracting(Performance::rang).containsExactly(1, 2, 3);
    }

    @Test
    void posteNonSaisiEstListeSansRang() {
        List<Performance> resultat = PerformancesPostes.calculer(List.of(
            saisi("CARAMEL", 90, 100, 1),
            nonSaisi("DATAGE")));

        Performance sansSaisie = resultat.stream().filter(p -> p.codePoste().equals("DATAGE")).findFirst().orElseThrow();
        assertThat(sansSaisie.joursSaisis()).isZero();
        assertThat(sansSaisie.tauxRealisation()).isZero();
        assertThat(sansSaisie.rang()).isNull();
        assertThat(resultat.get(0).codePoste()).isEqualTo("CARAMEL");
    }

    @Test
    void objectifNulOuEffectifNulDonneZeroSansErreur() {
        List<Performance> resultat = PerformancesPostes.calculer(List.of(
            saisi("CARAMEL", 40, 0, 0)));

        Performance p = resultat.get(0);
        assertThat(p.tauxRealisation()).isZero();
        assertThat(p.rendementParPersonne()).isZero();
        assertThat(p.effectifMoyen()).isZero();
    }
}
