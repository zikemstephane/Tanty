package com.NTFOODS.Api_tanty.modules.stock.application.sessioncommercial.dto.response;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

public record SessionCommercialResponse(
    Long id,
    String numeroSession,
    String matriculeCommercial,
    LocalDate datePreparation,
    LocalDate dateEffet,
    String statut,
    int nombreDotations,
    List<String> dotationsIds,
    String matriculeCloture,
    String matriculeValidateurSecretaire,
    String matriculeValidateurComptable,
    LocalDateTime heureOuverture,
    LocalDateTime heureCloture,
    LocalDateTime dateCreation
) {}
