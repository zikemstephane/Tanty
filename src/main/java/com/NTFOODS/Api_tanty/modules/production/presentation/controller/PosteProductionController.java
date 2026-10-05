package com.NTFOODS.Api_tanty.modules.production.presentation.controller;

import com.NTFOODS.Api_tanty.modules.production.application.affectation.dto.response.PosteProductionResponse;
import com.NTFOODS.Api_tanty.modules.production.application.affectation.service.PosteProductionService;
import com.NTFOODS.Api_tanty.shared.application.dto.ApiResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Set;

@RestController
@RequestMapping("/api/administration/postes-production")
@Tag(name = "Administration — Postes de production", description = "Gestion des postes, effectifs et objectifs de production")
@SecurityRequirement(name = "bearerAuth")
@PreAuthorize("hasAnyRole('ADMIN', 'DIRECTEUR_GENERAL')")
public class PosteProductionController {

    private final PosteProductionService service;

    public PosteProductionController(PosteProductionService service) {
        this.service = service;
    }

    @GetMapping
    @Operation(summary = "Lister tous les postes de production")
    public ResponseEntity<ApiResponse<List<PosteProductionResponse>>> lister() {
        var liste = service.listerTous().stream()
            .map(this::toResponse)
            .toList();
        return ResponseEntity.ok(ApiResponse.succes(liste, liste.size() + " poste(s)"));
    }

    @PostMapping
    @Operation(summary = "Créer un poste de production")
    public ResponseEntity<ApiResponse<PosteProductionResponse>> creer(
        @RequestParam String code,
        @RequestParam String libelle,
        @RequestParam(defaultValue = "1") int effectifReference,
        @RequestParam(defaultValue = "0") double productionParHeure,
        @RequestParam(defaultValue = "0") double productionJournaliere,
        @RequestParam(defaultValue = "AUCUNE") String unite,
        @RequestParam(defaultValue = "0") int ordre,
        @RequestParam(required = false) Set<String> categoriesMetier) {
        var p = service.creer(code, libelle, effectifReference, productionParHeure,
            productionJournaliere, unite, ordre, categoriesMetier != null ? categoriesMetier : Set.of());
        return ResponseEntity.ok(ApiResponse.succes(toResponse(p), "Poste créé"));
    }

    @PutMapping("/{id}")
    @Operation(summary = "Modifier un poste de production")
    public ResponseEntity<ApiResponse<PosteProductionResponse>> modifier(
        @PathVariable Long id,
        @RequestParam String code,
        @RequestParam String libelle,
        @RequestParam int effectifReference,
        @RequestParam double productionParHeure,
        @RequestParam double productionJournaliere,
        @RequestParam String unite,
        @RequestParam boolean actif,
        @RequestParam int ordre,
        @RequestParam(required = false) Set<String> categoriesMetier) {
        var p = service.modifier(id, code, libelle, effectifReference, productionParHeure,
            productionJournaliere, unite, actif, ordre, categoriesMetier != null ? categoriesMetier : Set.of());
        return ResponseEntity.ok(ApiResponse.succes(toResponse(p), "Poste mis à jour"));
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Supprimer un poste de production")
    public ResponseEntity<ApiResponse<Void>> supprimer(@PathVariable Long id) {
        service.supprimer(id);
        return ResponseEntity.ok(ApiResponse.succes(null, "Poste supprimé"));
    }

    private PosteProductionResponse toResponse(com.NTFOODS.Api_tanty.modules.production.domain.affectation.entity.PosteProduction p) {
        return new PosteProductionResponse(p.getId(), p.getCode(), p.getLibelle(), p.getEffectifReference(),
            p.getProductionParHeure(), p.getProductionJournaliere(), p.getUnite(), p.isActif(), p.getOrdre(),
            p.getCategoriesMetier());
    }
}
