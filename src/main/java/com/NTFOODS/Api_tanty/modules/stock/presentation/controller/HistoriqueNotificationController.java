package com.NTFOODS.Api_tanty.modules.stock.presentation.controller;

import com.NTFOODS.Api_tanty.modules.stock.domain.notification.model.HistoriqueNotification;
import com.NTFOODS.Api_tanty.modules.stock.domain.notification.repository.HistoriqueNotificationRepository;
import com.NTFOODS.Api_tanty.shared.application.dto.ApiResponse;
import com.NTFOODS.Api_tanty.shared.infrastructure.security.AuthenticatedUserResolver;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/stock/notifications/historique")
@Tag(name = "Stock — Historique des notifications", description = "Consultation et marquage des notifications envoyées")
@SecurityRequirement(name = "bearerAuth")
public class HistoriqueNotificationController {

    private final HistoriqueNotificationRepository repository;
    private final AuthenticatedUserResolver userResolver;

    public HistoriqueNotificationController(HistoriqueNotificationRepository repository,
                                            AuthenticatedUserResolver userResolver) {
        this.repository = repository;
        this.userResolver = userResolver;
    }

    @GetMapping
    @PreAuthorize("isAuthenticated()")
    @Operation(summary = "Lister mon historique de notifications")
    public ResponseEntity<ApiResponse<List<NotificationHistoryItem>>> monHistorique() {
        String matricule = userResolver.getUtilisateurCourant().getMatricule().getValeur();
        List<HistoriqueNotification> items = repository.trouverParMatricule(matricule);
        return ResponseEntity.ok(ApiResponse.succes(items.stream().map(this::toResponse).toList(),
            items.size() + " notification(s)"));
    }

    @GetMapping("/non-lus")
    @PreAuthorize("isAuthenticated()")
    @Operation(summary = "Lister mes notifications non lues")
    public ResponseEntity<ApiResponse<List<NotificationHistoryItem>>> nonLus() {
        String matricule = userResolver.getUtilisateurCourant().getMatricule().getValeur();
        List<HistoriqueNotification> items = repository.trouverNonLusParMatricule(matricule);
        return ResponseEntity.ok(ApiResponse.succes(items.stream().map(this::toResponse).toList(),
            items.size() + " notification(s) non lue(s)"));
    }

    @GetMapping("/role/{role}")
    @PreAuthorize("hasAnyRole('GESTIONNAIRE_STOCK', 'DIRECTEUR_GENERAL', 'CONTROLEUR_GENERAL', 'ADMIN')")
    @Operation(summary = "Lister les notifications par rôle destinataire")
    public ResponseEntity<ApiResponse<List<NotificationHistoryItem>>> parRole(@PathVariable String role) {
        List<HistoriqueNotification> items = repository.trouverParRole(role);
        return ResponseEntity.ok(ApiResponse.succes(items.stream().map(this::toResponse).toList(),
            items.size() + " notification(s)"));
    }

    @PatchMapping("/{id}/lu")
    @PreAuthorize("isAuthenticated()")
    @Operation(summary = "Marquer une notification comme lue")
    public ResponseEntity<ApiResponse<NotificationHistoryItem>> marquerCommeLu(@PathVariable Long id) {
        HistoriqueNotification notification = repository.trouverParId(id)
            .orElseThrow(() -> new IllegalArgumentException("Notification introuvable: " + id));
        notification.marquerCommeLu();
        HistoriqueNotification saved = repository.sauvegarder(notification);
        return ResponseEntity.ok(ApiResponse.succes(toResponse(saved), "Notification marquée comme lue"));
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("isAuthenticated()")
    @Operation(summary = "Supprimer une notification")
    public ResponseEntity<ApiResponse<Void>> supprimer(@PathVariable Long id) {
        repository.trouverParId(id)
            .orElseThrow(() -> new IllegalArgumentException("Notification introuvable: " + id));
        repository.supprimerParId(id);
        return ResponseEntity.ok(ApiResponse.succes(null, "Notification supprimée"));
    }

    private NotificationHistoryItem toResponse(HistoriqueNotification h) {
        return new NotificationHistoryItem(
            h.getId(), h.getTypeNotification(), h.getTitre(), h.getMessage(),
            h.getDestinataireRole(), h.getMatriculeDestinataire(), h.getCanal(),
            h.getPriorite(), h.getReferenceLiee(), h.isLu(),
            h.getDateCreation(), h.getDateLecture());
    }

    public record NotificationHistoryItem(
        Long id, String typeNotification, String titre, String message,
        String destinataireRole, String matriculeDestinataire, String canal,
        String priorite, String referenceLiee, boolean lu,
        java.time.LocalDateTime dateCreation, java.time.LocalDateTime dateLecture) {}
}
