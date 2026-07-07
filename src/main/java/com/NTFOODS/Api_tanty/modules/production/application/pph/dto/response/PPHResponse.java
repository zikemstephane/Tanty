package com.NTFOODS.Api_tanty.modules.production.application.pph.dto.response;

import java.time.LocalDate;
import java.util.List;

public record PPHResponse(
    Long id,
    String referencePPH,
    String referenceBC,
    String semaine,
    LocalDate dateDebut,
    LocalDate dateFin,
    LocalDate dateSoumission,
    String matriculeChefProduction,
    String statut,
    List<LigneResponse> lignes,
    List<LigneDetailResponse> lignesDetaillees,
    List<RepartitionResponse> repartitionJours
) {
    public record LigneResponse(String codeProduit, String nomProduit, int objectifSemaine, int productionRealisee) {}

    public record LigneDetailResponse(
        String codeProduit,
        String nomProduit,
        String categorieMetier,
        int quantiteCartons,
        int nombreUnites,
        double poidsUnitaireGrammes,
        String recette,
        LocalDate dateJour,
        int quantiteJour,
        double poudreMaisKg,
        double poudreSojaKg,
        double poudreArachideKg,
        int futsPrevus,
        int sachetsPrevus,
        int couverclesPrevus,
        int scellesPrevus,
        double poidsTotalGrammes
    ) {}

    public record RepartitionResponse(
        LocalDate dateJour,
        double poudreMaisKg,
        double poudreSojaKg,
        double poudreArachideKg,
        int futsPrevus,
        int sachetsPrevus,
        int couverclesPrevus,
        int scellesPrevus
    ) {}
}
