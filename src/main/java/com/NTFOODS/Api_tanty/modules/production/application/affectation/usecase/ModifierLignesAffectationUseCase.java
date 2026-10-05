package com.NTFOODS.Api_tanty.modules.production.application.affectation.usecase;

import com.NTFOODS.Api_tanty.modules.auth.domain.utilisateur.aggregate.Utilisateur;
import com.NTFOODS.Api_tanty.modules.production.application.affectation.dto.request.LigneAffectationRequest;
import com.NTFOODS.Api_tanty.modules.production.application.affectation.dto.response.AffectationJournaliereResponse;
import com.NTFOODS.Api_tanty.modules.production.application.affectation.mapper.AffectationMapper;
import com.NTFOODS.Api_tanty.modules.production.application.affectation.service.PosteProductionService;
import com.NTFOODS.Api_tanty.modules.production.domain.affectation.aggregate.AffectationJournaliere;
import com.NTFOODS.Api_tanty.modules.production.domain.affectation.entity.LigneAffectationPoste;
import com.NTFOODS.Api_tanty.modules.production.domain.affectation.repository.AffectationJournaliereRepository;
import com.NTFOODS.Api_tanty.shared.kernel.exception.AccesRefuseException;
import com.NTFOODS.Api_tanty.shared.kernel.exception.EntiteNonTrouveeException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * Use Case — Modifier les lignes d'une affectation journalière (Responsable de Salle).
 *
 * Permet de corriger les postes, les employés affectés et la durée
 * tant que l'affectation n'est pas encore validée (BROUILLON).
 */
@Service
@Transactional
public class ModifierLignesAffectationUseCase {

    private final AffectationJournaliereRepository repository;
    private final AffectationMapper mapper;
    private final PosteProductionService posteService;

    public ModifierLignesAffectationUseCase(AffectationJournaliereRepository repository,
                                             AffectationMapper mapper,
                                             PosteProductionService posteService) {
        this.repository = repository;
        this.mapper = mapper;
        this.posteService = posteService;
    }

    public AffectationJournaliereResponse executer(Long affectationId,
                                                    List<LigneAffectationRequest> lignesReq,
                                                    Utilisateur acteur) {
        if (!peutModifier(acteur)) {
            throw new AccesRefuseException("Seul le Responsable de Salle peut modifier une affectation journalière");
        }

        AffectationJournaliere affectation = repository.trouverParId(affectationId)
            .orElseThrow(() -> EntiteNonTrouveeException.de("Affectation", String.valueOf(affectationId)));

        List<LigneAffectationPoste> nouvellesLignes = lignesReq.stream()
            .map(l -> {
                var poste = posteService.parCode(l.poste());
                double objectif = poste.calculerObjectifAjuste(l.matriculesEmployes().size(), l.duree());
                return new LigneAffectationPoste(l.poste(), l.matriculesEmployes(), l.duree(), objectif);
            })
            .toList();

        affectation.modifierLignes(nouvellesLignes);

        return mapper.versResponse(repository.sauvegarder(affectation));
    }

    private boolean peutModifier(Utilisateur u) {
        String role = u.getRole().name();
        return role.equals("RESPONSABLE_SALLE") || role.equals("CHEF_PRODUCTION") || role.equals("ADMIN");
    }
}
