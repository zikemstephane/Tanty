package com.NTFOODS.Api_tanty.shared.infrastructure.seeding;

import com.NTFOODS.Api_tanty.modules.stock.domain.catalogue.enums.CategorieMetier;
import com.NTFOODS.Api_tanty.modules.stock.domain.catalogue.enums.CategorieProduit;
import com.NTFOODS.Api_tanty.modules.stock.domain.catalogue.enums.NiveauConditionnement;
import com.NTFOODS.Api_tanty.modules.stock.domain.catalogue.model.Produit;
import com.NTFOODS.Api_tanty.modules.stock.domain.catalogue.repository.ProduitRepository;
import com.NTFOODS.Api_tanty.modules.stock.domain.catalogue.valueobject.CodeProduit;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;

/**
 * Catalogue produits NTFOODS TANTY.
 *
 * <p>Premier maillon de la chaîne : tout le reste — prix homologués, seuils,
 * dotations, ventes — désigne un produit par son code. Un catalogue incohérent
 * rend tous les seeders suivants faux sans qu'aucun n'échoue, ce qui est la
 * pire des situations. Les codes posés ici sont donc la référence unique.</p>
 *
 * <p>Chaque produit porte <strong>deux conditionnements</strong> : l'unité de
 * base, qui est ce que le commercial vend au client, et le carton, qui est ce
 * qui circule entre les entrepôts. Le facteur de conversion est ce qui permet
 * de compter un stock en unités et de le mouvoir en cartons ; sans lui, une
 * dotation de 5 cartons n'aurait aucune valeur en stock.</p>
 *
 * <p>Le SKU — et non le libellé — identifie le conditionnement partout dans le
 * système. Les SKU suivent la forme {@code CODE-NIVEAU}.</p>
 */
@Component
@Order(2)
public class CatalogueSeeder implements DataSeeder {

    private static final Logger log = LoggerFactory.getLogger(CatalogueSeeder.class);

    private final ProduitRepository produitRepository;

    public CatalogueSeeder(ProduitRepository produitRepository) {
        this.produitRepository = produitRepository;
    }

    /**
     * Définition d'un produit du catalogue.
     *
     * @param unitesParCarton facteur de conversion carton → unité de base
     * @param prixDetaillant  prix de référence au détail, en FCFA par unité
     */
    private record Ligne(String code, String marque, String gamme, String variete,
                         String designation, CategorieProduit categorie, CategorieMetier metier,
                         String uniteLibelle, long unitesParCarton, double poidsUnitaireG,
                         double prixDistributeur, double prixGrossiste, double prixDetaillant) {}

    /**
     * Catalogue réel NTFOODS TANTY (issu de « LISTE DES PRODUITS.xlsx », déjà
     * utilisé côté web pour la saisie des Bons de Commande — voir
     * {@code CATALOGUE_PRODUITS} dans {@code cp-bons-commande.component.ts}).
     *
     * Remplace l'ancien catalogue de démonstration (12 produits génériques,
     * jamais aligné sur les codes réels) : un BC créé avec un code de ce
     * catalogue web échouait systématiquement à la génération automatique du
     * PPH, faute de produit correspondant en base.
     *
     * <p><strong>Conditionnement provisoire</strong> : {@code unitesParCarton=1}
     * et {@code poidsUnitaireG=0} pour tous les produits ci-dessous — ces
     * valeurs physiques réelles (poids unitaire, unités par carton) ne sont
     * connues d'aucune source actuelle et ne doivent pas être devinées, car
     * elles pilotent directement les calculs de poudre/fûts/emballages du plan
     * de production. Elles sont à corriger via l'écran de gestion des produits
     * avant toute utilisation des quantités calculées à des fins réelles.
     * Aucun prix n'est fixé pour la même raison.</p>
     */
    private static final List<Ligne> CATALOGUE = List.of(
        // ── TANTY BOUILLIE DE SOJA — Sachets ────────────────────────────────
        new Ligne("TBS-SAC-001", "TANTY", "Sachets", "Reine Bouillie Soja Arachides",
            "REINE BOUILLIE SOJA ARACHIDES", CategorieProduit.PRODUIT_FINI,
            CategorieMetier.TANTY_BOUILLIE_DE_SOJA, "Sachets", 1, 0, 0, 0, 0),
        new Ligne("TBS-SAC-002", "TANTY", "Sachets", "Reine Bouillie Soja Classic",
            "REINE BOUILLIE SOJA CLASSIC", CategorieProduit.PRODUIT_FINI,
            CategorieMetier.TANTY_BOUILLIE_DE_SOJA, "Sachets", 1, 0, 0, 0, 0),
        new Ligne("TBS-SAC-003", "TANTY", "Sachets", "Reine Bouillie Soja Lactée Croissance",
            "REINE BOUILLIE SOJA LACTEE CROISSANCE", CategorieProduit.PRODUIT_FINI,
            CategorieMetier.TANTY_BOUILLIE_DE_SOJA, "Sachets", 1, 0, 0, 0, 0),
        new Ligne("TBS-SAC-004", "TANTY", "Sachets", "Reine Bouillie Soja Multi Céréale",
            "REINE BOUILLIE SOJA MULTI CEREALE", CategorieProduit.PRODUIT_FINI,
            CategorieMetier.TANTY_BOUILLIE_DE_SOJA, "Sachets", 1, 0, 0, 0, 0),
        new Ligne("TBS-SAC-005", "TANTY", "Sachets", "Reine Bouillie Soja Poisson",
            "REINE BOUILLIE SOJA POISSON", CategorieProduit.PRODUIT_FINI,
            CategorieMetier.TANTY_BOUILLIE_DE_SOJA, "Sachets", 1, 0, 0, 0, 0),
        new Ligne("TBS-SAC-006", "TANTY", "Sachets", "Tanty Bouillie Soja Arachides",
            "TANTY BOUILLIE SOJA ARACHIDES", CategorieProduit.PRODUIT_FINI,
            CategorieMetier.TANTY_BOUILLIE_DE_SOJA, "Sachets", 1, 0, 0, 0, 0),
        new Ligne("TBS-SAC-007", "TANTY", "Sachets", "Tanty Bouillie Soja Lactée",
            "TANTY BOUILLIE SOJA LACTEE", CategorieProduit.PRODUIT_FINI,
            CategorieMetier.TANTY_BOUILLIE_DE_SOJA, "Sachets", 1, 0, 0, 0, 0),
        new Ligne("TBS-SAC-008", "TANTY", "Sachets", "Tanty Bouillie Soja Nature",
            "TANTY BOUILLIE SOJA NATURE", CategorieProduit.PRODUIT_FINI,
            CategorieMetier.TANTY_BOUILLIE_DE_SOJA, "Sachets", 1, 0, 0, 0, 0),
        new Ligne("TBS-SAC-009", "TANTY", "Sachets", "Tanty Bouillie Soja Parfumé",
            "TANTY BOUILLIE SOJA PARFUME", CategorieProduit.PRODUIT_FINI,
            CategorieMetier.TANTY_BOUILLIE_DE_SOJA, "Sachets", 1, 0, 0, 0, 0),
        new Ligne("TBS-SAC-010", "TANTY", "Sachets", "Tanty Bouillie Soja Poisson",
            "TANTY BOUILLIE SOJA POISSON", CategorieProduit.PRODUIT_FINI,
            CategorieMetier.TANTY_BOUILLIE_DE_SOJA, "Sachets", 1, 0, 0, 0, 0),

        // ── TANTY BOUILLIE DE SOJA — Étuis ───────────────────────────────────
        new Ligne("TBS-ETU-001", "TANTY", "Étuis", "Prestige Reine Bouillie Arachides",
            "PRESTIGE REINE BOUILLIE ARACHIDES", CategorieProduit.PRODUIT_FINI,
            CategorieMetier.TANTY_BOUILLIE_DE_SOJA, "Étuis", 1, 0, 0, 0, 0),
        new Ligne("TBS-ETU-002", "TANTY", "Étuis", "Prestige Reine Bouillie Classic",
            "PRESTIGE REINE BOUILLIE CLASSIC", CategorieProduit.PRODUIT_FINI,
            CategorieMetier.TANTY_BOUILLIE_DE_SOJA, "Étuis", 1, 0, 0, 0, 0),
        new Ligne("TBS-ETU-003", "TANTY", "Étuis", "Prestige Reine Bouillie Lactée Croissance",
            "PRESTIGE REINE BOUILLIE LACTEE CROISSANCE", CategorieProduit.PRODUIT_FINI,
            CategorieMetier.TANTY_BOUILLIE_DE_SOJA, "Étuis", 1, 0, 0, 0, 0),
        new Ligne("TBS-ETU-004", "TANTY", "Étuis", "Prestige Reine Bouillie Multi Céréale",
            "PRESTIGE REINE BOUILLIE MULTI CEREALE", CategorieProduit.PRODUIT_FINI,
            CategorieMetier.TANTY_BOUILLIE_DE_SOJA, "Étuis", 1, 0, 0, 0, 0),
        new Ligne("TBS-ETU-005", "TANTY", "Étuis", "Prestige Reine Bouillie Poisson",
            "PRESTIGE REINE BOUILLIE POISSON", CategorieProduit.PRODUIT_FINI,
            CategorieMetier.TANTY_BOUILLIE_DE_SOJA, "Étuis", 1, 0, 0, 0, 0),
        new Ligne("TBS-ETU-006", "TANTY", "Étuis", "Prestige Tanty Bouillie Arachides",
            "PRESTIGE TANTY BOUILLIE ARACHIDES", CategorieProduit.PRODUIT_FINI,
            CategorieMetier.TANTY_BOUILLIE_DE_SOJA, "Étuis", 1, 0, 0, 0, 0),
        new Ligne("TBS-ETU-007", "TANTY", "Étuis", "Prestige Tanty Bouillie Lactée",
            "PRESTIGE TANTY BOUILLIE LACTEE", CategorieProduit.PRODUIT_FINI,
            CategorieMetier.TANTY_BOUILLIE_DE_SOJA, "Étuis", 1, 0, 0, 0, 0),
        new Ligne("TBS-ETU-008", "TANTY", "Étuis", "Prestige Tanty Bouillie Nature",
            "PRESTIGE TANTY BOUILLIE NATURE", CategorieProduit.PRODUIT_FINI,
            CategorieMetier.TANTY_BOUILLIE_DE_SOJA, "Étuis", 1, 0, 0, 0, 0),
        new Ligne("TBS-ETU-009", "TANTY", "Étuis", "Prestige Tanty Bouillie Parfumé",
            "PRESTIGE TANTY BOUILLIE PARFUME", CategorieProduit.PRODUIT_FINI,
            CategorieMetier.TANTY_BOUILLIE_DE_SOJA, "Étuis", 1, 0, 0, 0, 0),
        new Ligne("TBS-ETU-010", "TANTY", "Étuis", "Prestige Tanty Bouillie Poisson",
            "PRESTIGE TANTY BOUILLIE POISSON", CategorieProduit.PRODUIT_FINI,
            CategorieMetier.TANTY_BOUILLIE_DE_SOJA, "Étuis", 1, 0, 0, 0, 0),

        // ── Reine Costard Powder — Seaux 1L / 2L / 5L ───────────────────────
        new Ligne("RCP-1L-001", "TANTY", "Seaux Costard 1L", "Reine Costard Powder 1L Banane",
            "REINE COSTARD POWDER 1L BANANE", CategorieProduit.PRODUIT_FINI,
            CategorieMetier.TANTY_BOUILLIE_DE_SOJA, "Seaux Costard 1L", 1, 0, 0, 0, 0),
        new Ligne("RCP-1L-002", "TANTY", "Seaux Costard 1L", "Reine Costard Powder 1L Fraise",
            "REINE COSTARD POWDER 1L FRAISE", CategorieProduit.PRODUIT_FINI,
            CategorieMetier.TANTY_BOUILLIE_DE_SOJA, "Seaux Costard 1L", 1, 0, 0, 0, 0),
        new Ligne("RCP-1L-003", "TANTY", "Seaux Costard 1L", "Reine Costard Powder 1L Fruits et Œufs",
            "REINE COSTARD POWDER 1L FRUITS ET OEUFS", CategorieProduit.PRODUIT_FINI,
            CategorieMetier.TANTY_BOUILLIE_DE_SOJA, "Seaux Costard 1L", 1, 0, 0, 0, 0),
        new Ligne("RCP-1L-004", "TANTY", "Seaux Costard 1L", "Reine Costard Powder 1L Lactée Biscuitée",
            "REINE COSTARD POWDER 1L LACTEE BISCUITE", CategorieProduit.PRODUIT_FINI,
            CategorieMetier.TANTY_BOUILLIE_DE_SOJA, "Seaux Costard 1L", 1, 0, 0, 0, 0),
        new Ligne("RCP-2L-001", "TANTY", "Seaux Costard 2L", "Reine Costard Powder 2L Banane",
            "REINE COSTARD POWDER 2L BANANE", CategorieProduit.PRODUIT_FINI,
            CategorieMetier.TANTY_BOUILLIE_DE_SOJA, "Seaux Costard 2L", 1, 0, 0, 0, 0),
        new Ligne("RCP-2L-002", "TANTY", "Seaux Costard 2L", "Reine Costard Powder 2L Fraise",
            "REINE COSTARD POWDER 2L FRAISE", CategorieProduit.PRODUIT_FINI,
            CategorieMetier.TANTY_BOUILLIE_DE_SOJA, "Seaux Costard 2L", 1, 0, 0, 0, 0),
        new Ligne("RCP-2L-003", "TANTY", "Seaux Costard 2L", "Reine Costard Powder 2L Fruits et Œufs",
            "REINE COSTARD POWDER 2L FRUITS ET OEUFS", CategorieProduit.PRODUIT_FINI,
            CategorieMetier.TANTY_BOUILLIE_DE_SOJA, "Seaux Costard 2L", 1, 0, 0, 0, 0),
        new Ligne("RCP-2L-004", "TANTY", "Seaux Costard 2L", "Reine Costard Powder 2L Lactée Biscuitée",
            "REINE COSTARD POWDER 2L LACTEE BISCUITE", CategorieProduit.PRODUIT_FINI,
            CategorieMetier.TANTY_BOUILLIE_DE_SOJA, "Seaux Costard 2L", 1, 0, 0, 0, 0),
        new Ligne("RCP-5L-001", "TANTY", "Seaux Costard 5L", "Reine Costard Powder 5L Fruits et Œufs",
            "REINE COSTARD POWDER 5L FRUITS ET OEUFS", CategorieProduit.PRODUIT_FINI,
            CategorieMetier.TANTY_BOUILLIE_DE_SOJA, "Seaux Costard 5L", 1, 0, 0, 0, 0),

        // ── Babyvita — catégorie métier non déterminée à ce jour, rattachée
        // provisoirement à Bouillie de Soja (boisson en poudre similaire) ────
        new Ligne("BVT-001", "TANTY", "Babyvita", "Babyvita 1L",
            "BABYVITA 1L", CategorieProduit.PRODUIT_FINI,
            CategorieMetier.TANTY_BOUILLIE_DE_SOJA, "12 seaux x 390g", 1, 0, 0, 0, 0),
        new Ligne("BVT-002", "TANTY", "Babyvita", "Babyvita 2L",
            "BABYVITA 2L", CategorieProduit.PRODUIT_FINI,
            CategorieMetier.TANTY_BOUILLIE_DE_SOJA, "4 seaux x 880g", 1, 0, 0, 0, 0),

        // ── Tanty Chocolat — Seaux ───────────────────────────────────────────
        new Ligne("TCH-001", "TANTY", "Seaux", "Tanty Chocolat 2L",
            "TANTY CHOCOLAT 2L", CategorieProduit.PRODUIT_FINI,
            CategorieMetier.TANTY_CHOCOLAT, "4 seaux x 2,2kg", 1, 0, 0, 0, 0),
        new Ligne("TCH-002", "TANTY", "Seaux", "Tanty Chocolat 5L",
            "TANTY CHOCOLAT 5L", CategorieProduit.PRODUIT_FINI,
            CategorieMetier.TANTY_CHOCOLAT, "seau de 4,1kg", 1, 0, 0, 0, 0),
        new Ligne("TCH-003", "TANTY", "Seaux", "Tanty Chocolat 10L",
            "TANTY CHOCOLAT 10L", CategorieProduit.PRODUIT_FINI,
            CategorieMetier.TANTY_CHOCOLAT, "seau de 9,2kg", 1, 0, 0, 0, 0),

        // ── Ingrédients — catégorie métier non déterminée à ce jour, rattachée
        // provisoirement à Ingrédients ────────────────────────────────────────
        new Ligne("ING-CHP-001", "TANTY", "Étuis", "Tanty Chapelure",
            "TANTY CHAPELURE", CategorieProduit.PRODUIT_FINI,
            CategorieMetier.INGREDIENTS, "12 étuis x 200g", 1, 0, 0, 0, 0),

        // ── TANTY À GRIGNOTER — Minis / Moyens / Grands ─────────────────────
        new Ligne("TAG-MIN-001", "TANTY", "Minis", "Mini à Grignoter Arachides Enrobées",
            "MIN A GRIGNOTER ARACHIDES ENROBEES", CategorieProduit.PRODUIT_FINI,
            CategorieMetier.TANTY_A_GRIGNOTER, "Minis", 1, 0, 0, 0, 0),
        new Ligne("TAG-MIN-002", "TANTY", "Minis", "Mini à Grignoter Caramels",
            "MIN A GRIGNOTER CARAMELS", CategorieProduit.PRODUIT_FINI,
            CategorieMetier.TANTY_A_GRIGNOTER, "Minis", 1, 0, 0, 0, 0),
        new Ligne("TAG-MIN-003", "TANTY", "Minis", "Mini à Grignoter Croquettes",
            "MIN A GRIGNOTER CROQUETTES", CategorieProduit.PRODUIT_FINI,
            CategorieMetier.TANTY_A_GRIGNOTER, "Minis", 1, 0, 0, 0, 0),
        new Ligne("TAG-MIN-004", "TANTY", "Minis", "Mini à Grignoter Chips de Plantains",
            "MINI A GRIGNOTER CHIPS DE PLANTAINS", CategorieProduit.PRODUIT_FINI,
            CategorieMetier.TANTY_A_GRIGNOTER, "Minis", 1, 0, 0, 0, 0),
        new Ligne("TAG-MIN-005", "TANTY", "Minis", "Tanty Beignets Soufflés",
            "TANTY BEIGNETS SOUFFLES", CategorieProduit.PRODUIT_FINI,
            CategorieMetier.TANTY_A_GRIGNOTER, "Minis", 1, 0, 0, 0, 0),
        new Ligne("TAG-MOY-001", "TANTY", "Moyens", "Moyen à Grignoter Arachides Enrobées",
            "MOYEN A GRIGNOTER ARACHIDES ENROBEES", CategorieProduit.PRODUIT_FINI,
            CategorieMetier.TANTY_A_GRIGNOTER, "Moyens", 1, 0, 0, 0, 0),
        new Ligne("TAG-MOY-002", "TANTY", "Moyens", "Moyen à Grignoter Caramels",
            "MOYEN A GRIGNOTER CARAMELS", CategorieProduit.PRODUIT_FINI,
            CategorieMetier.TANTY_A_GRIGNOTER, "Moyens", 1, 0, 0, 0, 0),
        new Ligne("TAG-MOY-003", "TANTY", "Moyens", "Moyen à Grignoter Croquettes",
            "MOYEN A GRIGNOTER CROQUETTES", CategorieProduit.PRODUIT_FINI,
            CategorieMetier.TANTY_A_GRIGNOTER, "Moyens", 1, 0, 0, 0, 0),
        new Ligne("TAG-MOY-004", "TANTY", "Moyens", "Moyen à Grignoter Chips de Plantains",
            "MOYEN A GRIGNOTER CHIPS DE PLANTAINS", CategorieProduit.PRODUIT_FINI,
            CategorieMetier.TANTY_A_GRIGNOTER, "Moyens", 1, 0, 0, 0, 0),
        new Ligne("TAG-GRD-001", "TANTY", "Grands", "Grand à Grignoter Arachides Enrobées",
            "GRAND A GRIGNOTER ARACHIDES ENROBEES", CategorieProduit.PRODUIT_FINI,
            CategorieMetier.TANTY_A_GRIGNOTER, "Grands", 1, 0, 0, 0, 0),
        new Ligne("TAG-GRD-002", "TANTY", "Grands", "Grand à Grignoter Caramels",
            "GRAND A GRIGNOTER CARAMELS", CategorieProduit.PRODUIT_FINI,
            CategorieMetier.TANTY_A_GRIGNOTER, "Grands", 1, 0, 0, 0, 0),
        new Ligne("TAG-GRD-003", "TANTY", "Grands", "Grand à Grignoter Croquettes",
            "GRAND A GRIGNOTER CROQUETTES", CategorieProduit.PRODUIT_FINI,
            CategorieMetier.TANTY_A_GRIGNOTER, "Grands", 1, 0, 0, 0, 0)
    );

    @Override
    @Transactional
    public void seed() {
        int crees = 0;
        for (Ligne l : CATALOGUE) {
            CodeProduit code = CodeProduit.de(l.code());
            if (produitRepository.existeParCode(code)) {
                continue;
            }
            Produit p = Produit.creer(code, l.marque(), l.gamme(), l.variete(),
                l.designation(), l.categorie(), l.metier());

            String skuUnite = l.code() + "-UNITE";
            p.ajouterConditionnement(skuUnite, NiveauConditionnement.UNITE_BASE,
                l.uniteLibelle(), 1L, l.poidsUnitaireG(), 0d,
                BigDecimal.valueOf(l.prixDetaillant()), null);

            // Le carton n'a de sens que s'il contient plus d'une unité. Un sac
            // de matière première EST l'unité : lui ajouter un carton de
            // facteur 1 créerait deux conditionnements interchangeables, et
            // donc deux façons de compter le même stock.
            if (l.unitesParCarton() > 1) {
                p.ajouterConditionnement(l.code() + "-CARTON", NiveauConditionnement.CARTON,
                    "Carton de " + l.unitesParCarton(), l.unitesParCarton(),
                    l.poidsUnitaireG() * l.unitesParCarton(), 0d,
                    BigDecimal.valueOf(l.prixDetaillant() * l.unitesParCarton()), null);
            }

            if (l.prixDetaillant() > 0) {
                p.definirPrixCommerciaux(l.prixDistributeur(), l.prixGrossiste(), l.prixDetaillant(),
                    false, skuUnite, l.uniteLibelle());
            }

            produitRepository.sauvegarder(p);
            crees++;
        }
        log.info("[SEED] Catalogue : {} produit(s) créé(s), {} déjà présent(s)",
            crees, CATALOGUE.size() - crees);
    }
}
