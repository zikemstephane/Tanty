package com.NTFOODS.Api_tanty.modules.stock.application.dotation.usecase.modifier;

import com.NTFOODS.Api_tanty.modules.auth.domain.utilisateur.aggregate.Utilisateur;
import com.NTFOODS.Api_tanty.modules.stock.application.dotation.dto.request.DecisionLigneRequest;
import com.NTFOODS.Api_tanty.modules.stock.application.dotation.dto.response.DotationResponse;
import com.NTFOODS.Api_tanty.modules.stock.application.dotation.mapper.DotationMapper;
import com.NTFOODS.Api_tanty.modules.stock.domain.dotation.model.Dotation;
import com.NTFOODS.Api_tanty.modules.stock.domain.dotation.repository.DotationRepository;
import com.NTFOODS.Api_tanty.shared.kernel.exception.EntiteNonTrouveeException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Use Case — le gestionnaire modifie une ligne de dotation : ajustement de
 * quantité et/ou substitution de conditionnement (avec motif).
 */
@Service
@Transactional
public class ModifierDotationUseCase {

    private final DotationRepository dotationRepository;
    private final DotationMapper dotationMapper;

    public ModifierDotationUseCase(DotationRepository dotationRepository, DotationMapper dotationMapper) {
        this.dotationRepository = dotationRepository;
        this.dotationMapper = dotationMapper;
    }

    public DotationResponse executer(String numero, DecisionLigneRequest request, Utilisateur acteur) {
        Dotation dotation = dotationRepository.trouverParNumero(numero)
            .orElseThrow(() -> EntiteNonTrouveeException.de("Dotation", numero));

        if (request.nouvelleQuantite() != null) {
            dotation.modifierLigne(request.idLigne(), request.nouvelleQuantite(), request.motif(), acteur.getRole());
        }
        if (request.nouveauSku() != null && !request.nouveauSku().isBlank()) {
            dotation.substituerConditionnement(request.idLigne(), request.nouveauSku(), request.motif(), acteur.getRole());
        }
        return dotationMapper.versResponse(dotationRepository.sauvegarder(dotation));
    }
}
