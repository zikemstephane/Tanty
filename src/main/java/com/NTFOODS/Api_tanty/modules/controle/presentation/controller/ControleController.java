package com.NTFOODS.Api_tanty.modules.controle.presentation.controller;

import com.NTFOODS.Api_tanty.modules.controle.application.ControleDashboardService;
import com.NTFOODS.Api_tanty.modules.controle.application.dto.ControleDashboardResponse;
import com.NTFOODS.Api_tanty.modules.controle.application.dto.ControleDashboardResponse.BudgetResponse;
import com.NTFOODS.Api_tanty.modules.controle.application.dto.ControleDashboardResponse.MargeProduitResponse;
import com.NTFOODS.Api_tanty.modules.controle.application.dto.ControleDashboardResponse.VarianceResponse;
import com.NTFOODS.Api_tanty.shared.application.dto.ApiResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/controle")
@Tag(name = "Contrôle de Gestion — Dashboard", description = "Audit, marges, budget, variance, décaissements")
@SecurityRequirement(name = "bearerAuth")
@PreAuthorize("hasAnyRole('CONTROLEUR_GENERAL', 'COMPTABLE', 'DIRECTEUR_GENERAL', 'ADMIN')")
public class ControleController {

    private final ControleDashboardService service;

    public ControleController(ControleDashboardService service) {
        this.service = service;
    }

    @GetMapping("/dashboard")
    @Operation(summary = "Dashboard Contrôle de Gestion",
        description = "Trésorerie, écarts rouges, ventes à crédit, marges, budget, variance, décaissements en attente, anomalies")
    public ResponseEntity<ApiResponse<ControleDashboardResponse>> dashboard() {
        return ResponseEntity.ok(ApiResponse.succes(service.dashboard(), "Dashboard Contrôle"));
    }

    @GetMapping("/marges")
    @Operation(summary = "Marges par produit (prix vente, coût revient, marge, marge totale)")
    public ResponseEntity<ApiResponse<List<MargeProduitResponse>>> marges() {
        return ResponseEntity.ok(ApiResponse.succes(service.marges(), "Marges par produit"));
    }

    @GetMapping("/budget")
    @Operation(summary = "Budget par poste (budget, engagé, réalisé)")
    public ResponseEntity<ApiResponse<List<BudgetResponse>>> budget() {
        return ResponseEntity.ok(ApiResponse.succes(service.budget(), "Budget"));
    }

    @GetMapping("/variances")
    @Operation(summary = "Variances coûts standard vs réel")
    public ResponseEntity<ApiResponse<List<VarianceResponse>>> variances() {
        return ResponseEntity.ok(ApiResponse.succes(service.variances(), "Variances"));
    }
}
