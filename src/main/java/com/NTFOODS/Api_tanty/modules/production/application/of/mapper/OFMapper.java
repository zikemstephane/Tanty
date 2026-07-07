package com.NTFOODS.Api_tanty.modules.production.application.of.mapper;

import com.NTFOODS.Api_tanty.modules.production.application.of.dto.response.OFResponse;
import com.NTFOODS.Api_tanty.modules.production.domain.of.aggregate.OrdreFabrication;
import org.springframework.stereotype.Component;

@Component
public class OFMapper {

    public OFResponse versResponse(OrdreFabrication of) {
        return new OFResponse(of.getId(), of.getIdOF(), of.getReferencePPH(), of.getCodeProduit(),
            of.getDesignationProduit(), of.getQteDemandee(), of.getQteRealisee(), of.getDateButoir(),
            of.getStatut().name());
    }
}
