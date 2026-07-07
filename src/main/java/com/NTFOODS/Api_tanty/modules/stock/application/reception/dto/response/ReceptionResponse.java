package com.NTFOODS.Api_tanty.modules.stock.application.reception.dto.response;

import java.time.LocalDate;
import java.util.List;

public record ReceptionResponse(
    Long id,
    String numero,
    String source,
    String categorie,
    String codeFournisseur,
    String numeroBonCommande,
    String numeroLotProduction,
    String codeEntrepotCible,
    String statut,
    String statutLibelle,
    boolean ecartSignificatif,
    String matriculeValidateur1,
    String matriculeValidateur2,
    String motifRejet,
    LocalDate dateReception,
    List<LigneResponse> lignes
) {
    public record LigneResponse(
        String id,
        String codeProduit,
        String sku,
        long quantiteAttendue,
        long quantiteRecue,
        long ecart,
        double pourcentageEcart
    ) {}
}
