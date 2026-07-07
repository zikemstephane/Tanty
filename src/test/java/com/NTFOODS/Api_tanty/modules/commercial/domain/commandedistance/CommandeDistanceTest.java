package com.NTFOODS.Api_tanty.modules.commercial.domain.commandedistance;

import com.NTFOODS.Api_tanty.modules.commercial.domain.commandedistance.aggregate.CommandeDistance;
import com.NTFOODS.Api_tanty.modules.commercial.domain.commandedistance.entity.LigneCommandeDistance;
import com.NTFOODS.Api_tanty.modules.commercial.domain.commandedistance.enums.CanalCommande;
import com.NTFOODS.Api_tanty.modules.commercial.domain.commandedistance.enums.StatutCommandeDistance;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class CommandeDistanceTest {

    @Test
    void recevoirCommandeDistanceSucces() {
        LigneCommandeDistance ligne = new LigneCommandeDistance("ART-001", "Farine 1kg", "Sachet 1kg", 5, 1200);
        CommandeDistance cd = CommandeDistance.recevoir("CDIST-20260703-001", "CLI-001", "MAT-COM-001",
            CanalCommande.APPEL_TELEPHONIQUE, LocalDate.of(2026, 7, 3), List.of(ligne), "Client hors zone");

        assertEquals(StatutCommandeDistance.RECUE, cd.getStatut());
        assertEquals(6000, cd.montantTotal());
    }

    @Test
    void validationCroisee_SecretairePuisComptablePuisLivree() {
        LigneCommandeDistance ligne = new LigneCommandeDistance("ART-001", "Farine 1kg", "Sachet 1kg", 5, 1200);
        CommandeDistance cd = CommandeDistance.recevoir("CDIST-20260703-002", "CLI-001", "MAT-COM-001",
            CanalCommande.WHATSAPP, LocalDate.of(2026, 7, 3), List.of(ligne), null);

        cd.validerParSecretaire("MAT-SEC-001");
        assertEquals(StatutCommandeDistance.VALIDEE_SECRETAIRE, cd.getStatut());

        cd.validerParComptable("MAT-CPT-001");
        assertEquals(StatutCommandeDistance.VALIDEE_COMPTABLE, cd.getStatut());

        cd.marquerLivree();
        assertEquals(StatutCommandeDistance.LIVREE, cd.getStatut());
    }

    @Test
    void validerComptableAvantSecretaireEchoue() {
        LigneCommandeDistance ligne = new LigneCommandeDistance("ART-001", "Farine 1kg", "Sachet 1kg", 5, 1200);
        CommandeDistance cd = CommandeDistance.recevoir("CDIST-20260703-003", "CLI-001", "MAT-COM-001",
            CanalCommande.SMS, LocalDate.of(2026, 7, 3), List.of(ligne), null);

        assertThrows(IllegalStateException.class, () -> cd.validerParComptable("MAT-CPT-001"));
    }

    @Test
    void annulerCommandeLivreeEchoue() {
        LigneCommandeDistance ligne = new LigneCommandeDistance("ART-001", "Farine 1kg", "Sachet 1kg", 5, 1200);
        CommandeDistance cd = CommandeDistance.recevoir("CDIST-20260703-004", "CLI-001", "MAT-COM-001",
            CanalCommande.EMAIL, LocalDate.of(2026, 7, 3), List.of(ligne), null);
        cd.validerParSecretaire("MAT-SEC-001");
        cd.validerParComptable("MAT-CPT-001");
        cd.marquerLivree();

        assertThrows(IllegalStateException.class, () -> cd.annuler("test"));
    }
}
