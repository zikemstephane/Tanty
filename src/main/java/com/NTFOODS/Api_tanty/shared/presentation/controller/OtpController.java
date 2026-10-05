package com.NTFOODS.Api_tanty.shared.presentation.controller;

import com.NTFOODS.Api_tanty.shared.application.dto.ApiResponse;
import com.NTFOODS.Api_tanty.shared.infrastructure.otp.OtpService;
import com.NTFOODS.Api_tanty.shared.infrastructure.security.AuthenticatedUserResolver;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

/**
 * Contrôleur REST — Codes OTP (Pilier 3 : sécurité des opérations sensibles).
 *
 * Utilisé en amont de toute opération financière sensible (validation d'un
 * versement, déblocage d'un prix exceptionnel...). L'appelant génère un code
 * pour une "opération cible" (ex: "VERSEMENT:VRS-2026-0012"), le titulaire du
 * compte le reçoit par SMS, puis le fournit à l'endpoint métier concerné qui
 * appelle en interne {@code OtpService.verifier(...)} avant d'exécuter
 * l'action.
 */
@RestController
@RequestMapping("/api/securite/otp")
@Tag(name = "Sécurité — OTP", description = "Codes à usage unique pour les opérations financières sensibles")
@SecurityRequirement(name = "bearerAuth")
public class OtpController {

    private final OtpService otpService;
    private final AuthenticatedUserResolver userResolver;

    public OtpController(OtpService otpService, AuthenticatedUserResolver userResolver) {
        this.otpService = otpService;
        this.userResolver = userResolver;
    }

    @PostMapping("/generer")
    @PreAuthorize("isAuthenticated()")
    @Operation(summary = "Génère et envoie par SMS un code OTP pour une opération donnée")
    public ResponseEntity<ApiResponse<Void>> generer(@RequestBody GenererOtpRequest request) {
        String matricule = userResolver.getMatriculeCourant();
        otpService.genererEtEnvoyer(matricule, request.operationCible());
        return ResponseEntity.ok(ApiResponse.succes(null, "Code envoyé par SMS"));
    }

    @PostMapping("/verifier")
    @PreAuthorize("isAuthenticated()")
    @Operation(summary = "Vérifie un code OTP (usage indépendant, sans exécuter l'opération)")
    public ResponseEntity<ApiResponse<Void>> verifier(@RequestBody VerifierOtpRequest request) {
        String matricule = userResolver.getMatriculeCourant();
        otpService.verifier(matricule, request.operationCible(), request.code());
        return ResponseEntity.ok(ApiResponse.succes(null, "Code OTP valide"));
    }

    public record GenererOtpRequest(String operationCible) {}
    public record VerifierOtpRequest(String operationCible, String code) {}
}
