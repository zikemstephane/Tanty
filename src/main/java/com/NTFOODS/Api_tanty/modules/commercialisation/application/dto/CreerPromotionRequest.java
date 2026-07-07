package com.NTFOODS.Api_tanty.modules.commercialisation.application.dto;

import com.NTFOODS.Api_tanty.modules.commercial.domain.client.enums.TypeClient;
import com.NTFOODS.Api_tanty.modules.commercialisation.domain.enums.StatutPromotion;
import com.NTFOODS.Api_tanty.modules.commercialisation.domain.enums.TypePromotion;
import jakarta.validation.constraints.*;

import java.time.LocalDate;

public record CreerPromotionRequest(
    @NotBlank String code,
    @NotBlank String libelle,
    String description,
    @NotNull TypePromotion type,
    @DecimalMin(value = "0.0", inclusive = false) double valeurReduction,
    String codeProduit,
    TypeClient segmentCible,
    @NotNull LocalDate dateDebut,
    LocalDate dateFin,
    @Min(0) int limiteUtilisations
) {}
