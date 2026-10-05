package com.NTFOODS.Api_tanty.modules.commercial.application.vente.mapper;

import com.NTFOODS.Api_tanty.modules.commercial.application.vente.dto.response.VenteResponse;
import com.NTFOODS.Api_tanty.modules.commercial.domain.vente.aggregate.Vente;
import org.springframework.stereotype.Component;

@Component
public class VenteMapper {
    public VenteResponse versResponse(Vente v) {
        var lignes = v.getLignes().stream()
            .map(l -> new VenteResponse.LigneResponse(l.getCodeProduit(), l.getDesignationProduit(),
                l.getConditionnement(), l.getQuantite(), l.getPrixUnitaire(), l.getRemise()))
            .toList();
        return new VenteResponse(v.getId(), v.getNumeroVente(), v.getMatriculeCommercial(), v.getCodeClient(),
            v.getDate(), v.getModeReglement().name(), v.getTypeVente().name(), v.getStatut().name(),
            v.getSyncStatus().name(), v.getCoordonneesGPS(), v.getIdFeuillRoute(),
            v.montantTotal(), v.getObservations(), v.getPhotoProduits(), lignes);
    }
}
