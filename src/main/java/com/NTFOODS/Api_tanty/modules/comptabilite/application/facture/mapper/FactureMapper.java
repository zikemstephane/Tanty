package com.NTFOODS.Api_tanty.modules.comptabilite.application.facture.mapper;

import com.NTFOODS.Api_tanty.modules.comptabilite.application.facture.dto.response.FactureResponse;
import com.NTFOODS.Api_tanty.modules.comptabilite.domain.facture.aggregate.Facture;
import org.springframework.stereotype.Component;

@Component
public class FactureMapper {
    public FactureResponse versResponse(Facture f) {
        var lignes = f.getLignes().stream()
            .map(l -> new FactureResponse.LigneResponse(l.getReferenceArticle(), l.getDesignation(), l.getQuantite(),
                l.getPrixUnitaireHT(), l.getTauxTVA(), l.getRemise(), l.getMontantHT(), l.getMontantTVA(), l.getMontantTTC()))
            .toList();
        return new FactureResponse(f.getId(), f.getNumeroFacture(), f.getCodeClient(), f.getTypeFacture().name(),
            f.getReferenceFactureOrigine(), f.getDateEmission(), f.getStatut().name(), f.getMatriculeEmetteur(),
            f.getMontantTotalHT(), f.getMontantTotalTVA(), f.getMontantTotalTTC(), lignes);
    }
}
