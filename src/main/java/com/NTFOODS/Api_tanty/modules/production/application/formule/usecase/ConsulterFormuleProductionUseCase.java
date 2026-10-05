package com.NTFOODS.Api_tanty.modules.production.application.formule.usecase;

import com.NTFOODS.Api_tanty.modules.production.domain.formule.aggregate.FormuleProduction;
import com.NTFOODS.Api_tanty.modules.production.domain.formule.repository.FormuleProductionRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

/**
 * Use Case — Consulter la formule de production d'un produit.
 *
 * Renvoie {@code Optional.empty()}, jamais une exception, lorsque le produit
 * n'a pas de formule : c'est un état valide (cf. FormuleProduction), à
 * charge de l'appelant de le signaler explicitement s'il en a besoin.
 */
@Service
@Transactional(readOnly = true)
public class ConsulterFormuleProductionUseCase {

    private final FormuleProductionRepository formuleProductionRepository;

    public ConsulterFormuleProductionUseCase(FormuleProductionRepository formuleProductionRepository) {
        this.formuleProductionRepository = formuleProductionRepository;
    }

    public Optional<FormuleProduction> executer(String codeProduit) {
        return formuleProductionRepository.trouverParCodeProduit(codeProduit);
    }
}
