package com.NTFOODS.Api_tanty.modules.production.presentation.controller;

import com.NTFOODS.Api_tanty.modules.production.application.formule.dto.request.DefinirCompositionEmballageRequest;
import com.NTFOODS.Api_tanty.modules.production.application.formule.dto.request.SauvegarderFormuleRequest;
import com.NTFOODS.Api_tanty.modules.production.application.formule.dto.response.ReferentielProductionResponse;
import com.NTFOODS.Api_tanty.modules.production.application.formule.usecase.DefinirCompositionEmballageUseCase;
import com.NTFOODS.Api_tanty.modules.production.application.formule.usecase.ListerReferentielProductionUseCase;
import com.NTFOODS.Api_tanty.modules.production.application.formule.usecase.SauvegarderFormuleUseCase;
import com.NTFOODS.Api_tanty.modules.production.application.formule.usecase.SupprimerFormuleUseCase;
import com.NTFOODS.Api_tanty.shared.application.dto.ApiResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Contrôleur REST — formules de production par produit et composition
 * d'emballage par conditionnement (Chef de Production, Responsable de Salle).
 */
@RestController
@RequestMapping("/api/production/formules")
@Tag(name = "Production — Formules et emballage", description = "Formules de production et compositions d'emballage")
@SecurityRequirement(name = "bearerAuth")
public class FormuleProductionController {

    private final ListerReferentielProductionUseCase listerUseCase;
    private final SauvegarderFormuleUseCase sauvegarderUseCase;
    private final SupprimerFormuleUseCase supprimerUseCase;
    private final DefinirCompositionEmballageUseCase compositionUseCase;

    public FormuleProductionController(ListerReferentielProductionUseCase listerUseCase,
                                       SauvegarderFormuleUseCase sauvegarderUseCase,
                                       SupprimerFormuleUseCase supprimerUseCase,
                                       DefinirCompositionEmballageUseCase compositionUseCase) {
        this.listerUseCase = listerUseCase;
        this.sauvegarderUseCase = sauvegarderUseCase;
        this.supprimerUseCase = supprimerUseCase;
        this.compositionUseCase = compositionUseCase;
    }

    @GetMapping
    @PreAuthorize("hasAuthority('PRODUCTION_FORMULE_GERER')")
    @Operation(summary = "Référentiel : produits avec formule et composition d'emballage de leurs conditionnements")
    public ResponseEntity<ApiResponse<List<ReferentielProductionResponse>>> lister() {
        var produits = listerUseCase.executer();
        return ResponseEntity.ok(ApiResponse.succes(produits, produits.size() + " produit(s)"));
    }

    @PutMapping("/{codeProduit}")
    @PreAuthorize("hasAuthority('PRODUCTION_FORMULE_GERER')")
    @Operation(summary = "Créer ou remplacer la formule d'un produit",
        description = "La somme des proportions doit égaler la base. N'affecte pas les PPH déjà générés (recalcul explicite requis).")
    public ResponseEntity<ApiResponse<ReferentielProductionResponse>> sauvegarder(
        @PathVariable String codeProduit, @Valid @RequestBody SauvegarderFormuleRequest req) {
        return ResponseEntity.ok(ApiResponse.succes(sauvegarderUseCase.executer(codeProduit, req), "Formule enregistrée"));
    }

    @DeleteMapping("/{codeProduit}")
    @PreAuthorize("hasAuthority('PRODUCTION_FORMULE_GERER')")
    @Operation(summary = "Retirer la formule d'un produit")
    public ResponseEntity<ApiResponse<Void>> supprimer(@PathVariable String codeProduit) {
        supprimerUseCase.executer(codeProduit);
        return ResponseEntity.ok(ApiResponse.succes(null, "Formule supprimée"));
    }

    @PutMapping("/{codeProduit}/conditionnements/{conditionnementId}/emballage")
    @PreAuthorize("hasAuthority('PRODUCTION_FORMULE_GERER')")
    @Operation(summary = "Définir (ou effacer) le type et la composition d'emballage d'un conditionnement")
    public ResponseEntity<ApiResponse<ReferentielProductionResponse>> definirComposition(
        @PathVariable String codeProduit, @PathVariable String conditionnementId,
        @Valid @RequestBody DefinirCompositionEmballageRequest req) {
        return ResponseEntity.ok(ApiResponse.succes(
            compositionUseCase.executer(codeProduit, conditionnementId, req), "Composition d'emballage enregistrée"));
    }
}
