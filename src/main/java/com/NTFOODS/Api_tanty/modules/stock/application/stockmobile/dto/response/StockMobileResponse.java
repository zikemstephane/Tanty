package com.NTFOODS.Api_tanty.modules.stock.application.stockmobile.dto.response;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

public record StockMobileResponse(
    Long id,
    String matriculeCommercial,
    LocalDate dateSession,
    long totalVentes,
    long totalRestant,
    long totalAvaries,
    LocalDateTime dateModification,
    List<LigneResponse> lignes
) {
    public record LigneResponse(
        String codeProduit, String designation, String conditionnement,
        long quantiteDisponible, long quantiteInitiale, long quantiteVendue,
        long quantiteAvariee, long quantiteDonnee, LocalDateTime dateDernierMouvement
    ) {}
}
