package com.NTFOODS.Api_tanty.modules.production.application.affectation.dto.response;

/**
 * Classement hebdomadaire d'un exécutant (agent de production) affecté aux postes.
 */
public record ClassementExecuteurResponse(
    int rang,
    String matriculeEmploye,
    String nomEmploye,
    double quantiteTotaleRealisee,
    String unite,
    double tauxRealisationMoyen,
    int nbJoursAffectes
) {}
