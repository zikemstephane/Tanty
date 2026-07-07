package com.NTFOODS.Api_tanty.modules.commercial.application.tarification.mapper;

import com.NTFOODS.Api_tanty.modules.commercial.application.tarification.dto.response.GrilleTarifaireResponse;
import com.NTFOODS.Api_tanty.modules.commercial.domain.tarification.model.GrilleTarifaire;
import org.springframework.stereotype.Component;

@Component
public class GrilleTarifaireMapper {
    public GrilleTarifaireResponse versResponse(GrilleTarifaire g) {
        return new GrilleTarifaireResponse(g.getId(), g.getCodeProduit(), g.getSegment().name(),
            g.getPrixHomologueFCFA(), g.isActif(), g.getMatriculeAuteur(), g.getDateModification());
    }
}
