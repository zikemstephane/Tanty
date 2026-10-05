package com.NTFOODS.Api_tanty.modules.stock.domain;

import com.NTFOODS.Api_tanty.modules.stock.domain.sessioncommercial.enums.StatutSession;
import com.NTFOODS.Api_tanty.modules.stock.domain.sessioncommercial.model.SessionCommercial;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Cycle de fin de tournée : le commercial déclare, la secrétaire rapproche, le
 * comptable clôt.
 *
 * <p>La séparation des rôles est l'invariant protégé ici : le commercial signale
 * que sa journée est finie mais ne valide jamais sa propre recette, et aucune
 * étape ne peut être franchie avant la précédente.</p>
 */
class CycleFinTourneeTest {

    private static final String COMMERCIAL = "MAT-COM-001";
    private static final String SECRETAIRE = "MAT-SEC-001";
    private static final String COMPTABLE = "MAT-CPT-001";

    private SessionCommercial sessionOuverte() {
        SessionCommercial s = SessionCommercial.preparer("SESS-20260815-001", COMMERCIAL, LocalDate.now());
        s.lierDotation("DOT-001");
        s.ouvrir();
        return s;
    }

    @Test
    @DisplayName("Le cycle complet mène la tournée jusqu'à son terme")
    void cycle_complet() {
        SessionCommercial s = sessionOuverte();
        assertEquals(StatutSession.OUVERTE, s.getStatut());

        s.cloturer(COMMERCIAL);
        assertEquals(StatutSession.CLOTUREE, s.getStatut());

        s.validerParSecretaire(SECRETAIRE);
        assertEquals(StatutSession.VALIDE_SECRETAIRE, s.getStatut());

        s.validerParComptable(COMPTABLE);
        assertEquals(StatutSession.TERMINEE, s.getStatut());
    }

    @Test
    @DisplayName("La déclaration conserve l'auteur et l'heure de fin")
    void declaration_est_tracable() {
        SessionCommercial s = sessionOuverte();

        s.cloturer(COMMERCIAL);

        assertEquals(COMMERCIAL, s.getMatriculeCloture());
        assertNotNull(s.getHeureCloture());
    }

    @Test
    @DisplayName("Chaque validation conserve son auteur")
    void validations_tracables() {
        SessionCommercial s = sessionOuverte();
        s.cloturer(COMMERCIAL);

        s.validerParSecretaire(SECRETAIRE);
        s.validerParComptable(COMPTABLE);

        assertEquals(SECRETAIRE, s.getMatriculeValidateurSecretaire());
        assertEquals(COMPTABLE, s.getMatriculeValidateurComptable());
    }

    @Test
    @DisplayName("La secrétaire ne peut pas valider une tournée non déclarée terminée")
    void secretaire_ne_precede_pas_le_commercial() {
        SessionCommercial s = sessionOuverte();

        IllegalStateException erreur =
            assertThrows(IllegalStateException.class, () -> s.validerParSecretaire(SECRETAIRE));

        assertTrue(erreur.getMessage().contains("clôturée"));
    }

    @Test
    @DisplayName("Le comptable ne peut pas valider avant la secrétaire")
    void comptable_ne_precede_pas_la_secretaire() {
        SessionCommercial s = sessionOuverte();
        s.cloturer(COMMERCIAL);

        assertThrows(IllegalStateException.class, () -> s.validerParComptable(COMPTABLE));
    }

    @Test
    @DisplayName("Une tournée déjà déclarée terminée ne peut pas l'être une seconde fois")
    void double_declaration_refusee() {
        SessionCommercial s = sessionOuverte();
        s.cloturer(COMMERCIAL);

        assertThrows(IllegalStateException.class, () -> s.cloturer(COMMERCIAL));
    }

    @Test
    @DisplayName("Une tournée non ouverte ne peut pas être déclarée terminée")
    void declaration_impossible_avant_ouverture() {
        SessionCommercial s = SessionCommercial.preparer("SESS-20260815-002", COMMERCIAL, LocalDate.now());

        assertThrows(IllegalStateException.class, () -> s.cloturer(COMMERCIAL));
    }

    @Test
    @DisplayName("Une tournée terminée est définitivement close")
    void tournee_terminee_est_close() {
        SessionCommercial s = sessionOuverte();
        s.cloturer(COMMERCIAL);
        s.validerParSecretaire(SECRETAIRE);
        s.validerParComptable(COMPTABLE);

        assertThrows(IllegalStateException.class, () -> s.validerParSecretaire(SECRETAIRE));
        assertThrows(IllegalStateException.class, () -> s.validerParComptable(COMPTABLE));
        assertFalse(s.estEnCours());
    }

    @Test
    @DisplayName("Une session sans dotation ne peut pas être ouverte")
    void ouverture_exige_une_dotation() {
        SessionCommercial s = SessionCommercial.preparer("SESS-20260815-003", COMMERCIAL, LocalDate.now());

        assertThrows(IllegalStateException.class, s::ouvrir);
    }

    @Test
    @DisplayName("Aucune dotation ne peut être liée après la fin de tournée")
    void dotation_impossible_apres_declaration() {
        SessionCommercial s = sessionOuverte();
        s.cloturer(COMMERCIAL);

        assertThrows(IllegalStateException.class, () -> s.lierDotation("DOT-002"));
    }
}
