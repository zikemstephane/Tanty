package com.NTFOODS.Api_tanty.modules.stock.presentation.controller;

import com.NTFOODS.Api_tanty.modules.stock.application.alerte.dto.response.AlerteResponse;
import com.NTFOODS.Api_tanty.modules.stock.application.alerte.usecase.ListerAlertesUseCase;
import com.NTFOODS.Api_tanty.modules.stock.application.alerte.usecase.TraiterAlerteUseCase;
import com.NTFOODS.Api_tanty.shared.application.dto.ApiResponse;
import com.NTFOODS.Api_tanty.shared.infrastructure.security.AuthenticatedUserResolver;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/stock/alertes")
@Tag(name = "Stock — Alertes", description = "Alertes actionnables (journal, traitement)")
@SecurityRequirement(name = "bearerAuth")
public class AlerteController {

    private final ListerAlertesUseCase listerAlertesUseCase;
    private final TraiterAlerteUseCase traiterAlerteUseCase;
    private final AuthenticatedUserResolver userResolver;

    public AlerteController(ListerAlertesUseCase listerAlertesUseCase, TraiterAlerteUseCase traiterAlerteUseCase,
                          AuthenticatedUserResolver userResolver) {
        this.listerAlertesUseCase = listerAlertesUseCase;
        this.traiterAlerteUseCase = traiterAlerteUseCase;
        this.userResolver = userResolver;
    }

    @GetMapping
    @PreAuthorize("isAuthenticated()")
    @Operation(summary = "Journal des alertes (filtres : statut, role)")
    public ResponseEntity<ApiResponse<List<AlerteResponse>>> lister(
        @RequestParam(required = false) String statut,
        @RequestParam(required = false) String role) {
        List<AlerteResponse> alertes = listerAlertesUseCase.executer(statut, role);
        return ResponseEntity.ok(ApiResponse.succes(alertes, alertes.size() + " alerte(s)"));
    }

    @PostMapping("/{reference}/lue")
    @PreAuthorize("isAuthenticated()")
    @Operation(summary = "Marquer une alerte comme lue")
    public ResponseEntity<ApiResponse<AlerteResponse>> marquerLue(@PathVariable String reference) {
        AlerteResponse response = traiterAlerteUseCase.executer(reference,
            TraiterAlerteUseCase.Action.MARQUER_LUE, userResolver.getUtilisateurCourant());
        return ResponseEntity.ok(ApiResponse.succes(response, "Alerte marquée comme lue"));
    }

    @PostMapping("/{reference}/traiter")
    @PreAuthorize("hasAnyRole('COMMERCIAL', 'GESTIONNAIRE_STOCK', 'DIRECTEUR_GENERAL', 'CONTROLEUR_GENERAL', 'ADMIN')")
    @Operation(summary = "Marquer une alerte comme traitée")
    public ResponseEntity<ApiResponse<AlerteResponse>> traiter(@PathVariable String reference) {
        AlerteResponse response = traiterAlerteUseCase.executer(reference,
            TraiterAlerteUseCase.Action.TRAITER, userResolver.getUtilisateurCourant());
        return ResponseEntity.ok(ApiResponse.succes(response, "Alerte traitée"));
    }
}
