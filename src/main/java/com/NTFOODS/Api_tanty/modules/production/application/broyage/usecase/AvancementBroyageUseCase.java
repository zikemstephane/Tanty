package com.NTFOODS.Api_tanty.modules.production.application.broyage.usecase;

import com.NTFOODS.Api_tanty.modules.production.application.broyage.dto.response.AvancementBroyageResponse;
import com.NTFOODS.Api_tanty.modules.production.domain.broyage.repository.SessionBroyageRepository;
import com.NTFOODS.Api_tanty.modules.production.domain.broyage.service.AvancementBroyage;
import com.NTFOODS.Api_tanty.modules.production.domain.pph.aggregate.PlanProductionHebdomadaire;
import com.NTFOODS.Api_tanty.modules.production.domain.pph.repository.PPHRepository;
import com.NTFOODS.Api_tanty.modules.production.domain.formule.entity.BesoinMatierePremiere;
import com.NTFOODS.Api_tanty.shared.kernel.exception.EntiteNonTrouveeException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/** Poudre prévue par le plan face à la poudre écrasée (lecture seule). */
@Service
@Transactional(readOnly = true)
public class AvancementBroyageUseCase {

    private final PPHRepository pphRepository;
    private final SessionBroyageRepository sessionRepository;

    public AvancementBroyageUseCase(PPHRepository pphRepository, SessionBroyageRepository sessionRepository) {
        this.pphRepository = pphRepository;
        this.sessionRepository = sessionRepository;
    }

    public AvancementBroyageResponse executer(String referencePPH) {
        PlanProductionHebdomadaire pph = pphRepository.trouverParReference(referencePPH)
            .orElseThrow(() -> EntiteNonTrouveeException.de("PlanProductionHebdomadaire", referencePPH));

        List<BesoinMatierePremiere> prevus = pph.getRepartitionJours().stream()
            .flatMap(j -> j.getBesoinsMatieresPremiere().stream())
            .toList();

        return AvancementBroyageResponse.de(
            AvancementBroyage.calculer(referencePPH, prevus, sessionRepository.listerParPPH(referencePPH)));
    }
}
