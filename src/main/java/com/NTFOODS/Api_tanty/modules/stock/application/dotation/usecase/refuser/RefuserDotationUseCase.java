package com.NTFOODS.Api_tanty.modules.stock.application.dotation.usecase.refuser;

import com.NTFOODS.Api_tanty.modules.auth.domain.utilisateur.aggregate.Utilisateur;
import com.NTFOODS.Api_tanty.modules.stock.application.dotation.dto.response.DotationResponse;
import com.NTFOODS.Api_tanty.modules.stock.application.dotation.mapper.DotationMapper;
import com.NTFOODS.Api_tanty.modules.stock.domain.dotation.model.Dotation;
import com.NTFOODS.Api_tanty.modules.stock.domain.dotation.repository.DotationRepository;
import com.NTFOODS.Api_tanty.shared.kernel.exception.EntiteNonTrouveeException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class RefuserDotationUseCase {

    private final DotationRepository dotationRepository;
    private final DotationMapper dotationMapper;

    public RefuserDotationUseCase(DotationRepository dotationRepository, DotationMapper dotationMapper) {
        this.dotationRepository = dotationRepository;
        this.dotationMapper = dotationMapper;
    }

    public DotationResponse executer(String numero, String motif, Utilisateur acteur) {
        Dotation dotation = dotationRepository.trouverParNumero(numero)
            .orElseThrow(() -> EntiteNonTrouveeException.de("Dotation", numero));
        dotation.refuser(acteur.getMatricule().getValeur(), acteur.getRole(), motif);
        return dotationMapper.versResponse(dotationRepository.sauvegarder(dotation));
    }
}
