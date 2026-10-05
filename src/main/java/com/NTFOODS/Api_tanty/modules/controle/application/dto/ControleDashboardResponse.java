package com.NTFOODS.Api_tanty.modules.controle.application.dto;

import java.util.List;

public record ControleDashboardResponse(
    double tresorerie,
    int ecartsRouges,
    double ventesCredit,
    int margeMoyenne,
    double valoStock,
    double budgetEngage,
    double budgetTotal,
    int budgetDepasse,
    int decaissementsAttente,
    int anomaliesElevees,
    List<MargeProduitResponse> marges,
    List<BudgetResponse> budget,
    List<VarianceResponse> variances
) {
    public record MargeProduitResponse(
        String code, String designation, double prixVente, double coutRevient,
        double marge, int margePct, double margeTotale, int ventesMois
    ) {}

    public record BudgetResponse(
        String poste, double budget, double engage, double realise
    ) {}

    public record VarianceResponse(
        String poste, double standard, double reel, String unite
    ) {}
}
