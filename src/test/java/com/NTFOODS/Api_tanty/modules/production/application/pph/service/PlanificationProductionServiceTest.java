package com.NTFOODS.Api_tanty.modules.production.application.pph.service;

import com.NTFOODS.Api_tanty.modules.production.domain.formule.aggregate.FormuleProduction;
import com.NTFOODS.Api_tanty.modules.production.domain.formule.entity.LigneFormule;
import com.NTFOODS.Api_tanty.modules.production.domain.formule.repository.FormuleProductionRepository;
import com.NTFOODS.Api_tanty.modules.production.domain.pph.entity.LignePlanDetail;
import com.NTFOODS.Api_tanty.modules.stock.domain.catalogue.enums.CategorieMetier;
import com.NTFOODS.Api_tanty.modules.stock.domain.catalogue.enums.CategorieProduit;
import com.NTFOODS.Api_tanty.modules.stock.domain.catalogue.enums.NiveauConditionnement;
import com.NTFOODS.Api_tanty.modules.stock.domain.catalogue.enums.TypeEmballage;
import com.NTFOODS.Api_tanty.modules.stock.domain.catalogue.model.ComposantEmballage;
import com.NTFOODS.Api_tanty.modules.stock.domain.catalogue.model.Conditionnement;
import com.NTFOODS.Api_tanty.modules.stock.domain.catalogue.model.Produit;
import com.NTFOODS.Api_tanty.modules.stock.domain.catalogue.valueobject.CodeProduit;
import com.NTFOODS.Api_tanty.modules.stock.domain.commande.model.BonCommande;
import com.NTFOODS.Api_tanty.shared.kernel.exception.RegleMetierException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

/**
 * PlanificationProductionService — calcul du plan de production à partir d'un bon de commande.
 *
 * <p>Couvre le garde-fou sur le poids unitaire (Bloc 2) et le branchement sur
 * FormuleProduction / composition d'emballage (Bloc 5) : un produit sans
 * formule ou un conditionnement sans composition ne bloquent pas le calcul,
 * mais c'est signalé explicitement sur chaque ligne de détail plutôt que
 * traité en silence comme un besoin nul.</p>
 */
class PlanificationProductionServiceTest {

    /** Fake en mémoire — aucun framework de mock utilisé ailleurs dans ce module. */
    private static class FormuleProductionRepositoryDeTest implements FormuleProductionRepository {
        private final Map<String, FormuleProduction> formules = new HashMap<>();

        void enregistrer(FormuleProduction formule) {
            formules.put(formule.getCodeProduit(), formule);
        }

        @Override
        public FormuleProduction sauvegarder(FormuleProduction formule) {
            formules.put(formule.getCodeProduit(), formule);
            return formule;
        }

        @Override
        public Optional<FormuleProduction> trouverParCodeProduit(String codeProduit) {
            return Optional.ofNullable(formules.get(codeProduit));
        }

        @Override
        public List<FormuleProduction> listerToutes() {
            return List.copyOf(formules.values());
        }

        @Override
        public void supprimerParCodeProduit(String codeProduit) {
            formules.remove(codeProduit);
        }
    }

    private final FormuleProductionRepositoryDeTest formuleRepository = new FormuleProductionRepositoryDeTest();
    private final PlanificationProductionService service = new PlanificationProductionService(formuleRepository);

    private Produit produit(String code, String designation, double poidsUnitaireGrammes) {
        Produit p = Produit.creer(CodeProduit.de(code), "TANTY", "Sachets", designation,
            designation, CategorieProduit.PRODUIT_FINI, CategorieMetier.TANTY_BOUILLIE_DE_SOJA);
        p.ajouterConditionnement(code + "-UNITE", NiveauConditionnement.UNITE_BASE, "Sachets",
            1L, poidsUnitaireGrammes, 0d, null, null);
        return p;
    }

    private BonCommande bonCommandeAvecLigne(String codeProduit, String designation, long quantiteCommandee) {
        BonCommande bc = BonCommande.creer("BC-TEST-0001", null, LocalDate.now(), null, "CP001");
        bc.ajouterLigne(codeProduit, designation, null, quantiteCommandee, null);
        return bc;
    }

    private BonCommande bonCommandeAvecPeriode(String codeProduit, String designation, long quantiteCommandee,
                                               LocalDate dateDebutProduction, LocalDate dateFinProduction) {
        BonCommande bc = BonCommande.creer("BC-TEST-0001", null, LocalDate.now(), null, "CP001",
            dateDebutProduction, dateFinProduction);
        bc.ajouterLigne(codeProduit, designation, null, quantiteCommandee, null);
        return bc;
    }

    @Test
    @DisplayName("Un poids unitaire à zéro bloque le calcul, en nommant le produit")
    void poids_nul_leve_exception() {
        Produit produit = produit("TBS-SAC-001", "REINE BOUILLIE SOJA ARACHIDES", 0);
        BonCommande bc = bonCommandeAvecLigne("TBS-SAC-001", "REINE BOUILLIE SOJA ARACHIDES", 10);

        RegleMetierException erreur = assertThrows(RegleMetierException.class,
            () -> service.calculer(bc, Map.of("TBS-SAC-001", produit)));

        assertTrue(erreur.getMessage().contains("TBS-SAC-001"),
            "le message doit nommer le code produit en cause");
        assertTrue(erreur.getMessage().contains("REINE BOUILLIE SOJA ARACHIDES"),
            "le message doit nommer la désignation du produit en cause");
    }

    // Pas de cas « poids négatif » testé ici : Conditionnement.creer() refuse déjà
    // un poids négatif à la construction (IllegalArgumentException), et
    // Conditionnement.reconstituer() retombe sur une valeur ré-analysée depuis le
    // libellé plutôt que de conserver un négatif. Aucune des deux voies de
    // construction du domaine ne peut donc produire l'état que ce test viserait.
    // Le garde-fou `<= 0` de PlanificationProductionService le couvre néanmoins
    // en défense en profondeur, à coût nul, si cet invariant venait à changer.

    @Test
    @DisplayName("Un poids unitaire valide ne déclenche jamais le garde-fou")
    void poids_valide_ne_leve_pas() {
        Produit produit = produit("TBS-SAC-003", "REINE BOUILLIE SOJA LACTEE", 500);
        BonCommande bc = bonCommandeAvecLigne("TBS-SAC-003", "REINE BOUILLIE SOJA LACTEE", 10);

        PlanificationProductionService.ResultatPlanification resultat =
            assertDoesNotThrow(() -> service.calculer(bc, Map.of("TBS-SAC-003", produit)));

        assertEquals(1, resultat.lignesPPH().size());
        assertFalse(resultat.lignesDetaillees().isEmpty());
    }

    @Test
    @DisplayName("500 kg de bouillie (40/9/1) se répartissent en 400/90/10 kg de maïs/soja/arachide sur la semaine")
    void bouillie_500kg_repartit_400_90_10() {
        // 5000 unités x 100 g = 500 kg, répartis sur 5 jours ouvrés (1000 unités = 100 kg / jour) :
        // chaque jour est un multiple exact de la base 50 kg, aucun arrondi à plus grand reste nécessaire.
        Produit produit = produit("TBS-SAC-002", "TANTY BOUILLIE SOJA", 100);
        formuleRepository.enregistrer(FormuleProduction.creer("TBS-SAC-002", BigDecimal.valueOf(50), List.of(
            new LigneFormule("Maïs", BigDecimal.valueOf(40)),
            new LigneFormule("Soja", BigDecimal.valueOf(9)),
            new LigneFormule("Arachide", BigDecimal.valueOf(1))
        )));
        BonCommande bc = bonCommandeAvecLigne("TBS-SAC-002", "TANTY BOUILLIE SOJA", 5000);

        PlanificationProductionService.ResultatPlanification resultat =
            service.calculer(bc, Map.of("TBS-SAC-002", produit));

        BigDecimal totalMais = sommeMatiere(resultat.lignesDetaillees(), "Maïs");
        BigDecimal totalSoja = sommeMatiere(resultat.lignesDetaillees(), "Soja");
        BigDecimal totalArachide = sommeMatiere(resultat.lignesDetaillees(), "Arachide");

        assertEquals(0, BigDecimal.valueOf(400).compareTo(totalMais), "400 kg de maïs attendus, obtenu " + totalMais);
        assertEquals(0, BigDecimal.valueOf(90).compareTo(totalSoja), "90 kg de soja attendus, obtenu " + totalSoja);
        assertEquals(0, BigDecimal.valueOf(10).compareTo(totalArachide), "10 kg d'arachide attendus, obtenu " + totalArachide);
        assertTrue(resultat.lignesDetaillees().stream().noneMatch(LignePlanDetail::isFormuleManquante));
    }

    private BigDecimal sommeMatiere(List<LignePlanDetail> lignes, String matiere) {
        return lignes.stream().map(l -> l.getBesoinMatierePremiere(matiere))
            .reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    @Test
    @DisplayName("Composition d'emballage propre au conditionnement : 1 couvercle pour 24 sachets, pas d'hypothèse 1:1:1")
    void composition_emballage_propre_au_conditionnement() {
        Produit produit = Produit.creer(CodeProduit.de("PROD-SACHET-TEST"), "TANTY", "Sachets", "PRODUIT SACHET TEST",
            "PRODUIT SACHET TEST", CategorieProduit.PRODUIT_FINI, CategorieMetier.TANTY_A_GRIGNOTER);
        Conditionnement conditionnement = produit.ajouterConditionnement("PROD-SACHET-TEST-UNITE",
            NiveauConditionnement.UNITE_BASE, "Sachets", 1L, 50, 0d, null, null);
        conditionnement.definirTypeEtComposants(TypeEmballage.SACHET, List.of(
            new ComposantEmballage(TypeEmballage.SACHET, 1, 1),
            new ComposantEmballage(TypeEmballage.COUVERCLE, 1, 24)
        ));
        // Pas de formule pour ce produit : catégorie non-bouillie, seul l'emballage est testé ici.
        BonCommande bc = bonCommandeAvecLigne("PROD-SACHET-TEST", "PRODUIT SACHET TEST", 240);

        PlanificationProductionService.ResultatPlanification resultat =
            service.calculer(bc, Map.of("PROD-SACHET-TEST", produit));

        long totalSachets = resultat.lignesDetaillees().stream()
            .mapToLong(l -> l.getBesoinEmballage(TypeEmballage.SACHET)).sum();
        long totalCouvercles = resultat.lignesDetaillees().stream()
            .mapToLong(l -> l.getBesoinEmballage(TypeEmballage.COUVERCLE)).sum();

        assertEquals(240L, totalSachets);
        assertEquals(10L, totalCouvercles, "ceil(240/24) = 10 au total sur la semaine");
        assertTrue(resultat.lignesDetaillees().stream().noneMatch(LignePlanDetail::isCompositionEmballageManquante));
    }

    @Test
    @DisplayName("Un produit sans formule ne bloque pas le calcul : besoins vides, mais signalé explicitement")
    void produit_sans_formule_est_signale_sans_bloquer() {
        Produit produit = produit("TBS-SAC-999", "PRODUIT SANS FORMULE", 500);
        // Aucune formule enregistrée dans le repository pour ce produit.
        BonCommande bc = bonCommandeAvecLigne("TBS-SAC-999", "PRODUIT SANS FORMULE", 10);

        PlanificationProductionService.ResultatPlanification resultat =
            assertDoesNotThrow(() -> service.calculer(bc, Map.of("TBS-SAC-999", produit)));

        assertFalse(resultat.lignesDetaillees().isEmpty());
        assertTrue(resultat.lignesDetaillees().stream().allMatch(LignePlanDetail::isFormuleManquante),
            "l'absence de formule doit être signalée explicitement sur chaque ligne");
        assertTrue(resultat.lignesDetaillees().stream().allMatch(l -> l.getBesoinsMatieresPremiere().isEmpty()));
        assertTrue(resultat.lignesDetaillees().stream().allMatch(LignePlanDetail::isCompositionEmballageManquante),
            "l'absence de composition d'emballage doit aussi être signalée (conditionnement de test sans type)");
    }

    @Test
    @DisplayName("Bloc 6 — 13 cartons sur 5 jours ouvrés : la somme des jours reproduit exactement le total, pour la production, les matières premières et l'emballage")
    void repartition_sur_5_jours_sommes_exactes() {
        // 13 (non divisible par 5) force un vrai partage au plus grand reste sur les 3 dimensions.
        Produit produit = Produit.creer(CodeProduit.de("PROD-13"), "TANTY", "Sachets", "PRODUIT 13",
            "PRODUIT 13", CategorieProduit.PRODUIT_FINI, CategorieMetier.TANTY_BOUILLIE_DE_SOJA);
        Conditionnement conditionnement = produit.ajouterConditionnement("PROD-13-UNITE",
            NiveauConditionnement.UNITE_BASE, "Sachets", 1L, 1000, 0d, null, null);
        conditionnement.definirTypeEtComposants(TypeEmballage.SACHET, List.of(
            new ComposantEmballage(TypeEmballage.SACHET, 1, 1),
            new ComposantEmballage(TypeEmballage.COUVERCLE, 1, 24)
        ));
        formuleRepository.enregistrer(FormuleProduction.creer("PROD-13", BigDecimal.valueOf(50), List.of(
            new LigneFormule("Maïs", BigDecimal.valueOf(40)),
            new LigneFormule("Soja", BigDecimal.valueOf(9)),
            new LigneFormule("Arachide", BigDecimal.valueOf(1))
        )));
        BonCommande bc = bonCommandeAvecLigne("PROD-13", "PRODUIT 13", 13);

        PlanificationProductionService.ResultatPlanification resultat =
            service.calculer(bc, Map.of("PROD-13", produit));

        assertEquals(5, resultat.lignesDetaillees().size(), "un jour ouvré = une ligne de détail");
        int totalCartons = resultat.lignesDetaillees().stream().mapToInt(LignePlanDetail::getQuantiteCartons).sum();
        assertEquals(13, totalCartons, "la somme des jours doit reproduire exactement les 13 cartons commandés");
        assertTrue(resultat.lignesDetaillees().stream().allMatch(l -> l.getQuantiteCartons() >= 0));

        BigDecimal totalMais = sommeMatiere(resultat.lignesDetaillees(), "Maïs");
        BigDecimal totalSoja = sommeMatiere(resultat.lignesDetaillees(), "Soja");
        BigDecimal totalArachide = sommeMatiere(resultat.lignesDetaillees(), "Arachide");
        assertEquals(0, BigDecimal.valueOf(10.4).setScale(2).compareTo(totalMais), "obtenu " + totalMais);
        assertEquals(0, BigDecimal.valueOf(2.34).setScale(2).compareTo(totalSoja), "obtenu " + totalSoja);
        assertEquals(0, BigDecimal.valueOf(0.26).setScale(2).compareTo(totalArachide), "obtenu " + totalArachide);

        long totalSachets = resultat.lignesDetaillees().stream()
            .mapToLong(l -> l.getBesoinEmballage(TypeEmballage.SACHET)).sum();
        long totalCouvercles = resultat.lignesDetaillees().stream()
            .mapToLong(l -> l.getBesoinEmballage(TypeEmballage.COUVERCLE)).sum();
        assertEquals(13L, totalSachets);
        assertEquals(1L, totalCouvercles, "ceil(13/24) = 1, réparti sur un seul jour sans jamais être perdu");
    }

    @Test
    @DisplayName("Bloc 6 — une période explicite de deux semaines est respectée intégralement (10 jours ouvrés)")
    void periode_sur_plusieurs_semaines() {
        LocalDate debut = LocalDate.now().with(DayOfWeek.MONDAY);
        LocalDate fin = debut.plusDays(11); // vendredi de la semaine suivante : 2 semaines ouvrées complètes (10 jours)
        Produit produit = produit("PROD-MULTI-SEMAINE", "PRODUIT MULTI SEMAINE", 500);
        BonCommande bc = bonCommandeAvecPeriode("PROD-MULTI-SEMAINE", "PRODUIT MULTI SEMAINE", 20, debut, fin);

        PlanificationProductionService.ResultatPlanification resultat =
            service.calculer(bc, Map.of("PROD-MULTI-SEMAINE", produit));

        assertEquals(debut, resultat.dateDebut());
        assertEquals(fin, resultat.dateFin());
        assertEquals(10, resultat.lignesDetaillees().size(), "2 semaines ouvrées complètes = 10 jours");
        int totalCartons = resultat.lignesDetaillees().stream().mapToInt(LignePlanDetail::getQuantiteCartons).sum();
        assertEquals(20, totalCartons);
    }

    // Le cas « date de fin antérieure à la date de début » n'est pas testable ici :
    // BonCommande.definirPeriodeProduction() refuse déjà cet état à la construction
    // (IllegalArgumentException), avant même d'atteindre le service — même raisonnement
    // que pour le poids négatif (cf. plus haut). Le seul cas de période invalide
    // réellement atteignable par le service est une période sans aucun jour ouvré,
    // testé ci-dessous.
    @Test
    @DisplayName("Bloc 6 — une période sans aucun jour ouvré (un seul samedi) lève une erreur explicite")
    void periode_sans_jour_ouvre_leve_exception() {
        LocalDate samedi = LocalDate.now().with(DayOfWeek.SATURDAY);
        Produit produit = produit("PROD-WEEKEND", "PRODUIT WEEKEND", 500);
        BonCommande bc = bonCommandeAvecPeriode("PROD-WEEKEND", "PRODUIT WEEKEND", 5, samedi, samedi);

        RegleMetierException erreur = assertThrows(RegleMetierException.class,
            () -> service.calculer(bc, Map.of("PROD-WEEKEND", produit)));

        assertTrue(erreur.getMessage().contains("BC-TEST-0001"));
    }
}
