package com.NTFOODS.Api_tanty.modules.comptabilite.application.objectif.dto.response;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

/**
 * Objectif hebdomadaire d'un commercial.
 *
 * <p>L'objectif initial voyage à côté de l'objectif courant : c'est la seule façon
 * pour un lecteur de savoir qu'une prime a été calculée sur une cible abaissée en
 * cours de route.</p>
 */
public record ObjectifCommercialResponse(
    Long id,
    String matriculeCommercial,
    LocalDate semaineDebut,
    LocalDate semaineFin,
    double objectifGlobalFCFA,
    double objectifGlobalInitialFCFA,
    Map<String, Double> objectifsParGammeFCFA,
    String matriculeAuteur,
    boolean revise,
    List<RevisionResponse> revisions,
    Double seuilPrimeGlobalPct,
    Double seuilPrimeGammePct
) {
    public record RevisionResponse(
        Long id,
        double objectifGlobalAvantFCFA,
        double objectifGlobalApresFCFA,
        double variationFCFA,
        boolean abaissement,
        Map<String, Double> objectifsParGammeAvantFCFA,
        String matriculeAuteur,
        String motif,
        LocalDateTime dateRevision
    ) {}
}
