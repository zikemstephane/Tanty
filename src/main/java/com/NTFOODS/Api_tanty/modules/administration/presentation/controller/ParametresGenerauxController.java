package com.NTFOODS.Api_tanty.modules.administration.presentation.controller;

import com.NTFOODS.Api_tanty.modules.administration.application.ParametresAdminService;
import com.NTFOODS.Api_tanty.modules.administration.application.dto.ParametresResponse;
import com.NTFOODS.Api_tanty.shared.application.dto.ApiResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Paramètres généraux applicables en lecture seule pour toutes les applications.
 *
 * <p>Contrairement à {@link ParametresAdminController}, ce contrôleur est
 * accessible à tout utilisateur authentifié. Il expose uniquement les valeurs
 * métier que les clients (mobile, tablette, etc.) doivent connaître pour
 * afficher ou calculer localement des informations cohérentes avec le backend.</p>
 */
@RestController
@RequestMapping("/api/parametres-generaux")
@Tag(name = "Paramètres généraux", description = "Paramètres métier consultables par tout utilisateur authentifié")
@SecurityRequirement(name = "bearerAuth")
public class ParametresGenerauxController {

    private final ParametresAdminService service;

    public ParametresGenerauxController(ParametresAdminService service) {
        this.service = service;
    }

    @GetMapping
    @PreAuthorize("isAuthenticated()")
    @Operation(summary = "Consulter les paramètres métier applicables aux clients",
        description = "Retourne les seuils et taux utilisés par le mobile (TVA, seuil client stratégique, etc.)")
    public ResponseEntity<ApiResponse<ParametresResponse>> consulter() {
        return ResponseEntity.ok(ApiResponse.succes(service.consulter(), "Paramètres généraux"));
    }
}
