package com.NTFOODS.Api_tanty.modules.stock.application.inventaire.usecase.saisir;

import com.NTFOODS.Api_tanty.modules.stock.application.inventaire.dto.request.SaisirComptageRequest;
import com.NTFOODS.Api_tanty.modules.stock.application.inventaire.dto.response.InventaireResponse;
import com.NTFOODS.Api_tanty.modules.stock.application.inventaire.mapper.InventaireMapper;
import com.NTFOODS.Api_tanty.modules.stock.domain.inventaire.model.SessionInventaire;
import com.NTFOODS.Api_tanty.modules.stock.domain.inventaire.repository.InventaireRepository;
import com.NTFOODS.Api_tanty.shared.kernel.exception.EntiteNonTrouveeException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class SaisirComptageUseCase {

    private final InventaireRepository inventaireRepository;
    private final InventaireMapper inventaireMapper;

    public SaisirComptageUseCase(InventaireRepository inventaireRepository, InventaireMapper inventaireMapper) {
        this.inventaireRepository = inventaireRepository;
        this.inventaireMapper = inventaireMapper;
    }

    public InventaireResponse executer(String numero, SaisirComptageRequest request) {
        SessionInventaire session = inventaireRepository.trouverParNumero(numero)
            .orElseThrow(() -> EntiteNonTrouveeException.de("Inventaire", numero));
        session.saisirComptage(request.idLigne(), request.quantiteComptee());
        return inventaireMapper.versResponse(inventaireRepository.sauvegarder(session));
    }
}
