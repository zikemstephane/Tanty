package com.NTFOODS.Api_tanty.modules.production.domain.formule;

import com.NTFOODS.Api_tanty.modules.production.domain.formule.aggregate.FormuleProduction;
import com.NTFOODS.Api_tanty.modules.production.domain.formule.entity.BesoinMatierePremiere;
import com.NTFOODS.Api_tanty.modules.production.domain.formule.entity.LigneFormule;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Formule de production : calcul des besoins en matières premières.
 *
 * <p>L'invariant protégé : la somme des besoins calculés doit toujours être
 * exactement égale au poids total fourni, arrondis compris (méthode du plus
 * grand reste). C'est ce qui rend le calcul substituable à RecetteBouillie
 * sans perte de précision sur le total, tout en acceptant un nombre
 * quelconque de matières premières plutôt que les trois figées de l'enum.</p>
 */
class FormuleProductionTest {

    @Test
    @DisplayName("Simple Porridge (40/9/1 pour une base de 50 kg) appliqué à 500 kg de bouillie")
    void bouillie_500kg_simple_porridge() {
        FormuleProduction formule = FormuleProduction.creer("TBS-SAC-002", new BigDecimal("50"), List.of(
            new LigneFormule("Maïs", new BigDecimal("40")),
            new LigneFormule("Soja", new BigDecimal("9")),
            new LigneFormule("Arachide", new BigDecimal("1"))
        ));

        List<BesoinMatierePremiere> besoins = formule.calculerBesoins(new BigDecimal("500"));

        assertEquals(3, besoins.size());
        assertEquals(0, besoinDe(besoins, "Maïs").compareTo(new BigDecimal("400.00")));
        assertEquals(0, besoinDe(besoins, "Soja").compareTo(new BigDecimal("90.00")));
        assertEquals(0, besoinDe(besoins, "Arachide").compareTo(new BigDecimal("10.00")));
    }

    @Test
    @DisplayName("Une formule à 4 matières premières dont la répartition ne tombe pas rond garde une somme exacte")
    void formule_a_quatre_matieres_somme_exacte_avec_arrondi() {
        // Base 7 kg, poids total 10 kg : le facteur d'échelle (10/7) ne tombe pas rond,
        // donc chaque besoin individuel nécessite un arrondi — c'est précisément le cas
        // que la méthode du plus grand reste doit couvrir.
        FormuleProduction formule = FormuleProduction.creer("TCH-TEST", new BigDecimal("7"), List.of(
            new LigneFormule("Cacao", new BigDecimal("3")),
            new LigneFormule("Sucre", new BigDecimal("2")),
            new LigneFormule("Lait en poudre", new BigDecimal("1")),
            new LigneFormule("Lécithine", new BigDecimal("1"))
        ));

        List<BesoinMatierePremiere> besoins = formule.calculerBesoins(new BigDecimal("10"));

        BigDecimal somme = besoins.stream().map(BesoinMatierePremiere::quantiteKg)
            .reduce(BigDecimal.ZERO, BigDecimal::add);
        assertEquals(0, somme.compareTo(new BigDecimal("10.00")),
            "la somme des besoins doit être exactement égale au poids total, malgré l'arrondi de chaque ligne");
        assertEquals(4, besoins.size());
    }

    @Test
    @DisplayName("Un poids total nul renvoie des besoins tous nuls, sans division par zéro")
    void poids_total_nul() {
        FormuleProduction formule = FormuleProduction.creer("TBS-SAC-003", new BigDecimal("50"), List.of(
            new LigneFormule("Maïs", new BigDecimal("40")),
            new LigneFormule("Soja", new BigDecimal("9")),
            new LigneFormule("Arachide", new BigDecimal("1"))
        ));

        List<BesoinMatierePremiere> besoins = formule.calculerBesoins(BigDecimal.ZERO);

        besoins.forEach(b -> assertEquals(0, b.quantiteKg().compareTo(BigDecimal.ZERO)));
    }

    @Test
    @DisplayName("La base de la formule doit être strictement positive")
    void base_non_positive_refusee() {
        assertThrows(IllegalArgumentException.class, () -> FormuleProduction.creer(
            "X", BigDecimal.ZERO, List.of(new LigneFormule("Maïs", BigDecimal.TEN))));
    }

    @Test
    @DisplayName("Une formule sans ligne est refusée")
    void formule_sans_ligne_refusee() {
        assertThrows(IllegalArgumentException.class, () -> FormuleProduction.creer(
            "X", new BigDecimal("50"), List.of()));
    }

    @Test
    @DisplayName("La somme des proportions doit égaler la base — sinon la garantie de somme exacte serait fausse")
    void somme_proportions_differente_de_la_base_refusee() {
        IllegalArgumentException erreur = assertThrows(IllegalArgumentException.class, () -> FormuleProduction.creer(
            "X", new BigDecimal("50"), List.of(
                new LigneFormule("Maïs", new BigDecimal("40")),
                new LigneFormule("Soja", new BigDecimal("9"))
                // total 49, pas 50
            )));
        assertTrue(erreur.getMessage().contains("49"));
        assertTrue(erreur.getMessage().contains("50"));
    }

    @Test
    @DisplayName("Une même matière première deux fois dans la formule est refusée")
    void matiere_premiere_dupliquee_refusee() {
        assertThrows(IllegalArgumentException.class, () -> FormuleProduction.creer(
            "X", new BigDecimal("50"), List.of(
                new LigneFormule("Maïs", new BigDecimal("25")),
                new LigneFormule("Maïs", new BigDecimal("25"))
            )));
    }

    @Test
    @DisplayName("Une proportion négative est refusée dès la construction de la ligne")
    void proportion_negative_refusee() {
        assertThrows(IllegalArgumentException.class, () -> new LigneFormule("Maïs", new BigDecimal("-1")));
    }

    private BigDecimal besoinDe(List<BesoinMatierePremiere> besoins, String matierePremiere) {
        return besoins.stream().filter(b -> b.matierePremiere().equals(matierePremiere))
            .findFirst().orElseThrow().quantiteKg();
    }
}
