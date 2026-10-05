package com.NTFOODS.Api_tanty.modules.commercial.application.client.mapper;

import com.NTFOODS.Api_tanty.modules.commercial.application.client.dto.response.ClientResponse;
import com.NTFOODS.Api_tanty.modules.commercial.domain.client.aggregate.Client;
import org.springframework.stereotype.Component;

@Component
public class ClientMapper {
    public ClientResponse versResponse(Client c) {
        return versResponse(c, 0.0);
    }

    public ClientResponse versResponse(Client c, double dette) {
        return new ClientResponse(c.getId(), c.getCodeClient(), c.getNom(), c.getTelephone(),
            c.getAdresse(), c.getLocalite(), c.getType().name(), c.getMatriculeCommercialReferent(), c.isActif(),
            c.getLatitude(), c.getLongitude(), dette);
    }
}
