package com.NTFOODS.Api_tanty.modules.stock.application.reception.dto.request;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;

import java.util.List;

public record CreerReceptionFournisseurRequest(
    @NotBlank(message = "La catégorie est obligatoire (MATIERE_PREMIERE ou CONSOMMABLE)") String categorie,
    @NotBlank(message = "Le code fournisseur est obligatoire") String codeFournisseur,
    @NotBlank(message = "Le numéro de BC lié est obligatoire") String numeroBonCommande,
    String codeEntrepotCible,
    @NotEmpty(message = "Au moins une ligne est requise") @Valid List<LigneReceptionRequest> lignes
) {
    public record LigneReceptionRequest(
        @NotBlank String codeProduit,
        String sku,
        @NotNull Long quantiteAttendue,
        @NotNull Long quantiteRecue,
        String idLigneBC
    ) {}
}
