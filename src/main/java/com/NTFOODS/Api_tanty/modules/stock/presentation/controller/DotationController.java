package com.NTFOODS.Api_tanty.modules.stock.presentation.controller;

import com.NTFOODS.Api_tanty.modules.stock.application.dotation.dto.request.DecisionLigneRequest;
import com.NTFOODS.Api_tanty.modules.stock.application.dotation.dto.request.SoumettreDotationRequest;
import com.NTFOODS.Api_tanty.modules.stock.application.dotation.dto.response.DotationResponse;
import com.NTFOODS.Api_tanty.modules.stock.application.dotation.usecase.approuver.ApprouverDotationUseCase;
import com.NTFOODS.Api_tanty.modules.stock.application.dotation.usecase.consulter.ConsulterDotationUseCase;
import com.NTFOODS.Api_tanty.modules.stock.application.dotation.usecase.lister.ListerDotationsUseCase;
import com.NTFOODS.Api_tanty.modules.stock.application.dotation.usecase.modifier.ModifierDotationUseCase;
import com.NTFOODS.Api_tanty.modules.stock.application.dotation.usecase.refuser.RefuserDotationUseCase;
import com.NTFOODS.Api_tanty.modules.stock.application.dotation.usecase.soumettre.SoumettreDotationUseCase;
import com.NTFOODS.Api_tanty.shared.application.dto.ApiResponse;
import com.NTFOODS.Api_tanty.shared.infrastructure.security.AuthenticatedUserResolver;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/stock/dotations")
@Tag(name = "Stock — Dotations", description = "Dotations commerciales J+1 depuis le tampon")
@SecurityRequirement(name = "bearerAuth")
public class DotationController {

    private final SoumettreDotationUseCase soumettreDotationUseCase;
    private final ApprouverDotationUseCase approuverDotationUseCase;
    private final ModifierDotationUseCase modifierDotationUseCase;
    private final RefuserDotationUseCase refuserDotationUseCase;
    private final ListerDotationsUseCase listerDotationsUseCase;
    private final ConsulterDotationUseCase consulterDotationUseCase;
    private final AuthenticatedUserResolver userResolver;

    public DotationController(SoumettreDotationUseCase soumettreDotationUseCase,
                            ApprouverDotationUseCase approuverDotationUseCase,
                            ModifierDotationUseCase modifierDotationUseCase,
                            RefuserDotationUseCase refuserDotationUseCase,
                            ListerDotationsUseCase listerDotationsUseCase,
                            ConsulterDotationUseCase consulterDotationUseCase,
                            AuthenticatedUserResolver userResolver) {
        this.soumettreDotationUseCase = soumettreDotationUseCase;
        this.approuverDotationUseCase = approuverDotationUseCase;
        this.modifierDotationUseCase = modifierDotationUseCase;
        this.refuserDotationUseCase = refuserDotationUseCase;
        this.listerDotationsUseCase = listerDotationsUseCase;
        this.consulterDotationUseCase = consulterDotationUseCase;
        this.userResolver = userResolver;
    }

    @PostMapping
    @PreAuthorize("hasAnyRole('COMMERCIAL', 'ADMIN')")
    @Operation(summary = "Soumettre une dotation (pour le lendemain)")
    public ResponseEntity<ApiResponse<DotationResponse>> soumettre(@Valid @RequestBody SoumettreDotationRequest request) {
        DotationResponse response = soumettreDotationUseCase.executer(request, userResolver.getUtilisateurCourant());
        return ResponseEntity.status(HttpStatus.CREATED).body(ApiResponse.succes(response, "Dotation soumise : " + response.numero()));
    }

    @PostMapping("/{numero}/approuver")
    @PreAuthorize("hasAnyRole('GESTIONNAIRE_STOCK', 'ADMIN')")
    @Operation(summary = "Approuver une dotation (débit tampon / crédit mobile)")
    public ResponseEntity<ApiResponse<DotationResponse>> approuver(@PathVariable String numero) {
        DotationResponse response = approuverDotationUseCase.executer(numero, userResolver.getUtilisateurCourant());
        return ResponseEntity.ok(ApiResponse.succes(response, "Dotation approuvée — stock mobile crédité"));
    }

    @PatchMapping("/{numero}/ligne")
    @PreAuthorize("hasAnyRole('GESTIONNAIRE_STOCK', 'ADMIN')")
    @Operation(summary = "Modifier/substituer une ligne de dotation")
    public ResponseEntity<ApiResponse<DotationResponse>> modifierLigne(
        @PathVariable String numero, @Valid @RequestBody DecisionLigneRequest request) {
        DotationResponse response = modifierDotationUseCase.executer(numero, request, userResolver.getUtilisateurCourant());
        return ResponseEntity.ok(ApiResponse.succes(response, "Ligne modifiée"));
    }

    @PostMapping("/{numero}/refuser")
    @PreAuthorize("hasAnyRole('GESTIONNAIRE_STOCK', 'ADMIN')")
    @Operation(summary = "Refuser une dotation")
    public ResponseEntity<ApiResponse<DotationResponse>> refuser(@PathVariable String numero, @RequestParam String motif) {
        DotationResponse response = refuserDotationUseCase.executer(numero, motif, userResolver.getUtilisateurCourant());
        return ResponseEntity.ok(ApiResponse.succes(response, "Dotation refusée"));
    }

    @GetMapping
    @PreAuthorize("isAuthenticated()")
    @Operation(summary = "Lister les dotations (filtres : statut, commercial)")
    public ResponseEntity<ApiResponse<List<DotationResponse>>> lister(
        @RequestParam(required = false) String statut,
        @RequestParam(required = false) String commercial) {
        List<DotationResponse> dotations = listerDotationsUseCase.executer(statut, commercial);
        return ResponseEntity.ok(ApiResponse.succes(dotations, dotations.size() + " dotation(s)"));
    }

    @GetMapping("/{numero}")
    @PreAuthorize("isAuthenticated()")
    @Operation(summary = "Consulter une dotation")
    public ResponseEntity<ApiResponse<DotationResponse>> consulter(@PathVariable String numero) {
        return ResponseEntity.ok(ApiResponse.succes(consulterDotationUseCase.executer(numero)));
    }
}
