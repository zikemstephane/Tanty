package com.NTFOODS.Api_tanty.modules.stock.domain;

import com.NTFOODS.Api_tanty.modules.stock.domain.retour.enums.StatutBonRetour;
import com.NTFOODS.Api_tanty.modules.stock.domain.retour.model.BonRetour;
import com.NTFOODS.Api_tanty.modules.stock.domain.retour.model.LigneRetour;
import com.NTFOODS.Api_tanty.shared.kernel.exception.RegleMetierException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Constat d'invendus : le commercial déclare, le gestionnaire compte.
 *
 * <p>L'invariant protégé est que la déclaration survit au comptage. Écraser la
 * quantité annoncée par la quantité constatée effacerait précisément
 * l'information que ce constat existe pour produire.</p>
 */
class ConstatInvendusTest {

    private static final String COMMERCIAL = "MAT-COM-001";
    private static final String GESTIONNAIRE = "MAT-GS-001";

    private BonRetour constatDeclare() {
        BonRetour constat = BonRetour.declarer("BRET-20260815-000001", COMMERCIAL, LocalDate.now());
        constat.ajouterLigne("TBSA", "TBSA-UNITE_BASE", 10, "Invendus tournée");
        constat.ajouterLigne("MGAE", "MGAE-UNITE_BASE", 4, null);
        return constat;
    }

    @Test
    @DisplayName("Une déclaration naît sans gestionnaire ni comptage")
    void declaration_initiale() {
        BonRetour constat = constatDeclare();

        assertEquals(StatutBonRetour.DECLARE, constat.getStatut());
        assertNull(constat.getMatriculeGestionnaire());
        assertNull(constat.getDateComptage());
        assertNull(constat.ecartTotal());
        assertFalse(constat.getLignes().get(0).estComptee());
    }

    @Test
    @DisplayName("Le comptage conserve la quantité déclarée et enregistre l'écart")
    void comptage_conserve_la_declaration() {
        BonRetour constat = constatDeclare();

        constat.compter(Map.of("TBSA", 8L, "MGAE", 4L), GESTIONNAIRE);

        LigneRetour tbsa = constat.getLignes().stream()
            .filter(l -> l.getCodeProduit().equals("TBSA")).findFirst().orElseThrow();
        assertEquals(10, tbsa.getQuantiteDeclaree());
        assertEquals(8L, tbsa.getQuantiteComptee());
        assertEquals(-2L, tbsa.ecart());
    }

    @Test
    @DisplayName("Le comptage trace son auteur et son horodatage")
    void comptage_est_tracable() {
        BonRetour constat = constatDeclare();

        constat.compter(Map.of("TBSA", 10L, "MGAE", 4L), GESTIONNAIRE);

        assertEquals(StatutBonRetour.COMPTE, constat.getStatut());
        assertEquals(GESTIONNAIRE, constat.getMatriculeGestionnaire());
        assertNotNull(constat.getDateComptage());
    }

    @Test
    @DisplayName("L'écart total additionne les écarts de chaque ligne")
    void ecart_total() {
        BonRetour constat = constatDeclare();

        constat.compter(Map.of("TBSA", 8L, "MGAE", 5L), GESTIONNAIRE);

        assertEquals(-1L, constat.ecartTotal());
    }

    @Test
    @DisplayName("Un constat conforme affiche un écart nul, pas une absence d'écart")
    void ecart_nul_est_une_information() {
        BonRetour constat = constatDeclare();

        constat.compter(Map.of("TBSA", 10L, "MGAE", 4L), GESTIONNAIRE);

        assertEquals(0L, constat.ecartTotal());
    }

    @Test
    @DisplayName("Compter zéro est admis : le produit annoncé n'a pas été rapporté")
    void quantite_comptee_nulle_admise() {
        BonRetour constat = constatDeclare();

        constat.compter(Map.of("TBSA", 0L, "MGAE", 4L), GESTIONNAIRE);

        LigneRetour tbsa = constat.getLignes().stream()
            .filter(l -> l.getCodeProduit().equals("TBSA")).findFirst().orElseThrow();
        assertTrue(tbsa.estComptee());
        assertEquals(0L, tbsa.getQuantiteComptee());
        assertEquals(-10L, tbsa.ecart());
    }

    @Test
    @DisplayName("Une ligne déclarée mais non comptée bloque le constat")
    void ligne_oubliee_refusee() {
        BonRetour constat = constatDeclare();

        RegleMetierException erreur = assertThrows(RegleMetierException.class,
            () -> constat.compter(Map.of("TBSA", 8L), GESTIONNAIRE));

        assertTrue(erreur.getMessage().contains("MGAE"));
        assertEquals(StatutBonRetour.DECLARE, constat.getStatut());
    }

    @Test
    @DisplayName("Un produit compté mais jamais déclaré est refusé")
    void produit_non_declare_refuse() {
        BonRetour constat = constatDeclare();

        RegleMetierException erreur = assertThrows(RegleMetierException.class,
            () -> constat.compter(Map.of("TBSA", 8L, "MGAE", 4L, "TC 1L", 2L), GESTIONNAIRE));

        assertTrue(erreur.getMessage().contains("TC 1L"));
    }

    @Test
    @DisplayName("Un constat déjà compté ne peut pas être recompté")
    void double_comptage_refuse() {
        BonRetour constat = constatDeclare();
        constat.compter(Map.of("TBSA", 8L, "MGAE", 4L), GESTIONNAIRE);

        assertThrows(RegleMetierException.class,
            () -> constat.compter(Map.of("TBSA", 10L, "MGAE", 4L), GESTIONNAIRE));
    }

    @Test
    @DisplayName("Aucune ligne ne peut être ajoutée après le comptage")
    void ajout_apres_comptage_refuse() {
        BonRetour constat = constatDeclare();
        constat.compter(Map.of("TBSA", 8L, "MGAE", 4L), GESTIONNAIRE);

        assertThrows(RegleMetierException.class,
            () -> constat.ajouterLigne("TC 1L", null, 3, null));
    }

    @Test
    @DisplayName("Un constat sans ligne est refusé")
    void constat_vide_refuse() {
        BonRetour constat = BonRetour.declarer("BRET-20260815-000002", COMMERCIAL, LocalDate.now());

        assertThrows(RegleMetierException.class, constat::exigerNonVide);
    }

    @Test
    @DisplayName("Une quantité déclarée nulle ou négative est refusée")
    void quantite_declaree_invalide() {
        BonRetour constat = BonRetour.declarer("BRET-20260815-000003", COMMERCIAL, LocalDate.now());

        assertThrows(IllegalArgumentException.class, () -> constat.ajouterLigne("TBSA", null, 0, null));
        assertThrows(IllegalArgumentException.class, () -> constat.ajouterLigne("TBSA", null, -3, null));
    }

    @Test
    @DisplayName("Une quantité comptée négative est refusée")
    void quantite_comptee_negative_refusee() {
        BonRetour constat = constatDeclare();

        assertThrows(IllegalArgumentException.class,
            () -> constat.compter(Map.of("TBSA", -1L, "MGAE", 4L), GESTIONNAIRE));
    }
}
