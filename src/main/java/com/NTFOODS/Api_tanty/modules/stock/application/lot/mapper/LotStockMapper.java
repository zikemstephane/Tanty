package com.NTFOODS.Api_tanty.modules.stock.application.lot.mapper;

import com.NTFOODS.Api_tanty.modules.stock.application.lot.dto.response.LotStockResponse;
import com.NTFOODS.Api_tanty.modules.stock.domain.lot.model.LotStock;
import org.springframework.stereotype.Component;

@Component
public class LotStockMapper {

    /** Seuil d'alerte : un lot qui périme dans le mois est signalé. */
    public static final int JOURS_ALERTE_PEREMPTION = 30;

    public LotStockResponse versResponse(LotStock l) {
        return new LotStockResponse(
            l.getId(), l.getNumeroLot(), l.getCodeProduit(), l.getSku(), l.getDesignation(),
            l.getCodeFournisseur(), l.getCodeEntrepot(), l.getDateFabrication(), l.getDatePeremption(),
            l.getQuantiteInitiale(), l.getQuantiteRestante(),
            l.getStatut().name(), l.getStatut().getLibelle(),
            l.estPerime(), l.perimeBientot(JOURS_ALERTE_PEREMPTION), l.joursAvantPeremption(),
            l.getObservations(), l.getDateCreation(), l.getDateDernierMouvement()
        );
    }
}
