package com.NTFOODS.Api_tanty.modules.stock.application.catalogue.dto.request;

/**
 * Requête — Modification des informations descriptives d'un produit.
 * Les champs nuls ne sont pas modifiés.
 */
public record ModifierProduitRequest(
    String marque,
    String gamme,
    String variete,
    String designation,
    String categorieMetier,
    Double prixDistributeur,
    Double prixGrossiste,
    Double prixDetaillant,
    Boolean tva,
    String conditionnementCommercial,
    String uniteVenteCommerciale
) {}
