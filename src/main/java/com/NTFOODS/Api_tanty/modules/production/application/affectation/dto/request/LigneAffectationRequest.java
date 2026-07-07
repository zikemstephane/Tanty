package com.NTFOODS.Api_tanty.modules.production.application.affectation.dto.request;

import com.NTFOODS.Api_tanty.modules.production.domain.affectation.enums.CodePoste;
import com.NTFOODS.Api_tanty.modules.production.domain.affectation.enums.DureeAffectation;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;

import java.util.List;

public record LigneAffectationRequest(
    @NotNull CodePoste poste,
    @NotEmpty List<String> matriculesEmployes,
    @NotNull DureeAffectation duree
) {}
