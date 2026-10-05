package com.NTFOODS.Api_tanty.modules.stock.application.mouvement.mapper;

import com.NTFOODS.Api_tanty.modules.stock.application.mouvement.dto.response.MouvementResponse;
import com.NTFOODS.Api_tanty.modules.stock.domain.mouvement.model.MouvementStock;
import org.springframework.stereotype.Component;

@Component
public class MouvementMapper {

    public MouvementResponse versResponse(MouvementStock m) {
        return new MouvementResponse(
            m.getId(), m.getReference(), m.getType().name(), m.getType().getLibelle(),
            m.getType().getSens().name(), m.getCodeProduit(), m.getSku(), m.getQuantiteEnBase(),
            m.getCodeEntrepotSource(), m.getCodeEntrepotCible(), m.getMatriculeActeur(),
            m.getReferenceDocument(), m.getMotif(), m.getHorodatage()
        );
    }
}
