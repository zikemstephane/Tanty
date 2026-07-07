package com.NTFOODS.Api_tanty.modules.production.application.broyage.dto.response;

import com.NTFOODS.Api_tanty.modules.production.domain.broyage.enums.StatutSessionBroyage;
import com.NTFOODS.Api_tanty.modules.production.domain.broyage.enums.TypePoudre;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

public record SessionBroyageResponse(
    Long id,
    String referencePPH,
    LocalDate date,
    TypePoudre typePoudre,
    String typePoudreLibelle,
    double objectifJournalierKg,
    String matriculeChefMachiniste,
    List<ObjectifMachinisteResponse> objectifsMachinistes,
    List<RealisationMachinisteResponse> realisations,
    Double quantiteNetteBroyeeKg,
    Double pertesKg,
    Double pctPertes,
    double totalRealiseKg,
    double depassementAutoriseKg,
    boolean journeeValidee,
    String justificatifNonValidation,
    StatutSessionBroyage statut,
    LocalDateTime dateCreation,
    LocalDateTime dateModification
) {
    public record ObjectifMachinisteResponse(
        String machinisteMatricule,
        String nomMachiniste,
        String operation,
        String operationLibelle,
        double objectifKg
    ) {}

    public record RealisationMachinisteResponse(
        String machinisteMatricule,
        String nomMachiniste,
        String operation,
        String operationLibelle,
        double objectifKg,
        double realiseKg,
        double pctRealisation
    ) {}
}
