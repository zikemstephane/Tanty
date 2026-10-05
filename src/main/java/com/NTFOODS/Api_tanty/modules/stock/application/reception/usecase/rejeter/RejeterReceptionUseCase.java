package com.NTFOODS.Api_tanty.modules.stock.application.reception.usecase.rejeter;

import com.NTFOODS.Api_tanty.modules.auth.domain.utilisateur.aggregate.Utilisateur;
import com.NTFOODS.Api_tanty.modules.stock.application.reception.dto.response.ReceptionResponse;
import com.NTFOODS.Api_tanty.modules.stock.application.reception.mapper.ReceptionMapper;
import com.NTFOODS.Api_tanty.modules.stock.domain.reception.model.Reception;
import com.NTFOODS.Api_tanty.modules.stock.domain.reception.repository.ReceptionRepository;
import com.NTFOODS.Api_tanty.shared.kernel.exception.EntiteNonTrouveeException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class RejeterReceptionUseCase {

    private final ReceptionRepository receptionRepository;
    private final ReceptionMapper receptionMapper;

    public RejeterReceptionUseCase(ReceptionRepository receptionRepository, ReceptionMapper receptionMapper) {
        this.receptionRepository = receptionRepository;
        this.receptionMapper = receptionMapper;
    }

    public ReceptionResponse executer(String numero, String motif, Utilisateur acteur) {
        Reception reception = receptionRepository.trouverParNumero(numero)
            .orElseThrow(() -> EntiteNonTrouveeException.de("Réception", numero));
        reception.rejeter(acteur.getMatricule().getValeur(), acteur.getRole(), motif);
        return receptionMapper.versResponse(receptionRepository.sauvegarder(reception));
    }
}
