package com.NTFOODS.Api_tanty.modules.dg.application.rapport;

import java.time.LocalDateTime;

public record ValidationEnAttenteResponse(
    String type,
    String reference,
    String description,
    String matriculeConcerne,
    LocalDateTime depuis
) {}
