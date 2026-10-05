package com.NTFOODS.Api_tanty.modules.production.domain.dosage;

import com.NTFOODS.Api_tanty.modules.production.domain.dosage.aggregate.SessionDosage;
import com.NTFOODS.Api_tanty.modules.production.domain.dosage.service.AvancementDosage;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

/** Les fûts dosés d'un plan se séparent entre sessions validées et sessions en cours, par jour. */
class AvancementDosageTest {

    private static final LocalDate LUNDI = LocalDate.of(2026, 9, 28);

    private static SessionDosage session(String pph, LocalDate date, int futs, boolean validee) {
        SessionDosage s = SessionDosage.ouvrir(pph, date, "AD-001");
        s.enregistrerFuts(futs);
        if (validee) s.valider();
        return s;
    }

    @Test
    void separeFutsValidesEtFutsEnCours() {
        var resultat = AvancementDosage.calculer("PPH-A", List.of(
            session("PPH-A", LUNDI, 10, true),
            session("PPH-A", LUNDI.plusDays(1), 4, false)));

        assertThat(resultat.nbSessions()).isEqualTo(2);
        assertThat(resultat.nbSessionsValidees()).isEqualTo(1);
        assertThat(resultat.futsValides()).isEqualTo(10);
        assertThat(resultat.futsEnCours()).isEqualTo(4);
        assertThat(resultat.futsDoses()).isEqualTo(14);
    }

    @Test
    void ignoreLesSessionsDUnAutrePlan() {
        var resultat = AvancementDosage.calculer("PPH-A", List.of(
            session("PPH-A", LUNDI, 3, true),
            session("PPH-B", LUNDI, 99, true)));

        assertThat(resultat.nbSessions()).isEqualTo(1);
        assertThat(resultat.futsDoses()).isEqualTo(3);
    }

    @Test
    void cumuleParJourEtTrieLesDates() {
        var resultat = AvancementDosage.calculer("PPH-A", List.of(
            session("PPH-A", LUNDI.plusDays(1), 5, false),
            session("PPH-A", LUNDI, 2, true),
            session("PPH-A", LUNDI, 3, false)));

        assertThat(resultat.parJour()).containsExactly(
            new AvancementDosage.Jour(LUNDI, 2, 3),
            new AvancementDosage.Jour(LUNDI.plusDays(1), 0, 5));
    }

    @Test
    void sansSessionDonneZero() {
        var resultat = AvancementDosage.calculer("PPH-A", List.of());

        assertThat(resultat.futsDoses()).isZero();
        assertThat(resultat.parJour()).isEmpty();
    }
}
