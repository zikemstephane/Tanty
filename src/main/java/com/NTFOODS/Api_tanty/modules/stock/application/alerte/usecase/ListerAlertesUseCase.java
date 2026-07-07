package com.NTFOODS.Api_tanty.modules.stock.application.alerte.usecase;

import com.NTFOODS.Api_tanty.modules.stock.application.alerte.dto.response.AlerteResponse;
import com.NTFOODS.Api_tanty.modules.stock.application.alerte.mapper.AlerteMapper;
import com.NTFOODS.Api_tanty.modules.stock.domain.alerte.enums.StatutAlerte;
import com.NTFOODS.Api_tanty.modules.stock.domain.alerte.repository.AlerteRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional(readOnly = true)
public class ListerAlertesUseCase {

    private final AlerteRepository alerteRepository;
    private final AlerteMapper alerteMapper;

    public ListerAlertesUseCase(AlerteRepository alerteRepository, AlerteMapper alerteMapper) {
        this.alerteRepository = alerteRepository;
        this.alerteMapper = alerteMapper;
    }

    public List<AlerteResponse> executer(String statut, String role) {
        var alertes = statut != null && !statut.isBlank()
            ? alerteRepository.trouverParStatut(StatutAlerte.valueOf(statut.trim().toUpperCase()))
            : (role != null && !role.isBlank()
                ? alerteRepository.trouverParDestinataire(role.trim().toUpperCase())
                : alerteRepository.trouverTous());
        return alertes.stream().map(alerteMapper::versResponse).toList();
    }
}
