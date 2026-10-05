package com.NTFOODS.Api_tanty.modules.production.application.besoin.usecase;

import com.NTFOODS.Api_tanty.modules.production.application.besoin.dto.response.ExpressionBesoinResponse;
import com.NTFOODS.Api_tanty.modules.production.application.besoin.mapper.ExpressionBesoinMapper;
import com.NTFOODS.Api_tanty.modules.production.domain.besoin.model.ExpressionBesoin;
import com.NTFOODS.Api_tanty.modules.production.domain.besoin.repository.ExpressionBesoinRepository;
import com.NTFOODS.Api_tanty.shared.kernel.exception.EntiteNonTrouveeException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class TraiterBesoinUseCase {

    private final ExpressionBesoinRepository repository;
    private final ExpressionBesoinMapper mapper;

    public TraiterBesoinUseCase(ExpressionBesoinRepository repository, ExpressionBesoinMapper mapper) {
        this.repository = repository;
        this.mapper = mapper;
    }

    public ExpressionBesoinResponse valider(String numero, String matriculeValidateur) {
        ExpressionBesoin besoin = repository.trouverParNumero(numero)
            .orElseThrow(() -> EntiteNonTrouveeException.de("Expression de besoin", numero));
        besoin.valider(matriculeValidateur);
        return mapper.versResponse(repository.sauvegarder(besoin));
    }

    public ExpressionBesoinResponse rejeter(String numero, String matriculeValidateur, String motifRejet) {
        ExpressionBesoin besoin = repository.trouverParNumero(numero)
            .orElseThrow(() -> EntiteNonTrouveeException.de("Expression de besoin", numero));
        besoin.rejeter(matriculeValidateur, motifRejet);
        return mapper.versResponse(repository.sauvegarder(besoin));
    }
}
