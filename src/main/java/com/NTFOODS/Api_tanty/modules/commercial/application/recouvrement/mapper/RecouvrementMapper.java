package com.NTFOODS.Api_tanty.modules.commercial.application.recouvrement.mapper;

import com.NTFOODS.Api_tanty.modules.commercial.application.recouvrement.dto.response.RecouvrementResponse;
import com.NTFOODS.Api_tanty.modules.commercial.domain.recouvrement.aggregate.Recouvrement;
import org.springframework.stereotype.Component;

@Component
public class RecouvrementMapper {
    public RecouvrementResponse versResponse(Recouvrement r) {
        return new RecouvrementResponse(r.getId(), r.getReferenceRecouvrement(), r.getCodeClient(), r.getNomClient(),
            r.getMontantDu(), r.getMontantRembourse(), r.montantRestant(), r.getDateEcheance(),
            r.getStatut().name(), r.getMatriculeCommercial(), r.getObservations());
    }
}
