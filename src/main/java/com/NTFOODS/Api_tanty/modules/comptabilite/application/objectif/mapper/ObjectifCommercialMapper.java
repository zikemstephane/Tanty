package com.NTFOODS.Api_tanty.modules.comptabilite.application.objectif.mapper;

import com.NTFOODS.Api_tanty.modules.comptabilite.application.objectif.dto.response.ObjectifCommercialResponse;
import com.NTFOODS.Api_tanty.modules.comptabilite.application.prime.service.BaremePrimeService;
import com.NTFOODS.Api_tanty.modules.comptabilite.domain.objectif.aggregate.ObjectifCommercial;
import com.NTFOODS.Api_tanty.modules.comptabilite.domain.objectif.aggregate.RevisionObjectif;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class ObjectifCommercialMapper {

    private final BaremePrimeService baremePrimeService;

    public ObjectifCommercialMapper(BaremePrimeService baremePrimeService) {
        this.baremePrimeService = baremePrimeService;
    }

    public ObjectifCommercialResponse versResponse(ObjectifCommercial o) {
        List<ObjectifCommercialResponse.RevisionResponse> revisions = o.getRevisions().stream()
            .map(this::versResponse)
            .toList();
        var bareme = baremePrimeService.baremeApplicableA(o.getSemaineDebut());
        return new ObjectifCommercialResponse(o.getId(), o.getMatriculeCommercial(), o.getSemaineDebut(),
            o.getSemaineFin(), o.getObjectifGlobalFCFA(), o.objectifGlobalInitialFCFA(),
            o.getObjectifsParGammeFCFA(), o.getMatriculeAuteur(), o.aEteRevise(), revisions,
            bareme.getTauxGlobalMinimumPourcent(), bareme.getTauxGammeMinimumPourcent());
    }

    private ObjectifCommercialResponse.RevisionResponse versResponse(RevisionObjectif r) {
        return new ObjectifCommercialResponse.RevisionResponse(r.getId(), r.getObjectifGlobalAvantFCFA(),
            r.getObjectifGlobalApresFCFA(), r.variationFCFA(), r.estUnAbaissement(),
            r.getObjectifsParGammeAvantFCFA(), r.getMatriculeAuteur(), r.getMotif(), r.getDateRevision());
    }
}
