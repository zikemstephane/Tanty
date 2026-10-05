package com.NTFOODS.Api_tanty.modules.stock.application.inventaire.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record SaisirComptageRequest(
    @NotBlank String idLigne,
    @NotNull Long quantiteComptee
) {}
