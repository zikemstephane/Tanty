package com.NTFOODS.Api_tanty.modules.comptabilite.application.versement.usecase.valider;

import com.NTFOODS.Api_tanty.modules.comptabilite.application.versement.command.ValiderVersementCommand;
import com.NTFOODS.Api_tanty.modules.comptabilite.application.versement.dto.response.VersementResponse;
import com.NTFOODS.Api_tanty.modules.comptabilite.application.versement.mapper.VersementMapper;
import com.NTFOODS.Api_tanty.modules.comptabilite.domain.versement.aggregate.Versement;
import com.NTFOODS.Api_tanty.modules.comptabilite.domain.versement.repository.VersementRepository;
import com.NTFOODS.Api_tanty.shared.infrastructure.otp.OtpService;
import com.NTFOODS.Api_tanty.shared.kernel.event.DomainEventPublisher;
import com.NTFOODS.Api_tanty.shared.kernel.exception.EntiteNonTrouveeException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Use Case — valider un versement (justification requise si alerte rouge).
 *
 * Pilier 3 (Sécurisation Anti-Fraude) : la validation d'un versement est une
 * opération financière sensible qui exige la saisie préalable d'un code OTP
 * envoyé par SMS au comptable (généré via POST /api/securite/otp/generer
 * avec operationCible = "VERSEMENT:{referenceVersement}").
 */
@Service
@Transactional
public class ValiderVersementUseCase {

    private final VersementRepository versementRepository;
    private final VersementMapper versementMapper;
    private final DomainEventPublisher eventPublisher;
    private final OtpService otpService;

    public ValiderVersementUseCase(VersementRepository versementRepository, VersementMapper versementMapper,
                                   DomainEventPublisher eventPublisher, OtpService otpService) {
        this.versementRepository = versementRepository;
        this.versementMapper = versementMapper;
        this.eventPublisher = eventPublisher;
        this.otpService = otpService;
    }

    public VersementResponse executer(ValiderVersementCommand cmd) {
        otpService.verifier(cmd.matriculeValidateur(), "VERSEMENT:" + cmd.referenceVersement(), cmd.codeOtp());

        Versement versement = versementRepository.trouverParReference(cmd.referenceVersement())
            .orElseThrow(() -> EntiteNonTrouveeException.de("Versement", cmd.referenceVersement()));
        versement.valider(cmd.matriculeValidateur());
        Versement sauvegarde = versementRepository.sauvegarder(versement);
        sauvegarde.getDomainEvents().forEach(eventPublisher::publish);
        sauvegarde.clearDomainEvents();
        return versementMapper.versResponse(sauvegarde);
    }
}
