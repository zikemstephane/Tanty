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
    public record LigneResponse(
        String codeProduit,
        String nomProduit,
        String categorieMetier,
        int objectifSemaine,
        int productionRealisee,
        double quantiteAttendueKg,
        int quantiteAttendueUnites
    ) {}

    public record BesoinMatierePremiereResponse(
        String matierePremiere,
        double quantiteKg
    ) {}

    public record BesoinEmballageResponse(
        String type,
        long quantite
    ) {}

    public record LigneDetailResponse(
        String codeProduit,
        String nomProduit,
        String categorieMetier,
        int quantiteCartons,
        int nombreUnites,
        double poidsUnitaireGrammes,
        LocalDate dateJour,
        int quantiteJour,
        List<BesoinMatierePremiereResponse> besoinsMatieresPremiere,
        boolean formuleManquante,
        List<BesoinEmballageResponse> besoinsEmballage,
        boolean compositionEmballageManquante,
        double totalMatieresPremieresKg,
        double futsPrevus,
        double poidsTotalGrammes,
        double quantiteAttendueKg,
        int quantiteAttendueUnites
    ) {}

    public record RepartitionResponse(
        LocalDate dateJour,
        List<BesoinMatierePremiereResponse> besoinsMatieresPremiere,
        List<BesoinEmballageResponse> besoinsEmballage,
        double totalMatieresPremieresKg,
        double futsPrevus
    ) {}
}
