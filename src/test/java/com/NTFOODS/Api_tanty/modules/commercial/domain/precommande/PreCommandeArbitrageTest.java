package com.NTFOODS.Api_tanty.modules.commercial.domain.precommande;

import com.NTFOODS.Api_tanty.modules.commercial.domain.precommande.aggregate.PreCommande;
import com.NTFOODS.Api_tanty.modules.commercial.domain.precommande.entity.LignePreCommande;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Arbitrage des quantités d'une précommande par le gestionnaire de stock.
 *
 * <p>L'invariant central : la quantité demandée par le commercial n'est jamais
 * écrasée, afin que l'écart entre demande et service reste lisible après coup.</p>
 */
class PreCommandeArbitrageTest {

    private static final String ARBITRE = "STK-001";

    private PreCommande precommandeDeDeuxLignes() {
        return PreCommande.soumettre("PCM-20260815-000001", "COM-001", LocalDate.now(),
            List.of(
                new LignePreCommande("TBSA", "Bouillie Soja Arachide", "CARTON", 100),
                new LignePreCommande("MGAE", "À Grignoter", "CARTON", 40)));
    }

    @Test
    @DisplayName("La quantité demandée reste intacte après arbitrage à la baisse")
    void arbitrage_conserve_la_quantite_demandee() {
        PreCommande pc = precommandeDeDeuxLignes();

        pc.arbitrerQuantites(Map.of("TBSA", 70), ARBITRE, "Stock insuffisant");

        LignePreCommande ligne = ligneDe(pc, "TBSA");
        assertEquals(100, ligne.getQuantiteDemandee(), "la demande initiale doit être conservée");
        assertEquals(70, ligne.getQuantiteValidee(), "la quantité servie doit refléter l'arbitrage");
    }

    @Test
    @DisplayName("Une ligne non arbitrée conserve sa quantité validée")
    void arbitrage_partiel_laisse_les_autres_lignes_inchangees() {
        PreCommande pc = precommandeDeDeuxLignes();

        pc.arbitrerQuantites(Map.of("TBSA", 70), ARBITRE, null);

        assertEquals(40, ligneDe(pc, "MGAE").getQuantiteValidee());
    }

    @Test
    @DisplayName("L'auteur et l'horodatage de l'arbitrage sont conservés")
    void arbitrage_est_tracable() {
        PreCommande pc = precommandeDeDeuxLignes();

        pc.arbitrerQuantites(Map.of("TBSA", 70), ARBITRE, "Rupture partielle");

        assertTrue(pc.estArbitree());
        assertEquals(ARBITRE, pc.getMatriculeArbitre());
        assertNotNull(pc.getDateArbitrage());
        assertEquals("Rupture partielle", pc.getMotifArbitrage());
    }

    @Test
    @DisplayName("Un motif vide n'est pas conservé comme motif")
    void motif_vide_est_normalise_a_null() {
        PreCommande pc = precommandeDeDeuxLignes();

        pc.arbitrerQuantites(Map.of("TBSA", 70), ARBITRE, "   ");

        assertNull(pc.getMotifArbitrage());
    }

    @Test
    @DisplayName("Servir zéro est un arbitrage valide")
    void arbitrage_a_zero_est_accepte() {
        PreCommande pc = precommandeDeDeuxLignes();

        pc.arbitrerQuantites(Map.of("TBSA", 0), ARBITRE, "Rupture totale");

        assertEquals(0, ligneDe(pc, "TBSA").getQuantiteValidee());
        assertEquals(100, ligneDe(pc, "TBSA").getQuantiteDemandee());
    }

    @Test
    @DisplayName("Un produit absent de la précommande est refusé")
    void produit_inconnu_est_refuse() {
        PreCommande pc = precommandeDeDeuxLignes();

        IllegalArgumentException erreur = assertThrows(IllegalArgumentException.class,
            () -> pc.arbitrerQuantites(Map.of("INCONNU", 5), ARBITRE, null));

        assertTrue(erreur.getMessage().contains("INCONNU"));
    }

    @Test
    @DisplayName("Un arbitrage sans ligne est refusé")
    void arbitrage_vide_est_refuse() {
        PreCommande pc = precommandeDeDeuxLignes();

        assertThrows(IllegalArgumentException.class,
            () -> pc.arbitrerQuantites(Map.of(), ARBITRE, null));
    }

    @Test
    @DisplayName("L'auteur de l'arbitrage est obligatoire")
    void auteur_obligatoire() {
        PreCommande pc = precommandeDeDeuxLignes();

        assertThrows(NullPointerException.class,
            () -> pc.arbitrerQuantites(Map.of("TBSA", 70), null, null));
    }

    @Test
    @DisplayName("Une précommande livrée ne peut plus être arbitrée")
    void arbitrage_impossible_apres_livraison() {
        PreCommande pc = precommandeDeDeuxLignes();
        pc.validerParSecretaire("SEC-001");
        pc.validerParComptable("CPT-001", Map.of());
        pc.marquerLivree();

        assertThrows(IllegalStateException.class,
            () -> pc.arbitrerQuantites(Map.of("TBSA", 70), ARBITRE, null));
    }

    @Test
    @DisplayName("Une précommande refusée ne peut pas être arbitrée")
    void arbitrage_impossible_apres_refus() {
        PreCommande pc = precommandeDeDeuxLignes();
        pc.refuser("Hors délai", "SEC-001");

        assertThrows(IllegalStateException.class,
            () -> pc.arbitrerQuantites(Map.of("TBSA", 70), ARBITRE, null));
    }

    @Test
    @DisplayName("L'arbitrage reste possible avant la livraison, à chaque étape de validation")
    void arbitrage_possible_pendant_le_circuit_de_validation() {
        PreCommande pc = precommandeDeDeuxLignes();

        pc.arbitrerQuantites(Map.of("TBSA", 90), ARBITRE, null);
        pc.validerParSecretaire("SEC-001");
        pc.arbitrerQuantites(Map.of("TBSA", 80), ARBITRE, null);
        pc.validerParComptable("CPT-001", Map.of());
        pc.arbitrerQuantites(Map.of("TBSA", 70), ARBITRE, null);

        assertEquals(70, ligneDe(pc, "TBSA").getQuantiteValidee());
        assertEquals(100, ligneDe(pc, "TBSA").getQuantiteDemandee());
    }

    private LignePreCommande ligneDe(PreCommande pc, String codeProduit) {
        return pc.getLignes().stream()
            .filter(l -> l.getCodeProduit().equals(codeProduit))
            .findFirst()
            .orElseThrow();
    }
}
