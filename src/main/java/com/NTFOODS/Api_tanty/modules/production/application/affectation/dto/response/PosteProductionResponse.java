package com.NTFOODS.Api_tanty.modules.production.application.affectation.dto.response;

import java.util.Set;

public record PosteProductionResponse(
    Long id,
    String code,
    String libelle,
    int effectifReference,
    double productionParHeure,
    double productionJournaliere,
    String unite,
    boolean actif,
    int ordre,
    Set<String> categoriesMetier
) {}
