package com.NTFOODS.Api_tanty.modules.production.presentation.controller;

import com.NTFOODS.Api_tanty.modules.production.application.affectation.dto.response.PerformancePosteResponse;
import com.NTFOODS.Api_tanty.modules.production.application.affectation.usecase.PerformancesPostesPeriodeUseCase;
import com.NTFOODS.Api_tanty.shared.application.dto.ApiResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/api/production/performances-postes")
@Tag(name = "Production — Performances", description = "Performances des postes par période")
@SecurityRequirement(name = "bearerAuth")
public class PerformancesPostesController {

    private final PerformancesPostesPeriodeUseCase performancesUseCase;

    public PerformancesPostesController(PerformancesPostesPeriodeUseCase performancesUseCase) {
        this.performancesUseCase = performancesUseCase;
    }

    @GetMapping
    @PreAuthorize("hasAuthority('PRODUCTION_REGISTRE_CONSULTER')")
    @Operation(summary = "Performances des postes sur une période (objectif, réalisé, taux, rendement par personne)")
    public ResponseEntity<ApiResponse<List<PerformancePosteResponse>>> performances(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate debut,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate fin) {
        List<PerformancePosteResponse> resultat = performancesUseCase.executer(debut, fin);
        return ResponseEntity.ok(ApiResponse.succes(resultat, resultat.size() + " poste(s) sur la période"));
    }
}
