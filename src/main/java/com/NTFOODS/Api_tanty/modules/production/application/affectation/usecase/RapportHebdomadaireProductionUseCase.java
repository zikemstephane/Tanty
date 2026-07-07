package com.NTFOODS.Api_tanty.modules.production.application.affectation.usecase;

import com.NTFOODS.Api_tanty.modules.production.application.affectation.dto.response.ComparaisonFutsProductionResponse;
import com.NTFOODS.Api_tanty.modules.production.application.affectation.dto.response.RapportHebdomadaireProductionResponse;
import com.NTFOODS.Api_tanty.modules.production.domain.affectation.aggregate.AffectationJournaliere;
import com.NTFOODS.Api_tanty.modules.production.domain.affectation.enums.CodePoste;
import com.NTFOODS.Api_tanty.modules.production.domain.affectation.repository.AffectationJournaliereRepository;
import com.NTFOODS.Api_tanty.modules.production.domain.dosage.repository.SessionDosageRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.temporal.WeekFields;
import java.util.ArrayList;
import java.util.List;

/**
 * Rapport hebdomadaire de production : volumes, fûts, comparaisons journalières et classement.
 */
@Service
@Transactional(readOnly = true)
public class RapportHebdomadaireProductionUseCase {

    private final AffectationJournaliereRepository affectationRepository;
    private final SessionDosageRepository dosageRepository;
    private final ComparaisonFutsProductionUseCase comparaisonUseCase;
    private final ClassementExecuteursHebdomadaireUseCase classementUseCase;

    public RapportHebdomadaireProductionUseCase(AffectationJournaliereRepository affectationRepository,
                                                 SessionDosageRepository dosageRepository,
                                                 ComparaisonFutsProductionUseCase comparaisonUseCase,
                                                 ClassementExecuteursHebdomadaireUseCase classementUseCase) {
        this.affectationRepository = affectationRepository;
        this.dosageRepository = dosageRepository;
        this.comparaisonUseCase = comparaisonUseCase;
        this.classementUseCase = classementUseCase;
    }

    public RapportHebdomadaireProductionResponse executer(LocalDate debut, LocalDate fin) {
        List<AffectationJournaliere> affectations = affectationRepository.listerParPeriode(debut, fin);

        int nbFutsNets = 0;
        double poidsTotalPoudreKg = 0.0;
        double productionTotaleKg = 0.0;
        int nbJoursAffectes = affectations.size();

        List<ComparaisonFutsProductionResponse> comparaisons = new ArrayList<>();
        for (AffectationJournaliere a : affectations) {
            var dosageOpt = dosageRepository.trouverParPPHEtDate(a.getReferencePPH(), a.getDate());
            if (dosageOpt.isPresent()) {
                var s = dosageOpt.get();
                nbFutsNets += s.getNbFutsNets();
                poidsTotalPoudreKg += s.getNbFutsNets() * 50.0;
            }
            productionTotaleKg += a.getSaisies().stream()
                .filter(s -> estPosteProductionKg(s.getPoste()))
                .mapToDouble(s -> s.getQuantiteRealisee())
                .sum();
            try {
                comparaisons.add(comparaisonUseCase.executer(a.getReferencePPH(), a.getDate()));
            } catch (Exception ignored) {}
        }

        int nbSessionsDosage = (int) affectations.stream()
            .filter(a -> dosageRepository.trouverParPPHEtDate(a.getReferencePPH(), a.getDate()).isPresent())
            .count();

        String periode = String.format("Semaine %s — %s au %s",
            debut.get(WeekFields.ISO.weekOfYear()),
            debut.format(DateTimeFormatter.ofPattern("dd/MM/yyyy")),
            fin.format(DateTimeFormatter.ofPattern("dd/MM/yyyy")));

        return new RapportHebdomadaireProductionResponse(
            debut, fin, periode, nbFutsNets, poidsTotalPoudreKg, productionTotaleKg,
            nbSessionsDosage, nbJoursAffectes,
            classementUseCase.executer(debut, fin),
            comparaisons);
    }

    private boolean estPosteProductionKg(CodePoste poste) {
        return poste == CodePoste.CARAMEL || poste == CodePoste.CROQUETTE
            || poste == CodePoste.ARACHIDE_ENROBEE || poste == CodePoste.CHIPS_PLANTAIN;
    }
}
