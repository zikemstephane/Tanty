package com.NTFOODS.Api_tanty.modules.stock.application.alerte.mapper;

import com.NTFOODS.Api_tanty.modules.stock.application.alerte.dto.response.AlerteResponse;
import com.NTFOODS.Api_tanty.modules.stock.domain.alerte.model.Alerte;
import org.springframework.stereotype.Component;

@Component
public class AlerteMapper {

    public AlerteResponse versResponse(Alerte a) {
        return new AlerteResponse(a.getId(), a.getReference(), a.getType().name(), a.getType().getLibelle(),
            a.getMessage(), a.getDestinataireRole(), a.getActionSuggeree(),
            a.getType().getPriorite().name(),
            a.getCodeProduit(), a.getCodeEntrepot(),
            a.getReferenceLiee(), a.getStatut().name(), a.getDateCreation(), a.getDateTraitement());
    }
}
