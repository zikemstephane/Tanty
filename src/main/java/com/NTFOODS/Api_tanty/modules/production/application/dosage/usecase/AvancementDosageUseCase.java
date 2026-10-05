package com.NTFOODS.Api_tanty.modules.production.application.dosage.usecase;

import com.NTFOODS.Api_tanty.modules.production.application.dosage.dto.response.AvancementDosageResponse;
import com.NTFOODS.Api_tanty.modules.production.domain.dosage.repository.SessionDosageRepository;
import com.NTFOODS.Api_tanty.modules.production.domain.dosage.service.AvancementDosage;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/** Avancement du dosage d'un plan PPH (lecture seule). */
@Service
@Transactional(readOnly = true)
public class AvancementDosageUseCase {

    private final SessionDosageRepository repository;

    public AvancementDosageUseCase(SessionDosageRepository repository) {
        this.repository = repository;
    }

    public AvancementDosageResponse executer(String referencePPH) {
        return AvancementDosageResponse.de(
            AvancementDosage.calculer(referencePPH, repository.listerParPPH(referencePPH)));
    }
}
