package com.NTFOODS.Api_tanty.modules.stock.application.transfert.mapper;

import com.NTFOODS.Api_tanty.modules.stock.application.transfert.dto.response.TransfertResponse;
import com.NTFOODS.Api_tanty.modules.stock.domain.transfert.model.BonTransfert;
import org.springframework.stereotype.Component;

@Component
public class TransfertMapper {

    public TransfertResponse versResponse(BonTransfert bt) {
        var lignes = bt.getLignes().stream()
            .map(l -> new TransfertResponse.LigneResponse(l.getId(), l.getCodeProduit(), l.getSku(), l.getQuantite()))
            .toList();
        return new TransfertResponse(bt.getId(), bt.getNumero(), bt.getCodeEntrepotSource(),
            bt.getCodeEntrepotCible(), bt.getStatut().name(), bt.getStatut().getLibelle(),
            bt.isDeclencheParSeuil(), bt.getMatriculeGestionnaire(), bt.getDateExecution(), lignes);
    }
}
