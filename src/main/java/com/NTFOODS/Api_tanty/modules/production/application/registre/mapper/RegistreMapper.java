package com.NTFOODS.Api_tanty.modules.production.application.registre.mapper;

import com.NTFOODS.Api_tanty.modules.production.application.registre.dto.response.RegistreProductionResponse;
import com.NTFOODS.Api_tanty.modules.production.application.registre.dto.response.RegistreProductionResponse.LigneRegistreResponse;
import com.NTFOODS.Api_tanty.modules.production.domain.registre.aggregate.RegistreProduction;
import com.NTFOODS.Api_tanty.modules.production.domain.registre.entity.LigneRegistreEmploye;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class RegistreMapper {

    public RegistreProductionResponse versResponse(RegistreProduction r) {
        List<LigneRegistreResponse> lignes = r.getLignesEmployes().stream()
            .map(l -> new LigneRegistreResponse(l.getMatriculeEmploye(), l.getNomEmploye(),
                l.getPoste(), l.isPresent(), l.getMotifAbsence(),
                l.getHeureArrivee(), l.getQteRealisee(), l.getUnite()))
            .toList();
        return new RegistreProductionResponse(r.getId(), r.getReferencePPH(), r.getDate(),
            r.getMatriculeChefProduction(), lignes, r.nbPresents(), r.nbAbsents(),
            r.getDateCreation(), r.getDateModification());
    }
}
