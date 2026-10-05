package com.NTFOODS.Api_tanty.modules.stock.application.lot.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;

import java.time.LocalDate;

/**
 * Requête — déclaration d'un lot.
 *
 * Le numéro de lot est facultatif : sans lui, il est engendré. Il est en
 * revanche unique — deux arrivages ne peuvent pas porter la même référence,
 * sans quoi la traçabilité ne remonte plus à un arrivage précis.
 */
public record CreerLotRequest(
    @Size(max = 40) String numeroLot,
    @NotBlank(message = "Le code produit est obligatoire") String codeProduit,
    @Size(max = 60) String sku,
    @Size(max = 200) String designation,
    @Size(max = 50) String codeFournisseur,
    @NotBlank(message = "L'entrepôt est obligatoire") String codeEntrepot,
    LocalDate dateFabrication,
    LocalDate datePeremption,
    @Positive(message = "La quantité du lot doit être strictement positive") long quantiteInitiale,
    @Size(max = 1000) String observations
) {}
