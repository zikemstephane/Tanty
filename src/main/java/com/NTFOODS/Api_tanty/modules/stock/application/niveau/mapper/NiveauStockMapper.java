package com.NTFOODS.Api_tanty.modules.stock.application.niveau.mapper;

import com.NTFOODS.Api_tanty.modules.stock.application.niveau.dto.response.NiveauStockResponse;
import com.NTFOODS.Api_tanty.modules.stock.domain.catalogue.model.Conditionnement;
import com.NTFOODS.Api_tanty.modules.stock.domain.catalogue.model.Produit;
import com.NTFOODS.Api_tanty.modules.stock.domain.catalogue.repository.ProduitRepository;
import com.NTFOODS.Api_tanty.modules.stock.domain.catalogue.valueobject.CodeProduit;
import com.NTFOODS.Api_tanty.modules.stock.domain.niveau.model.NiveauStock;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;

/**
 * Mapper application — NiveauStock → DTO, avec décomposition de la quantité
 * (unités de base) dans les conditionnements du produit, du plus grand au plus petit.
 */
@Component
public class NiveauStockMapper {

    private final ProduitRepository produitRepository;

    public NiveauStockMapper(ProduitRepository produitRepository) {
        this.produitRepository = produitRepository;
    }

    public NiveauStockResponse versResponse(NiveauStock niveau) {
        Optional<Produit> produit = produitRepository.trouverParCode(CodeProduit.de(niveau.getCodeProduit()));

        String designation = produit.map(Produit::getDesignation).orElse(niveau.getCodeProduit());
        String marque = produit.map(Produit::getMarque).orElse(null);
        String gamme = produit.map(Produit::getGamme).orElse(null);
        List<NiveauStockResponse.LigneDecomposition> decomposition =
            produit.map(p -> decomposer(niveau.getQuantiteEnBase(), p)).orElse(List.of());

        return new NiveauStockResponse(
            niveau.getCodeProduit(), designation, marque, gamme,
            niveau.getCodeEntrepot(), niveau.getTypeEntrepot().name(),
            niveau.getQuantiteEnBase(), decomposition
        );
    }

    /** Décompose une quantité en unités de base dans les conditionnements (greedy, du plus grand au plus petit). */
    private List<NiveauStockResponse.LigneDecomposition> decomposer(long quantiteEnBase, Produit produit) {
        List<Conditionnement> tries = new ArrayList<>(produit.getConditionnements());
        tries.sort(Comparator.comparingLong(Conditionnement::getFacteurConversionVersBase).reversed());

        List<NiveauStockResponse.LigneDecomposition> lignes = new ArrayList<>();
        long reste = quantiteEnBase;
        for (Conditionnement c : tries) {
            long facteur = c.getFacteurConversionVersBase();
            if (facteur <= 0) continue;
            long nombre = reste / facteur;
            if (nombre > 0) {
                lignes.add(new NiveauStockResponse.LigneDecomposition(c.getNiveau().name(), c.getLibelle(), nombre));
                reste = reste % facteur;
            }
        }
        return lignes;
    }
}
