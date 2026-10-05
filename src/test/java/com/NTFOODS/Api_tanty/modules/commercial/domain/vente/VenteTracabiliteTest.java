package com.NTFOODS.Api_tanty.modules.commercial.domain.vente;

import com.NTFOODS.Api_tanty.modules.commercial.domain.vente.aggregate.Vente;
import com.NTFOODS.Api_tanty.modules.commercial.domain.vente.entity.LigneVente;
import com.NTFOODS.Api_tanty.modules.commercial.domain.vente.enums.ModeReglement;
import com.NTFOODS.Api_tanty.modules.commercial.domain.vente.enums.StatutVente;
import com.NTFOODS.Api_tanty.modules.commercial.domain.vente.enums.TypeVente;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Traçabilité d'une vente terrain : position relevée et rattachement à la tournée.
 *
 * <p>Ces deux informations étaient transmises par le mobile puis abandonnées faute
 * de chemin dans le domaine ; elles sont désormais portées par l'agrégat.</p>
 */
class VenteTracabiliteTest {

    private Vente venteEnCours() {
        return Vente.creer("VTE-20260815-000001", "uuid-local-1", "COM-001", "CLI-001",
            LocalDate.now(), ModeReglement.COMPTANT, TypeVente.COMPTANT,
            List.of(new LigneVente("TBSA", "Bouillie Soja Arachide", "CARTON", 10, 2500, 0)),
            "photo://produits");
    }

    @Test
    @DisplayName("La position relevée est conservée")
    void position_est_enregistree() {
        Vente vente = venteEnCours();

        vente.enregistrerPosition("3.8480,11.5021");

        assertEquals("3.8480,11.5021", vente.getCoordonneesGPS());
    }

    @Test
    @DisplayName("Une position absente laisse la vente inchangée plutôt que d'échouer")
    void position_absente_est_ignoree() {
        Vente vente = venteEnCours();

        vente.enregistrerPosition(null);
        vente.enregistrerPosition("   ");

        assertNull(vente.getCoordonneesGPS(), "un relevé GPS peut légitimement échouer sur le terrain");
    }

    @Test
    @DisplayName("La vente est rattachée à sa feuille de route")
    void rattachement_a_la_tournee() {
        Vente vente = venteEnCours();

        vente.rattacherATournee("FR-20260815-001");

        assertEquals("FR-20260815-001", vente.getIdFeuillRoute());
    }

    @Test
    @DisplayName("Une vente hors tournée planifiée reste valide")
    void rattachement_absent_est_ignore() {
        Vente vente = venteEnCours();

        vente.rattacherATournee(null);

        assertNull(vente.getIdFeuillRoute());
    }

    @Test
    @DisplayName("La position d'une vente finalisée n'est plus modifiable")
    void position_figee_apres_finalisation() {
        Vente vente = venteEnCours();
        vente.enregistrerPosition("3.8480,11.5021");
        vente.finaliser();

        assertThrows(IllegalStateException.class, () -> vente.enregistrerPosition("0,0"));
        assertEquals("3.8480,11.5021", vente.getCoordonneesGPS());
    }

    @Test
    @DisplayName("Une vente finalisée ne peut plus changer de tournée")
    void rattachement_fige_apres_finalisation() {
        Vente vente = venteEnCours();
        vente.rattacherATournee("FR-20260815-001");
        vente.finaliser();

        assertThrows(IllegalStateException.class, () -> vente.rattacherATournee("FR-20260815-002"));
    }

    @Test
    @DisplayName("La finalisation émet l'événement qui alimente stock et réconciliation")
    void finalisation_emet_l_evenement_de_domaine() {
        Vente vente = venteEnCours();

        vente.finaliser();

        assertEquals(StatutVente.FINALISEE, vente.getStatut());
        assertEquals(1, vente.getDomainEvents().size(),
            "VenteFinaliseEvent conditionne le décrément du stock mobile");
    }

    @Test
    @DisplayName("Une vente à crédit non signée peut être finalisée : la signature n'est plus exigée")
    void credit_sans_signature_finalisable() {
        Vente vente = Vente.creer("VTE-20260815-000002", "uuid-local-2", "COM-001", "CLI-001",
            LocalDate.now(), ModeReglement.CREDIT, TypeVente.CREDIT,
            List.of(new LigneVente("TBSA", "Bouillie Soja Arachide", "CARTON", 5, 2500, 0)),
            "photo://produits");

        vente.finaliser();

        assertEquals(StatutVente.FINALISEE, vente.getStatut());
    }
}
