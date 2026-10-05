package com.NTFOODS.Api_tanty.modules.stock.application.lot.dto.response;

import java.time.LocalDate;
import java.time.LocalDateTime;

/** Réponse — lot de stock, avec ce que la date de péremption implique aujourd'hui. */
public record LotStockResponse(
    Long id,
    String numeroLot,
    String codeProduit,
    String sku,
    String designation,
    String codeFournisseur,
    String codeEntrepot,
    LocalDate dateFabrication,
    LocalDate datePeremption,
    long quantiteInitiale,
    long quantiteRestante,
    String statut,
    String statutLibelle,
    boolean perime,
    boolean perimeBientot,
    /** Négatif si la date est passée ; null si le produit n'a pas de péremption. */
    Long joursAvantPeremption,
    String observations,
    LocalDateTime dateCreation,
    LocalDateTime dateDernierMouvement
) {}
