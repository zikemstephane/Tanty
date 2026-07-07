package com.NTFOODS.Api_tanty.modules.commercial.presentation.controller;

import com.NTFOODS.Api_tanty.modules.commercial.application.dashboard.dto.FicheSyntheseResponse;
import com.NTFOODS.Api_tanty.modules.commercial.application.dashboard.usecase.DashboardCommercialUseCase;
import com.NTFOODS.Api_tanty.modules.commercial.application.dashboard.usecase.FicheSyntheseUseCase;
import com.NTFOODS.Api_tanty.modules.dg.application.dto.response.ClassementCommercialResponse;
import com.NTFOODS.Api_tanty.modules.dg.application.usecase.ObtenirClassementDgUseCase;
import com.NTFOODS.Api_tanty.shared.application.dto.ApiResponse;
import com.NTFOODS.Api_tanty.shared.infrastructure.security.AuthenticatedUserResolver;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/api/commercial/dashboard")
@Tag(name = "Commercial — Dashboard", description = "Tableaux de bord commerciaux (KPIs temps réel)")
@SecurityRequirement(name = "bearerAuth")
public class DashboardCommercialController {

    private final DashboardCommercialUseCase dashboardUseCase;
    private final FicheSyntheseUseCase ficheSyntheseUseCase;
    private final ObtenirClassementDgUseCase classementUseCase;
    private final AuthenticatedUserResolver userResolver;

    public DashboardCommercialController(DashboardCommercialUseCase dashboardUseCase,
                                          FicheSyntheseUseCase ficheSyntheseUseCase,
                                          ObtenirClassementDgUseCase classementUseCase,
                                          AuthenticatedUserResolver userResolver) {
        this.dashboardUseCase = dashboardUseCase;
        this.ficheSyntheseUseCase = ficheSyntheseUseCase;
        this.classementUseCase = classementUseCase;
        this.userResolver = userResolver;
    }

    @GetMapping("/commercial")
    @PreAuthorize("hasAnyRole('COMMERCIAL', 'DIRECTEUR_COMMERCIAL', 'DIRECTEUR_GENERAL', 'ADMIN')")
    @Operation(summary = "Dashboard Commercial",
        description = "KPIs: CA jour/semaine, nombre de ventes, taux de visite, top 5 produits, carburant semaine")
    public ResponseEntity<ApiResponse<DashboardCommercialUseCase.DashboardCommercial>> dashboardCommercial() {
        String matricule = userResolver.getMatriculeCourant();
        return ResponseEntity.ok(ApiResponse.succes(dashboardUseCase.executerCommercial(matricule),
            "Dashboard Commercial"));
    }

    @GetMapping("/dg")
    @PreAuthorize("hasAnyRole('DIRECTEUR_GENERAL', 'DIRECTEUR_COMMERCIAL', 'ADMIN')")
    @Operation(summary = "Dashboard DG — Vue globale",
        description = "KPIs: CA global jour/semaine, taux de visite global, retards recouvrement, rendement carburant, CA par commercial")
    public ResponseEntity<ApiResponse<DashboardCommercialUseCase.DashboardDG>> dashboardDG() {
        return ResponseEntity.ok(ApiResponse.succes(dashboardUseCase.executerDG(), "Dashboard DG"));
    }

    @GetMapping("/classement")
    @PreAuthorize("hasAnyRole('COMMERCIAL', 'DIRECTEUR_COMMERCIAL', 'DIRECTEUR_GENERAL', 'ADMIN')")
    @Operation(summary = "Classement des commerciaux par CA (mois courant)",
        description = "Accessible aux commerciaux pour voir leur positionnement par rapport aux autres")
    public ResponseEntity<ApiResponse<List<ClassementCommercialResponse>>> classement() {
        return ResponseEntity.ok(ApiResponse.succes(classementUseCase.executer(), "Classement commerciaux"));
    }

    @GetMapping("/fiche-synthese")
    @PreAuthorize("hasAnyRole('COMMERCIAL', 'DIRECTEUR_COMMERCIAL', 'DIRECTEUR_GENERAL', 'COMPTABLE', 'SECRETAIRE', 'ADMIN')")
    @Operation(summary = "Fiche de synthèse journalière",
        description = "Récapitulatif complet: CA, VAC, versements, recouvrements, caisse, stock par produit")
    public ResponseEntity<ApiResponse<FicheSyntheseResponse>> ficheSynthese(
        @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date) {
        return ResponseEntity.ok(ApiResponse.succes(ficheSyntheseUseCase.executer(date), "Fiche synthèse"));
    }
}
