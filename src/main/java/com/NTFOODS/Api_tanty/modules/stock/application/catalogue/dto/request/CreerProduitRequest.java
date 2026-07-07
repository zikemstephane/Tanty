package com.NTFOODS.Api_tanty.modules.stock.application.catalogue.dto.request;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.math.BigDecimal;
import java.util.List;

/**
 * Requête — Création d'un produit avec ses conditionnements initiaux.
 */
public record CreerProduitRequest(
    @NotBlank(message = "Le code produit est obligatoire") String code,
    @NotBlank(message = "La marque est obligatoire") String marque,
    @NotBlank(message = "La gamme est obligatoire") String gamme,
    @NotBlank(message = "La variété est obligatoire") String variete,
    @NotBlank(message = "La désignation est obligatoire") String designation,
    @NotBlank(message = "La catégorie est obligatoire") String categorie,
    String categorieMetier,
    Double prixDistributeur,
    Double prixGrossiste,
    Double prixDetaillant,
    Boolean tva,
    String conditionnementCommercial,
    String uniteVenteCommerciale,
    @Valid List<ConditionnementRequest> conditionnements
) {
    public record ConditionnementRequest(
        String sku,
        @NotBlank(message = "Le niveau de conditionnement est obligatoire") String niveau,
        @NotBlank(message = "Le libellé est obligatoire") String libelle,
        @NotNull(message = "Le facteur de conversion est obligatoire") Long facteurConversionVersBase,
        double poidsUnitaireGrammes,
        double volumeUnitaireCm3,
        BigDecimal prixUnitaireHT,
        String codeBarres
    ) {}
}
