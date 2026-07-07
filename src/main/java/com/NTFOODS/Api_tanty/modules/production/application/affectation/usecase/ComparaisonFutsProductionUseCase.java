package com.NTFOODS.Api_tanty.modules.production.application.affectation.usecase;

import com.NTFOODS.Api_tanty.modules.production.application.affectation.dto.response.ComparaisonFutsProductionResponse;
import com.NTFOODS.Api_tanty.modules.production.domain.affectation.aggregate.AffectationJournaliere;
import com.NTFOODS.Api_tanty.modules.production.domain.affectation.enums.CodePoste;
import com.NTFOODS.Api_tanty.modules.production.domain.affectation.repository.AffectationJournaliereRepository;
import com.NTFOODS.Api_tanty.modules.production.domain.dosage.aggregate.SessionDosage;
import com.NTFOODS.Api_tanty.modules.production.domain.dosage.repository.SessionDosageRepository;
import com.NTFOODS.Api_tanty.shared.kernel.exception.EntiteNonTrouveeException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;

/**
 * Compare les fûts nets dosés avec la production réelle en aval (conditionnement).
 */
@Service
@Transactional(readOnly = true)
public class ComparaisonFutsProductionUseCase {

    private final SessionDosageRepository dosageRepository;
    private final AffectationJournaliereRepository affectationRepository;

    public ComparaisonFutsProductionUseCase(SessionDosageRepository dosageRepository,
                                             AffectationJournaliereRepository affectationRepository) {
        this.dosageRepository = dosageRepository;
        this.affectationRepository = affectationRepository;
    }

    public ComparaisonFutsProductionResponse executer(String referencePPH, LocalDate date) {
        SessionDosage dosage = dosageRepository.trouverParPPHEtDate(referencePPH, date)
            .orElseThrow(() -> EntiteNonTrouveeException.de("SessionDosage", referencePPH + " / " + date));

        AffectationJournaliere affectation = affectationRepository.trouverParPPHEtDate(referencePPH, date)
            .orElse(null);

        int nbFutsNets = dosage.getNbFutsNets();
        double poidsTotalPoudreKg = nbFutsNets * 50.0;
        double productionReelleKg = calculerProductionAval(affectation);
        double ecartKg = productionReelleKg - poidsTotalPoudreKg;
        double ecartPct = poidsTotalPoudreKg > 0 ? (ecartKg / poidsTotalPoudreKg) * 100.0 : 0.0;

        return new ComparaisonFutsProductionResponse(
            referencePPH, date, nbFutsNets, poidsTotalPoudreKg, productionReelleKg,
            ecartKg, ecartPct, "kg");
    }

    private double calculerProductionAval(AffectationJournaliere affectation) {
        if (affectation == null) return 0.0;
        return affectation.getSaisies().stream()
            .filter(s -> estPosteConditionnement(s.getPoste()))
            .mapToDouble(s -> s.getQuantiteRealisee() * 50.0)
            .sum();
    }

    private boolean estPosteConditionnement(CodePoste poste) {
        return poste == CodePoste.CONDITIONNEMENT_MINI_GRIGNOTER
            || poste == CodePoste.CONDITIONNEMENT_GRIGNOTER
            || poste == CodePoste.CONDITIONNEMENT_SCEAUX_2L
            || poste == CodePoste.CONDITIONNEMENT_RASS
            || poste == CodePoste.CONDITIONNEMENT_PRESTIGE_CHAP;
    }
}
