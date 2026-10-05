package com.NTFOODS.Api_tanty.modules.production.infrastructure.messaging;

import com.NTFOODS.Api_tanty.modules.production.domain.fiche.aggregate.FicheProduitJournaliere;
import com.NTFOODS.Api_tanty.modules.production.domain.fiche.repository.FicheProduitRepository;
import com.NTFOODS.Api_tanty.modules.production.domain.lot.events.LotValideParStockEvent;
import com.NTFOODS.Api_tanty.shared.kernel.event.EventPublisher;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

/**
 * "Lock Numérique" (Pilier 1 — Immuabilité) : dès que le Gestionnaire de
 * Stock valide physiquement un lot de produit fini, la fiche de production
 * journalière correspondante (même referencePPH + même date) est
 * automatiquement verrouillée. L'responsable de salle ne peut plus en
 * modifier les chiffres — c'est désormais une preuve numérique immuable.
 */
@Component
public class FicheVerrouillageListener {

    private static final Logger log = LoggerFactory.getLogger(FicheVerrouillageListener.class);

    private final FicheProduitRepository ficheRepository;
    private final EventPublisher eventPublisher;

    public FicheVerrouillageListener(FicheProduitRepository ficheRepository, EventPublisher eventPublisher) {
        this.ficheRepository = ficheRepository;
        this.eventPublisher = eventPublisher;
    }

    @EventListener
    @Transactional
    public void surLotValideParStock(LotValideParStockEvent event) {
        ficheRepository.trouverParPPHEtDate(event.getReferencePPH(), event.getDateProduction())
            .ifPresentOrElse(fiche -> {
                fiche.verrouiller();
                FicheProduitJournaliere sauvegardee = ficheRepository.sauvegarder(fiche);
                sauvegardee.getDomainEvents().forEach(eventPublisher::publish);
                sauvegardee.clearDomainEvents();
                log.info("Fiche de production verrouillée : PPH={}, date={} (suite validation stock du lot {})",
                    event.getReferencePPH(), event.getDateProduction(), event.getNumeroLot());
            }, () -> log.debug(
                "Aucune fiche de production trouvée pour PPH={} / date={} — rien à verrouiller",
                event.getReferencePPH(), event.getDateProduction()));
    }
}
