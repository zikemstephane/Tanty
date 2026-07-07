package com.NTFOODS.Api_tanty.modules.commercial.application.rapport.usecase;

import com.NTFOODS.Api_tanty.modules.commercial.domain.feuillroute.aggregate.FeuillRoute;
import com.NTFOODS.Api_tanty.modules.commercial.domain.feuillroute.repository.FeuillRouteRepository;
import com.NTFOODS.Api_tanty.modules.commercial.domain.prospect.aggregate.Prospect;
import com.NTFOODS.Api_tanty.modules.commercial.domain.prospect.enums.StatutProspect;
import com.NTFOODS.Api_tanty.modules.commercial.domain.prospect.repository.ProspectRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class GenererRapportTourneeUseCase {

    private final FeuillRouteRepository feuillRouteRepository;
    private final ProspectRepository prospectRepository;

    public GenererRapportTourneeUseCase(FeuillRouteRepository feuillRouteRepository,
                                          ProspectRepository prospectRepository) {
        this.feuillRouteRepository = feuillRouteRepository;
        this.prospectRepository = prospectRepository;
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
            ligne.arretsVises += fr.nombreArretVises();
            ligne.arretsNonVises += fr.nombreArretNonVises();
        }
        parCommercial.values().forEach(l -> {
            l.tauxVisite = l.totalArrets > 0 ? (int) ((l.arretsVises * 100.0) / l.totalArrets) : 0;
        });

        List<Prospect> prospects = prospectRepository.listerTous();
        int prospectNouveaux = (int) prospects.stream()
            .filter(p -> p.getStatut() == StatutProspect.NOUVEAU).count();
        int prospectConvertis = (int) prospects.stream()
            .filter(p -> p.getStatut() == StatutProspect.CONVERTI).count();
        int prospectNonInteresses = (int) prospects.stream()
            .filter(p -> p.getStatut() == StatutProspect.NON_INTERESSE).count();
        int prospectArchives = (int) prospects.stream()
            .filter(p -> p.getStatut() == StatutProspect.ARCHIVE).count();

        int totalArrets = parCommercial.values().stream().mapToInt(l -> l.totalArrets).sum();
        int totalVises = parCommercial.values().stream().mapToInt(l -> l.arretsVises).sum();
        int tauxGlobal = totalArrets > 0 ? (int) ((totalVises * 100.0) / totalArrets) : 0;

        return new RapportTournee(
            debut, fin,
            parCommercial.values().stream().sorted(
                Comparator.comparingInt(LigneTournee::tauxVisite).reversed()).toList(),
            tauxGlobal,
            prospectNouveaux, prospectConvertis, prospectNonInteresses, prospectArchives
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
        int tauxVisiteGlobal,
        int prospectNouveaux,
        int prospectConvertis,
        int prospectNonInteresses,
        int prospectArchives
    ) {}
}
