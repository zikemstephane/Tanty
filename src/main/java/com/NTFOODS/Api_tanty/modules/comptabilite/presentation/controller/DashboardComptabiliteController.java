package com.NTFOODS.Api_tanty.modules.comptabilite.presentation.controller;

import com.NTFOODS.Api_tanty.modules.comptabilite.application.dashboard.usecase.DashboardComptabiliteUseCase;
import com.NTFOODS.Api_tanty.shared.application.dto.ApiResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/comptabilite/dashboard")
@Tag(name = "Comptabilité — Dashboard", description = "Tableau de bord financier (KPIs temps réel)")
@SecurityRequirement(name = "bearerAuth")
public class DashboardComptabiliteController {

    private final DashboardComptabiliteUseCase dashboardUseCase;

    public DashboardComptabiliteController(DashboardComptabiliteUseCase dashboardUseCase) {
        this.dashboardUseCase = dashboardUseCase;
    }

    @GetMapping
    @PreAuthorize("hasAnyRole('COMPTABLE', 'CONTROLEUR_GENERAL', 'DIRECTEUR_GENERAL', 'ADMIN')")
    @Operation(summary = "Dashboard Comptabilité",
        description = "Solde caisse, factures émises/payées/impayées, décaissements, versements en attente, alertes rouges, primes à verser")
    public ResponseEntity<ApiResponse<DashboardComptabiliteUseCase.DashboardComptabilite>> dashboard() {
        return ResponseEntity.ok(ApiResponse.succes(dashboardUseCase.executer(), "Dashboard Comptabilité"));
    }
}
