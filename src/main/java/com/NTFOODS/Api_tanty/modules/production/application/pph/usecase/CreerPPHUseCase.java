package com.NTFOODS.Api_tanty.modules.production.application.pph.usecase;

import com.NTFOODS.Api_tanty.modules.auth.domain.utilisateur.aggregate.Utilisateur;
import com.NTFOODS.Api_tanty.modules.auth.domain.utilisateur.enums.Role;
import com.NTFOODS.Api_tanty.modules.production.application.pph.dto.request.CreerPPHRequest;
import com.NTFOODS.Api_tanty.modules.production.application.pph.dto.response.PPHResponse;
import com.NTFOODS.Api_tanty.modules.production.application.pph.mapper.PPHMapper;
import com.NTFOODS.Api_tanty.modules.production.domain.pph.aggregate.PlanProductionHebdomadaire;
import com.NTFOODS.Api_tanty.modules.production.domain.pph.entity.LignePPH;
import com.NTFOODS.Api_tanty.modules.production.domain.pph.repository.PPHRepository;
import com.NTFOODS.Api_tanty.shared.kernel.exception.AccesRefuseException;
import com.NTFOODS.Api_tanty.shared.kernel.exception.RegleMetierException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Use Case — Créer un Plan de Production Hebdomadaire (document de référence
 * de la semaine, socle de tout le module Production : fiches journalières,
 * affectations, sessions de broyage, ordres de fabrication en dépendent tous
 * via leur {@code referencePPH}).
 */
@Service
@Transactional
public class CreerPPHUseCase {

    private final PPHRepository pphRepository;
    private final PPHMapper pphMapper;

    public CreerPPHUseCase(PPHRepository pphRepository, PPHMapper pphMapper) {
        this.pphRepository = pphRepository;
        this.pphMapper = pphMapper;
    }

    public PPHResponse executer(CreerPPHRequest request, Utilisateur acteur) {
        if (acteur.getRole() != Role.CHEF_PRODUCTION && acteur.getRole() != Role.ADMIN) {
            throw new AccesRefuseException("Seul le Chef de Production peut créer un PPH");
        }
        if (pphRepository.trouverParReference(request.referencePPH()).isPresent()) {
            throw new RegleMetierException("Un PPH existe déjà avec la référence " + request.referencePPH());
        }

        var lignes = request.lignes().stream()
            .map(l -> new LignePPH(l.codeProduit(), l.nomProduit(), l.objectifSemaine()))
            .toList();

        PlanProductionHebdomadaire pph;
        if (request.referenceBC() != null && !request.referenceBC().isBlank()) {
            pph = PlanProductionHebdomadaire.creerAvecBC(request.referencePPH(), request.referenceBC(),
                request.semaine(), request.dateDebut(), request.dateFin(),
                acteur.getMatricule().getValeur(), lignes);
        } else {
            pph = PlanProductionHebdomadaire.creer(request.referencePPH(), request.semaine(),
                request.dateDebut(), request.dateFin(), acteur.getMatricule().getValeur(), lignes);
        }

        return pphMapper.versResponse(pphRepository.sauvegarder(pph));
    }
}
