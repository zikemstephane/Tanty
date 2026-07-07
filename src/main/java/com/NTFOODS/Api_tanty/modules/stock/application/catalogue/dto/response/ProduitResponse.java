package com.NTFOODS.Api_tanty.modules.stock.application.catalogue.dto.response;

import java.time.LocalDateTime;
import java.util.List;

/** Réponse — Représentation complète d'un produit du catalogue. */
public record ProduitResponse(
    Long id,
    String code,
    String marque,
    String gamme,
    String variete,
    String designation,
    String categorie,
    String categorieLibelle,
    String categorieMetier,
    String categorieMetierLibelle,
    boolean actif,
    Double prixDistributeur,
    Double prixGrossiste,
    Double prixDetaillant,
    boolean tva,
    String conditionnementCommercial,
    String uniteVenteCommerciale,
    List<ConditionnementResponse> conditionnements,
    LocalDateTime dateCreation,
    LocalDateTime dateModification
) {}
