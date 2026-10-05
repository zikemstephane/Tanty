package com.NTFOODS.Api_tanty.modules.commercial.presentation.controller;

import com.NTFOODS.Api_tanty.modules.commercial.application.feuillroute.dto.response.CodeObservationResponse;
import com.NTFOODS.Api_tanty.modules.commercial.application.feuillroute.service.CodeObservationService;
import com.NTFOODS.Api_tanty.shared.application.dto.ApiResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/administration/codes-observation")
@Tag(name = "Administration — Codes observation tournée", description = "Gestion des codes de saisie terrain")
@SecurityRequirement(name = "bearerAuth")
@PreAuthorize("hasAnyRole('ADMIN', 'DIRECTEUR_GENERAL')")
public class CodeObservationController {

    private final CodeObservationService service;

    public CodeObservationController(CodeObservationService service) {
        this.service = service;
    }

    @GetMapping
    @Operation(summary = "Lister tous les codes observation")
    public ResponseEntity<ApiResponse<List<CodeObservationResponse>>> lister() {
        var liste = service.listerTous().stream()
            .map(c -> new CodeObservationResponse(c.getId(), c.getCode(), c.getLibelle(),
                c.isRequireQuantity(), c.isActif(), c.getOrdre()))
            .toList();
        return ResponseEntity.ok(ApiResponse.succes(liste, liste.size() + " code(s)"));
    }

    @PostMapping
    @Operation(summary = "Créer un code observation")
    public ResponseEntity<ApiResponse<CodeObservationResponse>> creer(
        @RequestParam String code,
        @RequestParam String libelle,
        @RequestParam(defaultValue = "false") boolean requireQuantity,
        @RequestParam(defaultValue = "0") int ordre) {
        var c = service.creer(code, libelle, requireQuantity, ordre);
        return ResponseEntity.ok(ApiResponse.succes(
            new CodeObservationResponse(c.getId(), c.getCode(), c.getLibelle(),
                c.isRequireQuantity(), c.isActif(), c.getOrdre()),
            "Code créé"));
    }

    @PutMapping("/{id}")
    @Operation(summary = "Modifier un code observation")
    public ResponseEntity<ApiResponse<CodeObservationResponse>> modifier(
        @PathVariable Long id,
        @RequestParam String code,
        @RequestParam String libelle,
        @RequestParam boolean requireQuantity,
        @RequestParam boolean actif,
        @RequestParam int ordre) {
        var c = service.modifier(id, code, libelle, requireQuantity, actif, ordre);
        return ResponseEntity.ok(ApiResponse.succes(
            new CodeObservationResponse(c.getId(), c.getCode(), c.getLibelle(),
                c.isRequireQuantity(), c.isActif(), c.getOrdre()),
            "Code mis à jour"));
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Supprimer un code observation")
    public ResponseEntity<ApiResponse<Void>> supprimer(@PathVariable Long id) {
        service.supprimer(id);
        return ResponseEntity.ok(ApiResponse.succes(null, "Code supprimé"));
    }
}
