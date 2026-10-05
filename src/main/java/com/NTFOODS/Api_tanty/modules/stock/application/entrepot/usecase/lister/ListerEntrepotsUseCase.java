package com.NTFOODS.Api_tanty.modules.stock.application.entrepot.usecase.lister;

import com.NTFOODS.Api_tanty.modules.stock.application.entrepot.dto.response.EntrepotResponse;
import com.NTFOODS.Api_tanty.modules.stock.application.entrepot.mapper.EntrepotMapper;
import com.NTFOODS.Api_tanty.modules.stock.domain.entrepot.enums.TypeEntrepot;
import com.NTFOODS.Api_tanty.modules.stock.domain.entrepot.repository.EntrepotRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional(readOnly = true)
public class ListerEntrepotsUseCase {

    private final EntrepotRepository entrepotRepository;
    private final EntrepotMapper entrepotMapper;

    public ListerEntrepotsUseCase(EntrepotRepository entrepotRepository, EntrepotMapper entrepotMapper) {
        this.entrepotRepository = entrepotRepository;
        this.entrepotMapper = entrepotMapper;
    }

    public List<EntrepotResponse> executer(String type) {
        var entrepots = (type == null || type.isBlank())
            ? entrepotRepository.trouverTous()
            : entrepotRepository.trouverParType(TypeEntrepot.valueOf(type.trim().toUpperCase()));
        return entrepots.stream().map(entrepotMapper::versResponse).toList();
    }
}
