package com.NTFOODS.Api_tanty.modules.stock.domain;

import com.NTFOODS.Api_tanty.modules.stock.domain.lot.enums.StatutLot;
import com.NTFOODS.Api_tanty.modules.stock.domain.lot.model.LotStock;
import com.NTFOODS.Api_tanty.shared.kernel.exception.RegleMetierException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Lots de stock : traçabilité de l'arrivage et date limite.
 *
 * <p>Deux invariants sont protégés ici. Le premier : un lot périmé ne sort
 * pas, quelle que soit la quantité qu'il porte encore — c'est toute la raison
 * d'être de la date. Le second : un lot ne disparaît pas tant qu'il reste de
 * la marchandise, sans quoi des produits présents dans l'entrepôt cesseraient
 * d'être traçables.</p>
 */
class LotStockTest {

    private static final String PRODUIT = "TBSA";
    private static final String SKU = "TBSA-CARTON";
    private static final String ENTREPOT = "CENTRAL-01";

    private LotStock lot(LocalDate peremption, long quantite) {
        return LotStock.creer("LOT-20260818-0001", PRODUIT, SKU, "Tanty Bâton Sucré",
            "FRN-001", ENTREPOT, LocalDate.now().minusMonths(1), peremption, quantite, null);
    }

    @Nested
    @DisplayName("Déclaration")
    class Declaration {

        @Test
        @DisplayName("Un lot naît disponible, plein, sans mouvement")
        void naissance() {
            LotStock l = lot(LocalDate.now().plusMonths(6), 100);

            assertEquals(StatutLot.DISPONIBLE, l.getStatut());
            assertEquals(100, l.getQuantiteInitiale());
            assertEquals(100, l.getQuantiteRestante());
            assertNull(l.getDateDernierMouvement());
            assertFalse(l.estPerime());
        }

        @Test
        @DisplayName("Un lot déjà périmé à la déclaration entre périmé, pas disponible")
        void declare_deja_perime() {
            LotStock l = lot(LocalDate.now().minusDays(1), 50);

            assertEquals(StatutLot.PERIME, l.getStatut());
            assertTrue(l.estPerime());
        }

        @Test
        @DisplayName("Une quantité nulle ou négative est refusée")
        void quantite_invalide() {
            assertThrows(RegleMetierException.class, () -> lot(LocalDate.now().plusMonths(6), 0));
            assertThrows(RegleMetierException.class, () -> lot(LocalDate.now().plusMonths(6), -5));
        }

        @Test
        @DisplayName("Une péremption antérieure à la fabrication est refusée")
        void dates_incoherentes() {
            RegleMetierException e = assertThrows(RegleMetierException.class, () ->
                LotStock.creer("LOT-X", PRODUIT, SKU, null, null, ENTREPOT,
                    LocalDate.now(), LocalDate.now().minusDays(1), 10, null));

            assertTrue(e.getMessage().contains("précède"));
        }

        @Test
        @DisplayName("Un produit sans date de péremption n'est jamais périmé")
        void sans_peremption() {
            LotStock l = lot(null, 20);

            assertFalse(l.estPerime());
            assertFalse(l.perimeBientot(365));
            assertNull(l.joursAvantPeremption());
        }
    }

    @Nested
    @DisplayName("Péremption")
    class Peremption {

        @Test
        @DisplayName("Le jour de la date, le lot est déjà hors délai")
        void le_jour_meme_compte_comme_perime() {
            LotStock l = lot(LocalDate.now(), 30);

            assertTrue(l.estPerime(), "La date portée sur l'emballage est le dernier jour inclus");
            assertEquals(0L, l.joursAvantPeremption());
        }

        @Test
        @DisplayName("La veille, il ne l'est pas encore")
        void la_veille_reste_valide() {
            LotStock l = lot(LocalDate.now().plusDays(1), 30);

            assertFalse(l.estPerime());
            assertTrue(l.perimeBientot(30));
            assertEquals(1L, l.joursAvantPeremption());
        }

        @Test
        @DisplayName("Un lot déjà périmé ne périme pas « bientôt » : il l'est")
        void perime_n_est_pas_perimant() {
            LotStock l = lot(LocalDate.now().minusDays(3), 30);

            assertTrue(l.estPerime());
            assertFalse(l.perimeBientot(30));
            assertEquals(-3L, l.joursAvantPeremption());
        }

        @Test
        @DisplayName("Au-delà de l'horizon, aucune alerte")
        void hors_horizon() {
            LotStock l = lot(LocalDate.now().plusDays(31), 30);

            assertFalse(l.perimeBientot(30));
            assertTrue(l.perimeBientot(60));
        }
    }

    @Nested
    @DisplayName("Prélèvement")
    class Prelevement {

        @Test
        @DisplayName("Prélever réduit le reste et horodate le mouvement")
        void consommation_partielle() {
            LotStock l = lot(LocalDate.now().plusMonths(3), 100);

            l.consommer(40);

            assertEquals(100, l.getQuantiteInitiale(), "La quantité d'origine ne bouge pas");
            assertEquals(60, l.getQuantiteRestante());
            assertEquals(StatutLot.DISPONIBLE, l.getStatut());
            assertNotNull(l.getDateDernierMouvement());
        }

        @Test
        @DisplayName("Vider un lot le déclare épuisé")
        void consommation_totale() {
            LotStock l = lot(LocalDate.now().plusMonths(3), 100);

            l.consommer(100);

            assertEquals(0, l.getQuantiteRestante());
            assertEquals(StatutLot.EPUISE, l.getStatut());
        }

        @Test
        @DisplayName("On ne prélève pas plus que le lot ne contient")
        void consommation_excessive() {
            LotStock l = lot(LocalDate.now().plusMonths(3), 100);

            RegleMetierException e = assertThrows(RegleMetierException.class, () -> l.consommer(101));

            assertTrue(e.getMessage().contains("100"));
            assertEquals(100, l.getQuantiteRestante(), "Un refus ne laisse pas de trace");
        }

        @Test
        @DisplayName("Un lot périmé ne sort pas, même plein")
        void consommation_lot_perime() {
            LotStock l = lot(LocalDate.now().minusDays(1), 100);

            RegleMetierException e = assertThrows(RegleMetierException.class, () -> l.consommer(1));

            assertTrue(e.getMessage().contains("périmé"));
            assertEquals(100, l.getQuantiteRestante());
        }

        @Test
        @DisplayName("Un lot en quarantaine ne sort pas")
        void consommation_lot_en_quarantaine() {
            LotStock l = lot(LocalDate.now().plusMonths(3), 100);
            l.changerStatut(StatutLot.QUARANTAINE);

            assertThrows(RegleMetierException.class, () -> l.consommer(1));
        }

        @Test
        @DisplayName("Une quantité nulle ou négative est refusée")
        void quantite_invalide() {
            LotStock l = lot(LocalDate.now().plusMonths(3), 100);

            assertThrows(RegleMetierException.class, () -> l.consommer(0));
            assertThrows(RegleMetierException.class, () -> l.consommer(-10));
        }
    }

    @Nested
    @DisplayName("Statut et retrait")
    class StatutEtRetrait {

        @Test
        @DisplayName("Un lot non vide ne peut pas être déclaré épuisé")
        void epuise_avec_reste() {
            LotStock l = lot(LocalDate.now().plusMonths(3), 100);

            RegleMetierException e = assertThrows(RegleMetierException.class,
                () -> l.changerStatut(StatutLot.EPUISE));

            assertTrue(e.getMessage().contains("100"));
        }

        @Test
        @DisplayName("Un lot non vide ne peut pas être retiré du suivi")
        void retrait_avec_reste() {
            LotStock l = lot(LocalDate.now().plusMonths(3), 100);

            RegleMetierException e = assertThrows(RegleMetierException.class, l::retirer);

            assertTrue(e.getMessage().contains("ne peut être retiré"));
            assertEquals(StatutLot.DISPONIBLE, l.getStatut());
        }

        @Test
        @DisplayName("Un lot vidé se retire")
        void retrait_lot_vide() {
            LotStock l = lot(LocalDate.now().plusMonths(3), 100);
            l.consommer(100);

            l.retirer();

            assertEquals(StatutLot.RETIRE, l.getStatut());
        }

        @Test
        @DisplayName("Un lot retiré ne revient pas")
        void retire_est_definitif() {
            LotStock l = lot(LocalDate.now().plusMonths(3), 100);
            l.consommer(100);
            l.retirer();

            assertThrows(RegleMetierException.class, () -> l.changerStatut(StatutLot.DISPONIBLE));
        }

        @Test
        @DisplayName("Marquer périmé n'écrase ni l'épuisement ni le retrait")
        void marquer_perime_respecte_les_etats_terminaux() {
            LotStock epuise = lot(LocalDate.now().plusMonths(3), 10);
            epuise.consommer(10);
            epuise.marquerPerime();
            assertEquals(StatutLot.EPUISE, epuise.getStatut());

            LotStock disponible = lot(LocalDate.now().plusMonths(3), 10);
            disponible.marquerPerime();
            assertEquals(StatutLot.PERIME, disponible.getStatut());
        }

        @Test
        @DisplayName("Un lot mis en quarantaine puis relâché redevient consommable")
        void quarantaine_reversible() {
            LotStock l = lot(LocalDate.now().plusMonths(3), 100);

            l.changerStatut(StatutLot.QUARANTAINE);
            l.changerStatut(StatutLot.DISPONIBLE);
            l.consommer(10);

            assertEquals(90, l.getQuantiteRestante());
        }
    }
}
