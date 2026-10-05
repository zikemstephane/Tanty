package com.NTFOODS.Api_tanty.modules.production.domain.broyage;

import com.NTFOODS.Api_tanty.modules.production.domain.broyage.aggregate.SessionBroyage;
import com.NTFOODS.Api_tanty.modules.production.domain.broyage.entity.ObjectifMachiniste;
import com.NTFOODS.Api_tanty.modules.production.domain.broyage.entity.RealisationMachiniste;
import com.NTFOODS.Api_tanty.modules.production.domain.broyage.enums.OperationBroyage;
import com.NTFOODS.Api_tanty.modules.production.domain.broyage.enums.TypePoudre;
import com.NTFOODS.Api_tanty.modules.production.domain.broyage.service.ApercuBroyage;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

/** La vue d'ensemble du broyage : synthèses, répartition par poudre, série journalière complète. */
class ApercuBroyageTest {

    private static final LocalDate JOUR = LocalDate.of(2026, 10, 4);

    /** Session avec une réalisation par machiniste, éventuellement clôturée. */
    private static SessionBroyage session(LocalDate date, TypePoudre type, double objectif, String[] machinistes,
                                          double[] realises) {
        List<ObjectifMachiniste> objectifs = List.of(new ObjectifMachiniste(machinistes[0], "M1", OperationBroyage.ECRASAGE, type, objectif));
        SessionBroyage s = SessionBroyage.ouvrir("PPH-A", date, type, objectif, "CM-001", objectifs);
        for (int i = 0; i < machinistes.length; i++) {
            s.enregistrerRealisation(new RealisationMachiniste(machinistes[i], "Machiniste " + i,
                OperationBroyage.ECRASAGE, type, objectif, realises[i]));
        }
        return s;
    }

    @Test
    void syntheseDuJourNeRetientQueLesSessionsDeLaJournee() {
        SessionBroyage hier = session(JOUR.minusDays(1), TypePoudre.MAIS, 100, new String[]{"MA-1"}, new double[]{50});
        SessionBroyage auj = session(JOUR, TypePoudre.MAIS, 100, new String[]{"MA-1"}, new double[]{90});

        var a = ApercuBroyage.calculer(List.of(hier, auj), JOUR.minusDays(1), JOUR, JOUR);

        assertThat(a.duJour().nbSessions()).isEqualTo(1);
        assertThat(a.duJour().realise()).isEqualTo(90);
        assertThat(a.duJour().taux()).isEqualTo(90.0);
        assertThat(a.periode().nbSessions()).isEqualTo(2);
        assertThat(a.periode().realise()).isEqualTo(140);
    }

    @Test
    void pertesEtPourcentageSeCalculentSurLesSessionsRenseignees() {
        SessionBroyage s = session(JOUR, TypePoudre.SOJA, 100, new String[]{"MA-1"}, new double[]{90});
        s.cloturer(80, 20);

        var a = ApercuBroyage.calculer(List.of(s), JOUR, JOUR, JOUR);

        assertThat(a.periode().nette()).isEqualTo(80);
        assertThat(a.periode().pertes()).isEqualTo(20);
        assertThat(a.periode().pctPertes()).isEqualTo(20.0);
        assertThat(a.periode().nbClotures()).isEqualTo(1);
    }

    @Test
    void laSerieCouvreChaqueJourEtVautZeroSansSession() {
        SessionBroyage s = session(JOUR.minusDays(1), TypePoudre.MAIS, 100, new String[]{"MA-1"}, new double[]{60});

        var a = ApercuBroyage.calculer(List.of(s), JOUR.minusDays(2), JOUR, JOUR);

        assertThat(a.jours()).hasSize(3);
        assertThat(a.jours().get(0).nbSessions()).isZero();
        assertThat(a.jours().get(0).taux()).isNull();
        assertThat(a.jours().get(1).realise()).isEqualTo(60);
        assertThat(a.jours().get(2).realise()).isZero();
    }

    @Test
    void laRepartitionParPoudreDonneDesPartsQuiTotalisent100() {
        SessionBroyage mais = session(JOUR, TypePoudre.MAIS, 100, new String[]{"MA-1"}, new double[]{75});
        SessionBroyage soja = session(JOUR, TypePoudre.SOJA, 100, new String[]{"MA-2"}, new double[]{25});

        var a = ApercuBroyage.calculer(List.of(mais, soja), JOUR, JOUR, JOUR);

        var parCode = a.poudres().stream().collect(java.util.stream.Collectors.toMap(p -> p.code(), p -> p.part()));
        assertThat(parCode.get("MAIS")).isEqualTo(75.0);
        assertThat(parCode.get("SOJA")).isEqualTo(25.0);
    }

    @Test
    void lesMachinistesSontComptesUneFoisChacun() {
        SessionBroyage s1 = session(JOUR, TypePoudre.MAIS, 100, new String[]{"MA-1", "MA-2"}, new double[]{30, 30});
        SessionBroyage s2 = session(JOUR, TypePoudre.SOJA, 100, new String[]{"MA-1"}, new double[]{20});

        var a = ApercuBroyage.calculer(List.of(s1, s2), JOUR, JOUR, JOUR);

        assertThat(a.periode().machinistes()).isEqualTo(2);
    }

    @Test
    void sansObjectifLeTauxEstAbsentEtNonZero() {
        var a = ApercuBroyage.calculer(List.of(), JOUR, JOUR, JOUR);

        assertThat(a.periode().taux()).isNull();
        assertThat(a.periode().nbSessions()).isZero();
    }

    @Test
    void unePeriodeInverseOuTropLongueEstRefusee() {
        assertThatThrownBy(() -> ApercuBroyage.verifierPeriode(JOUR, JOUR.minusDays(1)))
            .isInstanceOf(IllegalArgumentException.class);
        assertThatThrownBy(() -> ApercuBroyage.verifierPeriode(JOUR.minusDays(400), JOUR))
            .isInstanceOf(IllegalArgumentException.class);
    }
}
