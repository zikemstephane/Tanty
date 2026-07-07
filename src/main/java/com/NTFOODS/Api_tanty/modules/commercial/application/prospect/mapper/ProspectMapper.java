package com.NTFOODS.Api_tanty.modules.commercial.application.prospect.mapper;

import com.NTFOODS.Api_tanty.modules.commercial.application.prospect.dto.response.ProspectResponse;
import com.NTFOODS.Api_tanty.modules.commercial.domain.prospect.aggregate.Prospect;
import org.springframework.stereotype.Component;

@Component
public class ProspectMapper {

    public ProspectResponse versResponse(Prospect p) {
        return new ProspectResponse(p.getId(), p.getCodeProspect(), p.getNom(), p.getTelephone(),
            p.getAdresse(), p.getLocalite(), p.getMatriculeCommercial(),
            p.getCanal() != null ? p.getCanal().name() : null,
            p.getStatut().name(), p.getObservations(), p.getCodeClientConverti(),
            p.getDateProspection(), p.getDateCreation());
    }
}
