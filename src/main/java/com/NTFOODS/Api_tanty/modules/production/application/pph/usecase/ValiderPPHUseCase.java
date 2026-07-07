package com.NTFOODS.Api_tanty.modules.production.application.pph.usecase;

import com.NTFOODS.Api_tanty.modules.auth.domain.utilisateur.aggregate.Utilisateur;
import com.NTFOODS.Api_tanty.modules.auth.domain.utilisateur.enums.Role;
import com.NTFOODS.Api_tanty.modules.production.application.pph.dto.response.PPHResponse;
import com.NTFOODS.Api_tanty.modules.production.application.pph.mapper.PPHMapper;
import com.NTFOODS.Api_tanty.modules.production.domain.pph.aggregate.PlanProductionHebdomadaire;
import com.NTFOODS.Api_tanty.modules.production.domain.pph.repository.PPHRepository;
import com.NTFOODS.Api_tanty.shared.kernel.event.EventPublisher;
import com.NTFOODS.Api_tanty.shared.kernel.exception.AccesRefuseException;
import com.NTFOODS.Api_tanty.shared.kernel.exception.EntiteNonTrouveeException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/** Use Case — Valider un PPH (BROUILLON → VALIDE), publie {@code PPHValideEvent}. */
@Service
@Transactional
public class ValiderPPHUseCase {

    private final PPHRepository pphRepository;
    private final PPHMapper pphMapper;
    private final EventPublisher eventPublisher;

    public ValiderPPHUseCase(PPHRepository pphRepository, PPHMapper pphMapper, EventPublisher eventPublisher) {
        this.pphRepository = pphRepository;
        this.pphMapper = pphMapper;
        this.eventPublisher = eventPublisher;
    }

    public PPHResponse executer(String referencePPH, Utilisateur acteur) {
        if (acteur.getRole() != Role.CHEF_PRODUCTION && acteur.getRole() != Role.ADMIN) {
            throw new AccesRefuseException("Seul le Chef de Production peut valider un PPH");
        }
        PlanProductionHebdomadaire pph = pphRepository.trouverParReference(referencePPH)
            .orElseThrow(() -> EntiteNonTrouveeException.de("PPH", referencePPH));

        pph.valider();

        PlanProductionHebdomadaire sauvegarde = pphRepository.sauvegarder(pph);
        sauvegarde.getDomainEvents().forEach(eventPublisher::publish);
        sauvegarde.clearDomainEvents();
        return pphMapper.versResponse(sauvegarde);
    }
}
