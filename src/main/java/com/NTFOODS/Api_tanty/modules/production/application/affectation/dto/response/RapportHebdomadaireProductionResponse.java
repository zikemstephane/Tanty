package com.NTFOODS.Api_tanty.modules.production.application.affectation.dto.response;

import java.time.LocalDate;
import java.util.List;

/**
 * Rapport hebdomadaire de production pour l'atelier.
 */
public record RapportHebdomadaireProductionResponse(
    LocalDate debut,
    LocalDate fin,
    String periodeLibelle,
    int nbFutsNets,
    double poidsTotalPoudreKg,
    double productionTotaleKg,
    int nbSessionsDosage,
    int nbJoursAffectes,
    List<ClassementExecuteurResponse> classementExecuteurs,
    List<ComparaisonFutsProductionResponse> comparaisonsJournalieres
) {}
