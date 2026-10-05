package com.NTFOODS.Api_tanty.modules.sync.application.service;

import com.NTFOODS.Api_tanty.modules.commercial.domain.vente.aggregate.Vente;
import com.NTFOODS.Api_tanty.modules.commercial.domain.vente.repository.VenteRepository;
import com.NTFOODS.Api_tanty.modules.sync.domain.aggregate.SynchronisationLog;
import com.NTFOODS.Api_tanty.modules.sync.domain.enums.SyncEntityType;
import com.NTFOODS.Api_tanty.modules.sync.domain.repository.SynchronisationLogRepository;
import com.NTFOODS.Api_tanty.modules.commercial.domain.feuillroute.aggregate.FeuillRoute;
import com.NTFOODS.Api_tanty.modules.commercial.domain.feuillroute.repository.FeuillRouteRepository;
import com.NTFOODS.Api_tanty.modules.commercial.domain.precommande.aggregate.PreCommande;
import com.NTFOODS.Api_tanty.modules.commercial.domain.precommande.repository.PreCommandeRepository;
import com.NTFOODS.Api_tanty.shared.kernel.exception.EntiteNonTrouveeException;
import com.NTFOODS.Api_tanty.shared.kernel.exception.RegleMetierException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

/**
 * Service de synchronisation offline-first (§12).
 *
 * <p><strong>Push idempotent</strong> — une clé d'idempotence déjà traitée
 * renvoie le résultat précédent sans rejouer l'opération. C'est ce qui permet au
 * terminal de réémettre sans crainte un envoi dont il n'a pas reçu la réponse.</p>
 *
 * <p><strong>Pull incrémental</strong> — le terminal annonce sa dernière
 * synchronisation et reçoit ce qui a bougé depuis.</p>
 */
@Service
public class SyncService {

    private static final Logger log = LoggerFactory.getLogger(SyncService.class);

    private final SynchronisationLogRepository syncLogRepository;
    private final SyncPushDispatcher dispatcher;
    private final VenteRepository venteRepository;
    private final PreCommandeRepository preCommandeRepository;
    private final FeuillRouteRepository feuillRouteRepository;

    public SyncService(SynchronisationLogRepository syncLogRepository,
                       SyncPushDispatcher dispatcher,
                       VenteRepository venteRepository,
                       PreCommandeRepository preCommandeRepository,
                       FeuillRouteRepository feuillRouteRepository) {
        this.syncLogRepository = syncLogRepository;
        this.dispatcher = dispatcher;
        this.venteRepository = venteRepository;
        this.preCommandeRepository = preCommandeRepository;
        this.feuillRouteRepository = feuillRouteRepository;
    }

    /**
     * Traite un push : rejoue réellement l'opération et journalise son issue.
     *
     * <p>Trois issues, et elles n'appellent pas la même réaction du terminal :</p>
     * <ul>
     *   <li><strong>SUCCESS</strong> — l'entité existe côté serveur, sa référence
     *       est retournée. Le terminal peut retirer l'opération de sa file.</li>
     *   <li><strong>CONFLICT</strong> — une règle métier l'a refusée : stock
     *       devenu insuffisant, client désactivé, remise non couverte. Réessayer
     *       à l'identique donnerait le même refus ; la décision revient au
     *       commercial, l'opération reste dans sa file avec le motif.</li>
     *   <li><strong>REJECTED</strong> — le contenu transmis est inexploitable.
     *       Le terminal ne doit pas le renvoyer en boucle.</li>
     * </ul>
     *
     * <p>Une panne technique, elle, ne donne lieu à <em>aucune</em> écriture de
     * journal : l'exception remonte, le terminal reçoit une erreur HTTP et
     * réessaiera plus tard. Consigner un échec réseau comme un conflit métier
     * bloquerait définitivement une opération parfaitement valide.</p>
     *
     * <p>Chaque push s'exécute dans sa propre transaction : l'échec de l'un ne
     * doit pas annuler ceux qui l'ont précédé dans le même vidage de file.</p>
     */
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public SynchronisationLog traiterPush(String idempotencyKey, String matriculeCommercial,
                                           SyncEntityType entityType, String entityRef,
                                           String payloadHash, String payloadJson) {
        var existant = syncLogRepository.trouverParIdempotencyKey(idempotencyKey);
        if (existant.isPresent()) {
            log.debug("[SYNC] Push {} déjà traité — résultat précédent renvoyé", idempotencyKey);
            return existant.get();
        }

        SynchronisationLog journal = SynchronisationLog.recevoirPush(
            idempotencyKey, matriculeCommercial, entityType, entityRef, payloadHash);

        try {
            String referenceServeur = dispatcher.rejouer(entityType, payloadJson, matriculeCommercial);
            journal.marquerSucces(referenceServeur);
            log.info("[SYNC] {} rejoué pour {} → {}", entityType, matriculeCommercial, referenceServeur);

        } catch (SyncPushDispatcher.SyncPayloadInvalideException e) {
            journal.marquerRejete(e.getMessage());
            log.warn("[SYNC] Push {} rejeté — contenu inexploitable : {}", idempotencyKey, e.getMessage());

        } catch (RegleMetierException | EntiteNonTrouveeException | IllegalArgumentException | IllegalStateException e) {
            journal.marquerConflit(e.getMessage());
            log.warn("[SYNC] Push {} en conflit — {}", idempotencyKey, e.getMessage());
        }

        return syncLogRepository.sauvegarder(journal);
    }

    /**
     * Pull incrémental — entités modifiées depuis {@code lastSync} pour un commercial.
     */
    @Transactional(readOnly = true)
    public SyncPullData pullIncremental(String matriculeCommercial, LocalDateTime lastSync) {
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
