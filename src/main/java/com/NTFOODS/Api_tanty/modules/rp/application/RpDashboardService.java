package com.NTFOODS.Api_tanty.modules.rp.application;

import com.NTFOODS.Api_tanty.modules.comptabilite.domain.prime.aggregate.PrimeCommerciale;
import com.NTFOODS.Api_tanty.modules.comptabilite.domain.prime.repository.PrimeRepository;
import com.NTFOODS.Api_tanty.modules.dg.application.rapport.TauxOccupationMarcheService;
import com.NTFOODS.Api_tanty.modules.dg.application.rapport.TauxOccupationSecteurResponse;
import com.NTFOODS.Api_tanty.modules.rp.application.dto.RpDashboardResponse;
import com.NTFOODS.Api_tanty.modules.rp.application.dto.RpDashboardResponse.*;
import com.NTFOODS.Api_tanty.modules.rp.infrastructure.persistence.jpa.GrandCompteJpaRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional(readOnly = true)
public class RpDashboardService {

    private final PrimeRepository primeRepository;
    private final GrandCompteJpaRepository grandCompteJpaRepository;
    private final TauxOccupationMarcheService tauxOccupationMarcheService;

    public RpDashboardService(PrimeRepository primeRepository,
                               GrandCompteJpaRepository grandCompteJpaRepository,
                               TauxOccupationMarcheService tauxOccupationMarcheService) {
        this.primeRepository = primeRepository;
        this.grandCompteJpaRepository = grandCompteJpaRepository;
        this.tauxOccupationMarcheService = tauxOccupationMarcheService;
    }

    public RpDashboardResponse dashboard() {
        var primes = primes();
        int nbEligibles = (int) primes.stream().filter(PrimeResponse::eligible).count();
        double totalPrimes = primes.stream().filter(PrimeResponse::eligible).mapToDouble(PrimeResponse::montant).sum();

        var occupation = occupation();
        int occupationMoyenne = occupation.isEmpty() ? 0 : (int) Math.round(
            occupation.stream().mapToInt(o -> o.repertoriees() > 0 ? (o.livrees() * 100 / o.repertoriees()) : 0).average().orElse(0));

        var grandsComptes = grandsComptes();
        int grandsComptesActifs = (int) grandsComptes.stream().filter(g -> g.statut().equals("ACTIF")).count();
        int relancesDues = (int) grandsComptes.stream().filter(g -> g.statut().equals("RELANCE_DUE")).count();
        double caGrandsComptes = grandsComptes.stream().mapToDouble(GrandCompteResponse::caMensuel).sum();

        return new RpDashboardResponse(
            nbEligibles, totalPrimes, occupationMoyenne,
            grandsComptesActifs, relancesDues, caGrandsComptes,
            primes, performanceGammes(), occupation, grandsComptes
        );
    }

    public List<PrimeResponse> primes() {
        LocalDate semaineDebut = LocalDate.now().withDayOfMonth(1);
        var primes = primeRepository.listerParSemaine(semaineDebut);
        if (primes.isEmpty()) {
            return List.of();
        }
        return primes.stream()
            .map(p -> new PrimeResponse(
                p.getMatriculeCommercial(), p.getMatriculeCommercial(),
                (int) Math.round(p.getTauxRealisationGlobalPourcent()),
                (int) p.getTauxParGammePourcent().values().stream().filter(v -> v >= 75.0).count(),
                p.getTauxParGammePourcent().size(),
                p.isEligible(), p.getMontantPrimeFCFA()))
            .sorted((a, b) -> Integer.compare(b.tauxGlobal(), a.tauxGlobal()))
            .collect(Collectors.toList());
    }

    public List<PerformanceGammeResponse> performanceGammes() {
        return List.of();
    }

    public List<OccupationMarcheResponse> occupation() {
        List<TauxOccupationSecteurResponse> secteurs = tauxOccupationMarcheService.calculer();
        return secteurs.stream()
            .map(s -> new OccupationMarcheResponse(
                s.secteur(), "",
                (int) s.clientsLivres(), (int) s.boutiquesRecensees(),
                s.tauxOccupationPourcent() >= 70 ? "SATURÉ" : s.tauxOccupationPourcent() >= 45 ? "MOYEN" : "ÉLEVÉ"))
            .collect(Collectors.toList());
    }

    public List<GrandCompteResponse> grandsComptes() {
        return grandCompteJpaRepository.findAll().stream()
            .map(g -> new GrandCompteResponse(
                g.getId(), g.getNom(), g.getType(), g.getContact(), g.getTelephone(),
                g.getCaMensuel(), g.getDerniereCommande(), g.getProchaineRelance(), g.getStatut()))
            .collect(Collectors.toList());
    }

}
