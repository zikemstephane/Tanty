package com.NTFOODS.Api_tanty.modules.production.application.registre.dto.response;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

public record RegistreProductionResponse(
    Long id,
    String referencePPH,
    LocalDate date,
    String matriculeChefProduction,
    List<LigneRegistreResponse> lignes,
    long nbPresents,
    long nbAbsents,
    LocalDateTime dateCreation,
    LocalDateTime dateModification
) {
    public record LigneRegistreResponse(
        String matriculeEmploye,
        String nomEmploye,
        String poste,
        boolean present,
        String motifAbsence,
        String heureArrivee,
        double qteRealisee,
        String unite
    ) {}
}
