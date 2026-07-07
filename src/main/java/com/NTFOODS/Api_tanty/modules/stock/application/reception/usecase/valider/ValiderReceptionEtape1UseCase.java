package com.NTFOODS.Api_tanty.modules.stock.application.reception.usecase.valider;

import com.NTFOODS.Api_tanty.modules.auth.domain.utilisateur.aggregate.Utilisateur;
import com.NTFOODS.Api_tanty.modules.stock.application.reception.dto.response.ReceptionResponse;
import com.NTFOODS.Api_tanty.modules.stock.application.reception.mapper.ReceptionMapper;
import com.NTFOODS.Api_tanty.modules.stock.domain.reception.model.Reception;
import com.NTFOODS.Api_tanty.modules.stock.domain.reception.repository.ReceptionRepository;
import com.NTFOODS.Api_tanty.shared.kernel.exception.EntiteNonTrouveeException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/** Use Case — 1ʳᵉ validation d'une réception (vérification physique, Gestionnaire). */
@Service
@Transactional
public class ValiderReceptionEtape1UseCase {

    private final ReceptionRepository receptionRepository;
    private final ReceptionMapper receptionMapper;

    public ValiderReceptionEtape1UseCase(ReceptionRepository receptionRepository, ReceptionMapper receptionMapper) {
        this.receptionRepository = receptionRepository;
        this.receptionMapper = receptionMapper;
    }

    public ReceptionResponse executer(String numero, Utilisateur acteur) {
        Reception reception = receptionRepository.trouverParNumero(numero)
            .orElseThrow(() -> EntiteNonTrouveeException.de("Réception", numero));
        reception.validerEtape1(acteur.getMatricule().getValeur(), acteur.getRole());
        return receptionMapper.versResponse(receptionRepository.sauvegarder(reception));
    }
}
