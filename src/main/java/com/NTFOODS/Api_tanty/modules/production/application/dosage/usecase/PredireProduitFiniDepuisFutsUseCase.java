package com.NTFOODS.Api_tanty.modules.production.application.dosage.usecase;

import com.NTFOODS.Api_tanty.modules.production.application.dosage.dto.response.PredictionProduitFiniResponse;
import com.NTFOODS.Api_tanty.modules.production.domain.pph.enums.RecetteBouillie;
import com.NTFOODS.Api_tanty.modules.stock.domain.catalogue.enums.CategorieMetier;
import com.NTFOODS.Api_tanty.modules.stock.domain.catalogue.enums.NiveauConditionnement;
import com.NTFOODS.Api_tanty.modules.stock.domain.catalogue.model.Conditionnement;
import com.NTFOODS.Api_tanty.modules.stock.domain.catalogue.model.Produit;
import com.NTFOODS.Api_tanty.modules.stock.domain.catalogue.repository.ProduitRepository;
import com.NTFOODS.Api_tanty.modules.stock.domain.catalogue.valueobject.CodeProduit;
import com.NTFOODS.Api_tanty.shared.kernel.exception.EntiteNonTrouveeException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * Use Case — Prédire la quantité de produit fini à partir du nombre de fûts nets dosés.
 *
 * Chaque fût net = 50 kg de poudre. La répartition de la poudre dépend de la recette
 * du produit (Bouillie standard ou Mélange d'arachide). Pour chaque produit BOUILLIE,
 * on calcule le nombre d'unités de base et de cartons que l'on peut produire.
 */
@Service
@Transactional(readOnly = true)
public class PredireProduitFiniDepuisFutsUseCase {

    private static final double POUDRE_FUT_KG = 50.0;

    private final ProduitRepository produitRepository;

    public PredireProduitFiniDepuisFutsUseCase(ProduitRepository produitRepository) {
        this.produitRepository = produitRepository;
    }

    public PredictionProduitFiniResponse executer(int nbFutsNets, String codeProduitOptionnel) {
        if (nbFutsNets <= 0) {
            throw new IllegalArgumentException("Le nombre de fûts nets doit être positif");
        }

        double poidsTotalPoudreKg = nbFutsNets * POUDRE_FUT_KG;

        List<Produit> produits;
        if (codeProduitOptionnel != null && !codeProduitOptionnel.isBlank()) {
            Produit p = produitRepository.trouverParCode(CodeProduit.de(codeProduitOptionnel))
                .orElseThrow(() -> EntiteNonTrouveeException.de("Produit", codeProduitOptionnel));
            produits = List.of(p);
        } else {
            produits = produitRepository.trouverParCategorieMetier(CategorieMetier.TANTY_BOUILLIE_DE_SOJA);
        }

        List<PredictionProduitFiniResponse.LignePrediction> predictions = new ArrayList<>();
        for (Produit p : produits) {
            Optional<PredictionProduitFiniResponse.LignePrediction> ligne = predirePourProduit(p, poidsTotalPoudreKg);
            ligne.ifPresent(predictions::add);
        }

        return new PredictionProduitFiniResponse(nbFutsNets, poidsTotalPoudreKg, predictions);
    }

    private Optional<PredictionProduitFiniResponse.LignePrediction> predirePourProduit(Produit produit, double poidsTotalPoudreKg) {
        if (!produit.isActif()) return Optional.empty();

        RecetteBouillie recette = RecetteBouillie.depuisDesignation(produit.getDesignation());
        // Le poids du produit fini correspond au poids total de poudre dosée (conservation de la masse).
        double poidsProduitKg = poidsTotalPoudreKg;

        // Unité de base
        Conditionnement uniteBase = produit.getConditionnements().stream()
            .filter(c -> c.getNiveau() == NiveauConditionnement.UNITE_BASE)
            .findFirst()
            .orElse(null);
        if (uniteBase == null) return Optional.empty();

        double poidsUnitaireGrammes = uniteBase.getPoidsUnitaireGrammes();
        if (poidsUnitaireGrammes <= 0) return Optional.empty();

        int quantiteUnitesBase = (int) Math.floor((poidsProduitKg * 1000.0) / poidsUnitaireGrammes);

        // Carton
        Conditionnement carton = produit.getConditionnements().stream()
            .filter(c -> c.getNiveau() == NiveauConditionnement.CARTON)
            .findFirst()
            .orElse(null);

        int quantiteCartons = 0;
        int nombreUnitesParCarton = 1;
        if (carton != null) {
            nombreUnitesParCarton = Math.max(1, carton.getNombreUnites());
            quantiteCartons = quantiteUnitesBase / nombreUnitesParCarton;
        }

        return Optional.of(new PredictionProduitFiniResponse.LignePrediction(
            produit.getCode().getValeur(),
            produit.getDesignation(),
            recette.getLibelle(),
            quantiteUnitesBase,
            quantiteCartons,
            poidsUnitaireGrammes,
            nombreUnitesParCarton
        ));
    }
}
