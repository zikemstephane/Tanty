package com.NTFOODS.Api_tanty.modules.stock.application.seuil.usecase;

import com.NTFOODS.Api_tanty.modules.stock.application.seuil.dto.response.SeuilResponse;
import com.NTFOODS.Api_tanty.modules.stock.domain.seuil.repository.SeuilStockRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional(readOnly = true)
public class ListerSeuilsUseCase {

    private final SeuilStockRepository seuilRepository;

    public ListerSeuilsUseCase(SeuilStockRepository seuilRepository) {
        this.seuilRepository = seuilRepository;
    }

    public List<SeuilResponse> executer() {
        return seuilRepository.trouverTous().stream()
            .map(s -> new SeuilResponse(s.getId(), s.getCodeProduit(), s.getTypeEntrepot().name(),
                s.getSeuilReapprovisionnement(), s.isActif()))
            .toList();
    }
}
