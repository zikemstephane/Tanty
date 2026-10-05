package com.NTFOODS.Api_tanty.modules.dg.application.dto.response;

/**
 * Snapshot KPI pilotage DG — agrège les données commerciales du mois courant.
 * Les champs financiers non encore calculables (marge, trésorerie) sont à 0.
 */
public record KpisDgResponse(
    double caTotal,
    double caMoisN1,
    double caMoisN2,
    long ventesCount,
    int commerciauxActifs,
    double objectifCible,
    double objectifRealise,
    double margeGlobale,
    double tresorerie,
    double rentabiliteProductionPct,
    double stockMatieresPremieres,
    double stockConsommables,
    double stockProduitsFinis
) {}
