package com.NTFOODS.Api_tanty.modules.stock.application.dotation.dto.request;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;

import java.util.List;

/**
 * Requête — soumission d'une dotation par un commercial (le soir, pour le lendemain).
 * Le matricule commercial est déduit de l'utilisateur authentifié.
 */
public record SoumettreDotationRequest(
    @NotEmpty(message = "Au moins une ligne est requise") @Valid List<LigneDotationRequest> lignes
) {
    public record LigneDotationRequest(
        @NotBlank String codeProduit,
        String sku,
        @NotNull Long quantiteDemandee
    ) {}
}
