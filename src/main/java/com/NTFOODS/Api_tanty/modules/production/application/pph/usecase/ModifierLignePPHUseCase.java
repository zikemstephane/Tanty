package com.NTFOODS.Api_tanty.modules.production.application.pph.usecase;

import com.NTFOODS.Api_tanty.modules.auth.domain.utilisateur.aggregate.Utilisateur;
import com.NTFOODS.Api_tanty.modules.auth.domain.utilisateur.enums.Role;
import com.NTFOODS.Api_tanty.modules.production.domain.pph.aggregate.PlanProductionHebdomadaire;
import com.NTFOODS.Api_tanty.modules.production.domain.pph.entity.LignePPH;
import com.NTFOODS.Api_tanty.modules.production.domain.pph.repository.PPHRepository;
import com.NTFOODS.Api_tanty.shared.kernel.exception.AccesRefuseException;
import com.NTFOODS.Api_tanty.shared.kernel.exception.EntiteNonTrouveeException;
import com.NTFOODS.Api_tanty.shared.kernel.exception.RegleMetierException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * Use Case — Modifier les lignes d'un PPH.
 *
 * Seul le RESPONSABLE_SALLE peut modifier les lignes de planification.
 * La référence BC et la date de soumission ne sont pas modifiables.
 */
@Service
@Transactional
public class ModifierLignePPHUseCase {

    private final PPHRepository pphRepository;

    public ModifierLignePPHUseCase(PPHRepository pphRepository) {
        this.pphRepository = pphRepository;
    }

    public PlanProductionHebdomadaire executer(String referencePPH, List<LigneModifiable> lignes, Utilisateur acteur) {
        if (acteur.getRole() != Role.RESPONSABLE_SALLE && acteur.getRole() != Role.ADMIN) {
            throw new AccesRefuseException("Seul le Responsable de Salle peut modifier un PPH");
        }

        PlanProductionHebdomadaire pph = pphRepository.trouverParReference(referencePPH)
            .orElseThrow(() -> EntiteNonTrouveeException.de("PPH", referencePPH));

        if (lignes == null || lignes.isEmpty()) {
            throw new RegleMetierException("Aucune ligne à modifier");
        }

        for (LigneModifiable l : lignes) {
            LignePPH ligne = pph.getLignes().stream()
                .filter(li -> li.getCodeProduit().equals(l.codeProduit()))
                .findFirst()
                .orElseThrow(() -> EntiteNonTrouveeException.de("Ligne PPH", l.codeProduit()));
            ligne.modifierObjectif(l.objectifSemaine());
        }

        return pphRepository.sauvegarder(pph);
    }

    public record LigneModifiable(String codeProduit, int objectifSemaine) {}
}
