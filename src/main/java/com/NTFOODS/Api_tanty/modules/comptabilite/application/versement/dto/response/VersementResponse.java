package com.NTFOODS.Api_tanty.modules.comptabilite.application.versement.dto.response;

import java.time.LocalDate;

public record VersementResponse(
    Long id,
    String referenceVersement,
    String matriculeCommercial,
    double montantAttendu,
    double cashVerse,
    double ecart,
    boolean alerteRouge,
    String typeVersement,
    LocalDate date,
    String statut,
    String justificationEcart,
    String matriculeValidateur
) {}
