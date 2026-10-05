package com.NTFOODS.Api_tanty.modules.stock.application.reception.usecase.consulter;

import com.NTFOODS.Api_tanty.modules.stock.application.reception.dto.response.ReceptionResponse;
import com.NTFOODS.Api_tanty.modules.stock.application.reception.mapper.ReceptionMapper;
import com.NTFOODS.Api_tanty.modules.stock.domain.reception.repository.ReceptionRepository;
import com.NTFOODS.Api_tanty.shared.kernel.exception.EntiteNonTrouveeException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
public class ConsulterReceptionUseCase {

    private final ReceptionRepository receptionRepository;
    private final ReceptionMapper receptionMapper;

    public ConsulterReceptionUseCase(ReceptionRepository receptionRepository, ReceptionMapper receptionMapper) {
        this.receptionRepository = receptionRepository;
        this.receptionMapper = receptionMapper;
    }

    public ReceptionResponse executer(String numero) {
        return receptionRepository.trouverParNumero(numero)
            .map(receptionMapper::versResponse)
            .orElseThrow(() -> EntiteNonTrouveeException.de("Réception", numero));
    }
}
