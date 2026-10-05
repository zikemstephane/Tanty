package com.NTFOODS.Api_tanty.modules.commercialisation.application.dto;

import com.NTFOODS.Api_tanty.modules.commercial.domain.client.enums.TypeClient;
import com.NTFOODS.Api_tanty.modules.commercialisation.domain.enums.StatutPromotion;
import com.NTFOODS.Api_tanty.modules.commercialisation.domain.enums.TypePromotion;

import java.time.LocalDate;

public record PromotionResponse(
    Long id,
    String code,
    String libelle,
    String description,
    TypePromotion type,
    double valeurReduction,
    String codeProduit,
    TypeClient segmentCible,
    LocalDate dateDebut,
    LocalDate dateFin,
    StatutPromotion statut,
    int nombreUtilisations,
    int limiteUtilisations,
    String matriculeCreateur,
    LocalDate dateCreation
) {}
