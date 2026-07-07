package com.NTFOODS.Api_tanty.modules.stock.application.alerte.usecase;

import com.NTFOODS.Api_tanty.modules.auth.domain.utilisateur.aggregate.Utilisateur;
import com.NTFOODS.Api_tanty.modules.stock.application.alerte.dto.response.AlerteResponse;
import com.NTFOODS.Api_tanty.modules.stock.application.alerte.mapper.AlerteMapper;
import com.NTFOODS.Api_tanty.modules.stock.domain.alerte.model.Alerte;
import com.NTFOODS.Api_tanty.modules.stock.domain.alerte.repository.AlerteRepository;
import com.NTFOODS.Api_tanty.shared.kernel.exception.EntiteNonTrouveeException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/** Use Case — marquer une alerte comme lue ou traitée. */
@Service
@Transactional
public class TraiterAlerteUseCase {

    public enum Action { MARQUER_LUE, TRAITER }

    private final AlerteRepository alerteRepository;
    private final AlerteMapper alerteMapper;

    public TraiterAlerteUseCase(AlerteRepository alerteRepository, AlerteMapper alerteMapper) {
        this.alerteRepository = alerteRepository;
        this.alerteMapper = alerteMapper;
    }

    public AlerteResponse executer(String reference, Action action, Utilisateur acteur) {
        Alerte alerte = alerteRepository.trouverParReference(reference)
            .orElseThrow(() -> EntiteNonTrouveeException.de("Alerte", reference));
        switch (action) {
            case MARQUER_LUE -> alerte.marquerLue();
            case TRAITER -> alerte.traiter(acteur.getMatricule().getValeur());
        }
        return alerteMapper.versResponse(alerteRepository.sauvegarder(alerte));
    }
}
