package com.NTFOODS.Api_tanty.modules.comptabilite.application.prime.mapper;

import com.NTFOODS.Api_tanty.modules.comptabilite.application.prime.dto.response.PrimeResponse;
import com.NTFOODS.Api_tanty.modules.comptabilite.domain.prime.aggregate.PrimeCommerciale;
import org.springframework.stereotype.Component;

@Component
public class PrimeMapper {
    public PrimeResponse versResponse(PrimeCommerciale p) {
        return new PrimeResponse(p.getId(), p.getReferencePrime(), p.getMatriculeCommercial(),
            p.getSemaineDebut(), p.getSemaineFin(), p.getObjectifGlobalFCFA(), p.getTotalVentesGlobalFCFA(),
            p.getTauxRealisationGlobalPourcent(), p.getObjectifsParGammeFCFA(), p.getRealisationsParGammeFCFA(),
            p.getTauxParGammePourcent(), p.isEligible(), p.getMontantPrimeFCFA(), p.getStatut().name(),
            p.getMatriculeValidateur());
    }
}
