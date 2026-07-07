package com.NTFOODS.Api_tanty.modules.production.application.fiche.mapper;

import com.NTFOODS.Api_tanty.modules.production.application.fiche.dto.response.FicheResponse;
import com.NTFOODS.Api_tanty.modules.production.domain.fiche.aggregate.FicheProduitJournaliere;
import org.springframework.stereotype.Component;

@Component
public class FicheMapper {

    public FicheResponse versResponse(FicheProduitJournaliere fiche) {
        var lignes = fiche.getLignes().stream()
            .map(l -> {
                int prediteDosage = fiche.getPredictionDosage().getOrDefault(l.getCodeProduit(), 0);
                int preditePostes = fiche.getPredictionPostes().getOrDefault(l.getCodeProduit(), 0);
                int ecart = fiche.calculerEcart(l.getCodeProduit());
                return new FicheResponse.LigneResponse(l.getCodeProduit(), l.getNomProduit(),
                    l.getQuantiteProduite(), prediteDosage, preditePostes, ecart);
            })
            .toList();
        return new FicheResponse(fiche.getId(), fiche.getReferencePPH(), fiche.getSemaine(), fiche.getDate(),
            fiche.getMatriculeAgentProduction(), fiche.getJustificationEcart(), fiche.isVerrouillee(),
            fiche.getPredictionDosage(), fiche.getPredictionPostes(), lignes);
    }
}
