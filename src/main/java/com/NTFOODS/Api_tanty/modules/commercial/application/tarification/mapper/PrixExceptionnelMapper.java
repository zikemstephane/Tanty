package com.NTFOODS.Api_tanty.modules.commercial.application.tarification.mapper;

import com.NTFOODS.Api_tanty.modules.commercial.application.tarification.dto.response.PrixExceptionnelResponse;
import com.NTFOODS.Api_tanty.modules.commercial.domain.tarification.model.PrixExceptionnel;
import org.springframework.stereotype.Component;

@Component
public class PrixExceptionnelMapper {
    public PrixExceptionnelResponse versResponse(PrixExceptionnel p) {
        return new PrixExceptionnelResponse(p.getId(), p.getReference(), p.getCodeProduit(), p.getCodeClient(),
            p.getPrixHomologueFCFA(), p.getPrixAppliqueFCFA(), p.getEcartFCFA(), p.getMotif(),
            p.getMatriculeAutorisateur(), p.getReferenceVenteCible(), p.getDateAutorisation());
    }
}
