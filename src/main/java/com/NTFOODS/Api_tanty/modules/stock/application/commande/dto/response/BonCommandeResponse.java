package com.NTFOODS.Api_tanty.modules.stock.application.commande.dto.response;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

public record BonCommandeResponse(
    Long id,
    String numero,
    String codeFournisseur,
    String statut,
    String statutLibelle,
    LocalDate dateCommande,
    LocalDate dateLivraisonPrevue,
    boolean enRetard,
    BigDecimal montantTotalHT,
    String matriculeCreateur,
    List<LigneResponse> lignes,
    LocalDateTime dateCreation
) {
    public record LigneResponse(
        String id,
        String codeProduit,
        String designation,
        String sku,
        long quantiteCommandee,
        long quantiteRecue,
        BigDecimal prixUnitaireHT
    ) {}
}
