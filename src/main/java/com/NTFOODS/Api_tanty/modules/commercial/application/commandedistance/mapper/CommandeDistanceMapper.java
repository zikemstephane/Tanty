package com.NTFOODS.Api_tanty.modules.commercial.application.commandedistance.mapper;

import com.NTFOODS.Api_tanty.modules.commercial.application.commandedistance.dto.response.CommandeDistanceResponse;
import com.NTFOODS.Api_tanty.modules.commercial.domain.commandedistance.aggregate.CommandeDistance;
import org.springframework.stereotype.Component;

@Component
public class CommandeDistanceMapper {

    public CommandeDistanceResponse versResponse(CommandeDistance cd) {
        var lignes = cd.getLignes().stream()
            .map(l -> new CommandeDistanceResponse.LigneResponse(l.getCodeProduit(), l.getDesignation(),
                l.getConditionnement(), l.getQuantite(), l.getPrixUnitaire()))
            .toList();
        return new CommandeDistanceResponse(cd.getId(), cd.getNumeroCommande(), cd.getCodeClient(),
            cd.getMatriculeCommercial(), cd.getCanal().name(), cd.getDateCommande(), cd.getStatut().name(),
            cd.getMatriculeValidateurSecretaire(), cd.getMatriculeValidateurComptable(),
            cd.getMotifAnnulation(), cd.getObservations(), cd.montantTotal(),
            cd.getDateCreation(), lignes);
    }
}
