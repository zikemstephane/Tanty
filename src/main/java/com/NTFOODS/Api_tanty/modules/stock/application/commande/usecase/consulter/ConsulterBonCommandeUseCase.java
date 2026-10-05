package com.NTFOODS.Api_tanty.modules.stock.application.commande.usecase.consulter;

import com.NTFOODS.Api_tanty.modules.stock.application.commande.dto.response.BonCommandeResponse;
import com.NTFOODS.Api_tanty.modules.stock.application.commande.mapper.BonCommandeMapper;
import com.NTFOODS.Api_tanty.modules.stock.domain.commande.repository.BonCommandeRepository;
import com.NTFOODS.Api_tanty.shared.kernel.exception.EntiteNonTrouveeException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
public class ConsulterBonCommandeUseCase {

    private final BonCommandeRepository bonCommandeRepository;
    private final BonCommandeMapper bonCommandeMapper;

    public ConsulterBonCommandeUseCase(BonCommandeRepository bonCommandeRepository, BonCommandeMapper bonCommandeMapper) {
        this.bonCommandeRepository = bonCommandeRepository;
        this.bonCommandeMapper = bonCommandeMapper;
    }

    public BonCommandeResponse executer(String numero) {
        return bonCommandeRepository.trouverParNumero(numero)
            .map(bonCommandeMapper::versResponse)
            .orElseThrow(() -> EntiteNonTrouveeException.de("Bon de commande", numero));
    }
}
