package com.NTFOODS.Api_tanty.modules.commercial.application.precommande.mapper;

import com.NTFOODS.Api_tanty.modules.commercial.application.precommande.dto.response.PreCommandeResponse;
import com.NTFOODS.Api_tanty.modules.commercial.domain.precommande.aggregate.PreCommande;
import org.springframework.stereotype.Component;

@Component
public class PreCommandeMapper {
    public PreCommandeResponse versResponse(PreCommande p) {
        var lignes = p.getLignes().stream()
            .map(l -> new PreCommandeResponse.LigneResponse(l.getCodeProduit(), l.getDesignationProduit(),
                l.getConditionnement(), l.getQuantiteDemandee(), l.getQuantiteValidee()))
            .toList();
        return new PreCommandeResponse(p.getId(), p.getNumeroPreCommande(), p.getMatriculeCommercial(),
            p.getDateSoumission(), p.getDateEffet(), p.getStatut().name(), p.getMatriculeValidateurSecretaire(),
            p.getMatriculeValidateurComptable(), p.getMotifRefus(), p.isSoumiseEnSoiree(), lignes);
    }
}
