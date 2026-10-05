package com.NTFOODS.Api_tanty.modules.stock.application.inventaire.dto.request;

import jakarta.validation.constraints.NotBlank;

public record OuvrirInventaireRequest(
    @NotBlank(message = "Le type est obligatoire (COMPLET, TOURNANT, CIBLE, SURPRISE)") String type,
    @NotBlank(message = "Le code entrepôt est obligatoire") String codeEntrepot,
    String perimetreGamme,
    String perimetreProduit
) {}
