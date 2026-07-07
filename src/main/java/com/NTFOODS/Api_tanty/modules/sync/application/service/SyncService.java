package com.NTFOODS.Api_tanty.modules.sync.application.service;

import com.NTFOODS.Api_tanty.modules.commercial.domain.vente.aggregate.Vente;
import com.NTFOODS.Api_tanty.modules.commercial.domain.vente.repository.VenteRepository;
import com.NTFOODS.Api_tanty.modules.sync.domain.aggregate.SynchronisationLog;
import com.NTFOODS.Api_tanty.modules.sync.domain.enums.SyncEntityType;
import com.NTFOODS.Api_tanty.modules.sync.domain.enums.SyncResult;
import com.NTFOODS.Api_tanty.modules.sync.domain.repository.SynchronisationLogRepository;
import com.NTFOODS.Api_tanty.modules.commercial.domain.feuillroute.aggregate.FeuillRoute;
import com.NTFOODS.Api_tanty.modules.commercial.domain.feuillroute.repository.FeuillRouteRepository;
import com.NTFOODS.Api_tanty.modules.commercial.domain.precommande.aggregate.PreCommande;
import com.NTFOODS.Api_tanty.modules.commercial.domain.precommande.repository.PreCommandeRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

/**
 * Service de synchronisation offline-first (§12).
 *
 * - Push idempotent : un client mobile soumet des entités créées hors-ligne
 *   avec une idempotencyKey. Si déjà traitée, on retourne le résultat précédent.
 * - Pull incrémental : le client demande les modifications depuis son lastSync
 *   et reçoit seulement les entités modifiées après cette date.
 */
@Service
public class SyncService {

    private final SynchronisationLogRepository syncLogRepository;
    private final VenteRepository venteRepository;
    private final PreCommandeRepository preCommandeRepository;
    private final FeuillRouteRepository feuillRouteRepository;

    public SyncService(SynchronisationLogRepository syncLogRepository,
                       VenteRepository venteRepository,
                       PreCommandeRepository preCommandeRepository,
                       FeuillRouteRepository feuillRouteRepository) {
        this.syncLogRepository = syncLogRepository;
        this.venteRepository = venteRepository;
        this.preCommandeRepository = preCommandeRepository;
        this.feuillRouteRepository = feuillRouteRepository;
    }

    /**
     * Traite un push idempotent. Si l'idempotencyKey a déjà été traitée,
     * retourne le résultat stocké sans retraiter.
     */
    public SynchronisationLog traiterPush(String idempotencyKey, String matriculeCommercial,
                                           SyncEntityType entityType, String entityRef,
                                           String payloadHash, Runnable processingLogic) {
        var existing = syncLogRepository.trouverParIdempotencyKey(idempotencyKey);
        if (existing.isPresent()) {
            return existing.get();
        }
        SynchronisationLog log = SynchronisationLog.recevoirPush(
            idempotencyKey, matriculeCommercial, entityType, entityRef, payloadHash);
        log = syncLogRepository.sauvegarder(log);
        try {
            processingLogic.run();
            log.marquerSucces(entityRef);
        } catch (Exception e) {
            log.marquerConflit(e.getMessage());
        }
        return syncLogRepository.sauvegarder(log);
    }

    /**
     * Pull incrémental — retourne les entités modifiées depuis lastSync
     * pour un commercial donné.
     */
    public SyncPullData pullIncremental(String matriculeCommercial, LocalDateTime lastSync) {
        LocalDate dateFrom = lastSync.toLocalDate();
        LocalDate today = LocalDate.now();

        List<Vente> ventes = venteRepository.listerParCommercial(matriculeCommercial)
            .stream().filter(v -> v.getDateModification() != null && v.getDateModification().isAfter(lastSync))
            .toList();

        List<PreCommande> preCommandes = preCommandeRepository.listerParCommercial(matriculeCommercial)
            .stream().filter(pc -> pc.getDateModification() != null && pc.getDateModification().isAfter(lastSync))
            .toList();

        List<FeuillRoute> feuillesRoute = feuillRouteRepository.listerParCommercial(matriculeCommercial)
            .stream().filter(fr -> fr.getDateModification() != null && fr.getDateModification().isAfter(lastSync))
            .toList();

        return new SyncPullData(LocalDateTime.now(), ventes, preCommandes, feuillesRoute);
    }

    public record SyncPullData(
        LocalDateTime serverTime,
        List<Vente> ventes,
        List<PreCommande> preCommandes,
        List<FeuillRoute> feuillesRoute
    ) {}
}
