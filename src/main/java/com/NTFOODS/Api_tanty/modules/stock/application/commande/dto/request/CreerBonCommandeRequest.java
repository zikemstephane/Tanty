package com.NTFOODS.Api_tanty.modules.stock.application.commande.dto.request;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

public record CreerBonCommandeRequest(
    /** Optionnel : un BC peut être créé sans fournisseur identifié (cf. BonCommande.creer, qui retient "NON_DEFINI"). */
    String codeFournisseur,
    @NotNull(message = "La date de commande est obligatoire") LocalDate dateCommande,
    LocalDate dateLivraisonPrevue,
    /** Période d'exécution de la production planifiée à partir de ce BC — facultative, solidaire (les deux ou aucune). */
    LocalDate dateDebutProduction,
    LocalDate dateFinProduction,
    @NotEmpty(message = "Au moins une ligne est requise") @Valid List<LigneRequest> lignes
) {
    public record LigneRequest(
        @NotBlank(message = "Le code produit est obligatoire") String codeProduit,
        String designation,
        String sku,
        @NotNull(message = "La quantité commandée est obligatoire") Long quantiteCommandee,
        BigDecimal prixUnitaireHT
    ) {}
}
