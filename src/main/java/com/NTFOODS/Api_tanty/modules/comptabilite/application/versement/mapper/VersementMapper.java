package com.NTFOODS.Api_tanty.modules.comptabilite.application.versement.mapper;

import com.NTFOODS.Api_tanty.modules.comptabilite.application.versement.dto.response.VersementResponse;
import com.NTFOODS.Api_tanty.modules.comptabilite.domain.versement.aggregate.Versement;
import org.springframework.stereotype.Component;

@Component
public class VersementMapper {
    public VersementResponse versResponse(Versement v) {
        return new VersementResponse(v.getId(), v.getReferenceVersement(), v.getMatriculeCommercial(),
            v.getMontantAttendu(), v.getCashVerse(), v.getEcart(), v.isAlerteRouge(), v.getTypeVersement().name(),
            v.getDate(), v.getStatut().name(), v.getJustificationEcart(), v.getMatriculeValidateur());
    }
}
