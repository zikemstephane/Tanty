package com.NTFOODS.Api_tanty.modules.stock.domain.catalogue.repository;

import com.NTFOODS.Api_tanty.modules.stock.domain.catalogue.enums.CategorieMetier;
import com.NTFOODS.Api_tanty.modules.stock.domain.catalogue.enums.CategorieProduit;
import com.NTFOODS.Api_tanty.modules.stock.domain.catalogue.model.Produit;
import com.NTFOODS.Api_tanty.modules.stock.domain.catalogue.valueobject.CodeProduit;

import java.util.List;
import java.util.Optional;

/**
 * Port — Persistance des produits du catalogue.
 * L'implémentation vit dans la couche infrastructure (adaptateur JPA).
 */
public interface ProduitRepository {

    Produit sauvegarder(Produit produit);

    Optional<Produit> trouverParCode(CodeProduit code);

    Optional<Produit> trouverParId(Long id);

    Optional<Produit> trouverParSku(String sku);

    boolean existeParCode(CodeProduit code);

    List<Produit> trouverTous();

    List<Produit> trouverParCategorie(CategorieProduit categorie);

    List<Produit> trouverParCategorieMetier(CategorieMetier categorieMetier);

    List<Produit> rechercher(String marque, String gamme, String variete, Boolean actif);

    void supprimer(CodeProduit code);
}
