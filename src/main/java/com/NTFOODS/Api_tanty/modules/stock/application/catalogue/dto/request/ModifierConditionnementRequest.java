package com.NTFOODS.Api_tanty.modules.stock.application.catalogue.dto.request;

import java.math.BigDecimal;

/**
 * Requête — Modification d'un conditionnement existant.
 * Tous les champs sont optionnels : seuls les champs renseignés seront mis à jour.
 */
public record ModifierConditionnementRequest(
    String libelle,
    Long facteurConversionVersBase,
    Double poidsUnitaireGrammes,
    Double volumeUnitaireCm3,
    BigDecimal prixUnitaireHT,
    String codeBarres
) {}
