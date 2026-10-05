package com.NTFOODS.Api_tanty.modules.sync.presentation.controller;

import com.NTFOODS.Api_tanty.modules.commercial.application.feuillroute.mapper.FeuillRouteMapper;
import com.NTFOODS.Api_tanty.modules.commercial.application.precommande.mapper.PreCommandeMapper;
import com.NTFOODS.Api_tanty.modules.commercial.application.vente.mapper.VenteMapper;
import com.NTFOODS.Api_tanty.modules.sync.application.dto.SyncLogResponse;
import com.NTFOODS.Api_tanty.modules.sync.application.dto.SyncPullResponse;
import com.NTFOODS.Api_tanty.modules.sync.application.dto.SyncPushResponse;
import com.NTFOODS.Api_tanty.modules.sync.application.service.SyncService;
import com.NTFOODS.Api_tanty.modules.sync.application.usecase.ConsulterHistoriqueSyncUseCase;
import com.NTFOODS.Api_tanty.modules.sync.domain.aggregate.SynchronisationLog;
import com.NTFOODS.Api_tanty.modules.sync.domain.enums.SyncEntityType;
import com.NTFOODS.Api_tanty.modules.sync.domain.enums.SyncResult;
import com.NTFOODS.Api_tanty.shared.application.dto.ApiResponse;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;

/**
 * Contrôleur REST — Synchronisation hors ligne.
 *
 * <p>Les gardes sont posées par permission (`SYNC_PUSH_PULL`,
 * `SYNC_HISTORIQUE_CONSULTER`) et non par rôle : le garde par rôle précédent
 * ignorait les permissions que la matrice attribue déjà, un écart récurrent
 * dans ce projet.</p>
 */
@RestController
@RequestMapping("/api/sync")
@Tag(name = "Synchronisation — Offline-First", description = "Push idempotent et pull incrémental pour clients mobiles (§12)")
@SecurityRequirement(name = "bearerAuth")
public class SyncController {

    private final SyncService syncService;
    private final ConsulterHistoriqueSyncUseCase historiqueSyncUseCase;
    private final VenteMapper venteMapper;
    private final PreCommandeMapper preCommandeMapper;
    private final FeuillRouteMapper feuillRouteMapper;
    private final ObjectMapper objectMapper;

    public SyncController(SyncService syncService, ConsulterHistoriqueSyncUseCase historiqueSyncUseCase,
                           VenteMapper venteMapper, PreCommandeMapper preCommandeMapper,
                           FeuillRouteMapper feuillRouteMapper, ObjectMapper objectMapper) {
        this.syncService = syncService;
        this.historiqueSyncUseCase = historiqueSyncUseCase;
        this.venteMapper = venteMapper;
        this.preCommandeMapper = preCommandeMapper;
        this.feuillRouteMapper = feuillRouteMapper;
        this.objectMapper = objectMapper;
    }

    @PostMapping("/push")
    @PreAuthorize("hasAuthority('SYNC_PUSH_PULL')")
    @Operation(summary = "Push idempotent d'une opération saisie hors ligne",
        description = "L'opération est réellement rejouée par le use case qui sert l'appel en ligne : " +
                      "une vente hors ligne subit la même vérification de grille tarifaire et la même " +
                      "résolution de remise qu'une vente connectée. La clé d'idempotence garantit qu'un " +
                      "renvoi ne crée pas de doublon : le résultat précédent est retourné tel quel. " +
                      "SUCCESS = l'entité existe côté serveur ; CONFLICT = refusée par une règle métier, " +
                      "à arbitrer par le commercial ; REJECTED = contenu inexploitable, ne pas renvoyer.")
    public ResponseEntity<ApiResponse<SyncPushResponse>> push(@RequestBody SyncPushRequest req) {
        SynchronisationLog journal = syncService.traiterPush(
            req.idempotencyKey(), req.matriculeCommercial(), req.entityType(),
            req.entityRef(), req.payloadHash(), req.payload());

        SyncPushResponse resp = new SyncPushResponse(
            journal.getIdempotencyKey(),
            journal.getResult() != null ? journal.getResult().name() : "PENDING",
            journal.getServerEntityRef(),
            journal.getConflictDetail(),
            journal.getDateTraitement());

        String message = switch (journal.getResult()) {
            case SUCCESS -> "Opération enregistrée : " + journal.getServerEntityRef();
            case CONFLICT -> "Opération refusée : " + journal.getConflictDetail();
            case REJECTED -> "Contenu inexploitable : " + journal.getConflictDetail();
            case PARTIAL -> "Opération partiellement traitée";
        };
        return ResponseEntity.ok(ApiResponse.succes(resp, message));
    }

    @GetMapping("/pull")
    @PreAuthorize("hasAuthority('SYNC_PUSH_PULL')")
    @Operation(summary = "Pull incrémental",
        description = "Entités modifiées depuis lastSync pour le commercial donné. Le contenu complet de " +
                      "chaque entité est transmis, sérialisé en JSON : le terminal doit pouvoir rafraîchir " +
                      "son cache local sans rappeler le serveur entité par entité.")
    public ResponseEntity<ApiResponse<SyncPullResponse>> pull(
        @RequestParam String matriculeCommercial,
        @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime lastSync) {

        SyncService.SyncPullData data = syncService.pullIncremental(matriculeCommercial, lastSync);

        SyncPullResponse resp = new SyncPullResponse(
            data.serverTime(),
            matriculeCommercial,
            List.of(
                new SyncPullResponse.EntityBatch(SyncEntityType.VENTE, lastSync,
                    charger(data.ventes(), v -> v.getNumeroVente(), v -> v.getDateModification(),
                        v -> venteMapper.versResponse(v))),
                new SyncPullResponse.EntityBatch(SyncEntityType.PRECOMMANDE, lastSync,
                    charger(data.preCommandes(), pc -> pc.getNumeroPreCommande(), pc -> pc.getDateModification(),
                        pc -> preCommandeMapper.versResponse(pc))),
                new SyncPullResponse.EntityBatch(SyncEntityType.FEUILL_ROUTE, lastSync,
                    charger(data.feuillesRoute(), fr -> fr.getNumeroFeuillRoute(), fr -> fr.getDateModification(),
                        fr -> feuillRouteMapper.versResponse(fr)))));

        return ResponseEntity.ok(ApiResponse.succes(resp, "Pull incrémental réussi"));
    }

    /**
     * Sérialise chaque entité du lot avec le mapper de son module.
     *
     * <p>Le contenu passait auparavant à {@code null} : le terminal recevait des
     * références sans rien pour reconstituer les entités, ce qui rendait le pull
     * inexploitable. Réutiliser les mappers existants évite qu'une seconde
     * représentation de la même entité se mette à diverger de la première.</p>
     */
    private <T> List<SyncPullResponse.EntityPayload> charger(List<T> entites,
                                                              Function<T, String> reference,
                                                              Function<T, LocalDateTime> derniereModification,
                                                              Function<T, Object> versDto) {
        List<SyncPullResponse.EntityPayload> resultat = new ArrayList<>(entites.size());
        for (T entite : entites) {
            resultat.add(new SyncPullResponse.EntityPayload(
                reference.apply(entite),
                serialiser(versDto.apply(entite)),
                derniereModification.apply(entite)));
        }
        return resultat;
    }

    private String serialiser(Object dto) {
        try {
            return objectMapper.writeValueAsString(dto);
        } catch (JsonProcessingException e) {
            // Sérialisation impossible : mieux vaut une entité absente du lot
            // qu'un contenu tronqué que le terminal prendrait pour la vérité.
            throw new IllegalStateException("Sérialisation impossible pour le pull : " + e.getMessage(), e);
        }
    }

    @GetMapping("/historique")
    @PreAuthorize("hasAuthority('SYNC_HISTORIQUE_CONSULTER')")
    @Operation(summary = "Historique des synchronisations",
        description = "Consulte les journaux de synchronisation avec filtres : matricule, entityType, result, période, limite")
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

    /**
     * @param payload contenu complet de l'opération, sérialisé en JSON par le
     *                terminal. Sans lui le serveur ne peut rien reconstituer —
     *                son absence était la raison pour laquelle le push
     *                n'écrivait rien.
     */
    public record SyncPushRequest(
        String idempotencyKey,
        String matriculeCommercial,
        SyncEntityType entityType,
        String entityRef,
        String payloadHash,
        String payload
    ) {}
}
