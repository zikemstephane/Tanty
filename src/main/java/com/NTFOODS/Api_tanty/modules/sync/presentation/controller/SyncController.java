package com.NTFOODS.Api_tanty.modules.sync.presentation.controller;

import com.NTFOODS.Api_tanty.modules.sync.application.dto.SyncLogResponse;
import com.NTFOODS.Api_tanty.modules.sync.application.dto.SyncPullResponse;
import com.NTFOODS.Api_tanty.modules.sync.application.dto.SyncPushResponse;
import com.NTFOODS.Api_tanty.modules.sync.application.service.SyncService;
import com.NTFOODS.Api_tanty.modules.sync.application.usecase.ConsulterHistoriqueSyncUseCase;
import com.NTFOODS.Api_tanty.modules.sync.domain.aggregate.SynchronisationLog;
import com.NTFOODS.Api_tanty.modules.sync.domain.enums.SyncEntityType;
import com.NTFOODS.Api_tanty.modules.sync.domain.enums.SyncResult;
import com.NTFOODS.Api_tanty.shared.application.dto.ApiResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/api/sync")
@Tag(name = "Synchronisation — Offline-First", description = "Push idempotent et pull incrémental pour clients mobiles (§12)")
@SecurityRequirement(name = "bearerAuth")
public class SyncController {

    private final SyncService syncService;
    private final ConsulterHistoriqueSyncUseCase historiqueSyncUseCase;

    public SyncController(SyncService syncService, ConsulterHistoriqueSyncUseCase historiqueSyncUseCase) {
        this.syncService = syncService;
        this.historiqueSyncUseCase = historiqueSyncUseCase;
    }

    @PostMapping("/push")
    @PreAuthorize("hasAnyRole('COMMERCIAL', 'ADMIN', 'DIRECTEUR_GENERAL')")
    @Operation(summary = "Push idempotent d'une entité",
        description = "Soumet une entité créée hors-ligne. L'idempotencyKey garantit qu'un push dupliqué " +
                      "ne sera pas retraité. Si déjà traité, retourne le résultat précédent.")
    public ResponseEntity<ApiResponse<SyncPushResponse>> push(@RequestBody SyncPushRequest req) {
        SynchronisationLog log = syncService.traiterPush(
            req.idempotencyKey(), req.matriculeCommercial(), req.entityType(),
            req.entityRef(), req.payloadHash(), () -> {});
        SyncPushResponse resp = new SyncPushResponse(
            log.getIdempotencyKey(),
            log.getResult() != null ? log.getResult().name() : "PENDING",
            log.getServerEntityRef(),
            log.getConflictDetail(),
            log.getDateTraitement()
        );
        return ResponseEntity.ok(ApiResponse.succes(resp, "Push traité"));
    }

    @GetMapping("/pull")
    @PreAuthorize("hasAnyRole('COMMERCIAL', 'ADMIN', 'DIRECTEUR_GENERAL')")
    @Operation(summary = "Pull incrémental",
        description = "Récupère toutes les entités modifiées depuis lastSync pour le commercial donné.")
    public ResponseEntity<ApiResponse<SyncPullResponse>> pull(
        @RequestParam String matriculeCommercial,
        @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime lastSync) {

        SyncService.SyncPullData data = syncService.pullIncremental(matriculeCommercial, lastSync);

        List<SyncPullResponse.EntityPayload> ventes = data.ventes().stream()
            .map(v -> new SyncPullResponse.EntityPayload(v.getNumeroVente(), null, v.getDateModification()))
            .toList();
        List<SyncPullResponse.EntityPayload> preCommandes = data.preCommandes().stream()
            .map(pc -> new SyncPullResponse.EntityPayload(pc.getNumeroPreCommande(), null, pc.getDateModification()))
            .toList();
        List<SyncPullResponse.EntityPayload> feuillesRoute = data.feuillesRoute().stream()
            .map(fr -> new SyncPullResponse.EntityPayload(fr.getNumeroFeuillRoute(), null, fr.getDateModification()))
            .toList();

        SyncPullResponse resp = new SyncPullResponse(
            data.serverTime(),
            matriculeCommercial,
            List.of(
                new SyncPullResponse.EntityBatch(SyncEntityType.VENTE, lastSync, ventes),
                new SyncPullResponse.EntityBatch(SyncEntityType.PRECOMMANDE, lastSync, preCommandes),
                new SyncPullResponse.EntityBatch(SyncEntityType.FEUILL_ROUTE, lastSync, feuillesRoute)
            )
        );
        return ResponseEntity.ok(ApiResponse.succes(resp, "Pull incrémental réussi"));
    }

    @GetMapping("/historique")
    @PreAuthorize("hasAnyRole('DIRECTEUR_GENERAL', 'ADMIN')")
    @Operation(summary = "Historique des synchronisations — DG/ADMIN",
        description = "Consulte tous les logs de sync avec filtres: matricule, entityType, result, période, limite")
    public ResponseEntity<ApiResponse<List<SyncLogResponse>>> historique(
        @RequestParam(required = false) String matricule,
        @RequestParam(required = false) SyncEntityType entityType,
        @RequestParam(required = false) SyncResult result,
        @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime debut,
        @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime fin,
        @RequestParam(required = false, defaultValue = "100") int limite) {
        var r = historiqueSyncUseCase.executer(matricule, entityType, result, debut, fin, limite);
        return ResponseEntity.ok(ApiResponse.succes(r, r.size() + " log(s) de synchronisation"));
    }

    public record SyncPushRequest(
        String idempotencyKey,
        String matriculeCommercial,
        SyncEntityType entityType,
        String entityRef,
        String payloadHash
    ) {}
}
