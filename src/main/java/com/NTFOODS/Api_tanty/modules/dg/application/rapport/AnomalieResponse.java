package com.NTFOODS.Api_tanty.modules.dg.application.rapport;

public record AnomalieResponse(
    String categorie,
    String reference,
    String description,
    String matriculeConcerne
) {}
