package com.NTFOODS.Api_tanty.modules.production.domain.lot;

import com.NTFOODS.Api_tanty.modules.production.domain.lot.aggregate.LotProduitFini;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;

import static org.assertj.core.api.Assertions.assertThat;

/** La production d'une période se mesure à la date de validation par le stock, pas à la date de production. */
class LotProduitFiniValidePeriodeTest {

    private static LotProduitFini lotDeclare(LocalDate dateProduction) {
        return LotProduitFini.declarer("LOT-TEST-001", "PPH-TEST", "SKU-1", "Produit test", "kg",
            120.0, 10, dateProduction, dateProduction.plusMonths(6), "CP-001", null, null, null, "CP-001");
    }

    @Test
    void lotDeclareNonValideNeComptePasDansLaPeriode() {
        LotProduitFini lot = lotDeclare(LocalDate.now());

        assertThat(lot.estValidePeriode(LocalDate.now().minusDays(7), LocalDate.now())).isFalse();
    }

    @Test
    void lotValideAujourdhuiCompteDansUnePeriodeQuiContientAujourdhui() {
        LotProduitFini lot = lotDeclare(LocalDate.now().minusDays(20));
        lot.validerParStock("GS-001");

        assertThat(lot.estValidePeriode(LocalDate.now().minusDays(7), LocalDate.now())).isTrue();
    }

    @Test
    void lotValideAujourdhuiNeComptePasDansUnePeriodePassee() {
        LotProduitFini lot = lotDeclare(LocalDate.now().minusDays(20));
        lot.validerParStock("GS-001");

        assertThat(lot.estValidePeriode(LocalDate.now().minusDays(30), LocalDate.now().minusDays(10))).isFalse();
    }
}
