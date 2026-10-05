package com.NTFOODS.Api_tanty.modules.production.application.formule.usecase;

import com.NTFOODS.Api_tanty.modules.production.domain.formule.aggregate.FormuleProduction;
import com.NTFOODS.Api_tanty.modules.production.domain.formule.repository.FormuleProductionRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/** Use Case — Lister toutes les formules de production existantes. */
@Service
@Transactional(readOnly = true)
public class ListerFormulesProductionUseCase {

    private final FormuleProductionRepository formuleProductionRepository;

    public ListerFormulesProductionUseCase(FormuleProductionRepository formuleProductionRepository) {
        this.formuleProductionRepository = formuleProductionRepository;
    }

    public List<FormuleProduction> executer() {
        return formuleProductionRepository.listerToutes();
    }
}
