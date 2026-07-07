package com.NTFOODS.Api_tanty.modules.stock.application.dotation.usecase.lister;

import com.NTFOODS.Api_tanty.modules.stock.application.dotation.dto.response.DotationResponse;
import com.NTFOODS.Api_tanty.modules.stock.application.dotation.mapper.DotationMapper;
import com.NTFOODS.Api_tanty.modules.stock.domain.dotation.enums.StatutDotation;
import com.NTFOODS.Api_tanty.modules.stock.domain.dotation.repository.DotationRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional(readOnly = true)
public class ListerDotationsUseCase {

    private final DotationRepository dotationRepository;
    private final DotationMapper dotationMapper;

    public ListerDotationsUseCase(DotationRepository dotationRepository, DotationMapper dotationMapper) {
        this.dotationRepository = dotationRepository;
        this.dotationMapper = dotationMapper;
    }

    public List<DotationResponse> executer(String statut, String commercial) {
        var dotations = statut != null && !statut.isBlank()
            ? dotationRepository.trouverParStatut(StatutDotation.valueOf(statut.trim().toUpperCase()))
            : (commercial != null && !commercial.isBlank()
                ? dotationRepository.trouverParCommercial(commercial)
                : dotationRepository.trouverTous());
        return dotations.stream().map(dotationMapper::versResponse).toList();
    }
}
