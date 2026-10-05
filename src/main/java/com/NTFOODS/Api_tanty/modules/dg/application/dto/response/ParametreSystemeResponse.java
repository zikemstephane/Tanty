package com.NTFOODS.Api_tanty.modules.dg.application.dto.response;

public record ParametreSystemeResponse(
    Long id,
    String cle,
    double valeurNumerique,
    String description,
    String modifiePar,
    java.time.LocalDateTime horodatage
) {}
