package com.NTFOODS.Api_tanty.modules.stock.application.reception.usecase.lister;

import com.NTFOODS.Api_tanty.modules.stock.application.reception.dto.response.ReceptionResponse;
import com.NTFOODS.Api_tanty.modules.stock.application.reception.mapper.ReceptionMapper;
import com.NTFOODS.Api_tanty.modules.stock.domain.reception.enums.StatutReception;
import com.NTFOODS.Api_tanty.modules.stock.domain.reception.repository.ReceptionRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional(readOnly = true)
public class ListerReceptionsUseCase {

    private final ReceptionRepository receptionRepository;
    private final ReceptionMapper receptionMapper;

    public ListerReceptionsUseCase(ReceptionRepository receptionRepository, ReceptionMapper receptionMapper) {
        this.receptionRepository = receptionRepository;
        this.receptionMapper = receptionMapper;
    }

    public List<ReceptionResponse> executer(String statut, String numeroBonCommande) {
        List<com.NTFOODS.Api_tanty.modules.stock.domain.reception.model.Reception> receptions;
        if (statut != null && !statut.isBlank()) {
            receptions = receptionRepository.trouverParStatut(StatutReception.valueOf(statut.trim().toUpperCase()));
        } else if (numeroBonCommande != null && !numeroBonCommande.isBlank()) {
            receptions = receptionRepository.trouverParBonCommande(numeroBonCommande);
        } else {
            receptions = receptionRepository.trouverTous();
        }
        return receptions.stream().map(receptionMapper::versResponse).toList();
    }
}
