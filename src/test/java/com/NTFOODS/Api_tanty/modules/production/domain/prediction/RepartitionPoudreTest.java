package com.NTFOODS.Api_tanty.modules.production.domain.prediction;

import com.NTFOODS.Api_tanty.modules.production.domain.prediction.service.RepartitionPoudre;
import com.NTFOODS.Api_tanty.modules.production.domain.prediction.service.RepartitionPoudre.Produit;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

/** La poudre dosée se partage entre les produits du BC : elle n'est jamais comptée deux fois. */
class RepartitionPoudreTest {

    @Test
    void laSommeDesPartsVautLaPoudreTotale() {
        // 500 g par unité, 200 unités demandées (100 kg) ; 250 g par unité, 200 unités (50 kg)
        List<Produit> produits = List.of(
            new Produit("A", "Produit A", null, 500, 12, 200),
            new Produit("B", "Produit B", null, 250, 10, 200));

        var resultat = RepartitionPoudre.repartir(150.0, produits);

        double total = resultat.lignes().stream().mapToDouble(RepartitionPoudre.Ligne::poudreAlloueeKg).sum();
        assertThat(total).isCloseTo(150.0, org.assertj.core.data.Offset.offset(0.02));
        // Au prorata du poids demandé : 100 kg / 50 kg => 2/3 et 1/3 de la poudre
        assertThat(resultat.lignes().get(0).poudreAlloueeKg()).isCloseTo(100.0, org.assertj.core.data.Offset.offset(0.02));
        assertThat(resultat.lignes().get(1).poudreAlloueeKg()).isCloseTo(50.0, org.assertj.core.data.Offset.offset(0.02));
    }

    @Test
    void lesUnitesEtLesCartonsSontCalculesDepuisLaPartDePoudre() {
        List<Produit> produits = List.of(new Produit("A", "Produit A", null, 500, 12, 100));

        var ligne = RepartitionPoudre.repartir(50.0, produits).lignes().get(0);

        // 50 kg de poudre pour un produit de 500 g => 100 unités ; 12 unités par carton => 8 cartons
        assertThat(ligne.quantiteUnitesBase()).isEqualTo(100);
        assertThat(ligne.quantiteCartons()).isEqualTo(8);
    }

    @Test
    void unProduitSansPoidsEstExcluSansFausserLesAutres() {
        List<Produit> produits = List.of(
            new Produit("A", "Produit A", null, 500, 12, 100),
            new Produit("SANS-POIDS", "Produit sans poids", null, 0, 10, 100));

        var resultat = RepartitionPoudre.repartir(50.0, produits);

        assertThat(resultat.lignes()).hasSize(1);
        assertThat(resultat.lignes().get(0).poudreAlloueeKg()).isCloseTo(50.0, org.assertj.core.data.Offset.offset(0.02));
        assertThat(resultat.exclusions()).extracting(RepartitionPoudre.Exclusion::codeProduit).containsExactly("SANS-POIDS");
    }

    @Test
    void sansPoudreOuSansProduitAucuneLigne() {
        assertThat(RepartitionPoudre.repartir(0.0, List.of(new Produit("A", "A", null, 500, 12, 100))).lignes()).isEmpty();
        assertThat(RepartitionPoudre.repartir(50.0, List.of()).lignes()).isEmpty();
    }
}
