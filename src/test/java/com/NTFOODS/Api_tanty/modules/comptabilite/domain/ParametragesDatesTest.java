package com.NTFOODS.Api_tanty.modules.comptabilite.domain;

import com.NTFOODS.Api_tanty.modules.commercial.domain.vente.aggregate.ReglesAvarie;
import com.NTFOODS.Api_tanty.modules.comptabilite.domain.caisse.aggregate.ParametrageCaisse;
import com.NTFOODS.Api_tanty.modules.comptabilite.domain.objectif.aggregate.ParametrageObjectif;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Familles de configuration datées autres que le barème des primes.
 *
 * <p>Chacune reprend, en repli, la valeur qui était compilée : introduire le
 * paramétrage ne devait modifier aucun comportement existant.</p>
 */
class ParametragesDatesTest {

    @Nested
    @DisplayName("Paramétrage de caisse")
    class Caisse {

        @Test
        @DisplayName("Le repli reproduit les valeurs historiques 500 000 et 5 000")
        void repli_historique() {
            ParametrageCaisse p = ParametrageCaisse.reglesHistoriques();

            assertEquals(500_000.0, p.getSeuilSecurisationFCFA());
            assertEquals(5_000.0, p.getSeuilAlerteRougeVersementFCFA());
        }

        @Test
        @DisplayName("Un manquant supérieur au seuil déclenche l'alerte rouge")
        void alerte_sur_manquant() {
            ParametrageCaisse p = ParametrageCaisse.reglesHistoriques();

            assertTrue(p.declencheAlerteRouge(100_000, 94_000), "6 000 FCFA manquants");
            assertFalse(p.declencheAlerteRouge(100_000, 96_000), "4 000 FCFA manquants, sous le seuil");
        }

        @Test
        @DisplayName("Un excédent ne déclenche jamais d'alerte")
        void pas_d_alerte_sur_excedent() {
            ParametrageCaisse p = ParametrageCaisse.reglesHistoriques();

            assertFalse(p.declencheAlerteRouge(100_000, 110_000),
                "un versement excédentaire ne met pas la recette en risque");
        }

        @Test
        @DisplayName("Un seuil de sécurisation nul ou négatif est refusé")
        void seuil_invalide_refuse() {
            assertThrows(IllegalArgumentException.class, () -> ParametrageCaisse.creer("P", 0, 5_000,
                LocalDate.now(), "DG-001", null));
            assertThrows(IllegalArgumentException.class, () -> ParametrageCaisse.creer("P", -1, 5_000,
                LocalDate.now(), "DG-001", null));
        }

        @Test
        @DisplayName("La clôture rend les périodes contiguës")
        void periodes_contigues() {
            ParametrageCaisse p = ParametrageCaisse.creer("P1", 500_000, 5_000,
                LocalDate.of(2026, 1, 1), "DG-001", null);

            p.cloturerAvant(LocalDate.of(2026, 7, 1));

            assertEquals(LocalDate.of(2026, 6, 30), p.getDateFin());
            assertTrue(p.estApplicableA(LocalDate.of(2026, 6, 30)));
            assertFalse(p.estApplicableA(LocalDate.of(2026, 7, 1)));
            assertFalse(p.estCourant());
        }
    }

    @Nested
    @DisplayName("Paramétrage des objectifs")
    class Objectif {

        @Test
        @DisplayName("Le repli reproduit les six jours ouvrés historiques")
        void repli_historique() {
            assertEquals(6, ParametrageObjectif.reglesHistoriques().getNombreJoursOuvres());
        }

        @Test
        @DisplayName("Un nombre de jours hors de l'intervalle 1–7 est refusé")
        void jours_hors_bornes_refuses() {
            assertThrows(IllegalArgumentException.class,
                () -> ParametrageObjectif.creer("P", 0, LocalDate.now(), "DG-001", null));
            assertThrows(IllegalArgumentException.class,
                () -> ParametrageObjectif.creer("P", 8, LocalDate.now(), "DG-001", null));
        }

        @Test
        @DisplayName("Une semaine de sept jours ouvrés est acceptée")
        void sept_jours_accepte() {
            assertEquals(7, ParametrageObjectif.creer("P", 7, LocalDate.now(), "DG-001", null)
                .getNombreJoursOuvres());
        }
    }

    @Nested
    @DisplayName("Règles d'avarie")
    class Avarie {

        @Test
        @DisplayName("Le repli exige la photo quelle que soit la quantité")
        void repli_historique() {
            // Le repli valait trois unités, valeur héritée d'un seuil qui comptait
            // des colis. Le commanditaire a tranché le 18/08/2026 : toute avarie
            // se photographie.
            assertEquals(0, ReglesAvarie.reglesHistoriques().getQuantiteSeuilPhotoObligatoire());
        }

        @Test
        @DisplayName("Avec le repli, aucune quantité n'échappe à la preuve")
        void photo_obligatoire_au_seuil() {
            ReglesAvarie r = ReglesAvarie.reglesHistoriques();

            assertTrue(r.photoObligatoirePour(1L));
            assertTrue(r.photoObligatoirePour(3L));
            assertTrue(r.photoObligatoirePour(10L));
        }

        @Test
        @DisplayName("Un seuil enregistré l'emporte sur le repli")
        void regle_enregistree_prime_sur_le_repli() {
            ReglesAvarie r = ReglesAvarie.creer("R", 10, LocalDate.now(), "DG-001", "Assouplissement");

            assertFalse(r.photoObligatoirePour(9L));
            assertTrue(r.photoObligatoirePour(10L), "le seuil lui-même déclenche l'obligation");
        }

        @Test
        @DisplayName("Le seuil s'apprécie en unités de base, pas en colis déclarés")
        void seuil_en_unites_de_base() {
            ReglesAvarie r = ReglesAvarie.creer("R", 40, LocalDate.now(), "DG-001", null);

            // Deux cartons de vingt-cinq : « 2 » passerait sous le seuil, les
            // cinquante unités qu'ils contiennent ne le peuvent pas. C'est la perte
            // réelle qui déclenche la preuve, pas la façon de l'énoncer.
            assertFalse(r.photoObligatoirePour(2L), "la quantité déclarée n'est pas la grandeur retenue");
            assertTrue(r.photoObligatoirePour(2L * 25L));
        }

        @Test
        @DisplayName("Un seuil à zéro rend la photo toujours obligatoire")
        void seuil_zero_rend_la_photo_systematique() {
            ReglesAvarie r = ReglesAvarie.creer("R", 0, LocalDate.now(), "DG-001", null);

            assertTrue(r.photoObligatoirePour(0L));
            assertTrue(r.photoObligatoirePour(1L));
        }

        @Test
        @DisplayName("Un seuil négatif est refusé")
        void seuil_negatif_refuse() {
            assertThrows(IllegalArgumentException.class,
                () -> ReglesAvarie.creer("R", -1, LocalDate.now(), "DG-001", null));
        }
    }
}
