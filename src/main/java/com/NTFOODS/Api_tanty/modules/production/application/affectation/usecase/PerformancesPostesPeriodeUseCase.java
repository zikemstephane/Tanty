package com.NTFOODS.Api_tanty.modules.production.application.affectation.usecase;

import com.NTFOODS.Api_tanty.modules.production.application.affectation.dto.response.PerformancePosteResponse;
import com.NTFOODS.Api_tanty.modules.production.domain.affectation.entity.LigneAffectationPoste;
import com.NTFOODS.Api_tanty.modules.production.domain.affectation.entity.PosteProduction;
import com.NTFOODS.Api_tanty.modules.production.domain.affectation.entity.SaisieRealisationPoste;
import com.NTFOODS.Api_tanty.modules.production.domain.affectation.repository.AffectationJournaliereRepository;
import com.NTFOODS.Api_tanty.modules.production.application.affectation.service.PosteProductionService;
import com.NTFOODS.Api_tanty.modules.production.domain.affectation.service.PerformancesPostes;
import com.NTFOODS.Api_tanty.modules.production.domain.affectation.service.PerformancesPostes.Observation;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * Performances des postes sur une période (jour, semaine, mois ou intervalle libre), à partir des affectations
 * de la période et de leurs réalisations.
 */
@Service
@Transactional(readOnly = true)
public class PerformancesPostesPeriodeUseCase {

    static final int JOURS_MAX = 366;

    private final AffectationJournaliereRepository repository;
    private final PosteProductionService posteService;

    public PerformancesPostesPeriodeUseCase(AffectationJournaliereRepository repository,
                                            PosteProductionService posteService) {
        this.repository = repository;
        this.posteService = posteService;
    }

    public List<PerformancePosteResponse> executer(LocalDate debut, LocalDate fin) {
        if (fin.isBefore(debut)) {
            throw new IllegalArgumentException("La date de fin précède la date de début.");
        }
        if (ChronoUnit.DAYS.between(debut, fin) + 1 > JOURS_MAX) {
            throw new IllegalArgumentException("La période ne peut pas dépasser " + JOURS_MAX + " jours.");
        }

        List<Observation> observations = new ArrayList<>();
        repository.listerParPeriode(debut, fin).forEach(a -> {
            for (LigneAffectationPoste l : a.getLignes()) {
                Optional<SaisieRealisationPoste> saisie = a.getSaisies().stream()
                    .filter(s -> s.getPoste().equals(l.getPoste()))
                    .findFirst();
                observations.add(saisie
                    .map(s -> new Observation(l.getPoste(), true, s.getQuantiteRealisee(), s.getObjectifAjuste(), l.getEffectifReel()))
                    .orElseGet(() -> new Observation(l.getPoste(), false, 0, 0, 0)));
            }
        });

        Map<String, PosteProduction> postes = posteService.listerTous().stream()
            .collect(Collectors.toMap(PosteProduction::getCode, p -> p, (a, b) -> a));

        return PerformancesPostes.calculer(observations).stream()
            .map(p -> {
                PosteProduction ref = postes.get(p.codePoste());
                return new PerformancePosteResponse(
                    p.codePoste(),
                    ref != null ? ref.getLibelle() : p.codePoste(),
                    ref != null ? ref.getUnite() : "",
                    p.joursAffectes(), p.joursSaisis(), p.objectifTotal(), p.realiseTotal(),
                    p.tauxRealisation(), p.effectifMoyen(), p.rendementParPersonne(), p.rang());
            })
            .toList();
    }
}
