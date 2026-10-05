package com.NTFOODS.Api_tanty.modules.commercial.application.feuillroute.dto.response;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

/**
 * Tournée telle qu'elle est rendue au terminal.
 *
 * <p>Aucun statut de tournée n'y figure : c'est la session qui porte l'état de la
 * journée, et {@code numeroSession} dit laquelle interroger. Les coordonnées
 * relevées à la visite ont disparu avec la saisie qui les produisait.</p>
 */
public record FeuillRouteResponse(
    Long id,
    String numeroFeuillRoute,
    String numeroSession,
    String matriculeCommercial,
    LocalDate date,
    int nombreVisites,
    int nombreNonVisites,
    boolean tousArretsTranches,
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
        LocalDateTime heureEffective
    ) {}
}
