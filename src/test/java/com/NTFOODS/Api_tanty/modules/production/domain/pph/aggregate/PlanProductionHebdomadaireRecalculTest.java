package com.NTFOODS.Api_tanty.modules.production.domain.pph.aggregate;

import com.NTFOODS.Api_tanty.modules.production.domain.pph.entity.LignePPH;
import com.NTFOODS.Api_tanty.modules.production.domain.pph.entity.RepartitionJournaliere;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Bloc 7 — recalcul d'un PPH : ne doit jamais être possible une fois la
 * production démarrée ou clôturée (productionRealisee ne peut alors plus
 * être garanti nul), et doit remettre à zéro le signal d'ajustement manuel
 * puisque la répartition régénérée est fraîche.
 */
class PlanProductionHebdomadaireRecalculTest {

    private PlanProductionHebdomadaire pphBrouillon() {
        return PlanProductionHebdomadaire.creerAvecBC("PPH-TEST-0001", "BC-TEST-0001", "S01-2026",
            LocalDate.of(2026, 1, 5), LocalDate.of(2026, 1, 9), "CP001",
            List.of(new LignePPH("PROD-1", "Produit 1", 10)));
    }

    @Test
    @DisplayName("Un PPH BROUILLON peut être recalculé : lignes, semaine et période remplacées")
    void recalcul_autorise_sur_brouillon() {
        PlanProductionHebdomadaire pph = pphBrouillon();

        pph.recalculer("S02-2026", LocalDate.of(2026, 1, 12), LocalDate.of(2026, 1, 16),
            List.of(new LignePPH("PROD-1", "Produit 1", 20)), List.of(), List.of());

        assertEquals("S02-2026", pph.getSemaine());
        assertEquals(LocalDate.of(2026, 1, 12), pph.getDateDebut());
        assertEquals(20, pph.getLignes().get(0).getObjectifSemaine());
    }

    @Test
    @DisplayName("Un PPH EN_COURS refuse le recalcul : la production réalisée ne peut pas être écrasée")
    void recalcul_refuse_sur_en_cours() {
        PlanProductionHebdomadaire pph = pphBrouillon();
        pph.valider();
        pph.demarrer();

        assertThrows(IllegalStateException.class, () ->
            pph.recalculer("S02-2026", LocalDate.of(2026, 1, 12), LocalDate.of(2026, 1, 16),
                List.of(new LignePPH("PROD-1", "Produit 1", 20)), List.of(), List.of()));
    }

    @Test
    @DisplayName("Un PPH CLOTURE refuse le recalcul")
    void recalcul_refuse_sur_cloture() {
        PlanProductionHebdomadaire pph = pphBrouillon();
        pph.valider();
        pph.demarrer();
        pph.cloturer();

        assertThrows(IllegalStateException.class, () ->
            pph.recalculer("S02-2026", LocalDate.of(2026, 1, 12), LocalDate.of(2026, 1, 16),
                List.of(new LignePPH("PROD-1", "Produit 1", 20)), List.of(), List.of()));
    }

    @Test
    @DisplayName("Le recalcul remet à zéro le signal d'ajustement manuel : la répartition régénérée est fraîche")
    void recalcul_reinitialise_le_signal_ajustement_manuel() {
        PlanProductionHebdomadaire pph = pphBrouillon();
        RepartitionJournaliere ajustement = new RepartitionJournaliere(
            LocalDate.of(2026, 1, 5), List.of(), List.of(), List.of(), 0.0);
        pph.ajusterRepartitionJours(List.of(ajustement));
        assertTrue(pph.isRepartitionAjusteeManuellement());

        pph.recalculer("S01-2026", LocalDate.of(2026, 1, 5), LocalDate.of(2026, 1, 9),
            List.of(new LignePPH("PROD-1", "Produit 1", 10)), List.of(), List.of());

        assertFalse(pph.isRepartitionAjusteeManuellement(),
            "après recalcul, la répartition est de nouveau système, pas manuelle");
    }
}
