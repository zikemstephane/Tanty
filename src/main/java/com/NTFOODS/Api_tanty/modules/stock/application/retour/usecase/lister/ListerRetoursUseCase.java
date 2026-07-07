package com.NTFOODS.Api_tanty.modules.stock.application.retour.usecase.lister;

import com.NTFOODS.Api_tanty.modules.stock.application.retour.dto.response.RetourResponse;
import com.NTFOODS.Api_tanty.modules.stock.application.retour.mapper.RetourMapper;
import com.NTFOODS.Api_tanty.modules.stock.domain.retour.repository.BonRetourRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional(readOnly = true)
public class ListerRetoursUseCase {

    private final BonRetourRepository retourRepository;
    private final RetourMapper retourMapper;

    public ListerRetoursUseCase(BonRetourRepository retourRepository, RetourMapper retourMapper) {
        this.retourRepository = retourRepository;
        this.retourMapper = retourMapper;
    }

    public List<RetourResponse> executer(String commercial) {
        var retours = (commercial == null || commercial.isBlank())
            ? retourRepository.trouverTous()
            : retourRepository.trouverParCommercial(commercial);
        return retours.stream().map(retourMapper::versResponse).toList();
    }
}
