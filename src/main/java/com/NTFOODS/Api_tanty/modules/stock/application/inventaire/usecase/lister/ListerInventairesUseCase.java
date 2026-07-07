package com.NTFOODS.Api_tanty.modules.stock.application.inventaire.usecase.lister;

import com.NTFOODS.Api_tanty.modules.stock.application.inventaire.dto.response.InventaireResponse;
import com.NTFOODS.Api_tanty.modules.stock.application.inventaire.mapper.InventaireMapper;
import com.NTFOODS.Api_tanty.modules.stock.domain.inventaire.enums.StatutInventaire;
import com.NTFOODS.Api_tanty.modules.stock.domain.inventaire.repository.InventaireRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional(readOnly = true)
public class ListerInventairesUseCase {

    private final InventaireRepository inventaireRepository;
    private final InventaireMapper inventaireMapper;

    public ListerInventairesUseCase(InventaireRepository inventaireRepository, InventaireMapper inventaireMapper) {
        this.inventaireRepository = inventaireRepository;
        this.inventaireMapper = inventaireMapper;
    }

    public List<InventaireResponse> executer(String statut) {
        var sessions = (statut == null || statut.isBlank())
            ? inventaireRepository.trouverTous()
            : inventaireRepository.trouverParStatut(StatutInventaire.valueOf(statut.trim().toUpperCase()));
        return sessions.stream().map(inventaireMapper::versResponse).toList();
    }
}
