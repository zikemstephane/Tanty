package com.NTFOODS.Api_tanty.modules.secretaire.presentation.controller;

import com.NTFOODS.Api_tanty.modules.secretaire.application.SecretaireDashboardService;
import com.NTFOODS.Api_tanty.modules.secretaire.application.dto.SecretaireDashboardResponse;
import com.NTFOODS.Api_tanty.modules.secretaire.application.dto.SecretaireDashboardResponse.*;
import com.NTFOODS.Api_tanty.shared.application.dto.ApiResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/secretaire")
@Tag(name = "Secrétaire — Dashboard", description = "Réconciliation ventes/cash, petite caisse, micro-stock, documents terrain")
@SecurityRequirement(name = "bearerAuth")
@PreAuthorize("hasAnyRole('SECRETAIRE', 'COMPTABLE', 'DIRECTEUR_GENERAL', 'ADMIN')")
public class SecretaireController {

    private final SecretaireDashboardService service;

    public SecretaireController(SecretaireDashboardService service) {
        this.service = service;
    }

    @GetMapping("/dashboard")
    @Operation(summary = "Dashboard Secrétaire",
        description = "Versements en attente, alertes rouges, petite caisse, micro-stock, documents terrain reçus")
    public ResponseEntity<ApiResponse<SecretaireDashboardResponse>> dashboard() {
        return ResponseEntity.ok(ApiResponse.succes(service.dashboard(), "Dashboard Secrétaire"));
    }

    @GetMapping("/petite-caisse")
    @Operation(summary = "Mouvements de petite caisse (entrées/sorties)")
    public ResponseEntity<ApiResponse<List<PetiteCaisseResponse>>> petiteCaisse() {
        return ResponseEntity.ok(ApiResponse.succes(service.petiteCaisse(), "Petite caisse"));
    }

    @GetMapping("/micro-stock")
    @Operation(summary = "Micro-stock secrétariat (ventes comptant accueil)")
    public ResponseEntity<ApiResponse<List<MicroStockResponse>>> microStock() {
        return ResponseEntity.ok(ApiResponse.succes(service.microStock(), "Micro-stock"));
    }

    @GetMapping("/documents")
    @Operation(summary = "Réception documents terrain (feuilles de route, bons, factures)")
    public ResponseEntity<ApiResponse<List<DocumentRecuResponse>>> documents() {
        return ResponseEntity.ok(ApiResponse.succes(service.documents(), "Documents reçus"));
    }
}
