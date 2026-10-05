package com.NTFOODS.Api_tanty.modules.stock.application.entrepot.mapper;

import com.NTFOODS.Api_tanty.modules.stock.application.entrepot.dto.response.EntrepotResponse;
import com.NTFOODS.Api_tanty.modules.stock.domain.entrepot.model.Entrepot;
import org.springframework.stereotype.Component;

@Component
public class EntrepotMapper {

    public EntrepotResponse versResponse(Entrepot e) {
        return new EntrepotResponse(
            e.getId(), e.getCode(), e.getLibelle(), e.getType().name(),
            e.getType().getLibelle(), e.getMatriculeProprietaire(), e.isActif()
        );
    }
}
