package com.NTFOODS.Api_tanty.modules.production.application.registre.dto.request;

import jakarta.validation.constraints.NotEmpty;

import java.util.List;

/** Correction des lignes d'un registre — refusée si le registre ne décrit pas la journée en cours. */
public record ModifierRegistreRequest(
    @NotEmpty List<EnregistrerRegistreRequest.LigneRegistreRequest> lignes
) {}
