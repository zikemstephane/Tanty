package com.NTFOODS.Api_tanty.modules.stock.application.inventaire.mapper;

import com.NTFOODS.Api_tanty.modules.stock.application.inventaire.dto.response.InventaireResponse;
import com.NTFOODS.Api_tanty.modules.stock.domain.inventaire.model.SessionInventaire;
import org.springframework.stereotype.Component;

@Component
public class InventaireMapper {

    public InventaireResponse versResponse(SessionInventaire s) {
        var lignes = s.getLignes().stream()
            .map(l -> new InventaireResponse.LigneResponse(l.getId(), l.getCodeProduit(), l.getEmplacement(),
                l.getQuantiteTheorique(), l.getQuantiteComptee(), l.getEcart()))
            .toList();
        return new InventaireResponse(s.getId(), s.getNumero(), s.getType().name(), s.getType().getLibelle(),
            s.getCodeEntrepot(), s.getPerimetre(), s.getStatut().name(), s.getStatut().getLibelle(),
            s.aEcartSignificatif(), s.getMatriculeInitiateur(), s.getMatriculeValidateurDG(),
            s.getJustificationEcarts(), s.getDateInventaire(), lignes);
    }
}
