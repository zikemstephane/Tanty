package com.NTFOODS.Api_tanty.modules.commercial.application.feuillroute.dto.response;

import com.NTFOODS.Api_tanty.modules.commercial.domain.feuillroute.enums.StatutArret;
import com.NTFOODS.Api_tanty.modules.commercial.domain.feuillroute.enums.StatutFeuillRoute;
import com.NTFOODS.Api_tanty.modules.commercial.domain.precommande.enums.InfoClientEnum;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

public record FeuillRouteResponse(
    Long id,
    String numeroFeuillRoute,
    String matriculeCommercial,
    LocalDate date,
    String statut,
    String matriculeCloture,
    LocalDateTime heureCloture,
    int nombreArretVises,
    int nombreArretNonVises,
    List<ArretResponse> arrets
) {
    public record ArretResponse(
        Long id,
        String codeClient,
        int ordre,
        String statut,
        String numeroVente,
        String codeInfoClient,
        Integer quantiteInfo,
        String observations,
        LocalDateTime heurePlanifiee,
        LocalDateTime heureEffective,
        String coordonneesGPS
    ) {}
}
