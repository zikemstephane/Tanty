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

    /**
     * @param commercial filtre optionnel sur l'auteur de la déclaration
     * @param statut     filtre optionnel sur l'état du constat ({@code DECLARE} pour
     *                   la file d'attente de comptage du gestionnaire)
     */
    public List<RetourResponse> executer(String commercial, String statut) {
        var retours = (commercial == null || commercial.isBlank())
            ? retourRepository.trouverTous()
            : retourRepository.trouverParCommercial(commercial);
        return retours.stream()
            .filter(r -> statut == null || statut.isBlank() || r.getStatut().name().equalsIgnoreCase(statut))
            .map(retourMapper::versResponse)
            .toList();
    }
}
