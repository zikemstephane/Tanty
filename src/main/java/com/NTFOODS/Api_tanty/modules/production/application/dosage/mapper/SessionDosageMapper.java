package com.NTFOODS.Api_tanty.modules.production.application.dosage.mapper;

import com.NTFOODS.Api_tanty.modules.production.application.dosage.dto.response.SessionDosageResponse;
import com.NTFOODS.Api_tanty.modules.production.domain.dosage.aggregate.SessionDosage;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class SessionDosageMapper {

    public SessionDosageResponse versResponse(SessionDosage s) {
        var matieres = s.getMatieresUtilisees().stream()
            .map(m -> new SessionDosageResponse.MatiereResponse(m.getTypeMatiere(), m.getQuantiteKg()))
            .toList();
        return new SessionDosageResponse(s.getId(), s.getReferencePPH(), s.getDate(),
            s.getMatriculeAgentDoseur(), matieres, s.getMachinesMobilisees(),
            s.getNbFutsProduits(), s.getNbFutsCasses(), s.getNbFutsNets(),
            s.getJustificationEcart(), s.isValidee(), s.isCloturee(),
            s.getDateCreation(), s.getDateModification());
    }

    public List<SessionDosageResponse> versResponseList(List<SessionDosage> list) {
        return list.stream().map(this::versResponse).toList();
    }
}
