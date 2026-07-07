package com.NTFOODS.Api_tanty.modules.production.application.of.usecase;

import com.NTFOODS.Api_tanty.modules.production.application.of.dto.response.OFResponse;
import com.NTFOODS.Api_tanty.modules.production.application.of.mapper.OFMapper;
import com.NTFOODS.Api_tanty.modules.production.domain.of.aggregate.OrdreFabrication;
import com.NTFOODS.Api_tanty.modules.production.domain.of.repository.OFRepository;
import com.NTFOODS.Api_tanty.shared.kernel.exception.EntiteNonTrouveeException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class EnregistrerProductionOFUseCase {

    private final OFRepository repository;
    private final OFMapper mapper;

    public EnregistrerProductionOFUseCase(OFRepository repository, OFMapper mapper) {
        this.repository = repository;
        this.mapper = mapper;
    }

    public OFResponse executer(String idOF, int quantite) {
        OrdreFabrication of = repository.trouverParId(idOF)
            .orElseThrow(() -> EntiteNonTrouveeException.de("Ordre de fabrication", idOF));
        of.enregistrerProduction(quantite);
        return mapper.versResponse(repository.sauvegarder(of));
    }
}
