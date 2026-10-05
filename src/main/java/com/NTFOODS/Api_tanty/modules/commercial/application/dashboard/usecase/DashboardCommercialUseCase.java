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
import com.NTFOODS.Api_tanty.modules.comptabilite.domain.objectif.aggregate.ObjectifCommercial;
import com.NTFOODS.Api_tanty.modules.comptabilite.domain.objectif.repository.ObjectifCommercialRepository;
import com.NTFOODS.Api_tanty.modules.stock.domain.catalogue.model.Produit;
import com.NTFOODS.Api_tanty.modules.stock.domain.catalogue.repository.ProduitRepository;
import com.NTFOODS.Api_tanty.modules.stock.domain.catalogue.valueobject.CodeProduit;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class DashboardCommercialUseCase {

    private final VenteRepository venteRepository;
    private final FeuillRouteRepository feuillRouteRepository;
    private final CarburantRepository carburantRepository;
    private final RecouvrementRepository recouvrementRepository;
    private final ObjectifCommercialRepository objectifRepository;
    private final ProduitRepository produitRepository;

    public DashboardCommercialUseCase(VenteRepository venteRepository,
                                       FeuillRouteRepository feuillRouteRepository,
                                       CarburantRepository carburantRepository,
                                       RecouvrementRepository recouvrementRepository,
                                       ObjectifCommercialRepository objectifRepository,
                                       ProduitRepository produitRepository) {
        this.venteRepository = venteRepository;
        this.feuillRouteRepository = feuillRouteRepository;
        this.carburantRepository = carburantRepository;
        this.recouvrementRepository = recouvrementRepository;
        this.objectifRepository = objectifRepository;
        this.produitRepository = produitRepository;
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
        int arretVises = feuillesJour.stream().mapToInt(FeuillRoute::nombreVisites).sum();
        int arretNonVises = feuillesJour.stream().mapToInt(FeuillRoute::nombreNonVisites).sum();
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
                Collectors.summingDouble(l -> l.montantHT())))
            .entrySet().stream()
            .sorted(Map.Entry.<String, Double>comparingByValue().reversed())
            .limit(5)
            .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue,
                (a, b) -> a, LinkedHashMap::new));

        // Ventilation jour par jour du taux de réalisation sur la semaine
        // commerciale. L'objectif journalier dérive de l'objectif hebdomadaire
        // défini par le DG (objectifGlobalFCFA / nb de jours de la semaine).
        List<RealisationJour> realisationHebdo = calculerRealisationHebdo(
            matriculeCommercial, debutSemaine, finSemaine);

        // CA par gamme — agrégation des lignes de ventes de la semaine par gamme produit
        Map<String, Double> caParGamme = calculerCaParGamme(ventesSemaine);

        return new DashboardCommercial(
            matriculeCommercial, today, caJour, caSemaine,
            ventesJour.size(), ventesSemaine.size(),
            arretVises, arretNonVises, tauxVisite,
            litresSemaine, topProduits,
            feuillesJour.stream().map(FeuillRoute::getNumeroFeuillRoute).toList(),
            realisationHebdo, caParGamme
        );
    }

    /**
     * Construit le CA réalisé et le taux de réalisation pour chaque jour de la
     * semaine commerciale. Le taux est relatif à l'objectif journalier déduit de
     * l'objectif hebdomadaire du commercial (0 % si aucun objectif défini).
     */
    private List<RealisationJour> calculerRealisationHebdo(String matriculeCommercial,
                                                           LocalDate debutSemaine,
                                                           LocalDate finSemaine) {
        LocalDate semDebut = debutSemaine;
        LocalDate semFin = finSemaine;
        double objectifJour = 0.0;

        Optional<ObjectifCommercial> objOpt = objectifRepository.trouver(matriculeCommercial, debutSemaine);
        if (objOpt.isEmpty()) {
            // Repli : dernier objectif connu du commercial (semaine précédente).
            List<ObjectifCommercial> objs = objectifRepository.listerParCommercial(matriculeCommercial);
            if (!objs.isEmpty()) objOpt = Optional.of(objs.get(objs.size() - 1));
        }
        if (objOpt.isPresent()) {
            ObjectifCommercial obj = objOpt.get();
            semDebut = obj.getSemaineDebut();
            semFin = obj.getSemaineFin();
            long nbJours = ChronoUnit.DAYS.between(semDebut, semFin) + 1;
            objectifJour = nbJours > 0 ? obj.getObjectifGlobalFCFA() / nbJours : 0.0;
        }

        long nbJours = ChronoUnit.DAYS.between(semDebut, semFin) + 1;
        List<RealisationJour> jours = new ArrayList<>();
        for (int i = 0; i < nbJours; i++) {
            LocalDate jour = semDebut.plusDays(i);
            double caJourVal = venteRepository.listerParCommercialEtDate(matriculeCommercial, jour).stream()
                .filter(v -> v.getStatut() == StatutVente.FINALISEE)
                .mapToDouble(Vente::montantTotal).sum();
            int tauxPct = objectifJour > 0 ? (int) Math.round(caJourVal / objectifJour * 100.0) : 0;
            jours.add(new RealisationJour(jour, caJourVal, objectifJour, tauxPct));
        }
        return jours;
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
        int totalArretsVises = feuillesSemaine.stream().mapToInt(FeuillRoute::nombreVisites).sum();
        int totalArretsNonVises = feuillesSemaine.stream().mapToInt(FeuillRoute::nombreNonVisites).sum();
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
        List<String> feuillesRouteJour,
        List<RealisationJour> realisationHebdo,
        Map<String, Double> caParGamme
    ) {}

    /** Réalisation d'une journée de la semaine commerciale (CA + taux vs objectif). */
    public record RealisationJour(
        LocalDate date,
        double ca,
        double objectifJour,
        int tauxPct
    ) {}

    private Map<String, Double> calculerCaParGamme(List<Vente> ventesSemaine) {
        return ventesSemaine.stream()
            .filter(v -> v.getStatut() == StatutVente.FINALISEE)
            .flatMap(v -> v.getLignes().stream())
            .collect(Collectors.groupingBy(
                l -> produitRepository.trouverParCode(CodeProduit.de(l.getCodeProduit()))
                    .map(Produit::getGamme)
                    .orElse("AUTRES"),
                Collectors.summingDouble(l -> l.montantHT())))
            .entrySet().stream()
            .sorted(Map.Entry.<String, Double>comparingByValue().reversed())
            .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue,
                (a, b) -> a, LinkedHashMap::new));
    }

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
