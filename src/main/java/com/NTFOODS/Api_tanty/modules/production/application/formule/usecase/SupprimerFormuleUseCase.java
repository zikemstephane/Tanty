package com.NTFOODS.Api_tanty.modules.production.application.formule.usecase;

import com.NTFOODS.Api_tanty.modules.production.domain.formule.repository.FormuleProductionRepository;
import com.NTFOODS.Api_tanty.shared.kernel.exception.EntiteNonTrouveeException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/** Use Case — Retirer la formule d'un produit (état « sans formule », valide et signalé dans les plans). */
@Service
@Transactional
public class SupprimerFormuleUseCase {

    private final FormuleProductionRepository formuleRepository;

    public SupprimerFormuleUseCase(FormuleProductionRepository formuleRepository) {
        this.formuleRepository = formuleRepository;
    }

    public void executer(String codeProduit) {
        String code = codeProduit.trim().toUpperCase();
        formuleRepository.trouverParCodeProduit(code)
            .orElseThrow(() -> EntiteNonTrouveeException.de("Formule de production", code));
        formuleRepository.supprimerParCodeProduit(code);
    }
}
