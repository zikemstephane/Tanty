package com.NTFOODS.Api_tanty.modules.commercial.domain.vente;

import com.NTFOODS.Api_tanty.modules.commercial.domain.vente.aggregate.Vente;
import com.NTFOODS.Api_tanty.modules.commercial.domain.vente.entity.LigneVente;
import com.NTFOODS.Api_tanty.modules.commercial.domain.vente.enums.ModeReglement;
import com.NTFOODS.Api_tanty.modules.commercial.domain.vente.enums.StatutVente;
import com.NTFOODS.Api_tanty.modules.commercial.domain.vente.enums.SyncStatus;
import com.NTFOODS.Api_tanty.modules.commercial.domain.vente.enums.TypeVente;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class VenteTest {

    @Test
    void creerVenteAvecLignesValides() {
        LigneVente ligne = new LigneVente("ART-001", "Farine 1kg", "Sachet 1kg", 5, 1200, 0);
        Vente vente = Vente.creer("VTE-20260703-001", "MAT-COM-001", "CLI-001",
            LocalDate.of(2026, 7, 3), ModeReglement.COMPTANT, TypeVente.COMPTANT, List.of(ligne));

        assertEquals(StatutVente.EN_COURS, vente.getStatut());
        assertEquals(SyncStatus.SYNCED, vente.getSyncStatus());
        assertEquals(TypeVente.COMPTANT, vente.getTypeVente());
        assertEquals(6000, vente.montantTotal());
    }

    @Test
    void creerVenteSansLignesEchoue() {
        assertThrows(IllegalArgumentException.class, () ->
            Vente.creer("VTE-20260703-002", "MAT-COM-001", "CLI-001",
                LocalDate.of(2026, 7, 3), ModeReglement.COMPTANT, TypeVente.COMPTANT, List.of()));
    }

    @Test
    void finaliserVenteEnCoursSucces() {
        LigneVente ligne = new LigneVente("ART-001", "Farine 1kg", "Sachet 1kg", 5, 1200, 0);
        Vente vente = Vente.creer("VTE-20260703-003", "MAT-COM-001", "CLI-001",
            LocalDate.of(2026, 7, 3), ModeReglement.COMPTANT, TypeVente.COMPTANT, List.of(ligne));

        vente.finaliser();

        assertEquals(StatutVente.FINALISEE, vente.getStatut());
    }

    @Test
    void finaliserVenteDejaFinaliseeEchoue() {
        LigneVente ligne = new LigneVente("ART-001", "Farine 1kg", "Sachet 1kg", 5, 1200, 0);
        Vente vente = Vente.creer("VTE-20260703-004", "MAT-COM-001", "CLI-001",
            LocalDate.of(2026, 7, 3), ModeReglement.COMPTANT, TypeVente.COMPTANT, List.of(ligne));
        vente.finaliser();

        assertThrows(IllegalStateException.class, vente::finaliser);
    }

    @Test
    void annulerVenteFinaliseeEchoue_RG_ContreEcriture() {
        LigneVente ligne = new LigneVente("ART-001", "Farine 1kg", "Sachet 1kg", 5, 1200, 0);
        Vente vente = Vente.creer("VTE-20260703-005", "MAT-COM-001", "CLI-001",
            LocalDate.of(2026, 7, 3), ModeReglement.COMPTANT, TypeVente.COMPTANT, List.of(ligne));
        vente.finaliser();

        IllegalStateException ex = assertThrows(IllegalStateException.class, () -> vente.annuler("test"));
        assertTrue(ex.getMessage().contains("avoir"));
    }

    @Test
    void syncStatusTransitions() {
        LigneVente ligne = new LigneVente("ART-001", "Farine 1kg", "Sachet 1kg", 5, 1200, 0);
        Vente vente = Vente.creer("VTE-20260703-006", "MAT-COM-001", "CLI-001",
            LocalDate.of(2026, 7, 3), ModeReglement.COMPTANT, TypeVente.COMPTANT, List.of(ligne));

        vente.marquerSyncEnAttente();
        assertEquals(SyncStatus.PENDING, vente.getSyncStatus());

        vente.marquerSyncConflit();
        assertEquals(SyncStatus.CONFLICT, vente.getSyncStatus());

        vente.marquerSyncFait();
        assertEquals(SyncStatus.SYNCED, vente.getSyncStatus());
    }

    @Test
    void ligneVenteRemiseBornee0a100() {
        LigneVente ligne = new LigneVente("ART-001", "Farine 1kg", "Sachet 1kg", 5, 1200, 150);
        assertEquals(0, ligne.getRemise());

        LigneVente ligne2 = new LigneVente("ART-001", "Farine 1kg", "Sachet 1kg", 5, 1200, -10);
        assertEquals(0, ligne2.getRemise());

        LigneVente ligne3 = new LigneVente("ART-001", "Farine 1kg", "Sachet 1kg", 5, 1200, 10);
        assertEquals(10, ligne3.getRemise());
        assertEquals(5400, ligne3.montantHT());
    }
}
