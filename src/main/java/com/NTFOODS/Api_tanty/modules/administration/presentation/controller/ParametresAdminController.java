package com.NTFOODS.Api_tanty.modules.administration.presentation.controller;

import com.NTFOODS.Api_tanty.modules.administration.application.ParametresAdminService;
import com.NTFOODS.Api_tanty.modules.administration.application.dto.ParametresResponse;
import com.NTFOODS.Api_tanty.shared.application.dto.ApiResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/administration/parametres")
@Tag(name = "Administration — Paramètres", description = "Paramètres système et référentiels (zones, gammes, villes)")
@SecurityRequirement(name = "bearerAuth")
@PreAuthorize("hasAnyRole('ADMIN', 'DIRECTEUR_GENERAL')")
public class ParametresAdminController {

    private final ParametresAdminService service;

    public ParametresAdminController(ParametresAdminService service) {
        this.service = service;
    }

    @GetMapping
    @Operation(summary = "Consulter tous les paramètres système et référentiels")
    public ResponseEntity<ApiResponse<ParametresResponse>> consulter() {
        return ResponseEntity.ok(ApiResponse.succes(service.consulter(), "Paramètres système"));
    }

    @PutMapping
    @PreAuthorize("hasRole('DIRECTEUR_GENERAL')")
    @Operation(summary = "Modifier les paramètres financiers (TVA, prime, seuil stock, seuil client stratégique)")
    public ResponseEntity<ApiResponse<ParametresResponse>> modifier(
        @RequestParam double tvaTaux,
        @RequestParam double primeMontant,
        @RequestParam double alerteStockSeuil,
        @RequestParam double seuilClientStrategique) {
        return ResponseEntity.ok(ApiResponse.succes(
            service.modifier(tvaTaux, primeMontant, alerteStockSeuil, seuilClientStrategique),
            "Paramètres mis à jour"));
    }

    @PostMapping("/referentiel")
    @Operation(summary = "Ajouter une valeur à un référentiel (ZONE, GAMME, VILLE)")
    public ResponseEntity<ApiResponse<ParametresResponse>> ajouterReferentiel(
        @RequestParam String categorie,
        @RequestParam String valeur) {
        return ResponseEntity.ok(ApiResponse.succes(
            service.ajouterReferentiel(categorie, valeur),
            "Valeur ajoutée au référentiel"));
    }

    @DeleteMapping("/referentiel")
    @Operation(summary = "Supprimer une valeur d'un référentiel (ZONE, GAMME, VILLE)")
    public ResponseEntity<ApiResponse<ParametresResponse>> supprimerReferentiel(
        @RequestParam String categorie,
        @RequestParam String valeur) {
        return ResponseEntity.ok(ApiResponse.succes(
            service.supprimerReferentiel(categorie, valeur),
            "Valeur supprimée du référentiel"));
    }
}
