package com.NTFOODS.Api_tanty.modules.production.application.affectation.dto.request;

import com.NTFOODS.Api_tanty.modules.production.domain.affectation.enums.CodePoste;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;

import java.util.List;

public record SaisirRealisationPosteRequest(
    @NotNull Long affectationId,
    @NotNull CodePoste poste,
    @Min(0) double quantiteRealisee,
    @Valid List<ProductionIndividuelleRequest> productionsIndividuelles,
    @Valid DetailsRealisationPosteRequest details
) {}
