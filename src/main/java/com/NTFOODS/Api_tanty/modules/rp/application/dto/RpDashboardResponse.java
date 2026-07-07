package com.NTFOODS.Api_tanty.modules.rp.application.dto;

import java.time.LocalDate;
import java.util.List;

public record RpDashboardResponse(
    int nbEligibles,
    double totalPrimes,
    int occupationMoyenne,
    int grandsComptesActifs,
    int relancesDues,
    double caGrandsComptes,
    List<PrimeResponse> primes,
    List<PerformanceGammeResponse> gammes,
    List<OccupationMarcheResponse> occupation,
    List<GrandCompteResponse> grandsComptes,
    List<CampagneResponse> campagnes
) {
    public record PrimeResponse(
        String commercialId, String nom, int tauxGlobal,
        int gammesValidees, int gammesTotal, boolean eligible, double montant
    ) {}

    public record PerformanceGammeResponse(
        String gamme, double ca, double objectif, int marge, String stock
    ) {}

    public record OccupationMarcheResponse(
        String zone, String commercialId, int livrees, int repertoriees, String potentiel
    ) {}

    public record GrandCompteResponse(
        Long id, String nom, String type, String contact, String telephone,
        double caMensuel, LocalDate derniereCommande, LocalDate prochaineRelance, String statut
    ) {}

    public record CampagneResponse(
        Long id, String titre, String cible, String canal, String statut,
        LocalDate datePublication, int portee
    ) {}
}
