package com.NTFOODS.Api_tanty.modules.stock.application.dashboard.dto.response;

import java.math.BigDecimal;

/**
 * Réponse — KPIs agrégés du tableau de bord Stock.
 * Consommé par l'écran « Dashboard » du frontend web.
 */
public record TableauBordStockResponse(
    long totalLignesStock,
    BigDecimal valeurTotaleStock,
    long alertesCritiques,
    long alertesFaibles,
    long receptionsEnAttente,
    long receptionsDuJour,
    long mouvementsDuJour,
    long lotsEnAttente,
    long commandesInternesActives,
    BigDecimal ventesDuJour
) {}
