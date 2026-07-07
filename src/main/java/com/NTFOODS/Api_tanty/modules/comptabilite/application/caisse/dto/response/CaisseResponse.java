package com.NTFOODS.Api_tanty.modules.comptabilite.application.caisse.dto.response;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

public record CaisseResponse(
    Long id,
    LocalDate date,
    double soldeCourant,
    double seuilSecurisation,
    double totalEntrees,
    double totalSorties,
    List<LigneResponse> lignes
) {
    public record LigneResponse(String referenceOperation, String typeMouvement, double montant,
                                String motif, LocalDateTime dateOperation, String matriculeOperateur) {}
}
