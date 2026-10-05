package com.NTFOODS.Api_tanty.modules.stock.application.transfert.dto.request;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;

import java.util.List;

public record CreerTransfertRequest(
    String codeEntrepotSource,
    String codeEntrepotCible,
    @NotEmpty(message = "Au moins une ligne est requise") @Valid List<LigneTransfertRequest> lignes
) {
    public record LigneTransfertRequest(
        @NotBlank String codeProduit,
        String sku,
        @NotNull Long quantite
    ) {}
}
