package com.NTFOODS.Api_tanty.modules.production.presentation.controller;

import com.NTFOODS.Api_tanty.modules.production.application.dashboard.usecase.DashboardProductionUseCase;
import com.NTFOODS.Api_tanty.shared.application.dto.ApiResponse;
import com.NTFOODS.Api_tanty.shared.infrastructure.security.AuthenticatedUserResolver;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/production/dashboard")
@Tag(name = "Production — Dashboard", description = "Tableaux de bord de la production")
@SecurityRequirement(name = "bearerAuth")
public class DashboardProductionController {

    private final DashboardProductionUseCase dashboardUseCase;
    private final AuthenticatedUserResolver userResolver;

    public DashboardProductionController(DashboardProductionUseCase dashboardUseCase,
                                          AuthenticatedUserResolver userResolver) {
        this.dashboardUseCase = dashboardUseCase;
        this.userResolver = userResolver;
    }

    @GetMapping("/chef")
    @PreAuthorize("hasAnyRole('CHEF_PRODUCTION', 'DIRECTEUR_GENERAL', 'ADMIN')")
    @Operation(summary = "Dashboard Chef de Production",
        description = "KPIs: quantités produites (kg + cartons), OF en cours/planifiés/honorés, taux d'avancement PPH, lots en attente de validation")
    public ResponseEntity<ApiResponse<DashboardProductionUseCase.DashboardChefProduction>> dashboardChef() {
        return ResponseEntity.ok(ApiResponse.succes(dashboardUseCase.executerChef(), "Dashboard Chef de Production"));
    }

    @GetMapping("/agent")
    @PreAuthorize("hasAnyRole('RESPONSABLE_SALLE', 'CHEF_PRODUCTION', 'ADMIN')")
    @Operation(summary = "Dashboard responsable de salle",
        description = "Tâches du jour: OF en cours, fiches à enregistrer, avancement par lot")
    public ResponseEntity<ApiResponse<DashboardProductionUseCase.DashboardAgentProduction>> dashboardAgent() {
        String matricule = userResolver.getMatriculeCourant();
        return ResponseEntity.ok(ApiResponse.succes(dashboardUseCase.executerAgent(matricule),
            "Dashboard responsable de salle"));
    }
}
