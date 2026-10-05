package com.NTFOODS.Api_tanty.modules.stock.application.transfert.usecase.lister;

import com.NTFOODS.Api_tanty.modules.stock.application.transfert.dto.response.TransfertResponse;
import com.NTFOODS.Api_tanty.modules.stock.application.transfert.mapper.TransfertMapper;
import com.NTFOODS.Api_tanty.modules.stock.domain.transfert.enums.StatutTransfert;
import com.NTFOODS.Api_tanty.modules.stock.domain.transfert.repository.BonTransfertRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional(readOnly = true)
public class ListerTransfertsUseCase {

    private final BonTransfertRepository transfertRepository;
    private final TransfertMapper transfertMapper;

    public ListerTransfertsUseCase(BonTransfertRepository transfertRepository, TransfertMapper transfertMapper) {
        this.transfertRepository = transfertRepository;
        this.transfertMapper = transfertMapper;
    }

    public List<TransfertResponse> executer(String statut) {
        var transferts = (statut == null || statut.isBlank())
            ? transfertRepository.trouverTous()
            : transfertRepository.trouverParStatut(StatutTransfert.valueOf(statut.trim().toUpperCase()));
        return transferts.stream().map(transfertMapper::versResponse).toList();
    }
}
