package com.NTFOODS.Api_tanty.modules.production.domain.dashboard;

import com.NTFOODS.Api_tanty.modules.production.domain.dashboard.service.SyntheseResponsableSalle;
import com.NTFOODS.Api_tanty.modules.production.domain.pph.entity.LignePPH;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

/** L'avancement d'un plan additionne ses lignes : réalisé ÷ objectif, sans objectif → pas de pourcentage. */
class SyntheseResponsableSalleTest {

    @Test
    void avancementAdditionneLesLignes() {
        LignePPH a = new LignePPH("P-A", "Produit A", 100);
        LignePPH b = new LignePPH("P-B", "Produit B", 50);
        a.cumulerProduction(50);
        b.cumulerProduction(25);

        var av = SyntheseResponsableSalle.avancementPph(List.of(a, b));

        assertThat(av.nbProduits()).isEqualTo(2);
        assertThat(av.objectif()).isEqualTo(150);
        assertThat(av.realise()).isEqualTo(75);
        assertThat(av.pct()).isEqualTo(50.0);
    }

    @Test
    void sansLignesIlNYAPasDePourcentage() {
        var av = SyntheseResponsableSalle.avancementPph(List.of());

        assertThat(av.nbProduits()).isZero();
        assertThat(av.pct()).isNull();
    }

    @Test
    void sansAffectationLeResumeEstAbsent() {
        var absente = SyntheseResponsableSalle.Affectation.absente();

        assertThat(absente.existe()).isFalse();
        assertThat(absente.postes()).isZero();
    }
}
