package com.NTFOODS.Api_tanty.modules.commercial.domain.prospect;

import com.NTFOODS.Api_tanty.modules.commercial.domain.prospect.aggregate.Prospect;
import com.NTFOODS.Api_tanty.modules.commercial.domain.prospect.enums.CanalProspection;
import com.NTFOODS.Api_tanty.modules.commercial.domain.prospect.enums.StatutProspect;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

class ProspectTest {

    @Test
    void creerProspectNouveau() {
        Prospect p = Prospect.creer("PROS-00001", "Boulangerie Moderne", "+237690000000",
            "Rue des Fleurs", "Yaoundé", "MAT-COM-001", CanalProspection.VISITE_TERRAIN,
            LocalDate.of(2026, 7, 3), "Intéressé par farine 10kg");

        assertEquals(StatutProspect.NOUVEAU, p.getStatut());
    }

    @Test
    void convertirProspectEnClient() {
        Prospect p = Prospect.creer("PROS-00002", "Boulangerie Moderne", "+237690000000",
            "Rue des Fleurs", "Yaoundé", "MAT-COM-001", CanalProspection.VISITE_TERRAIN,
            LocalDate.of(2026, 7, 3), null);
        p.convertir("CLI-010");

        assertEquals(StatutProspect.CONVERTI, p.getStatut());
        assertEquals("CLI-010", p.getCodeClientConverti());
    }

    @Test
    void convertirProspectDejaConvertiEchoue() {
        Prospect p = Prospect.creer("PROS-00003", "Boulangerie Moderne", "+237690000000",
            "Rue des Fleurs", "Yaoundé", "MAT-COM-001", CanalProspection.VISITE_TERRAIN,
            LocalDate.of(2026, 7, 3), null);
        p.convertir("CLI-011");

        assertThrows(IllegalStateException.class, () -> p.convertir("CLI-012"));
    }

    @Test
    void marquerNonInteresse() {
        Prospect p = Prospect.creer("PROS-00004", "Boulangerie Moderne", "+237690000000",
            "Rue des Fleurs", "Yaoundé", "MAT-COM-001", CanalProspection.APPEL_TELEPHONIQUE,
            LocalDate.of(2026, 7, 3), null);
        p.marquerNonInteresse("Pas intéressé");

        assertEquals(StatutProspect.NON_INTERESSE, p.getStatut());
    }
}
