package com.NTFOODS.Api_tanty.modules.comptabilite.application.versement.usecase.enregistrer;

import com.NTFOODS.Api_tanty.modules.comptabilite.application.versement.command.EnregistrerVersementCommand;
import com.NTFOODS.Api_tanty.modules.comptabilite.application.versement.dto.response.VersementResponse;
import com.NTFOODS.Api_tanty.modules.comptabilite.application.versement.mapper.VersementMapper;
import com.NTFOODS.Api_tanty.modules.comptabilite.application.caisse.service.ParametrageCaisseService;
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
    private final ParametrageCaisseService parametrageCaisseService;

    public EnregistrerVersementUseCase(VersementRepository versementRepository, VersementMapper versementMapper,
                                       DomainEventPublisher eventPublisher, ReconciliationService reconciliationService,
                                       ParametrageCaisseService parametrageCaisseService) {
        this.versementRepository = versementRepository;
        this.versementMapper = versementMapper;
        this.eventPublisher = eventPublisher;
        this.reconciliationService = reconciliationService;
        this.parametrageCaisseService = parametrageCaisseService;
    }

    public VersementResponse executer(EnregistrerVersementCommand cmd) {
        double montantAttendu = cmd.montantAttendu() != null
            ? cmd.montantAttendu()
            : reconciliationService.calculer(cmd.matriculeCommercial(), cmd.date()).montantAttenduFCFA();

        // Le seuil retenu est celui en vigueur à la date du versement, et non le seuil
        // courant : un versement d'hier reste apprécié selon les règles d'hier.
        double seuilAlerteRouge = parametrageCaisseService.parametrageApplicableA(cmd.date())
            .getSeuilAlerteRougeVersementFCFA();

        String reference = versementRepository.genererReference(cmd.date());
        Versement versement = Versement.enregistrer(reference, cmd.matriculeCommercial(),
            montantAttendu, cmd.cashVerse(), cmd.typeVersement(), cmd.date(), seuilAlerteRouge);

        // La justification n'a de sens que sur un versement en alerte : appliquée
        // ici, elle n'est jamais perdue entre la saisie terrain et l'enregistrement.
        if (versement.isAlerteRouge() && cmd.justificationEcart() != null
            && !cmd.justificationEcart().isBlank()) {
            versement.justifierEcart(cmd.justificationEcart());
        }

        Versement sauvegarde = versementRepository.sauvegarder(versement);
        sauvegarde.getDomainEvents().forEach(eventPublisher::publish);
        sauvegarde.clearDomainEvents();
        return versementMapper.versResponse(sauvegarde);
    }
}
