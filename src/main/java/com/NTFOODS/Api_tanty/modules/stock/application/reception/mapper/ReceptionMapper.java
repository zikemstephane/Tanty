package com.NTFOODS.Api_tanty.modules.stock.application.reception.mapper;

import com.NTFOODS.Api_tanty.modules.stock.application.reception.dto.response.ReceptionResponse;
import com.NTFOODS.Api_tanty.modules.stock.domain.reception.model.Reception;
import org.springframework.stereotype.Component;

@Component
public class ReceptionMapper {

    public ReceptionResponse versResponse(Reception r) {
        var lignes = r.getLignes().stream()
            .map(l -> new ReceptionResponse.LigneResponse(
                l.getId(), l.getCodeProduit(), l.getSku(), l.getQuantiteAttendue(),
                l.getQuantiteRecue(), l.getEcart(), l.pourcentageEcart()))
            .toList();

        return new ReceptionResponse(
            r.getId(), r.getNumero(), r.getSource().name(), r.getCategorie().name(),
            r.getCodeFournisseur(), r.getNumeroBonCommande(), r.getNumeroLotProduction(),
            r.getCodeEntrepotCible(), r.getStatut().name(), r.getStatut().getLibelle(),
            r.aEcartSignificatif(), r.getMatriculeValidateur1(), r.getMatriculeValidateur2(),
            r.getMotifRejet(), r.getDateReception(), lignes
        );
    }
}
