package com.NTFOODS.Api_tanty.modules.commercial.application.rapport.usecase;

import com.NTFOODS.Api_tanty.modules.commercial.domain.feuillroute.aggregate.FeuillRoute;
import com.NTFOODS.Api_tanty.modules.commercial.domain.feuillroute.repository.FeuillRouteRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.*;

/**
 * Rapport de tournée — taux de visite par commercial et taux global.
 *
 * <p>Les compteurs qu'il portait sur les clients potentiels ont été retirés :
 * seule la notion de client subsiste.</p>
 */
@Service
public class GenererRapportTourneeUseCase {

    private final FeuillRouteRepository feuillRouteRepository;

    public GenererRapportTourneeUseCase(FeuillRouteRepository feuillRouteRepository) {
        this.feuillRouteRepository = feuillRouteRepository;
    }

    public RapportTournee executer(LocalDate debut, LocalDate fin) {
        List<FeuillRoute> feuilles = feuillRouteRepository.listerParPeriode(debut, fin);

        Map<String, LigneTournee> parCommercial = new LinkedHashMap<>();
        for (FeuillRoute fr : feuilles) {
            String matricule = fr.getMatriculeCommercial();
            LigneTournee ligne = parCommercial.computeIfAbsent(matricule,
                k -> new LigneTournee(matricule, 0, 0, 0, 0, 0));
            ligne.nbFeuilles++;
            ligne.totalArrets += fr.getArrets().size();
            ligne.arretsVises += fr.nombreVisites();
            ligne.arretsNonVises += fr.nombreNonVisites();
        }
        parCommercial.values().forEach(l -> {
            l.tauxVisite = l.totalArrets > 0 ? (int) ((l.arretsVises * 100.0) / l.totalArrets) : 0;
        });

        int totalArrets = parCommercial.values().stream().mapToInt(l -> l.totalArrets).sum();
        int totalVises = parCommercial.values().stream().mapToInt(l -> l.arretsVises).sum();
        int tauxGlobal = totalArrets > 0 ? (int) ((totalVises * 100.0) / totalArrets) : 0;

        return new RapportTournee(
            debut, fin,
            parCommercial.values().stream().sorted(
                Comparator.comparingInt(LigneTournee::tauxVisite).reversed()).toList(),
            tauxGlobal
        );
    }

    public static class LigneTournee {
        public final String matriculeCommercial;
        public int nbFeuilles;
        public int totalArrets;
        public int arretsVises;
        public int arretsNonVises;
        public int tauxVisite;

        public LigneTournee(String matriculeCommercial, int nbFeuilles, int totalArrets,
                            int arretsVises, int arretsNonVises, int tauxVisite) {
            this.matriculeCommercial = matriculeCommercial;
            this.nbFeuilles = nbFeuilles;
            this.totalArrets = totalArrets;
            this.arretsVises = arretsVises;
            this.arretsNonVises = arretsNonVises;
            this.tauxVisite = tauxVisite;
        }

        public int tauxVisite() { return tauxVisite; }
    }

    public record RapportTournee(
        LocalDate debut, LocalDate fin,
        List<LigneTournee> detailsParCommercial,
        int tauxVisiteGlobal
    ) {}
}
