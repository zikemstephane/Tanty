package com.NTFOODS.Api_tanty.modules.stock.application.dotation.mapper;

import com.NTFOODS.Api_tanty.modules.stock.application.dotation.dto.response.DotationResponse;
import com.NTFOODS.Api_tanty.modules.stock.domain.dotation.model.Dotation;
import org.springframework.stereotype.Component;

@Component
public class DotationMapper {

    public DotationResponse versResponse(Dotation d) {
        var lignes = d.getLignes().stream()
            .map(l -> new DotationResponse.LigneResponse(l.getId(), l.getCodeProduit(), l.getSku(),
                l.getQuantiteDemandee(), l.getQuantiteApprouvee(), l.getMotifModification()))
            .toList();
        return new DotationResponse(d.getId(), d.getNumero(), d.getMatriculeCommercial(),
            d.getDateDotation(), d.getDateEffet(), d.getStatut().name(), d.getStatut().getLibelle(),
            d.getMatriculeGestionnaire(), d.getMotifRefus(), lignes);
    }
}
