package com.NTFOODS.Api_tanty.modules.comptabilite.application.decaissement.mapper;

import com.NTFOODS.Api_tanty.modules.comptabilite.application.decaissement.dto.response.DecaissementResponse;
import com.NTFOODS.Api_tanty.modules.comptabilite.domain.decaissement.aggregate.Decaissement;
import org.springframework.stereotype.Component;

@Component
public class DecaissementMapper {

    public DecaissementResponse versResponse(Decaissement d) {
        return new DecaissementResponse(d.getId(), d.getNumeroDecaissement(),
            d.getTypeDepense().name(), d.getStatut().name(),
            d.getMatriculeProposant(), d.getMatriculeValidateurCG(), d.getMatriculeValidateurDG(),
            d.getMatriculeExecuteur(), d.getMontantFCFA(), d.getBeneficiaire(),
            d.getDateDepense(), d.getMotif(), d.getMotifAnnulation(), d.getDateCreation());
    }
}
