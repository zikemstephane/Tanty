package com.NTFOODS.Api_tanty.modules.production.application.fiche.mapper;

import com.NTFOODS.Api_tanty.modules.production.application.fiche.dto.response.FicheResponse;
import com.NTFOODS.Api_tanty.modules.production.domain.dosage.aggregate.SessionDosage;
import com.NTFOODS.Api_tanty.modules.production.domain.dosage.repository.SessionDosageRepository;
import com.NTFOODS.Api_tanty.modules.production.domain.fiche.aggregate.FicheProduitJournaliere;
import com.NTFOODS.Api_tanty.modules.production.domain.pph.aggregate.PlanProductionHebdomadaire;
import com.NTFOODS.Api_tanty.modules.production.domain.pph.entity.LignePPH;
import com.NTFOODS.Api_tanty.modules.production.domain.pph.repository.PPHRepository;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class FicheMapper {

    private final SessionDosageRepository sessionDosageRepository;
    private final PPHRepository pphRepository;

    public FicheMapper(SessionDosageRepository sessionDosageRepository, PPHRepository pphRepository) {
        this.sessionDosageRepository = sessionDosageRepository;
        this.pphRepository = pphRepository;
    }

    public FicheResponse versResponse(FicheProduitJournaliere fiche) {
        var lignesPph = pphRepository.trouverParReference(fiche.getReferencePPH())
            .map(PlanProductionHebdomadaire::getLignes)
            .orElse(List.of());
        var lignes = fiche.getLignes().stream()
            .map(l -> {
                // null (pas de prédiction applicable à ce produit), distinct de 0 (prédit à zéro)
                Integer prediteDosage = fiche.getPredictionDosage().get(l.getCodeProduit());
                Integer preditePostes = fiche.getPredictionPostes().get(l.getCodeProduit());
                Integer ecart = fiche.calculerEcart(l.getCodeProduit());
                Integer tauxAvancement = lignesPph.stream()
                    .filter(lp -> lp.getCodeProduit().equals(l.getCodeProduit()))
                    .findFirst()
                    .map(LignePPH::tauxAvancement)
                    .orElse(null);
                return new FicheResponse.LigneResponse(l.getCodeProduit(), l.getNomProduit(),
                    l.getQuantiteProduite(), prediteDosage, preditePostes, ecart, tauxAvancement);
            })
            .toList();
        int nbFutsProduits = sessionDosageRepository.trouverParPPHEtDate(fiche.getReferencePPH(), fiche.getDate())
            .map(SessionDosage::getNbFutsProduits)
            .orElse(0);
        return new FicheResponse(fiche.getId(), fiche.getReferencePPH(), fiche.getSemaine(), fiche.getDate(),
            fiche.getMatriculeAgentProduction(), fiche.getJustificationEcart(), fiche.isVerrouillee(),
            nbFutsProduits,
            fiche.getPredictionDosage(), fiche.getPredictionPostes(), lignes);
    }
}
