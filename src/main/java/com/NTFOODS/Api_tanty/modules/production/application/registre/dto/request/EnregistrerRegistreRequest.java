package com.NTFOODS.Api_tanty.modules.production.application.registre.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import java.time.LocalDate;
import java.util.List;

public record EnregistrerRegistreRequest(
    @NotBlank String referencePPH,
    LocalDate date,
    @NotEmpty List<LigneRegistreRequest> lignes
) {
    public record LigneRegistreRequest(
        @NotBlank String matriculeEmploye,
        @NotBlank String nomEmploye,
        @NotBlank String poste,
        boolean present,
        String motifAbsence,
        String heureArrivee,
        double qteRealisee,
        String unite
    ) {}
}
