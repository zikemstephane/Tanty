package com.NTFOODS.Api_tanty.modules.production.domain.broyage;

import com.NTFOODS.Api_tanty.modules.production.domain.broyage.aggregate.SessionBroyage;
import com.NTFOODS.Api_tanty.modules.production.domain.broyage.entity.ObjectifMachiniste;
import com.NTFOODS.Api_tanty.modules.production.domain.broyage.entity.RealisationMachiniste;
import com.NTFOODS.Api_tanty.modules.production.domain.broyage.enums.OperationBroyage;
import com.NTFOODS.Api_tanty.modules.production.domain.broyage.enums.TypePoudre;
import com.NTFOODS.Api_tanty.modules.production.domain.broyage.service.AvancementBroyage;
import com.NTFOODS.Api_tanty.modules.production.domain.formule.entity.BesoinMatierePremiere;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

/** La poudre prévue du plan se rapproche de la poudre écrasée des sessions, par matière. */
class AvancementBroyageTest {

    private static final LocalDate JOUR = LocalDate.of(2026, 10, 4);

    private static BesoinMatierePremiere besoin(String matiere, double kg) {
        return new BesoinMatierePremiere(matiere, BigDecimal.valueOf(kg));
    }

    private static SessionBroyage session(String pph, TypePoudre type) {
        return SessionBroyage.ouvrir(pph, JOUR, type, 100, "CM-001",
            List.of(new ObjectifMachiniste("MA-1", "Machiniste 1", OperationBroyage.ECRASAGE, type, 100)));
    }

    @Test
    void sessionClotureeComptePourLaPoudreEcrasee() {
        SessionBroyage s = session("PPH-A", TypePoudre.MAIS);
        s.enregistrerRealisation(new RealisationMachiniste("MA-1", "M1", OperationBroyage.ECRASAGE, TypePoudre.MAIS, 100, 60));
        s.cloturer(60, 2);

        var r = AvancementBroyage.calculer("PPH-A", List.of(besoin("Maïs", 100)), List.of(s));

        var mais = r.matieres().stream().filter(m -> m.code().equals("MAIS")).findFirst().orElseThrow();
        assertThat(mais.prevuKg()).isEqualTo(100);
        assertThat(mais.ecraseKg()).isEqualTo(60);
        assertThat(mais.restantKg()).isEqualTo(40);
        assertThat(mais.pourcent()).isEqualTo(60);
        assertThat(r.nbSessionsCloturees()).isEqualTo(1);
    }

    @Test
    void sessionOuverteComptePourLeEnCoursSeulementLePosteEcrasage() {
        SessionBroyage s = session("PPH-A", TypePoudre.SOJA);
        s.enregistrerRealisation(new RealisationMachiniste("MA-1", "M1", OperationBroyage.ECRASAGE, TypePoudre.SOJA, 100, 30));
        s.enregistrerRealisation(new RealisationMachiniste("MA-2", "M2", OperationBroyage.TRIAGE, TypePoudre.SOJA, 50, 10));

        var r = AvancementBroyage.calculer("PPH-A", List.of(besoin("Soja", 80)), List.of(s));

        var soja = r.matieres().stream().filter(m -> m.code().equals("SOJA")).findFirst().orElseThrow();
        assertThat(soja.enCoursKg()).isEqualTo(30);
        assertThat(soja.ecraseKg()).isZero();
        assertThat(r.nbSessionsCloturees()).isZero();
    }

    @Test
    void ignoreLesSessionsDUnAutrePlan() {
        SessionBroyage autre = session("PPH-B", TypePoudre.MAIS);
        autre.enregistrerRealisation(new RealisationMachiniste("MA-1", "M1", OperationBroyage.ECRASAGE, TypePoudre.MAIS, 100, 90));
        autre.cloturer(90, 0);

        var r = AvancementBroyage.calculer("PPH-A", List.of(besoin("Maïs", 100)), List.of(autre));

        assertThat(r.nbSessions()).isZero();
        assertThat(r.matieres().get(0).ecraseKg()).isZero();
    }

    @Test
    void matiereSansSessionGardeToutSonRestant() {
        var r = AvancementBroyage.calculer("PPH-A", List.of(besoin("Arachide", 25)), List.of());

        var arachide = r.matieres().get(0);
        assertThat(arachide.code()).isEqualTo("ARACHIDE");
        assertThat(arachide.restantKg()).isEqualTo(25);
        assertThat(arachide.pourcent()).isZero();
    }

    @Test
    void lesNomsDeMatiereSeRapprochentSansAccents() {
        assertThat(AvancementBroyage.codeMatiere("  Maïs ")).isEqualTo("MAIS");
        assertThat(AvancementBroyage.codeMatiere("soja")).isEqualTo("SOJA");
    }

    @Test
    void lesVariantesCourantesSontRamenéesAuTypeDePoudre() {
        assertThat(AvancementBroyage.codeMatiere("Soya")).isEqualTo("SOJA");
        assertThat(AvancementBroyage.codeMatiere("Corn")).isEqualTo("MAIS");
        assertThat(AvancementBroyage.codeMatiere("Arachides")).isEqualTo("ARACHIDE");
        assertThat(AvancementBroyage.codeMatiere("Peanut")).isEqualTo("ARACHIDE");
    }

    @Test
    void uneMatiereHorsBroyageEstMarqueeNonSuivie() {
        SessionBroyage s = session("PPH-A", TypePoudre.MAIS);
        s.enregistrerRealisation(new RealisationMachiniste("MA-1", "M1", OperationBroyage.ECRASAGE, TypePoudre.MAIS, 100, 40));
        s.cloturer(40, 0);

        var r = AvancementBroyage.calculer("PPH-A", List.of(besoin("Maïs", 100), besoin("Sucre", 12)), List.of(s));

        var sucre = r.matieres().stream().filter(m -> m.code().equals("SUCRE")).findFirst().orElseThrow();
        assertThat(sucre.suivie()).isFalse();
        assertThat(sucre.ecraseKg()).isZero();
        assertThat(sucre.restantKg()).isZero();
        assertThat(sucre.pourcent()).isZero();
        var mais = r.matieres().stream().filter(m -> m.code().equals("MAIS")).findFirst().orElseThrow();
        assertThat(mais.suivie()).isTrue();
        assertThat(mais.restantKg()).isEqualTo(60);
    }
}
