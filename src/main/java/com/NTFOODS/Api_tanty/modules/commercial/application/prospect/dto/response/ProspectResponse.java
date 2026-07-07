package com.NTFOODS.Api_tanty.modules.commercial.application.prospect.dto.response;

import com.NTFOODS.Api_tanty.modules.commercial.domain.prospect.enums.CanalProspection;
import com.NTFOODS.Api_tanty.modules.commercial.domain.prospect.enums.StatutProspect;

import java.time.LocalDate;
import java.time.LocalDateTime;

public record ProspectResponse(
    Long id,
    String codeProspect,
    String nom,
    String telephone,
    String adresse,
    String localite,
    String matriculeCommercial,
    String canal,
    String statut,
    String observations,
    String codeClientConverti,
    LocalDate dateProspection,
    LocalDateTime dateCreation
) {}
