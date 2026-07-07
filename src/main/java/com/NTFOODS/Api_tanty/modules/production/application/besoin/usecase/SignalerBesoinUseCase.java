package com.NTFOODS.Api_tanty.modules.production.application.besoin.usecase;

import com.NTFOODS.Api_tanty.modules.auth.domain.utilisateur.aggregate.Utilisateur;
import com.NTFOODS.Api_tanty.modules.production.application.besoin.dto.request.SignalerBesoinRequest;
import com.NTFOODS.Api_tanty.modules.production.application.besoin.dto.response.ExpressionBesoinResponse;
import com.NTFOODS.Api_tanty.modules.production.application.besoin.mapper.ExpressionBesoinMapper;
import com.NTFOODS.Api_tanty.modules.production.domain.besoin.model.ExpressionBesoin;
import com.NTFOODS.Api_tanty.modules.production.domain.besoin.repository.ExpressionBesoinRepository;
import com.NTFOODS.Api_tanty.shared.kernel.exception.RegleMetierException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class SignalerBesoinUseCase {

    private final ExpressionBesoinRepository repository;
    private final ExpressionBesoinMapper mapper;

    public SignalerBesoinUseCase(ExpressionBesoinRepository repository, ExpressionBesoinMapper mapper) {
        this.repository = repository;
        this.mapper = mapper;
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
        return mapper.versResponse(repository.sauvegarder(besoin));
    }
}
