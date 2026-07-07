package com.NTFOODS.Api_tanty.modules.commercial.application.feuillroute.mapper;

import com.NTFOODS.Api_tanty.modules.commercial.application.feuillroute.dto.response.FeuillRouteResponse;
import com.NTFOODS.Api_tanty.modules.commercial.domain.feuillroute.aggregate.FeuillRoute;
import org.springframework.stereotype.Component;

@Component
public class FeuillRouteMapper {

    public FeuillRouteResponse versResponse(FeuillRoute fr) {
        var arrets = fr.getArrets().stream()
            .map(a -> new FeuillRouteResponse.ArretResponse(
                a.getId(), a.getCodeClient(), a.getOrdre(), a.getStatut().name(),
                a.getNumeroVente(),
                a.getSaisie() != null && a.getSaisie().getCodeInfoClient() != null
                    ? a.getSaisie().getCodeInfoClient().name() : null,
                a.getSaisie() != null ? a.getSaisie().getQuantiteInfo() : null,
                a.getSaisie() != null ? a.getSaisie().getObservations() : null,
                a.getHeurePlanifiee(), a.getHeureEffective(), a.getCoordonneesGPS()))
            .toList();
        return new FeuillRouteResponse(fr.getId(), fr.getNumeroFeuillRoute(),
            fr.getMatriculeCommercial(), fr.getDate(), fr.getStatut().name(),
            fr.getMatriculeCloture(), fr.getHeureCloture(),
            fr.nombreArretVises(), fr.nombreArretNonVises(), arrets);
    }
}
