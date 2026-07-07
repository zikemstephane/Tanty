package com.NTFOODS.Api_tanty.modules.comptabilite.application.versement.usecase.enregistrer;

import com.NTFOODS.Api_tanty.modules.comptabilite.application.versement.command.EnregistrerVersementCommand;
import com.NTFOODS.Api_tanty.modules.comptabilite.application.versement.dto.response.VersementResponse;
import com.NTFOODS.Api_tanty.modules.comptabilite.application.versement.mapper.VersementMapper;
import com.NTFOODS.Api_tanty.modules.comptabilite.application.versement.service.ReconciliationService;
import com.NTFOODS.Api_tanty.modules.comptabilite.domain.versement.aggregate.Versement;
import com.NTFOODS.Api_tanty.modules.comptabilite.domain.versement.repository.VersementRepository;
import com.NTFOODS.Api_tanty.shared.kernel.event.DomainEventPublisher;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Use Case — enregistrer un versement (génère la référence, détecte l'alerte rouge).
 *
 * Réconciliation Financière Immédiate (Pilier 2) : si {@code montantAttendu} n'est pas
 * fourni, il est calculé automatiquement à partir du chiffre d'affaires déclaré par le
 * commercial (ventes de la journée), déduction faite de la valeur des retours physiques
 * déjà validés par le stock.
 */
@Service
@Transactional
public class EnregistrerVersementUseCase {

    private final VersementRepository versementRepository;
    private final VersementMapper versementMapper;
    private final DomainEventPublisher eventPublisher;
    private final ReconciliationService reconciliationService;

    public EnregistrerVersementUseCase(VersementRepository versementRepository, VersementMapper versementMapper,
                                       DomainEventPublisher eventPublisher, ReconciliationService reconciliationService) {
        this.versementRepository = versementRepository;
        this.versementMapper = versementMapper;
        this.eventPublisher = eventPublisher;
        this.reconciliationService = reconciliationService;
    }

    public VersementResponse executer(EnregistrerVersementCommand cmd) {
        double montantAttendu = cmd.montantAttendu() != null
            ? cmd.montantAttendu()
            : reconciliationService.calculer(cmd.matriculeCommercial(), cmd.date()).montantAttenduFCFA();

        String reference = versementRepository.genererReference(cmd.date());
        Versement versement = Versement.enregistrer(reference, cmd.matriculeCommercial(),
            montantAttendu, cmd.cashVerse(), cmd.typeVersement(), cmd.date());
        Versement sauvegarde = versementRepository.sauvegarder(versement);
        sauvegarde.getDomainEvents().forEach(eventPublisher::publish);
        sauvegarde.clearDomainEvents();
        return versementMapper.versResponse(sauvegarde);
    }
}
