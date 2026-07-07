package com.NTFOODS.Api_tanty.modules.production.application.of.usecase;

import com.NTFOODS.Api_tanty.modules.production.application.of.dto.request.CreerOFRequest;
import com.NTFOODS.Api_tanty.modules.production.application.of.dto.response.OFResponse;
import com.NTFOODS.Api_tanty.modules.production.application.of.mapper.OFMapper;
import com.NTFOODS.Api_tanty.modules.production.domain.of.aggregate.OrdreFabrication;
import com.NTFOODS.Api_tanty.modules.production.domain.of.repository.OFRepository;
import com.NTFOODS.Api_tanty.shared.kernel.exception.RegleMetierException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;

@Service
@Transactional
public class CreerOFUseCase {

    private final OFRepository repository;
    private final OFMapper mapper;

    public CreerOFUseCase(OFRepository repository, OFMapper mapper) {
        this.repository = repository;
        this.mapper = mapper;
    }

    public OFResponse executer(CreerOFRequest req) {
        if (repository.trouverParId(req.idOF()).isPresent()) {
            throw new RegleMetierException("Un OF avec l'identifiant " + req.idOF() + " existe déjà");
        }
        LocalDate dateButoir = req.dateButoir() != null ? req.dateButoir() : LocalDate.now().plusDays(7);
        OrdreFabrication of = OrdreFabrication.creer(req.idOF(), req.referencePPH(), req.codeProduit(),
            req.designationProduit(), req.qteDemandee(), dateButoir);
        return mapper.versResponse(repository.sauvegarder(of));
    }
}
