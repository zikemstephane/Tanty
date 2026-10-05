package com.NTFOODS.Api_tanty.modules.comptabilite.domain.prime;

import com.NTFOODS.Api_tanty.modules.comptabilite.domain.prime.aggregate.BaremePrime;
import com.NTFOODS.Api_tanty.modules.comptabilite.domain.prime.aggregate.PrimeCommerciale;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Barème des primes : validité datée, succession des périodes, et effet sur le calcul.
 *
 * <p>L'enjeu du paramétrage daté est qu'une prime contestée reste justifiable :
 * la rejouer doit redonner le montant d'origine, même après un changement de
 * barème.</p>
 */
class BaremePrimeTest {

    private BaremePrime bareme(double tauxGlobal, double tauxGamme, double parGamme, double plafond,
                                LocalDate debut) {
        return BaremePrime.creer("BAREME-TEST-" + debut, tauxGlobal, tauxGamme, parGamme, plafond,
            debut, "DG-001", "Test");
    }

    // ── Invariants ──────────────────────────────────────────────────────────

    @Test
    @DisplayName("Un plafond inférieur au montant d'une gamme est refusé")
    void plafond_incoherent_refuse() {
        IllegalArgumentException erreur = assertThrows(IllegalArgumentException.class,
            () -> bareme(80, 75, 5_000, 3_000, LocalDate.now()));

        assertTrue(erreur.getMessage().contains("plafond"));
    }

    @Test
    @DisplayName("Un taux hors de l'intervalle 0–100 est refusé")
    void taux_hors_bornes_refuse() {
        assertThrows(IllegalArgumentException.class, () -> bareme(120, 75, 3_000, 12_000, LocalDate.now()));
        assertThrows(IllegalArgumentException.class, () -> bareme(80, -1, 3_000, 12_000, LocalDate.now()));
    }

    @Test
    @DisplayName("Le montant est plafonné quel que soit le nombre de gammes validées")
    void montant_plafonne() {
        BaremePrime b = bareme(80, 75, 3_000, 12_000, LocalDate.now());

        assertEquals(0, b.montantPour(0));
        assertEquals(3_000, b.montantPour(1));
        assertEquals(12_000, b.montantPour(4));
        assertEquals(12_000, b.montantPour(10), "le plafond ne peut jamais être dépassé");
    }

    // ── Succession des périodes ─────────────────────────────────────────────

    @Test
    @DisplayName("Un barème sans date de fin est le barème courant")
    void bareme_ouvert_est_courant() {
        BaremePrime b = bareme(80, 75, 3_000, 12_000, LocalDate.of(2026, 1, 1));

        assertTrue(b.estCourant());
        assertTrue(b.estApplicableA(LocalDate.of(2026, 6, 15)));
        assertFalse(b.estApplicableA(LocalDate.of(2025, 12, 31)), "antérieur à la prise d'effet");
    }

    @Test
    @DisplayName("La clôture s'opère la veille de la prise d'effet du successeur")
    void cloture_la_veille() {
        BaremePrime ancien = bareme(80, 75, 3_000, 12_000, LocalDate.of(2026, 1, 1));

        ancien.cloturerAvant(LocalDate.of(2026, 7, 1));

        assertEquals(LocalDate.of(2026, 6, 30), ancien.getDateFin());
        assertFalse(ancien.estCourant());
        assertTrue(ancien.estApplicableA(LocalDate.of(2026, 6, 30)), "dernier jour couvert");
        assertFalse(ancien.estApplicableA(LocalDate.of(2026, 7, 1)), "premier jour du successeur");
    }

    @Test
    @DisplayName("Un barème déjà clôturé ne peut pas l'être une seconde fois")
    void double_cloture_refusee() {
        BaremePrime b = bareme(80, 75, 3_000, 12_000, LocalDate.of(2026, 1, 1));
        b.cloturerAvant(LocalDate.of(2026, 7, 1));

        assertThrows(IllegalStateException.class, () -> b.cloturerAvant(LocalDate.of(2026, 9, 1)));
    }

    @Test
    @DisplayName("Une clôture antérieure à la prise d'effet est refusée")
    void cloture_avant_debut_refusee() {
        BaremePrime b = bareme(80, 75, 3_000, 12_000, LocalDate.of(2026, 7, 1));

        assertThrows(IllegalArgumentException.class, () -> b.cloturerAvant(LocalDate.of(2026, 6, 1)));
    }

    // ── Effet sur le calcul ─────────────────────────────────────────────────

    @Test
    @DisplayName("Le barème de repli reproduit les valeurs historiques 80 / 75 / 3 000 / 12 000")
    void repli_reproduit_les_regles_historiques() {
        BaremePrime historique = BaremePrime.reglesHistoriques();

        assertEquals(80.0, historique.getTauxGlobalMinimumPourcent());
        assertEquals(75.0, historique.getTauxGammeMinimumPourcent());
        assertEquals(3_000.0, historique.getMontantParGammeFCFA());
        assertEquals(12_000.0, historique.getMontantMaximumFCFA());
    }

    @Test
    @DisplayName("Deux barèmes différents produisent deux montants différents pour la même performance")
    void le_bareme_gouverne_le_montant() {
        Map<String, Double> objectifs = Map.of("BOUILLIES", 100_000.0, "CHOCO", 100_000.0);
        Map<String, Double> realisations = Map.of("BOUILLIES", 90_000.0, "CHOCO", 90_000.0);

        PrimeCommerciale ancienne = PrimeCommerciale.calculer("PRM-1", "COM-001",
            LocalDate.of(2026, 6, 1), LocalDate.of(2026, 6, 7), 200_000, 180_000,
            objectifs, realisations, bareme(80, 75, 3_000, 12_000, LocalDate.of(2026, 1, 1)));

        PrimeCommerciale nouvelle = PrimeCommerciale.calculer("PRM-2", "COM-001",
            LocalDate.of(2026, 8, 1), LocalDate.of(2026, 8, 7), 200_000, 180_000,
            objectifs, realisations, bareme(80, 75, 5_000, 20_000, LocalDate.of(2026, 7, 1)));

        assertEquals(6_000, ancienne.getMontantPrimeFCFA(), "2 gammes × 3 000");
        assertEquals(10_000, nouvelle.getMontantPrimeFCFA(), "2 gammes × 5 000");
    }

    @Test
    @DisplayName("Un seuil global relevé peut rendre inéligible une performance auparavant primée")
    void seuil_global_gouverne_l_eligibilite() {
        Map<String, Double> objectifs = Map.of("BOUILLIES", 100_000.0);
        Map<String, Double> realisations = Map.of("BOUILLIES", 85_000.0);

        PrimeCommerciale avec80 = PrimeCommerciale.calculer("PRM-3", "COM-001",
            LocalDate.of(2026, 6, 1), LocalDate.of(2026, 6, 7), 100_000, 85_000,
            objectifs, realisations, bareme(80, 75, 3_000, 12_000, LocalDate.of(2026, 1, 1)));

        PrimeCommerciale avec90 = PrimeCommerciale.calculer("PRM-4", "COM-001",
            LocalDate.of(2026, 8, 1), LocalDate.of(2026, 8, 7), 100_000, 85_000,
            objectifs, realisations, bareme(90, 75, 3_000, 12_000, LocalDate.of(2026, 7, 1)));

        assertTrue(avec80.isEligible(), "85 % dépasse le seuil de 80 %");
        assertEquals(3_000, avec80.getMontantPrimeFCFA());
        assertFalse(avec90.isEligible(), "85 % n'atteint pas le seuil relevé à 90 %");
        assertEquals(0, avec90.getMontantPrimeFCFA());
    }

    @Test
    @DisplayName("Le barème utilisé est conservé sur la prime, pour justification ultérieure")
    void bareme_trace_sur_la_prime() {
        BaremePrime b = bareme(80, 75, 3_000, 12_000, LocalDate.of(2026, 1, 1));

        PrimeCommerciale prime = PrimeCommerciale.calculer("PRM-5", "COM-001",
            LocalDate.of(2026, 6, 1), LocalDate.of(2026, 6, 7), 100_000, 95_000,
            Map.of("BOUILLIES", 100_000.0), Map.of("BOUILLIES", 95_000.0), b);

        assertEquals(b.getReference(), prime.getReferenceBareme());
    }

    @Test
    @DisplayName("Calculer sans barème est impossible")
    void bareme_obligatoire() {
        assertThrows(NullPointerException.class, () -> PrimeCommerciale.calculer("PRM-6", "COM-001",
            LocalDate.of(2026, 6, 1), LocalDate.of(2026, 6, 7), 100_000, 95_000,
            Map.of(), Map.of(), null));
    }

    @Test
    @DisplayName("Une prime rechargée conserve son montant, insensible aux barèmes ultérieurs")
    void montant_persiste_insensible_au_bareme_courant() {
        PrimeCommerciale rechargee = PrimeCommerciale.reconstituer(1L, "PRM-7", "COM-001",
            LocalDate.of(2026, 6, 1), LocalDate.of(2026, 6, 7), 200_000, 180_000,
            Map.of("BOUILLIES", 100_000.0), Map.of("BOUILLIES", 90_000.0),
            6_000, com.NTFOODS.Api_tanty.modules.comptabilite.domain.prime.enums.StatutPrime.VALIDEE,
            "CPT-001", "BAREME-20260101", null, null, 0L);

        assertEquals(6_000, rechargee.getMontantPrimeFCFA(),
            "le montant persisté fait foi, quel que soit le barème en vigueur aujourd'hui");
        assertEquals("BAREME-20260101", rechargee.getReferenceBareme());
    }
}
