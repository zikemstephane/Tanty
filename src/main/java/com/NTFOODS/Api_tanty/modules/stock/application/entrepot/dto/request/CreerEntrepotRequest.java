package com.NTFOODS.Api_tanty.modules.stock.application.entrepot.dto.request;

import jakarta.validation.constraints.NotBlank;

/**
 * Requête — création d'un entrepôt.
 * Pour un entrepôt MOBILE, {@code matriculeCommercial} est obligatoire ;
 * le code et le libellé sont alors dérivés automatiquement.
 */
public record CreerEntrepotRequest(
    @NotBlank(message = "Le type d'entrepôt est obligatoire") String type,
    String code,
    String libelle,
    String matriculeCommercial
) {}
