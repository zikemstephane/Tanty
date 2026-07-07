package com.NTFOODS.Api_tanty.modules.production.application.fiche.dto.request;

import jakarta.validation.constraints.NotEmpty;

import java.util.List;

/** Correction des chiffres de production — refusée si la fiche est verrouillée. */
public record ModifierFicheRequest(
    @NotEmpty List<EnregistrerFicheRequest.LigneFicheDto> lignes
) {}
