package com.NTFOODS.Api_tanty.modules.production.application.pph.usecase;

import com.NTFOODS.Api_tanty.modules.production.application.pph.dto.request.AjusterRepartitionPPHRequest;
import com.NTFOODS.Api_tanty.modules.production.domain.pph.aggregate.PlanProductionHebdomadaire;
import com.NTFOODS.Api_tanty.modules.production.domain.pph.entity.RepartitionJournaliere;
import com.NTFOODS.Api_tanty.modules.production.domain.pph.repository.PPHRepository;
import com.NTFOODS.Api_tanty.shared.kernel.exception.EntiteNonTrouveeException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * Ajuste manuellement la répartition journalière des ressources d'un PPH.
 * Permet au Responsable de Salle de corriger les quantités de poudre,
 * fûts, emballages selon les contraintes opérationnelles réelles du stock.
 */
@Service
@Transactional
public class AjusterRepartitionPPHUseCase {

    private final PPHRepository pphRepository;

    public AjusterRepartitionPPHUseCase(PPHRepository pphRepository) {
        this.pphRepository = pphRepository;
    }

    public PlanProductionHebdomadaire executer(String reference, AjusterRepartitionPPHRequest request) {
        PlanProductionHebdomadaire pph = pphRepository.trouverParReference(reference)
            .orElseThrow(() -> EntiteNonTrouveeException.de("PPH", reference));

        List<RepartitionJournaliere> nouvelle = request.repartition().stream()
            .map(dto -> new RepartitionJournaliere(
                dto.dateJour(),
                List.of(),                  // lignesDetaillees non portées par cet ajustement
                dto.poudreMaisKg(),
                dto.poudreSojaKg(),
                dto.poudreArachideKg(),
                dto.futsPrevus(),
                dto.sachetsPrevus(),
                dto.couverclesPrevus(),
                dto.scellesPrevus()
            ))
            .toList();

        pph.ajusterRepartitionJours(nouvelle);
        return pphRepository.sauvegarder(pph);
    }
}
