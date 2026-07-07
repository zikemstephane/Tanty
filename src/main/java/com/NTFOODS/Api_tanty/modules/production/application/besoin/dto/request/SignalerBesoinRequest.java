package com.NTFOODS.Api_tanty.modules.production.application.besoin.dto.request;

import com.NTFOODS.Api_tanty.modules.production.domain.besoin.enums.TypeBesoin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Positive;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;

public record SignalerBesoinRequest(
    @NotBlank String numero,
    String referencePPH,
    TypeBesoin type,
    @NotBlank String codeProduit,
    @NotBlank String designationProduit,
    @Positive double quantiteDemandee,
    @NotBlank String unite,
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate dateBesoin,
    String motif
) {}
