package com.NTFOODS.Api_tanty.modules.commercial.domain.feuillroute;

import com.NTFOODS.Api_tanty.modules.commercial.domain.feuillroute.aggregate.FeuillRoute;
import com.NTFOODS.Api_tanty.modules.commercial.domain.feuillroute.entity.ArretFeuillRoute;
import com.NTFOODS.Api_tanty.modules.commercial.domain.feuillroute.enums.SaisieArret;
import com.NTFOODS.Api_tanty.modules.commercial.domain.feuillroute.enums.StatutArret;
import com.NTFOODS.Api_tanty.modules.commercial.domain.feuillroute.enums.StatutFeuillRoute;
import com.NTFOODS.Api_tanty.modules.commercial.domain.precommande.enums.InfoClientEnum;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class FeuillRouteTest {

    @Test
    void planifierFeuillRouteAvecArrets() {
        ArretFeuillRoute a1 = new ArretFeuillRoute("CLI-001", 1, LocalDateTime.now());
        ArretFeuillRoute a2 = new ArretFeuillRoute("CLI-002", 2, LocalDateTime.now().plusMinutes(30));
        FeuillRoute fr = FeuillRoute.planifier("FR-20260703-001", "MAT-COM-001",
            LocalDate.of(2026, 7, 3), List.of(a1, a2));

        assertEquals(StatutFeuillRoute.PLANIFIE, fr.getStatut());
        assertEquals(2, fr.getArrets().size());
    }

    @Test
    void planifierSansArretsEchoue() {
        assertThrows(IllegalArgumentException.class, () ->
            FeuillRoute.planifier("FR-20260703-002", "MAT-COM-001",
                LocalDate.of(2026, 7, 3), List.of()));
    }

    @Test
    void viserArretApresDemarrage() {
        ArretFeuillRoute a1 = new ArretFeuillRoute("CLI-001", 1, LocalDateTime.now());
        FeuillRoute fr = FeuillRoute.planifier("FR-20260703-003", "MAT-COM-001",
            LocalDate.of(2026, 7, 3), List.of(a1));
        fr.demarrer();
        fr.viserArret("CLI-001", "VTE-20260703-001", "4.0512,9.7672");

        assertEquals(StatutArret.VISE, fr.getArrets().get(0).getStatut());
        assertEquals(1, fr.nombreArretVises());
    }

    @Test
    void viserArretAvantDemarrageEchoue() {
        ArretFeuillRoute a1 = new ArretFeuillRoute("CLI-001", 1, LocalDateTime.now());
        FeuillRoute fr = FeuillRoute.planifier("FR-20260703-004", "MAT-COM-001",
            LocalDate.of(2026, 7, 3), List.of(a1));

        assertThrows(IllegalStateException.class, () ->
            fr.viserArret("CLI-001", "VTE-001", null));
    }

    @Test
    void marquerNonViseAvecCodeInfoClient() {
        ArretFeuillRoute a1 = new ArretFeuillRoute("CLI-001", 1, LocalDateTime.now());
        FeuillRoute fr = FeuillRoute.planifier("FR-20260703-005", "MAT-COM-001",
            LocalDate.of(2026, 7, 3), List.of(a1));
        fr.demarrer();
        fr.marquerArretNonVise("CLI-001",
            new SaisieArret("CLI-001", InfoClientEnum.Ab, null, "Absent"), null);

        assertEquals(StatutArret.NON_VISE, fr.getArrets().get(0).getStatut());
        assertEquals(1, fr.nombreArretNonVises());
    }

    @Test
    void codeInfoClientEVExigeQuantite() {
        assertThrows(IllegalArgumentException.class, () ->
            new SaisieArret("CLI-001", InfoClientEnum.EV, null, null));
    }

    @Test
    void codeInfoClientEVAvecQuantiteSucces() {
        SaisieArret saisie = new SaisieArret("CLI-001", InfoClientEnum.EV, 5, null);
        assertEquals(5, saisie.getQuantiteInfo());
    }

    @Test
    void cloturerAvantTousTraitesEchoue() {
        ArretFeuillRoute a1 = new ArretFeuillRoute("CLI-001", 1, LocalDateTime.now());
        ArretFeuillRoute a2 = new ArretFeuillRoute("CLI-002", 2, LocalDateTime.now());
        FeuillRoute fr = FeuillRoute.planifier("FR-20260703-006", "MAT-COM-001",
            LocalDate.of(2026, 7, 3), List.of(a1, a2));
        fr.demarrer();
        fr.viserArret("CLI-001", "VTE-001", null);

        assertThrows(IllegalStateException.class, () -> fr.cloturer("MAT-COM-001"));
    }

    @Test
    void cloturerApresTousTraitesSucces() {
        ArretFeuillRoute a1 = new ArretFeuillRoute("CLI-001", 1, LocalDateTime.now());
        FeuillRoute fr = FeuillRoute.planifier("FR-20260703-007", "MAT-COM-001",
            LocalDate.of(2026, 7, 3), List.of(a1));
        fr.demarrer();
        fr.viserArret("CLI-001", "VTE-001", null);
        fr.cloturer("MAT-COM-001");

        assertEquals(StatutFeuillRoute.CLOTURE, fr.getStatut());
    }

    @Test
    void modifierApresClotureEchoue_LockNumerique() {
        ArretFeuillRoute a1 = new ArretFeuillRoute("CLI-001", 1, LocalDateTime.now());
        FeuillRoute fr = FeuillRoute.planifier("FR-20260703-008", "MAT-COM-001",
            LocalDate.of(2026, 7, 3), List.of(a1));
        fr.demarrer();
        fr.viserArret("CLI-001", "VTE-001", null);
        fr.cloturer("MAT-COM-001");

        assertThrows(IllegalStateException.class, () ->
            fr.viserArret("CLI-001", "VTE-002", null));
    }
}
