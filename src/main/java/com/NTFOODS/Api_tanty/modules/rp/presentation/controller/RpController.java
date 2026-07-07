package com.NTFOODS.Api_tanty.modules.rp.presentation.controller;

import com.NTFOODS.Api_tanty.modules.rp.application.RpDashboardService;
import com.NTFOODS.Api_tanty.modules.rp.application.dto.RpDashboardResponse;
import com.NTFOODS.Api_tanty.modules.rp.application.dto.RpDashboardResponse.*;
import com.NTFOODS.Api_tanty.shared.application.dto.ApiResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/rp")
@Tag(name = "RP & Commerciale — Dashboard", description = "Primes, performance gammes, grands comptes, campagnes marketing")
@SecurityRequirement(name = "bearerAuth")
@PreAuthorize("hasAnyRole('CHARGEE_RP', 'DIRECTEUR_GENERAL', 'ADMIN')")
public class RpController {

    private final RpDashboardService service;

    public RpController(RpDashboardService service) {
        this.service = service;
    }

    @GetMapping("/dashboard")
    @Operation(summary = "Dashboard RP",
        description = "Primes, performance par gamme, occupation marchés, grands comptes, campagnes marketing")
    public ResponseEntity<ApiResponse<RpDashboardResponse>> dashboard() {
        return ResponseEntity.ok(ApiResponse.succes(service.dashboard(), "Dashboard RP"));
    }

    @GetMapping("/primes")
    @Operation(summary = "Primes hebdomadaires (éligibilité, montant, taux par gamme)")
    public ResponseEntity<ApiResponse<List<PrimeResponse>>> primes() {
        return ResponseEntity.ok(ApiResponse.succes(service.primes(), "Primes"));
    }

    @GetMapping("/grands-comptes")
    @Operation(summary = "Grands comptes (CRM — hôtels, GMS, boulangeries)")
    public ResponseEntity<ApiResponse<List<GrandCompteResponse>>> grandsComptes() {
        return ResponseEntity.ok(ApiResponse.succes(service.grandsComptes(), "Grands comptes"));
    }

    @GetMapping("/campagnes")
    @Operation(summary = "Campagnes marketing")
    public ResponseEntity<ApiResponse<List<CampagneResponse>>> campagnes() {
        return ResponseEntity.ok(ApiResponse.succes(service.campagnes(), "Campagnes"));
    }

    @GetMapping("/occupation")
    @Operation(summary = "Taux d'occupation des marchés par secteur")
    public ResponseEntity<ApiResponse<List<OccupationMarcheResponse>>> occupation() {
        return ResponseEntity.ok(ApiResponse.succes(service.occupation(), "Occupation marchés"));
    }
}
