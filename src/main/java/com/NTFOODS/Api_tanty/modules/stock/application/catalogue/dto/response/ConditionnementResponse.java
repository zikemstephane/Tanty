package com.NTFOODS.Api_tanty.modules.stock.application.catalogue.dto.response;

import java.math.BigDecimal;

/** Réponse — Représentation d'un conditionnement. */
public record ConditionnementResponse(
    String id,
    String sku,
    String niveau,
    String niveauLibelle,
    String libelle,
    long facteurConversionVersBase,
    int nombreUnites,
    String uniteConditionnement,
    double poidsUnitaireGrammes,
    double volumeUnitaireCm3,
    BigDecimal prixUnitaireHT,
    String codeBarres
) {}
