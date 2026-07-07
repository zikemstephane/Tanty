package com.NTFOODS.Api_tanty.modules.stock.application.stockmobile.mapper;

import com.NTFOODS.Api_tanty.modules.stock.application.stockmobile.dto.response.StockMobileResponse;
import com.NTFOODS.Api_tanty.modules.stock.domain.stockmobile.model.StockMobile;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class StockMobileMapper {

    public StockMobileResponse versResponse(StockMobile sm) {
        var lignes = sm.getLignes().stream()
            .map(l -> new StockMobileResponse.LigneResponse(l.getCodeProduit(), l.getDesignation(),
                l.getConditionnement(), l.getQuantiteDisponible(), l.getQuantiteInitiale(),
                l.getQuantiteVendue(), l.getQuantiteAvariee(), l.getQuantiteDonnee(),
                l.getDateDernierMouvement()))
            .toList();
        return new StockMobileResponse(sm.getId(), sm.getMatriculeCommercial(), sm.getDateSession(),
            sm.totalVentes(), sm.totalRestant(), sm.totalAvaries(), sm.getDateModification(), lignes);
    }

    public List<StockMobileResponse> versResponseList(List<StockMobile> list) {
        return list.stream().map(this::versResponse).toList();
    }
}
