package com.NTFOODS.Api_tanty.modules.stock.application.dotation.dto.request;

import jakarta.validation.constraints.NotBlank;

/** Requête — modification d'une ligne de dotation (quantité et/ou conditionnement) avec motif. */
public record DecisionLigneRequest(
    @NotBlank String idLigne,
    Long nouvelleQuantite,
    String nouveauSku,
    String motif
) {}
