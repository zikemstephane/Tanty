package com.NTFOODS.Api_tanty.modules.stock.application.fournisseur.dto.request;

import jakarta.validation.constraints.NotBlank;

public record CreerFournisseurRequest(
    @NotBlank(message = "Le code est obligatoire") String code,
    @NotBlank(message = "La raison sociale est obligatoire") String raisonSociale,
    String telephone,
    String email,
    String adresse
) {}
