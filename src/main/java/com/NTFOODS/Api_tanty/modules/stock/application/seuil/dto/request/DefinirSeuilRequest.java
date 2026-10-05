package com.NTFOODS.Api_tanty.modules.stock.application.seuil.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record DefinirSeuilRequest(
    @NotBlank String codeProduit,
    @NotBlank(message = "Le type d'entrepôt est obligatoire (CENTRAL ou TAMPON)") String typeEntrepot,
    @NotNull Long seuilReapprovisionnement
) {}
