package com.NTFOODS.Api_tanty.modules.comptabilite.domain.decaissement;

import com.NTFOODS.Api_tanty.modules.comptabilite.domain.decaissement.aggregate.Decaissement;
import com.NTFOODS.Api_tanty.modules.comptabilite.domain.decaissement.enums.StatutDecaissement;
import com.NTFOODS.Api_tanty.modules.comptabilite.domain.decaissement.enums.TypeDepense;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

class DecaissementTest {

    @Test
    void proposerDecaissementSucces() {
        Decaissement d = Decaissement.proposer("DEC-20260703-001", TypeDepense.CARBURANT,
            "MAT-CG-001", 50000, "Station Total", LocalDate.of(2026, 7, 3), "Carburant tournée");
        assertEquals(StatutDecaissement.PROPOSE, d.getStatut());
    }

    @Test
    void montantNegatifEchoue() {
        assertThrows(IllegalArgumentException.class, () ->
            Decaissement.proposer("DEC-20260703-002", TypeDepense.CARBURANT,
                "MAT-CG-001", -100, "Station", LocalDate.of(2026, 7, 3), "test"));
    }

    @Test
    void workflowComplet_CG_Propose_DG_Valide_Secretaire_Execute() {
        Decaissement d = Decaissement.proposer("DEC-20260703-003", TypeDepense.SALAIRES,
            "MAT-CG-001", 500000, "Personnel", LocalDate.of(2026, 7, 3), "Salaires juillet");
        d.validerParDG("MAT-DG-001");
        assertEquals(StatutDecaissement.VALIDEE_CG, d.getStatut());

        d.approuverParDG("MAT-DG-001");
        assertEquals(StatutDecaissement.VALIDEE_DG, d.getStatut());

        d.executer("MAT-SEC-001");
        assertEquals(StatutDecaissement.EXECUTE, d.getStatut());
    }

    @Test
    void separationDesFonctions_ProposantNePeutPasValider() {
        Decaissement d = Decaissement.proposer("DEC-20260703-004", TypeDepense.AUTRE,
            "MAT-CG-001", 50000, "Test", LocalDate.of(2026, 7, 3), "test");

        IllegalStateException ex = assertThrows(IllegalStateException.class,
            () -> d.validerParDG("MAT-CG-001"));
        assertTrue(ex.getMessage().contains("Séparation des fonctions"));
    }

    @Test
    void separationDesFonctions_ProposantNePeutPasExecuter() {
        Decaissement d = Decaissement.proposer("DEC-20260703-005", TypeDepense.AUTRE,
            "MAT-CG-001", 50000, "Test", LocalDate.of(2026, 7, 3), "test");
        d.validerParDG("MAT-DG-001");
        d.approuverParDG("MAT-DG-001");

        IllegalStateException ex = assertThrows(IllegalStateException.class,
            () -> d.executer("MAT-CG-001"));
        assertTrue(ex.getMessage().contains("Séparation des fonctions"));
    }

    @Test
    void validerAvantPropositionEchoue() {
        Decaissement d = Decaissement.proposer("DEC-20260703-006", TypeDepense.AUTRE,
            "MAT-CG-001", 50000, "Test", LocalDate.of(2026, 7, 3), "test");
        d.validerParDG("MAT-DG-001");

        assertThrows(IllegalStateException.class, () -> d.validerParDG("MAT-DG-002"));
    }

    @Test
    void executerAvantApprobationEchoue() {
        Decaissement d = Decaissement.proposer("DEC-20260703-007", TypeDepense.AUTRE,
            "MAT-CG-001", 50000, "Test", LocalDate.of(2026, 7, 3), "test");
        d.validerParDG("MAT-DG-001");

        assertThrows(IllegalStateException.class, () -> d.executer("MAT-SEC-001"));
    }

    @Test
    void annulerDecaissementExecuteEchoue_ContreEcriture() {
        Decaissement d = Decaissement.proposer("DEC-20260703-008", TypeDepense.AUTRE,
            "MAT-CG-001", 50000, "Test", LocalDate.of(2026, 7, 3), "test");
        d.validerParDG("MAT-DG-001");
        d.approuverParDG("MAT-DG-001");
        d.executer("MAT-SEC-001");

        assertThrows(IllegalStateException.class, () -> d.annuler("erreur", "MAT-DG-001"));
    }
}
