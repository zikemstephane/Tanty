package com.NTFOODS.Api_tanty.modules.stock.application.retour.dto.request;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;

import java.util.List;

/**
 * Quantités réellement constatées par le gestionnaire.
 *
 * <p>Zéro est une valeur admise et signifiante : le produit annoncé n'a pas été
 * rapporté. C'est l'omission d'une ligne, et non la valeur zéro, qui est refusée.</p>
 */
public record CompterInvendusRequest(
    @NotEmpty(message = "Au moins une ligne comptée est requise") @Valid List<LigneComptageRequest> lignes
) {
    public record LigneComptageRequest(
        @NotBlank String codeProduit,
        @NotNull @PositiveOrZero(message = "La quantité comptée ne peut pas être négative") Long quantiteComptee
    ) {}
}
