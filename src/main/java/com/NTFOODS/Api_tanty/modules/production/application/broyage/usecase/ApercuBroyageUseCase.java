package com.NTFOODS.Api_tanty.modules.production.application.broyage.usecase;

import com.NTFOODS.Api_tanty.modules.production.application.broyage.dto.response.ApercuBroyageResponse;
import com.NTFOODS.Api_tanty.modules.production.domain.broyage.repository.SessionBroyageRepository;
import com.NTFOODS.Api_tanty.modules.production.domain.broyage.service.ApercuBroyage;
import com.NTFOODS.Api_tanty.modules.production.domain.pph.enums.StatutPPH;
import com.NTFOODS.Api_tanty.modules.production.domain.pph.repository.PPHRepository;
import com.NTFOODS.Api_tanty.shared.kernel.exception.RegleMetierException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;

/** Vue d'ensemble de l'atelier de broyage sur une période (lecture seule). */
@Service
@Transactional(readOnly = true)
public class ApercuBroyageUseCase {

    private final SessionBroyageRepository sessionRepository;
    private final PPHRepository pphRepository;

    public ApercuBroyageUseCase(SessionBroyageRepository sessionRepository, PPHRepository pphRepository) {
        this.sessionRepository = sessionRepository;
        this.pphRepository = pphRepository;
    }

    public ApercuBroyageResponse executer(LocalDate debut, LocalDate fin) {
        try {
            ApercuBroyage.verifierPeriode(debut, fin);
        } catch (IllegalArgumentException e) {
            throw new RegleMetierException(e.getMessage());
        }
        var apercu = ApercuBroyage.calculer(
            sessionRepository.listerParPeriode(debut, fin), debut, fin, LocalDate.now());
        String planEnCours = pphRepository.trouverEnCours().map(p -> p.getReferencePPH()).orElse(null);
        int plansValides = pphRepository.listerParStatut(StatutPPH.VALIDE).size();
        return ApercuBroyageResponse.de(apercu, planEnCours, plansValides);
    }
}
