package com.NTFOODS.Api_tanty.modules.production.application.lot.usecase.declarer;

import com.NTFOODS.Api_tanty.modules.auth.domain.utilisateur.aggregate.Utilisateur;
import com.NTFOODS.Api_tanty.modules.production.application.lot.dto.request.DeclarerLotRequest;
import com.NTFOODS.Api_tanty.modules.production.application.lot.dto.response.LotResponse;
import com.NTFOODS.Api_tanty.modules.production.application.lot.mapper.LotMapper;
import com.NTFOODS.Api_tanty.modules.production.domain.lot.aggregate.LotProduitFini;
import com.NTFOODS.Api_tanty.modules.production.domain.lot.repository.LotRepository;
import com.NTFOODS.Api_tanty.modules.production.domain.lot.specification.LotSpecification;
import com.NTFOODS.Api_tanty.shared.kernel.event.EventPublisher;
import com.NTFOODS.Api_tanty.shared.kernel.exception.AccesRefuseException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

/**
 * Use Case — déclarer un lot de produit fini (Chef de Production).
 * Publie l'événement de déclaration (déclenche la réception côté Stock).
 */
@Service
@Transactional
public class DeclarerLotUseCase {

    private static final DateTimeFormatter FMT = DateTimeFormatter.ofPattern("yyyyMMdd");

    private final LotRepository lotRepository;
    private final LotMapper lotMapper;
    private final EventPublisher eventPublisher;

    public DeclarerLotUseCase(LotRepository lotRepository, LotMapper lotMapper, EventPublisher eventPublisher) {
        this.lotRepository = lotRepository;
        this.lotMapper = lotMapper;
        this.eventPublisher = eventPublisher;
    }

    public LotResponse executer(DeclarerLotRequest req, Utilisateur acteur) {
        if (!LotSpecification.peutDeclarer(acteur.getRole())) {
            throw new AccesRefuseException("Seul le Chef de Production peut déclarer un lot");
        }
        String numeroLot = genererNumeroLot(req.dateProduction());
        LotProduitFini lot = LotProduitFini.declarer(numeroLot, req.referencePPH(), req.codeProduit(),
            req.designationProduit(), req.quantiteKg(), req.nbCartons(), req.dateProduction(), req.dlc(),
            acteur.getMatricule().getValeur());

        LotProduitFini saved = lotRepository.sauvegarder(lot);
        lot.getDomainEvents().forEach(eventPublisher::publish);
        lot.clearDomainEvents();
        return lotMapper.versResponse(saved);
    }

    private String genererNumeroLot(LocalDate date) {
        long seq = lotRepository.compterParStatutEtDate(
            com.NTFOODS.Api_tanty.modules.production.domain.lot.enums.StatutLot.DECLARE, date) + 1;
        return "LOT-" + FMT.format(date) + "-" + String.format("%03d", seq);
    }
}
