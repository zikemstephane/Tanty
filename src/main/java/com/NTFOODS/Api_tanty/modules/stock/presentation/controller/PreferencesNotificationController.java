package com.NTFOODS.Api_tanty.modules.stock.presentation.controller;

import com.NTFOODS.Api_tanty.modules.stock.application.notification.dto.MettreAJourPreferencesRequest;
import com.NTFOODS.Api_tanty.modules.stock.application.notification.dto.PreferencesNotificationResponse;
import com.NTFOODS.Api_tanty.modules.stock.domain.notification.model.PreferencesNotification;
import com.NTFOODS.Api_tanty.modules.stock.domain.notification.repository.PreferencesNotificationRepository;
import com.NTFOODS.Api_tanty.shared.application.dto.ApiResponse;
import com.NTFOODS.Api_tanty.shared.infrastructure.security.AuthenticatedUserResolver;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/stock/notifications/preferences")
@Tag(name = "Stock — Préférences de notification", description = "Préférences par utilisateur (canaux, priorité, plage horaire)")
@SecurityRequirement(name = "bearerAuth")
public class PreferencesNotificationController {

    private final PreferencesNotificationRepository repository;
    private final AuthenticatedUserResolver userResolver;

    public PreferencesNotificationController(PreferencesNotificationRepository repository,
                                             AuthenticatedUserResolver userResolver) {
        this.repository = repository;
        this.userResolver = userResolver;
    }

    @GetMapping
    @PreAuthorize("isAuthenticated()")
    @Operation(summary = "Consulter mes préférences de notification")
    public ResponseEntity<ApiResponse<PreferencesNotificationResponse>> consulter() {
        String matricule = userResolver.getUtilisateurCourant().getMatricule().getValeur();
        PreferencesNotification prefs = repository.trouverParMatricule(matricule)
            .orElseGet(() -> new PreferencesNotification(matricule));
        return ResponseEntity.ok(ApiResponse.succes(toResponse(prefs), "Préférences de notification"));
    }

    @PutMapping
    @PreAuthorize("isAuthenticated()")
    @Operation(summary = "Mettre à jour mes préférences de notification")
    public ResponseEntity<ApiResponse<PreferencesNotificationResponse>> mettreAJour(
            @Valid @RequestBody MettreAJourPreferencesRequest request) {
        String matricule = userResolver.getUtilisateurCourant().getMatricule().getValeur();
        PreferencesNotification prefs = repository.trouverParMatricule(matricule)
            .orElseGet(() -> new PreferencesNotification(matricule));

        prefs.mettreAJour(
            request.notificationInApp(),
            request.notificationEmail(),
            request.notificationSms(),
            request.notificationPush(),
            request.prioriteMinimale(),
            request.heureDebutReception(),
            request.heureFinReception(),
            request.typesFiltres());

        PreferencesNotification saved = repository.sauvegarder(prefs);
        return ResponseEntity.ok(ApiResponse.succes(toResponse(saved), "Préférences mises à jour"));
    }

    private PreferencesNotificationResponse toResponse(PreferencesNotification p) {
        return new PreferencesNotificationResponse(
            p.getMatriculeUtilisateur(),
            p.isNotificationInApp(),
            p.isNotificationEmail(),
            p.isNotificationSms(),
            p.isNotificationPush(),
            p.getPrioriteMinimale(),
            p.getHeureDebutReception(),
            p.getHeureFinReception(),
            p.getTypesFiltres());
    }
}
