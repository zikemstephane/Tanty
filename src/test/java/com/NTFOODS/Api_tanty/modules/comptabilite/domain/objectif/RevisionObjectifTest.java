package com.NTFOODS.Api_tanty.modules.comptabilite.domain.objectif;

import com.NTFOODS.Api_tanty.modules.comptabilite.domain.objectif.aggregate.ObjectifCommercial;
import com.NTFOODS.Api_tanty.modules.comptabilite.domain.objectif.aggregate.RevisionObjectif;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Révision d'objectif : la prime se calcule sur la valeur courante, mais la
 * valeur remplacée ne disparaît jamais.
 *
 * <p>L'invariant protégé est celui-ci : abaisser un objectif en fin de semaine
 * reste possible — un aléa terrain peut le justifier — mais devient opposable.
 * Sans ce journal, un commercial pouvait être rendu éligible à sa prime par une
 * simple retouche de sa cible, sans trace.</p>
 */
class RevisionObjectifTest {

    private static final String COMMERCIAL = "COM-001";
    private static final String DG = "MAT-DG-001";
    private static final String RP = "MAT-RP-001";

    private ObjectifCommercial objectif() {
        return ObjectifCommercial.definir(COMMERCIAL, LocalDate.of(2026, 8, 17), LocalDate.of(2026, 8, 22),
            2_000_000, Map.of("Bouillies", 1_200_000.0, "Snacks", 800_000.0), 6, DG);
    }

    @Test
    @DisplayName("Un objectif neuf n'a aucune révision et son initial vaut son courant")
    void objectif_neuf() {
        ObjectifCommercial o = objectif();

        assertFalse(o.aEteRevise());
        assertTrue(o.getRevisions().isEmpty());
        assertEquals(2_000_000, o.objectifGlobalInitialFCFA());
    }

    @Test
    @DisplayName("La révision remplace la valeur courante")
    void revision_remplace_la_valeur() {
        ObjectifCommercial o = objectif();

        o.reviser(1_800_000, Map.of("Bouillies", 1_000_000.0, "Snacks", 800_000.0), RP,
            "Rupture de stock sur les bouillies du 19 au 21");

        assertEquals(1_800_000, o.getObjectifGlobalFCFA());
        assertEquals(RP, o.getMatriculeAuteur());
        assertNotNull(o.getDateModification());
    }

    @Test
    @DisplayName("La valeur remplacée est conservée au journal")
    void valeur_remplacee_conservee() {
        ObjectifCommercial o = objectif();

        o.reviser(1_800_000, Map.of("Bouillies", 1_000_000.0), RP, "Rupture de stock");

        assertTrue(o.aEteRevise());
        assertEquals(1, o.getRevisions().size());
        RevisionObjectif r = o.getRevisions().get(0);
        assertEquals(2_000_000, r.getObjectifGlobalAvantFCFA());
        assertEquals(1_800_000, r.getObjectifGlobalApresFCFA());
        assertEquals(RP, r.getMatriculeAuteur());
        assertEquals("Rupture de stock", r.getMotif());
        assertNotNull(r.getDateRevision());
    }

    @Test
    @DisplayName("Les objectifs par gamme antérieurs sont conservés eux aussi")
    void gammes_anterieures_conservees() {
        ObjectifCommercial o = objectif();

        o.reviser(1_800_000, Map.of("Bouillies", 1_000_000.0), RP, "Rupture de stock");

        Map<String, Double> avant = o.getRevisions().get(0).getObjectifsParGammeAvantFCFA();
        assertEquals(1_200_000.0, avant.get("Bouillies"));
        assertEquals(800_000.0, avant.get("Snacks"));
    }

    @Test
    @DisplayName("Un abaissement est identifié comme tel")
    void abaissement_identifie() {
        ObjectifCommercial o = objectif();

        o.reviser(1_800_000, Map.of(), RP, "Aléa terrain");

        RevisionObjectif r = o.getRevisions().get(0);
        assertTrue(r.estUnAbaissement());
        assertEquals(-200_000, r.variationFCFA());
    }

    @Test
    @DisplayName("Un relèvement n'est pas un abaissement")
    void relevement_identifie() {
        ObjectifCommercial o = objectif();

        o.reviser(2_500_000, Map.of(), DG, "Renfort de tournée");

        assertFalse(o.getRevisions().get(0).estUnAbaissement());
        assertEquals(500_000, o.getRevisions().get(0).variationFCFA());
    }

    @Test
    @DisplayName("L'objectif initial survit à plusieurs révisions successives")
    void initial_survit_aux_revisions() {
        ObjectifCommercial o = objectif();

        o.reviser(1_800_000, Map.of(), RP, "Première révision");
        o.reviser(1_500_000, Map.of(), RP, "Seconde révision");

        assertEquals(2, o.getRevisions().size());
        assertEquals(2_000_000, o.objectifGlobalInitialFCFA());
        assertEquals(1_500_000, o.getObjectifGlobalFCFA());
    }

    @Test
    @DisplayName("Les révisions s'empilent dans l'ordre où elles ont eu lieu")
    void revisions_ordonnees() {
        ObjectifCommercial o = objectif();

        o.reviser(1_800_000, Map.of(), RP, "Première");
        o.reviser(1_500_000, Map.of(), RP, "Seconde");

        assertEquals(2_000_000, o.getRevisions().get(0).getObjectifGlobalAvantFCFA());
        assertEquals(1_800_000, o.getRevisions().get(1).getObjectifGlobalAvantFCFA());
    }

    @Test
    @DisplayName("Une révision sans motif est refusée")
    void motif_obligatoire() {
        ObjectifCommercial o = objectif();

        assertThrows(IllegalArgumentException.class, () -> o.reviser(1_800_000, Map.of(), RP, null));
        assertThrows(IllegalArgumentException.class, () -> o.reviser(1_800_000, Map.of(), RP, "   "));
        assertFalse(o.aEteRevise(), "Un refus ne doit rien inscrire au journal");
        assertEquals(2_000_000, o.getObjectifGlobalFCFA());
    }

    @Test
    @DisplayName("Un objectif révisé à zéro ou en négatif est refusé")
    void montant_invalide_refuse() {
        ObjectifCommercial o = objectif();

        assertThrows(IllegalArgumentException.class, () -> o.reviser(0, Map.of(), RP, "Erreur"));
        assertThrows(IllegalArgumentException.class, () -> o.reviser(-100, Map.of(), RP, "Erreur"));
        assertEquals(2_000_000, o.getObjectifGlobalFCFA());
    }

    @Test
    @DisplayName("Le journal n'est pas modifiable de l'extérieur")
    void journal_immuable() {
        ObjectifCommercial o = objectif();
        o.reviser(1_800_000, Map.of(), RP, "Rupture de stock");

        assertThrows(UnsupportedOperationException.class, () -> o.getRevisions().clear());
    }

    @Test
    @DisplayName("L'objectif quotidien suit la valeur révisée")
    void objectif_quotidien_suit_la_revision() {
        ObjectifCommercial o = objectif();
        assertEquals(2_000_000 / 6.0, o.objectifQuotidienGlobal(), 0.01);

        o.reviser(1_800_000, Map.of(), RP, "Rupture de stock");

        assertEquals(1_800_000 / 6.0, o.objectifQuotidienGlobal(), 0.01);
    }
}
