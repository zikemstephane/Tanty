package com.NTFOODS.Api_tanty.modules.comptabilite;

import com.NTFOODS.Api_tanty.modules.comptabilite.application.prime.service.BaremePrimeService;
import com.NTFOODS.Api_tanty.modules.comptabilite.domain.prime.aggregate.BaremePrime;
import com.NTFOODS.Api_tanty.shared.kernel.exception.RegleMetierException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Succession des barèmes en base : les périodes doivent se suivre sans
 * chevauchement, pour qu'une date désigne toujours un barème et un seul.
 */
@SpringBootTest
@ActiveProfiles("test")
@Transactional
class BaremePrimeSuccessionIT {

    @Autowired
    private BaremePrimeService service;

    @Test
    @DisplayName("Sans barème enregistré, les valeurs historiques s'appliquent")
    void repli_sur_les_regles_historiques() {
        BaremePrime courant = service.baremeCourant();

        assertEquals(80.0, courant.getTauxGlobalMinimumPourcent());
        assertEquals(12_000.0, courant.getMontantMaximumFCFA());
    }

    @Test
    @DisplayName("Ouvrir un barème clôture le précédent et rend les périodes contiguës")
    void succession_sans_chevauchement() {
        service.ouvrirNouveauBareme(80, 75, 3_000, 12_000,
            LocalDate.of(2026, 1, 1), "DG-001", "Barème initial");
        service.ouvrirNouveauBareme(85, 70, 5_000, 20_000,
            LocalDate.of(2026, 7, 1), "DG-001", "Revalorisation");

        BaremePrime avant = service.baremeApplicableA(LocalDate.of(2026, 6, 30));
        BaremePrime apres = service.baremeApplicableA(LocalDate.of(2026, 7, 1));

        assertEquals(3_000.0, avant.getMontantParGammeFCFA(), "veille de la bascule");
        assertEquals(5_000.0, apres.getMontantParGammeFCFA(), "jour de la bascule");
        assertFalse(avant.estCourant());
        assertTrue(apres.estCourant());
    }

    @Test
    @DisplayName("Une semaine passée reste régie par le barème de l'époque")
    void recalcul_d_une_semaine_passee() {
        service.ouvrirNouveauBareme(80, 75, 3_000, 12_000,
            LocalDate.of(2026, 1, 1), "DG-001", "Barème initial");
        service.ouvrirNouveauBareme(90, 80, 10_000, 40_000,
            LocalDate.of(2026, 8, 1), "DG-001", "Nouveau barème très différent");

        BaremePrime baremeDeJuin = service.baremeApplicableA(LocalDate.of(2026, 6, 15));

        assertEquals(3_000.0, baremeDeJuin.getMontantParGammeFCFA(),
            "rejouer juin ne doit jamais appliquer le barème d'août");
        assertEquals(6_000.0, baremeDeJuin.montantPour(2));
    }

    @Test
    @DisplayName("Un barème prenant effet avant le courant est refusé")
    void retro_activite_refusee() {
        service.ouvrirNouveauBareme(80, 75, 3_000, 12_000,
            LocalDate.of(2026, 7, 1), "DG-001", "Barème courant");

        assertThrows(RegleMetierException.class, () -> service.ouvrirNouveauBareme(85, 75, 4_000, 16_000,
            LocalDate.of(2026, 3, 1), "DG-001", "Tentative rétroactive"));
    }

    @Test
    @DisplayName("L'historique conserve chaque barème et son auteur")
    void historique_complet() {
        service.ouvrirNouveauBareme(80, 75, 3_000, 12_000,
            LocalDate.of(2026, 1, 1), "DG-001", "Initial");
        service.ouvrirNouveauBareme(85, 75, 4_000, 16_000,
            LocalDate.of(2026, 5, 1), "CPT-002", "Ajustement");
        service.ouvrirNouveauBareme(85, 80, 5_000, 20_000,
            LocalDate.of(2026, 9, 1), "DG-001", "Revalorisation");

        List<BaremePrime> historique = service.historique();

        assertEquals(3, historique.size());
        assertEquals(LocalDate.of(2026, 9, 1), historique.get(0).getDateDebut(), "le plus récent d'abord");
        assertEquals("CPT-002", historique.get(1).getMatriculeAuteur());
        assertEquals("Ajustement", historique.get(1).getMotif());
        assertEquals(1, historique.stream().filter(BaremePrime::estCourant).count(),
            "un seul barème peut être courant à la fois");
    }
}
