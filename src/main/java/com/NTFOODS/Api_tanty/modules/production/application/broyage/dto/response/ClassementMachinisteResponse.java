package com.NTFOODS.Api_tanty.modules.production.application.broyage.dto.response;

import java.util.List;

public record ClassementMachinisteResponse(
    List<LigneClassement> classements,
    String periode
) {
    public record LigneClassement(
        int rang,
        String machinisteMatricule,
        String nomMachiniste,
        double productiviteKg,
        double objectifTotalKg,
        double tauxRealisationMoyen,
        double efficacite,
        int nbJours,
        List<String> operations
    ) {}
}
