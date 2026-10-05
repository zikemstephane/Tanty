package com.NTFOODS.Api_tanty.modules.stock.domain.catalogue;

import com.NTFOODS.Api_tanty.modules.stock.domain.catalogue.enums.NiveauConditionnement;
import com.NTFOODS.Api_tanty.modules.stock.domain.catalogue.enums.TypeEmballage;
import com.NTFOODS.Api_tanty.modules.stock.domain.catalogue.model.BesoinEmballage;
import com.NTFOODS.Api_tanty.modules.stock.domain.catalogue.model.ComposantEmballage;
import com.NTFOODS.Api_tanty.modules.stock.domain.catalogue.model.Conditionnement;
import com.NTFOODS.Api_tanty.shared.kernel.exception.RegleMetierException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.List;

import static com.NTFOODS.Api_tanty.modules.stock.domain.catalogue.enums.TypeEmballage.*;
import static org.junit.jupiter.api.Assertions.*;

/**
 * Composition d'emballage d'un conditionnement.
 *
 * <p>L'invariant protégé : les composants d'emballage nécessaires à une
 * commande sont calculés depuis la composition propre à CE conditionnement
 * — jamais en supposant Sachet/Couvercle/Scellé pour tout produit — et
 * toujours arrondis au supérieur, un composant partagé entre plusieurs
 * unités ne pouvant être sous-évalué.</p>
 */
class ConditionnementEmballageTest {

    private Conditionnement conditionnement(String codeProduit, NiveauConditionnement niveau, long facteurConversionVersBase) {
        return Conditionnement.creer(codeProduit, codeProduit + "-UNITE", niveau, "Conditionnement test",
            facteurConversionVersBase, 100, 0, BigDecimal.TEN, null);
    }

    @Test
    @DisplayName("Produit en sachets : composition propre au sachet")
    void produit_en_sachets() {
        Conditionnement c = conditionnement("PROD-SACHET", NiveauConditionnement.UNITE_BASE, 1);
        c.definirTypeEtComposants(SACHET, List.of(new ComposantEmballage(SACHET, 1, 1)));

        List<BesoinEmballage> besoins = c.calculerComposantsEmballage(10);

        assertEquals(1, besoins.size());
        assertEquals(SACHET, besoins.get(0).type());
        assertEquals(10L, besoins.get(0).quantite());
    }

    @Test
    @DisplayName("Produit en seau : composition propre au seau, pas de sachet/couvercle/scellé supposés")
    void produit_en_seau() {
        Conditionnement c = conditionnement("PROD-SEAU", NiveauConditionnement.UNITE_BASE, 1);
        c.definirTypeEtComposants(SEAU, List.of(new ComposantEmballage(SEAU, 1, 1)));

        List<BesoinEmballage> besoins = c.calculerComposantsEmballage(7);

        assertEquals(1, besoins.size());
        assertEquals(SEAU, besoins.get(0).type());
        assertEquals(7L, besoins.get(0).quantite());
    }

    @Test
    @DisplayName("Produit en gaine : composition propre à la gaine")
    void produit_en_gaine() {
        Conditionnement c = conditionnement("PROD-GAINE", NiveauConditionnement.UNITE_BASE, 1);
        c.definirTypeEtComposants(GAINE, List.of(new ComposantEmballage(GAINE, 1, 1)));

        List<BesoinEmballage> besoins = c.calculerComposantsEmballage(3);

        assertEquals(1, besoins.size());
        assertEquals(GAINE, besoins.get(0).type());
        assertEquals(3L, besoins.get(0).quantite());
    }

    @Test
    @DisplayName("Produit en étui : composition propre à l'étui, avec un carton partagé")
    void produit_en_etui() {
        Conditionnement c = conditionnement("PROD-ETUI", NiveauConditionnement.UNITE_BASE, 1);
        c.definirTypeEtComposants(ETUI, List.of(
            new ComposantEmballage(ETUI, 1, 1),
            new ComposantEmballage(CARTON, 1, 12)
        ));

        // 25 étuis, 1 carton pour 12 : 3 cartons nécessaires (24 n'en couvrent que 24, il en faut un 3e pour le 25e).
        List<BesoinEmballage> besoins = c.calculerComposantsEmballage(25);

        assertEquals(25L, besoinDe(besoins, ETUI));
        assertEquals(3L, besoinDe(besoins, CARTON));
    }

    @Test
    @DisplayName("1 couvercle pour 24 sachets : arrondi au supérieur quand la commande ne tombe pas rond")
    void un_couvercle_pour_24_sachets_avec_arrondi() {
        // Carton de 10 sachets ; 5 cartons commandés = 50 sachets, soit 50/24 = 2,08 -> 3 couvercles.
        Conditionnement c = conditionnement("PROD-CARTON-10", NiveauConditionnement.CARTON, 10);
        c.definirTypeEtComposants(SACHET, List.of(
            new ComposantEmballage(SACHET, 1, 1),
            new ComposantEmballage(COUVERCLE, 1, 24)
        ));

        List<BesoinEmballage> besoins = c.calculerComposantsEmballage(5);

        assertEquals(50L, besoinDe(besoins, SACHET), "50 sachets pour 5 cartons de 10");
        assertEquals(3L, besoinDe(besoins, COUVERCLE), "ceil(50/24) = 3, jamais 2 : un couvercle partagé ne peut être sous-évalué");
    }

    @Test
    @DisplayName("Une commande exactement multiple du ratio ne produit pas de surplus inutile")
    void ratio_exact_sans_surplus() {
        Conditionnement c = conditionnement("PROD-CARTON-24", NiveauConditionnement.CARTON, 24);
        c.definirTypeEtComposants(SACHET, List.of(
            new ComposantEmballage(SACHET, 1, 1),
            new ComposantEmballage(COUVERCLE, 1, 24)
        ));

        List<BesoinEmballage> besoins = c.calculerComposantsEmballage(5); // 5 cartons * 24 = 120 sachets, 120/24 = 5 pile

        assertEquals(120L, besoinDe(besoins, SACHET));
        assertEquals(5L, besoinDe(besoins, COUVERCLE));
    }

    @Test
    @DisplayName("Type d'emballage manquant : erreur explicite, jamais une liste vide en silence")
    void type_manquant_leve_exception() {
        Conditionnement c = conditionnement("PROD-SANS-TYPE", NiveauConditionnement.UNITE_BASE, 1);
        // ni definirTypeEtComposants appelé : type et composants restent absents.

        RegleMetierException erreur = assertThrows(RegleMetierException.class,
            () -> c.calculerComposantsEmballage(10));
        assertTrue(erreur.getMessage().contains("PROD-SANS-TYPE-UNITE"), "le message doit nommer le conditionnement en cause");
    }

    @Test
    @DisplayName("Composition manquante malgré un type défini : erreur explicite")
    void composition_manquante_leve_exception() {
        Conditionnement c = conditionnement("PROD-SANS-COMPO", NiveauConditionnement.UNITE_BASE, 1);
        c.definirTypeEtComposants(SACHET, List.of()); // type renseigné, composition vide

        RegleMetierException erreur = assertThrows(RegleMetierException.class,
            () -> c.calculerComposantsEmballage(10));
        assertTrue(erreur.getMessage().contains("Composition"));
    }

    @Test
    @DisplayName("Une quantité commandée négative est refusée")
    void quantite_negative_refusee() {
        Conditionnement c = conditionnement("PROD-NEG", NiveauConditionnement.UNITE_BASE, 1);
        c.definirTypeEtComposants(SACHET, List.of(new ComposantEmballage(SACHET, 1, 1)));

        assertThrows(IllegalArgumentException.class, () -> c.calculerComposantsEmballage(-1));
    }

    @Test
    @DisplayName("Un composant avec une quantité ou un dénominateur non positif est refusé à la construction")
    void composant_invalide_refuse() {
        assertThrows(IllegalArgumentException.class, () -> new ComposantEmballage(SACHET, 0, 1));
        assertThrows(IllegalArgumentException.class, () -> new ComposantEmballage(SACHET, 1, 0));
    }

    private long besoinDe(List<BesoinEmballage> besoins, TypeEmballage type) {
        return besoins.stream().filter(b -> b.type() == type).findFirst()
            .orElseThrow(() -> new AssertionError("Aucun besoin pour " + type)).quantite();
    }
}
