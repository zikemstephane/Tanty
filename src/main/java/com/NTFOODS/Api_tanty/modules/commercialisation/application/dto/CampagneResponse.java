package com.NTFOODS.Api_tanty.modules.commercialisation.application.dto;

import com.NTFOODS.Api_tanty.modules.commercialisation.domain.enums.StatutCampagne;
import com.NTFOODS.Api_tanty.modules.commercialisation.domain.enums.TypeCampagne;

import java.time.LocalDate;
import java.util.List;

public record CampagneResponse(
    Long id,
    String code,
    String libelle,
    String description,
    TypeCampagne type,
    LocalDate dateDebut,
    LocalDate dateFin,
    String zoneCible,
    double budgetPrevuFCFA,
    double budgetDepenseFCFA,
    double tauxConsommationBudget,
    StatutCampagne statut,
    String matriculeCreateur,
    LocalDate dateCreation,
    List<String> promotionsLiees
) {}
