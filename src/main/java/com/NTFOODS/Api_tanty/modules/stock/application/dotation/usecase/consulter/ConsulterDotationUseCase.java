package com.NTFOODS.Api_tanty.modules.stock.application.dotation.usecase.consulter;

import com.NTFOODS.Api_tanty.modules.stock.application.dotation.dto.response.DotationResponse;
import com.NTFOODS.Api_tanty.modules.stock.application.dotation.mapper.DotationMapper;
import com.NTFOODS.Api_tanty.modules.stock.domain.dotation.repository.DotationRepository;
import com.NTFOODS.Api_tanty.shared.kernel.exception.EntiteNonTrouveeException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
public class ConsulterDotationUseCase {

    private final DotationRepository dotationRepository;
    private final DotationMapper dotationMapper;

    public ConsulterDotationUseCase(DotationRepository dotationRepository, DotationMapper dotationMapper) {
        this.dotationRepository = dotationRepository;
        this.dotationMapper = dotationMapper;
    }

    public DotationResponse executer(String numero) {
        return dotationRepository.trouverParNumero(numero)
            .map(dotationMapper::versResponse)
            .orElseThrow(() -> EntiteNonTrouveeException.de("Dotation", numero));
    }
}
