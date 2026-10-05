package com.NTFOODS.Api_tanty.modules.stock.application.retour.mapper;

import com.NTFOODS.Api_tanty.modules.stock.application.retour.dto.response.RetourResponse;
import com.NTFOODS.Api_tanty.modules.stock.domain.retour.model.BonRetour;
import org.springframework.stereotype.Component;

@Component
public class RetourMapper {

    public RetourResponse versResponse(BonRetour b) {
        var lignes = b.getLignes().stream()
            .map(l -> new RetourResponse.LigneResponse(l.getId(), l.getCodeProduit(), l.getSku(),
                l.getQuantiteDeclaree(), l.getQuantiteComptee(), l.ecart(), l.getMotif()))
            .toList();
        return new RetourResponse(b.getId(), b.getNumero(), b.getMatriculeCommercial(),
            b.getMatriculeGestionnaire(), b.getDateRetour(), b.getStatut().name(),
            b.getDateComptage(), b.ecartTotal(), lignes);
    }
}
