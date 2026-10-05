package com.NTFOODS.Api_tanty.modules.production.application.fiche.usecase.recalculer;

import com.NTFOODS.Api_tanty.modules.production.application.fiche.dto.response.FicheResponse;
import com.NTFOODS.Api_tanty.modules.production.application.fiche.mapper.FicheMapper;
import com.NTFOODS.Api_tanty.modules.production.domain.fiche.aggregate.FicheProduitJournaliere;
import com.NTFOODS.Api_tanty.modules.production.domain.fiche.repository.FicheProduitRepository;
import com.NTFOODS.Api_tanty.modules.production.domain.fiche.service.CalculateurPredictionsFiche;
import com.NTFOODS.Api_tanty.modules.production.domain.pph.aggregate.PlanProductionHebdomadaire;
import com.NTFOODS.Api_tanty.modules.production.domain.pph.repository.PPHRepository;
import com.NTFOODS.Api_tanty.shared.kernel.exception.EntiteNonTrouveeException;
import com.NTFOODS.Api_tanty.shared.kernel.exception.RegleMetierException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;

/**
 * Recalcule les prédictions d'une fiche à partir des données présentes : fûts dosés pour la bouillie,
 * réalisations des postes pour les produits grignoter. Une fiche créée avant les réalisations n'a pas de
 * prédiction postes : ce recalcul permet de la mettre à jour sans toucher aux quantités produites.
 */
@Service
@Transactional
public class RecalculerPredictionsFicheUseCase {

    private final FicheProduitRepository ficheRepository;
    private final PPHRepository pphRepository;
    private final CalculateurPredictionsFiche calculateur;
    private final FicheMapper ficheMapper;

    public RecalculerPredictionsFicheUseCase(FicheProduitRepository ficheRepository, PPHRepository pphRepository,
                                             CalculateurPredictionsFiche calculateur, FicheMapper ficheMapper) {
        this.ficheRepository = ficheRepository;
        this.pphRepository = pphRepository;
        this.calculateur = calculateur;
        this.ficheMapper = ficheMapper;
    }

    public FicheResponse executer(String referencePPH, LocalDate date) {
        FicheProduitJournaliere fiche = ficheRepository.trouverParPPHEtDate(referencePPH, date)
            .orElseThrow(() -> EntiteNonTrouveeException.de("Fiche de production", referencePPH + " / " + date));
        if (fiche.isVerrouillee()) {
            throw new RegleMetierException(
                "Fiche verrouillée : les prédictions ne peuvent plus être recalculées.");
        }
        PlanProductionHebdomadaire pph = pphRepository.trouverParReference(referencePPH)
            .orElseThrow(() -> EntiteNonTrouveeException.de("PPH", referencePPH));

        var predictions = calculateur.calculer(fiche, pph);
        fiche.definirPredictions(predictions.predictionDosage(), predictions.predictionPostes());
        return ficheMapper.versResponse(ficheRepository.sauvegarder(fiche));
    }
}
