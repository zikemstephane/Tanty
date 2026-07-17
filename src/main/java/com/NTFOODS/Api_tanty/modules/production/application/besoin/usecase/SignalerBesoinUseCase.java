package com.NTFOODS.Api_tanty.modules.production.application.besoin.usecase;

import com.NTFOODS.Api_tanty.modules.auth.domain.utilisateur.aggregate.Utilisateur;
import com.NTFOODS.Api_tanty.modules.production.application.besoin.dto.request.SignalerBesoinRequest;
import com.NTFOODS.Api_tanty.modules.production.application.besoin.dto.response.ExpressionBesoinResponse;
import com.NTFOODS.Api_tanty.modules.production.application.besoin.mapper.ExpressionBesoinMapper;
import com.NTFOODS.Api_tanty.modules.production.domain.besoin.model.ExpressionBesoin;
import com.NTFOODS.Api_tanty.modules.production.domain.besoin.repository.ExpressionBesoinRepository;
import com.NTFOODS.Api_tanty.modules.stock.domain.alerte.enums.TypeAlerte;
import com.NTFOODS.Api_tanty.modules.stock.domain.alerte.model.Alerte;
import com.NTFOODS.Api_tanty.modules.stock.domain.alerte.repository.AlerteRepository;
import com.NTFOODS.Api_tanty.shared.kernel.event.EventPublisher;
import com.NTFOODS.Api_tanty.shared.kernel.exception.RegleMetierException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class SignalerBesoinUseCase {

    private final ExpressionBesoinRepository repository;
    private final ExpressionBesoinMapper mapper;
    private final AlerteRepository alerteRepository;
    private final EventPublisher eventPublisher;

    public SignalerBesoinUseCase(ExpressionBesoinRepository repository, ExpressionBesoinMapper mapper,
                                  AlerteRepository alerteRepository, EventPublisher eventPublisher) {
        this.repository = repository;
        this.mapper = mapper;
        this.alerteRepository = alerteRepository;
        this.eventPublisher = eventPublisher;
    }

    public ExpressionBesoinResponse executer(SignalerBesoinRequest req, Utilisateur utilisateur) {
        if (repository.existeParNumero(req.numero())) {
            throw new RegleMetierException("Une expression de besoin avec le numéro " + req.numero() + " existe déjà");
        }
        ExpressionBesoin besoin = ExpressionBesoin.signaler(
            req.numero(), req.referencePPH(), req.type(), req.codeProduit(), req.designationProduit(),
            req.quantiteDemandee(), req.unite(), utilisateur.getMatricule().getValeur(),
            req.dateBesoin(), req.motif()
        );
        ExpressionBesoin sauvegarde = repository.sauvegarder(besoin);

        String message = String.format("Nouveau besoin %s signalé par %s : %s (%s %s)",
            req.numero(), utilisateur.getMatricule().getValeur(), req.designationProduit(),
            req.quantiteDemandee(), req.unite());
        Alerte alerte = Alerte.creer("ALT-" + req.numero(), TypeAlerte.EXPRESSION_DE_BESOIN, message,
            req.codeProduit(), null, req.numero());
        Alerte alerteSauvegardee = alerteRepository.sauvegarder(alerte);
        alerteSauvegardee.getDomainEvents().forEach(eventPublisher::publish);
        alerteSauvegardee.clearDomainEvents();

        return mapper.versResponse(sauvegarde);
    }
}
