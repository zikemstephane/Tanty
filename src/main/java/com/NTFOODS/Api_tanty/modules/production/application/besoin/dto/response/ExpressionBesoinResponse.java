package com.NTFOODS.Api_tanty.modules.production.application.besoin.dto.response;

import com.NTFOODS.Api_tanty.modules.production.domain.besoin.enums.StatutBesoin;
import com.NTFOODS.Api_tanty.modules.production.domain.besoin.enums.TypeBesoin;

import java.time.LocalDate;
import java.time.LocalDateTime;

public record ExpressionBesoinResponse(
    Long id,
    String numero,
    String referencePPH,
    TypeBesoin type,
    String codeProduit,
    String designationProduit,
    double quantiteDemandee,
    String unite,
    String matriculeDemandeur,
    LocalDate dateBesoin,
    String motif,
    StatutBesoin statut,
    String matriculeValidateur,
    String motifRejet,
    LocalDateTime dateCreation,
    LocalDateTime dateModification
) {}
