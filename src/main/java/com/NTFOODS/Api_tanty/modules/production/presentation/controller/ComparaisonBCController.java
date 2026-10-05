package com.NTFOODS.Api_tanty.modules.production.presentation.controller;

import com.NTFOODS.Api_tanty.modules.production.application.comparaison.dto.response.ComparaisonBCResponse;
import com.NTFOODS.Api_tanty.modules.production.application.comparaison.usecase.ComparaisonBCUseCase;
import com.NTFOODS.Api_tanty.shared.application.dto.ApiResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDate;

@RestController
@RequestMapping("/api/production/comparaisons")
@Tag(name = "Production — Comparaisons", description = "Comparaison du plan BC avec le dosage et la production")
@SecurityRequirement(name = "bearerAuth")
public class ComparaisonBCController {

    private final ComparaisonBCUseCase comparaisonBCUseCase;

    public ComparaisonBCController(ComparaisonBCUseCase comparaisonBCUseCase) {
        this.comparaisonBCUseCase = comparaisonBCUseCase;
    }

    @GetMapping("/{referencePPH}")
    @PreAuthorize("hasAuthority('PRODUCTION_REGISTRE_CONSULTER')")
    @Operation(summary = "Comparer le plan du BC au dosage et à la production d'une journée")
    public ResponseEntity<ApiResponse<ComparaisonBCResponse>> comparer(
            @PathVariable String referencePPH,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date) {
        return ResponseEntity.ok(ApiResponse.succes(
            comparaisonBCUseCase.executer(referencePPH, date), "Comparaison plan / dosage / production"));
    }
}
