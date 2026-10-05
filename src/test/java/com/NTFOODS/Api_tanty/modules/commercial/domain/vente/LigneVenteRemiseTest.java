package com.NTFOODS.Api_tanty.modules.commercial.domain.vente;

import com.NTFOODS.Api_tanty.modules.commercial.domain.vente.entity.LigneVente;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Remise d'une ligne de vente, exprimée en <strong>pourcentage</strong> de 0 à
 * 100 sur la ligne entière (décision du 18/08/2026, migration {@code V13}).
 *
 * <p>Le champ portait auparavant un montant en FCFA, alors que
 * {@code LigneFacture} exprimait déjà le sien en pourcentage : deux lignes
 * décrivant la même marchandise employaient un champ de même nom et de sens
 * opposé. C'est cette contradiction que le changement referme.</p>
 *
 * <p>L'unité importe, dans un sens comme dans l'autre : sur cette ligne à
 * 25 000 FCFA, « 10 » vaut 2 500 FCFA de remise en pourcentage et en valait 10
 * en francs.</p>
 */
class LigneVenteRemiseTest {

    private LigneVente ligne(double remise) {
        return new LigneVente("TBSA", "Bouillie Soja Arachide", "CARTON", 10, 2500, remise);
    }

    @Test
    @DisplayName("La remise s'applique en pourcentage du montant brut")
    void remise_appliquee_en_pourcentage() {
        LigneVente l = ligne(10);

        assertEquals(25_000, l.montantBrut());
        assertEquals(22_500, l.montantHT(), "10 × 2500 × (1 − 10 %)");
    }

    @Test
    @DisplayName("Sans remise, le net égale le brut")
    void sans_remise() {
        assertEquals(25_000, ligne(0).montantHT());
    }

    @Test
    @DisplayName("Une remise de 100 % ramène la ligne à zéro")
    void remise_totale_acceptee() {
        assertEquals(0, ligne(100).montantHT());
    }

    @Test
    @DisplayName("Au-delà de 100 %, la remise est refusée plutôt que ramenée à zéro")
    void remise_excessive_refusee() {
        IllegalArgumentException erreur =
            assertThrows(IllegalArgumentException.class, () -> ligne(101));

        assertTrue(erreur.getMessage().contains("ne peut pas excéder 100"),
            "le refus doit être explicite, jamais une correction silencieuse");
    }

    @Test
    @DisplayName("Une remise négative est refusée")
    void remise_negative_refusee() {
        assertThrows(IllegalArgumentException.class, () -> ligne(-1));
    }

    @Test
    @DisplayName("Une valeur autrefois lue en FCFA vaut désormais un pourcentage")
    void ancienne_interpretation_abandonnee() {
        // « 500 » désignait 500 FCFA de remise, soit un net de 24 500. Lu en
        // pourcentage il dépasserait 100 et doit être refusé, non requalifié :
        // une donnée non migrée doit se faire remarquer, pas s'appliquer de
        // travers. La migration V13 convertit les lignes existantes.
        assertThrows(IllegalArgumentException.class, () -> ligne(500));
    }
}
