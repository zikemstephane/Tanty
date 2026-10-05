package com.NTFOODS.Api_tanty.modules.commercial.presentation.controller;

import com.NTFOODS.Api_tanty.modules.commercial.application.feuillroute.dto.response.CodeObservationResponse;
import com.NTFOODS.Api_tanty.modules.commercial.application.feuillroute.service.CodeObservationService;
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

/**
 * Codes observation actifs, consultables par tout utilisateur authentifié
 * (mobile/tablette) pour remplir la feuille de route sans valeurs codées en dur.
 */
@RestController
@RequestMapping("/api/commercial/codes-observation")
@Tag(name = "Commercial — Codes observation", description = "Codes de saisie terrain pour feuille de route")
@SecurityRequirement(name = "bearerAuth")
public class CodeObservationPublicController {

    private final CodeObservationService service;

    public CodeObservationPublicController(CodeObservationService service) {
        this.service = service;
    }

    @GetMapping
    @PreAuthorize("isAuthenticated()")
    @Operation(summary = "Lister les codes observation actifs")
    public ResponseEntity<ApiResponse<List<CodeObservationResponse>>> lister() {
        var liste = service.listerActifs().stream()
            .map(c -> new CodeObservationResponse(c.getId(), c.getCode(), c.getLibelle(),
                c.isRequireQuantity(), c.isActif(), c.getOrdre()))
            .toList();
        return ResponseEntity.ok(ApiResponse.succes(liste, liste.size() + " code(s) actif(s)"));
    }
}
