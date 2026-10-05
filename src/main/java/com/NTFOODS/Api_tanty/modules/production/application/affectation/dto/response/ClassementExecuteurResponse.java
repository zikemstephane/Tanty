package com.NTFOODS.Api_tanty.modules.production.application.affectation.dto.response;

import java.util.List;

/**
 * Classement d'un exécutant (agent de production) sur une période.
 *
 * tauxRealisationMoyen : moyenne des taux individuels (quantité ÷ objectif individuel × 100), sans unité,
 * donc comparable entre postes. C'est le critère du rang.
 * quantitesParUnite : quantités produites, une par unité ; elles ne sont jamais additionnées entre unités.
 * quantiteTotaleRealisee et unite ne portent que sur l'unité la plus fréquente de l'exécutant.
 */
public record ClassementExecuteurResponse(
    int rang,
    String matriculeEmploye,
    String nomEmploye,
    double quantiteTotaleRealisee,
    String unite,
    double tauxRealisationMoyen,
    int nbJoursAffectes,
    List<QuantiteParUnite> quantitesParUnite
) {
    public record QuantiteParUnite(String unite, double quantite) {}
}
