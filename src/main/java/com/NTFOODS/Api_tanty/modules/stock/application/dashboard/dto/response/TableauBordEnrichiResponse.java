package com.NTFOODS.Api_tanty.modules.stock.application.dashboard.dto.response;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

/**
 * Réponse — Tableau de bord enrichi avec stats percutantes.
 * Inclut niveaux du tampon, stock central, actions rapides,
 * performances commerciaux, et alertes invendus.
 */
public record TableauBordEnrichiResponse(
    long totalLignesStock,
    BigDecimal valeurTotaleStock,
    long alertesCritiques,
    long alertesFaibles,
    long alertesSecurite,
    long receptionsEnAttente,
    long receptionsDuJour,
    long mouvementsDuJour,
    long lotsEnAttente,
    long commandesInternesActives,
    BigDecimal ventesDuJour,
    long stockCentralTotal,
    long stockTamponTotal,
    long stockMobileTotal,
    long sessionsOuvertes,
    long sessionsEnPreparation,
    long commerciauxActifs,
    List<StatutTamponResponse> niveauxTampon,
    List<PerformanceCommercialResponse> performancesCommerciaux,
    List<InvenduResponse> alertesInvendus,
    List<ActionRapideResponse> actionsRapides
) {
    public record StatutTamponResponse(
        String codeProduit, String designation,
        long quantiteCentrale, long quantiteTampon,
        long seuilReappro, long seuilSecurite,
        String statut
    ) {}

    public record PerformanceCommercialResponse(
        String matriculeCommercial,
        long totalVendu, long totalRestant, long totalAvarie,
        double tauxVente, String statutSession
    ) {}

    public record InvenduResponse(
        String matriculeCommercial, String codeProduit, String designation,
        long quantiteRestante, LocalDate dateSession
    ) {}

    public record ActionRapideResponse(
        String label, String endpoint, String couleur
    ) {}
}
