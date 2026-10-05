package com.NTFOODS.Api_tanty.modules.dg.application.validation;

import com.NTFOODS.Api_tanty.modules.dg.infrastructure.persistence.jpa.DgValidationResolutionEntity;
import com.NTFOODS.Api_tanty.modules.dg.infrastructure.persistence.jpa.DgValidationResolutionRepository;
import com.NTFOODS.Api_tanty.shared.kernel.exception.RegleMetierException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

/**
 * Enregistre une décision DG sur une validation affichée dans la vue synthétique.
 * Cette décision masque ensuite la validation de la liste DG sans modifier le workflow opérationnel.
 */
@Service
@Transactional
public class ResoudreValidationDgUseCase {

    private final DgValidationResolutionRepository resolutionRepository;

    public ResoudreValidationDgUseCase(DgValidationResolutionRepository resolutionRepository) {
        this.resolutionRepository = resolutionRepository;
    }

    public void executer(String reference, String type, boolean approbation, String matriculeDg) {
        if (reference == null || reference.isBlank()) {
            throw new RegleMetierException("La référence de la validation est obligatoire");
        }
        if (type == null || type.isBlank()) {
            throw new RegleMetierException("Le type de validation est obligatoire");
        }
        if (matriculeDg == null || matriculeDg.isBlank()) {
            throw new RegleMetierException("Le matricule DG est obligatoire");
        }

        resolutionRepository.findByReference(reference).ifPresent(r -> {
            throw new RegleMetierException("La validation " + reference + " a déjà été résolue");
        });

        DgValidationResolutionEntity resolution = new DgValidationResolutionEntity(
                reference,
                type,
                approbation,
                matriculeDg,
                LocalDateTime.now()
        );
        resolutionRepository.save(resolution);
    }
}
