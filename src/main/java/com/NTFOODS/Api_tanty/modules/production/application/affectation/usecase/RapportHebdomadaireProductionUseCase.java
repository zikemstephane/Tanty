package com.NTFOODS.Api_tanty.modules.production.application.affectation.usecase;

import com.NTFOODS.Api_tanty.modules.production.application.affectation.dto.response.ComparaisonFutsProductionResponse;
import com.NTFOODS.Api_tanty.modules.production.application.affectation.dto.response.RapportHebdomadaireProductionResponse;
import com.NTFOODS.Api_tanty.modules.production.application.affectation.service.PosteProductionService;
import com.NTFOODS.Api_tanty.modules.production.domain.affectation.aggregate.AffectationJournaliere;
import com.NTFOODS.Api_tanty.modules.production.domain.affectation.repository.AffectationJournaliereRepository;
import com.NTFOODS.Api_tanty.modules.production.domain.dosage.aggregate.SessionDosage;
import com.NTFOODS.Api_tanty.modules.production.domain.dosage.repository.SessionDosageRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.temporal.WeekFields;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * Rapport hebdomadaire de production : volumes, fûts, comparaisons journalières et classement.
 * Les fûts et la poudre portent sur toutes les sessions de la période, y compris les jours sans affectation.
 * La production en kg ne retient que les postes dont l'unité est le kg.
 */
@Service
@Transactional(readOnly = true)
public class RapportHebdomadaireProductionUseCase {

    private final AffectationJournaliereRepository affectationRepository;
    private final SessionDosageRepository dosageRepository;
    private final ComparaisonFutsProductionUseCase comparaisonUseCase;
    private final ClassementExecuteursHebdomadaireUseCase classementUseCase;
    private final PosteProductionService posteService;

    public RapportHebdomadaireProductionUseCase(AffectationJournaliereRepository affectationRepository,
                                                 SessionDosageRepository dosageRepository,
                                                 ComparaisonFutsProductionUseCase comparaisonUseCase,
                                                 ClassementExecuteursHebdomadaireUseCase classementUseCase,
                                                 PosteProductionService posteService) {
        this.affectationRepository = affectationRepository;
        this.dosageRepository = dosageRepository;
        this.comparaisonUseCase = comparaisonUseCase;
        this.classementUseCase = classementUseCase;
        this.posteService = posteService;
    }

    public RapportHebdomadaireProductionResponse executer(LocalDate debut, LocalDate fin) {
        List<SessionDosage> sessions = dosageRepository.listerParPeriode(debut, fin);
        int nbFutsProduits = sessions.stream().mapToInt(SessionDosage::getNbFutsProduits).sum();
        int nbFutsNets = sessions.stream().mapToInt(SessionDosage::getNbFutsNets).sum();
        int nbFutsCasses = sessions.stream().mapToInt(SessionDosage::getNbFutsCasses).sum();
        double poidsTotalPoudreKg = nbFutsNets * ComparaisonFutsProductionUseCase.KG_PAR_FUT;

        List<AffectationJournaliere> affectations = affectationRepository.listerParPeriode(debut, fin);
        Map<String, String> unites = posteService.listerTous().stream()
            .collect(Collectors.toMap(p -> p.getCode(), p -> p.getUnite(), (a, b) -> a));
        double productionTotaleKg = affectations.stream()
            .flatMap(a -> a.getSaisies().stream())
            .filter(s -> "KG".equalsIgnoreCase(unites.get(s.getPoste())))
            .mapToDouble(s -> s.getQuantiteRealisee())
            .sum();

        // Un jour affecté sans session de dosage est le cas courant, pas une erreur : on le vérifie avant d'appeler
        // comparaisonUseCase.executer(), qui lève sinon une exception au travers d'une frontière @Transactional
        // imbriquée — celle-ci marque la transaction de ce rapport "rollback-only" même si l'appelant l'attrape,
        // et le GET se termine en 500 (UnexpectedRollbackException) à la place de son contenu.
        List<ComparaisonFutsProductionResponse> comparaisons = new ArrayList<>();
        for (AffectationJournaliere a : affectations) {
            if (!comparaisonUseCase.sessionsDuJour(a.getReferencePPH(), a.getDate()).isEmpty()) {
                comparaisons.add(comparaisonUseCase.executer(a.getReferencePPH(), a.getDate()));
            }
        }

        String periode = String.format("Semaine %s — %s au %s",
            debut.get(WeekFields.ISO.weekOfYear()),
            debut.format(DateTimeFormatter.ofPattern("dd/MM/yyyy")),
            fin.format(DateTimeFormatter.ofPattern("dd/MM/yyyy")));

        return new RapportHebdomadaireProductionResponse(
            debut, fin, periode, nbFutsProduits, nbFutsNets, nbFutsCasses, poidsTotalPoudreKg, productionTotaleKg,
            sessions.size(), affectations.size(),
            classementUseCase.executer(debut, fin),
            comparaisons);
    }
}
