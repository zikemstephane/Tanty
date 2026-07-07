package com.NTFOODS.Api_tanty.modules.stock.application.sessioncommercial.mapper;

import com.NTFOODS.Api_tanty.modules.stock.application.sessioncommercial.dto.response.SessionCommercialResponse;
import com.NTFOODS.Api_tanty.modules.stock.domain.sessioncommercial.model.SessionCommercial;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class SessionCommercialMapper {

    public SessionCommercialResponse versResponse(SessionCommercial s) {
        return new SessionCommercialResponse(s.getId(), s.getNumeroSession(), s.getMatriculeCommercial(),
            s.getDatePreparation(), s.getDateEffet(), s.getStatut().name(), s.nombreDotations(),
            s.getDotationsIds(), s.getMatriculeCloture(), s.getMatriculeValidateurSecretaire(),
            s.getMatriculeValidateurComptable(), s.getHeureOuverture(), s.getHeureCloture(),
            s.getDateCreation());
    }

    public List<SessionCommercialResponse> versResponseList(List<SessionCommercial> list) {
        return list.stream().map(this::versResponse).toList();
    }
}
