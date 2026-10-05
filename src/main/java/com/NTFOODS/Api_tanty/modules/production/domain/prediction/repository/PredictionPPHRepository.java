package com.NTFOODS.Api_tanty.modules.production.domain.prediction.repository;

import com.NTFOODS.Api_tanty.modules.production.domain.prediction.aggregate.PredictionPPH;

import java.util.List;
import java.util.Optional;

public interface PredictionPPHRepository {
    /** Remplace la prédiction existante du plan : une seule prédiction de référence par PPH. */
    PredictionPPH enregistrer(PredictionPPH prediction);
    Optional<PredictionPPH> trouverParReference(String referencePPH);
    List<PredictionPPH> listerToutes();
}
