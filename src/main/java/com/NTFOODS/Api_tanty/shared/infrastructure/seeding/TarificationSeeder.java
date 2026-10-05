package com.NTFOODS.Api_tanty.shared.infrastructure.seeding;

import com.NTFOODS.Api_tanty.modules.commercial.domain.client.enums.TypeClient;
import com.NTFOODS.Api_tanty.modules.commercial.domain.tarification.model.GrilleTarifaire;
import com.NTFOODS.Api_tanty.modules.commercial.domain.tarification.repository.GrilleTarifaireRepository;
import com.NTFOODS.Api_tanty.modules.stock.domain.catalogue.model.Produit;
import com.NTFOODS.Api_tanty.modules.stock.domain.catalogue.repository.ProduitRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * Grille tarifaire homologuée : le prix opposable au commercial.
 *
 * <p>C'est la pièce qui rend le système contraignant. Sans elle, un commercial
 * peut vendre au prix qu'il veut ; avec elle, toute vente hors grille est
 * refusée par le domaine. Un catalogue sans grille est donc un catalogue dont
 * rien ne peut être vendu — d'où l'importance de couvrir <em>tous</em> les
 * produits vendables et <em>tous</em> les segments existants.</p>
 *
 * <p>Les prix ne sont pas inventés ici : ils sont lus sur le produit
 * ({@code prixDistributeur}, {@code prixGrossiste}, {@code prixDetaillant}) posé
 * par le {@link CatalogueSeeder}. Les recopier en dur créerait une seconde
 * source de vérité qui divergerait à la première révision de prix.</p>
 *
 * <p>Deux segments n'ont pas de prix propre au catalogue :
 * {@code SEMI_GROSSISTE} se place entre grossiste et détail,
 * {@code REVENDEUR} s'aligne sur le détail — c'est un détaillant qui revend.
 * Ces deux règles sont explicites ci-dessous plutôt que dispersées dans des
 * constantes, pour qu'une révision les trouve du premier coup.</p>
 *
 * <p>Les matières premières et consommables sont exclus : ils n'ont pas de prix
 * de vente au catalogue, et leur donner un prix homologué laisserait croire
 * qu'un commercial peut vendre un sac de soja brut.</p>
 */
@Component
@Order(5)
public class TarificationSeeder implements DataSeeder {

    private static final Logger log = LoggerFactory.getLogger(TarificationSeeder.class);

    /** Auteur des prix d'amorçage : la Chargée RP pilote la grille. */
    private static final String AUTEUR = "RP-001";

    private final ProduitRepository produitRepository;
    private final GrilleTarifaireRepository grilleRepository;

    public TarificationSeeder(ProduitRepository produitRepository,
                              GrilleTarifaireRepository grilleRepository) {
        this.produitRepository = produitRepository;
        this.grilleRepository = grilleRepository;
    }

    @Override
    @Transactional
    public void seed() {
        List<Produit> produits = produitRepository.trouverTous();
        int lignes = 0;

        for (Produit p : produits) {
            Double detail = p.getPrixDetaillant();
            Double grossiste = p.getPrixGrossiste();
            Double distributeur = p.getPrixDistributeur();

            // Un produit sans prix de vente n'est pas vendable : matière
            // première, consommable. On ne lui fabrique pas de prix.
            if (detail == null || detail <= 0) {
                continue;
            }

            double prixGrossiste = (grossiste != null && grossiste > 0) ? grossiste : detail;
            double prixDistributeur = (distributeur != null && distributeur > 0) ? distributeur : prixGrossiste;
            // Le semi-grossiste achète moins que le détaillant sans atteindre
            // le volume du grossiste : son prix est la moyenne des deux.
            double prixSemiGrossiste = Math.round((prixGrossiste + detail) / 2d);

            lignes += definir(p.getCode().getValeur(), TypeClient.DETAIL, detail);
            lignes += definir(p.getCode().getValeur(), TypeClient.REVENDEUR, detail);
            lignes += definir(p.getCode().getValeur(), TypeClient.SEMI_GROSSISTE, prixSemiGrossiste);
            lignes += definir(p.getCode().getValeur(), TypeClient.GROSSISTE, prixGrossiste);
            lignes += definir(p.getCode().getValeur(), TypeClient.DISTRIBUTEUR, prixDistributeur);
        }

        log.info("[SEED] Tarification : {} prix homologué(s) posé(s) sur {} produit(s)",
            lignes, produits.size());
    }

    /** Pose un prix s'il n'existe pas déjà. Ne révise jamais un prix en place. */
    private int definir(String codeProduit, TypeClient segment, double prix) {
        if (grilleRepository.trouver(codeProduit, segment).isPresent()) {
            return 0;
        }
        grilleRepository.sauvegarder(GrilleTarifaire.definir(codeProduit, segment, prix, AUTEUR));
        return 1;
    }
}
