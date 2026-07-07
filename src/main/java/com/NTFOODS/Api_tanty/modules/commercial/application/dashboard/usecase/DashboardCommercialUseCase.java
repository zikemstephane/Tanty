package com.NTFOODS.Api_tanty.modules.commercial.application.dashboard.usecase;

import com.NTFOODS.Api_tanty.modules.commercial.domain.carburant.aggregate.DemandeCarburant;
import com.NTFOODS.Api_tanty.modules.commercial.domain.carburant.enums.StatutCarburant;
import com.NTFOODS.Api_tanty.modules.commercial.domain.carburant.repository.CarburantRepository;
import com.NTFOODS.Api_tanty.modules.commercial.domain.feuillroute.aggregate.FeuillRoute;
import com.NTFOODS.Api_tanty.modules.commercial.domain.feuillroute.repository.FeuillRouteRepository;
import com.NTFOODS.Api_tanty.modules.commercial.domain.recouvrement.aggregate.Recouvrement;
import com.NTFOODS.Api_tanty.modules.commercial.domain.recouvrement.enums.StatutRecouvrement;
import com.NTFOODS.Api_tanty.modules.commercial.domain.recouvrement.repository.RecouvrementRepository;
import com.NTFOODS.Api_tanty.modules.commercial.domain.vente.aggregate.Vente;
import com.NTFOODS.Api_tanty.modules.commercial.domain.vente.enums.StatutVente;
import com.NTFOODS.Api_tanty.modules.commercial.domain.vente.repository.VenteRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class DashboardCommercialUseCase {

    private final VenteRepository venteRepository;
    private final FeuillRouteRepository feuillRouteRepository;
    private final CarburantRepository carburantRepository;
    private final RecouvrementRepository recouvrementRepository;

    public DashboardCommercialUseCase(VenteRepository venteRepository,
                                       FeuillRouteRepository feuillRouteRepository,
                                       CarburantRepository carburantRepository,
                                       RecouvrementRepository recouvrementRepository) {
        this.venteRepository = venteRepository;
        this.feuillRouteRepository = feuillRouteRepository;
        this.carburantRepository = carburantRepository;
        this.recouvrementRepository = recouvrementRepository;
    }

    public DashboardCommercial executerCommercial(String matriculeCommercial) {
        LocalDate today = LocalDate.now();
        LocalDate debutSemaine = today.minusDays(today.getDayOfWeek().getValue() - 1);
        LocalDate finSemaine = debutSemaine.plusDays(6);

        List<Vente> ventesJour = venteRepository.listerParCommercialEtDate(matriculeCommercial, today);
        List<Vente> ventesSemaine = venteRepository.listerParPeriode(debutSemaine, finSemaine).stream()
            .filter(v -> v.getMatriculeCommercial().equals(matriculeCommercial)).toList();

        double caJour = ventesJour.stream()
            .filter(v -> v.getStatut() == StatutVente.FINALISEE)
            .mapToDouble(Vente::montantTotal).sum();
        double caSemaine = ventesSemaine.stream()
            .filter(v -> v.getStatut() == StatutVente.FINALISEE)
            .mapToDouble(Vente::montantTotal).sum();

        List<FeuillRoute> feuillesJour = feuillRouteRepository.listerParCommercialEtDate(matriculeCommercial, today);
        int arretVises = feuillesJour.stream().mapToInt(FeuillRoute::nombreArretVises).sum();
        int arretNonVises = feuillesJour.stream().mapToInt(FeuillRoute::nombreArretNonVises).sum();
        int totalArrets = arretVises + arretNonVises;
        int tauxVisite = totalArrets > 0 ? (int) ((arretVises * 100.0) / totalArrets) : 0;

        List<DemandeCarburant> carburantSemaine = carburantRepository.listerParCommercial(matriculeCommercial);
        double litresSemaine = carburantSemaine.stream()
            .filter(d -> d.getStatut() == StatutCarburant.VALIDEE)
            .mapToDouble(DemandeCarburant::getLitresDemandes).sum();

        Map<String, Double> topProduits = ventesSemaine.stream()
            .filter(v -> v.getStatut() == StatutVente.FINALISEE)
            .flatMap(v -> v.getLignes().stream())
            .collect(Collectors.groupingBy(
                l -> l.getCodeProduit() + " — " + l.getDesignationProduit(),
                Collectors.summingDouble(l -> l.getPrixUnitaire() * l.getQuantite() * (1 - l.getRemise() / 100.0))))
            .entrySet().stream()
            .sorted(Map.Entry.<String, Double>comparingByValue().reversed())
            .limit(5)
            .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue,
                (a, b) -> a, LinkedHashMap::new));

        return new DashboardCommercial(
            matriculeCommercial, today, caJour, caSemaine,
            ventesJour.size(), ventesSemaine.size(),
            arretVises, arretNonVises, tauxVisite,
            litresSemaine, topProduits,
            feuillesJour.stream().map(FeuillRoute::getNumeroFeuillRoute).toList()
        );
    }

    public DashboardDG executerDG() {
        LocalDate today = LocalDate.now();
        LocalDate debutSemaine = today.minusDays(today.getDayOfWeek().getValue() - 1);
        LocalDate finSemaine = debutSemaine.plusDays(6);

        List<Vente> ventesJour = venteRepository.listerParPeriode(today, today);
        List<Vente> ventesSemaine = venteRepository.listerParPeriode(debutSemaine, finSemaine);

        double caJour = ventesJour.stream()
            .filter(v -> v.getStatut() == StatutVente.FINALISEE)
            .mapToDouble(Vente::montantTotal).sum();
        double caSemaine = ventesSemaine.stream()
            .filter(v -> v.getStatut() == StatutVente.FINALISEE)
            .mapToDouble(Vente::montantTotal).sum();

        List<FeuillRoute> feuillesSemaine = feuillRouteRepository.listerParPeriode(debutSemaine, finSemaine);
        int totalArretsVises = feuillesSemaine.stream().mapToInt(FeuillRoute::nombreArretVises).sum();
        int totalArretsNonVises = feuillesSemaine.stream().mapToInt(FeuillRoute::nombreArretNonVises).sum();
        int totalArrets = totalArretsVises + totalArretsNonVises;
        int tauxVisiteGlobal = totalArrets > 0 ? (int) ((totalArretsVises * 100.0) / totalArrets) : 0;

        List<Recouvrement> enContentieux = recouvrementRepository.listerEnContentieux();
        List<Recouvrement> enCours = recouvrementRepository.listerParStatut(StatutRecouvrement.EN_COURS);
        double totalCreances = enCours.stream().mapToDouble(Recouvrement::montantRestant).sum()
            + enContentieux.stream().mapToDouble(Recouvrement::montantRestant).sum();

        List<DemandeCarburant> carburantSemaine = carburantRepository.listerParPeriode(debutSemaine, finSemaine);
        double litresTotal = carburantSemaine.stream()
            .filter(d -> d.getStatut() == StatutCarburant.VALIDEE)
            .mapToDouble(DemandeCarburant::getLitresDemandes).sum();
        double rendement = litresTotal > 0 ? caSemaine / litresTotal : 0.0;

        Map<String, Double> caParCommercial = ventesSemaine.stream()
            .filter(v -> v.getStatut() == StatutVente.FINALISEE)
            .collect(Collectors.groupingBy(
                Vente::getMatriculeCommercial,
                Collectors.summingDouble(Vente::montantTotal)))
            .entrySet().stream()
            .sorted(Map.Entry.<String, Double>comparingByValue().reversed())
            .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue,
                (a, b) -> a, LinkedHashMap::new));

        return new DashboardDG(
            today, caJour, caSemaine,
            ventesJour.size(), ventesSemaine.size(),
            totalArretsVises, totalArretsNonVises, tauxVisiteGlobal,
            enCours.size(), enContentieux.size(), totalCreances,
            litresTotal, rendement, caParCommercial
        );
    }

    public record DashboardCommercial(
        String matriculeCommercial, LocalDate date,
        double caJour, double caSemaine,
        int nbVentesJour, int nbVentesSemaine,
        int arretVises, int arretNonVises, int tauxVisite,
        double litresCarburantSemaine,
        Map<String, Double> top5Produits,
        List<String> feuillesRouteJour
    ) {}

    public record DashboardDG(
        LocalDate date,
        double caJour, double caSemaine,
        int nbVentesJour, int nbVentesSemaine,
        int totalArretsVises, int totalArretsNonVises, int tauxVisiteGlobal,
        int nbRecouvrementsEnCours, int nbRecouvrementsContentieux, double totalCreances,
        double litresCarburantSemaine, double rendementCAparLitre,
        Map<String, Double> caParCommercial
    ) {}
}
