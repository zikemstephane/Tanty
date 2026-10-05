package com.NTFOODS.Api_tanty.modules.production.application.affectation.dto.response;

/**
 * Performance d'un poste sur une période. Quantités dans l'unité du poste ; le taux compare objectif et réalisé
 * sur les journées saisies. rang est nul pour un poste sans aucune journée saisie.
 */
public record PerformancePosteResponse(
    String codePoste,
    String libelle,
    String unite,
    int joursAffectes,
    int joursSaisis,
    double objectifTotal,
    double realiseTotal,
    double tauxRealisation,
    double effectifMoyen,
    double rendementParPersonne,
    Integer rang
) {}
