package com.NTFOODS.Api_tanty.modules.stock.application.catalogue.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.math.BigDecimal;

/**
 * Requête — Ajout d'un conditionnement à un produit existant.
 */
public record AjouterConditionnementRequest(
    String sku,
    @NotBlank(message = "Le niveau de conditionnement est obligatoire") String niveau,
    @NotBlank(message = "Le libellé est obligatoire") String libelle,
    @NotNull(message = "Le facteur de conversion est obligatoire") Long facteurConversionVersBase,
    double poidsUnitaireGrammes,
    double volumeUnitaireCm3,
    BigDecimal prixUnitaireHT,
    String codeBarres
) {}
