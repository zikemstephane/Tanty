package com.NTFOODS.Api_tanty.modules.production.presentation.controller;

import com.NTFOODS.Api_tanty.modules.production.application.affectation.dto.response.PosteProductionResponse;
import com.NTFOODS.Api_tanty.modules.production.application.affectation.service.PosteProductionService;
import com.NTFOODS.Api_tanty.shared.application.dto.ApiResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/production/postes-production")
@Tag(name = "Production — Postes", description = "Postes de production et objectifs")
@SecurityRequirement(name = "bearerAuth")
public class PosteProductionPublicController {

    private final PosteProductionService service;

    public PosteProductionPublicController(PosteProductionService service) {
        this.service = service;
    }

    @GetMapping
    @PreAuthorize("isAuthenticated()")
    @Operation(summary = "Lister les postes de production actifs")
    public ResponseEntity<ApiResponse<List<PosteProductionResponse>>> lister() {
        var liste = service.listerActifs().stream()
            .map(p -> new PosteProductionResponse(p.getId(), p.getCode(), p.getLibelle(),
                p.getEffectifReference(), p.getProductionParHeure(), p.getProductionJournaliere(),
                p.getUnite(), p.isActif(), p.getOrdre(), p.getCategoriesMetier()))
            .toList();
        return ResponseEntity.ok(ApiResponse.succes(liste, liste.size() + " poste(s) actif(s)"));
    }
}
