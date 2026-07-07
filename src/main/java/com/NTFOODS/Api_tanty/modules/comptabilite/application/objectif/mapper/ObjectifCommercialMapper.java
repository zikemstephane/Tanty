package com.NTFOODS.Api_tanty.modules.comptabilite.application.objectif.mapper;

import com.NTFOODS.Api_tanty.modules.comptabilite.application.objectif.dto.response.ObjectifCommercialResponse;
import com.NTFOODS.Api_tanty.modules.comptabilite.domain.objectif.aggregate.ObjectifCommercial;
import org.springframework.stereotype.Component;

@Component
public class ObjectifCommercialMapper {
    public ObjectifCommercialResponse versResponse(ObjectifCommercial o) {
        return new ObjectifCommercialResponse(o.getId(), o.getMatriculeCommercial(), o.getSemaineDebut(),
            o.getSemaineFin(), o.getObjectifGlobalFCFA(), o.getObjectifsParGammeFCFA(), o.getMatriculeAuteur());
    }
}
