package com.NTFOODS.Api_tanty.modules.stock.application.retour.dto.request;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;

import java.util.List;

public record EnregistrerRetourRequest(
    @NotBlank(message = "Le matricule du commercial est obligatoire") String matriculeCommercial,
    @NotEmpty(message = "Au moins une ligne est requise") @Valid List<LigneRetourRequest> lignes
) {
    public record LigneRetourRequest(
        @NotBlank String codeProduit,
        String sku,
        @NotNull Long quantite,
        String motif
    ) {}
}
