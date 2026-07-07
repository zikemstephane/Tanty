package com.NTFOODS.Api_tanty.modules.stock.application.inventaire.dto.response;

import java.time.LocalDate;
import java.util.List;

public record InventaireResponse(
    Long id,
    String numero,
    String type,
    String typeLibelle,
    String codeEntrepot,
    String perimetre,
    String statut,
    String statutLibelle,
    boolean ecartSignificatif,
    String matriculeInitiateur,
    String matriculeValidateurDG,
    String justificationEcarts,
    LocalDate dateInventaire,
    List<LigneResponse> lignes
) {
    public record LigneResponse(
        String id,
        String codeProduit,
        String emplacement,
        long quantiteTheorique,
        Long quantiteComptee,
        long ecart
    ) {}
}
