package com.NTFOODS.Api_tanty.modules.production.domain.pph.repository;

import com.NTFOODS.Api_tanty.modules.production.domain.pph.aggregate.PlanProductionHebdomadaire;
import com.NTFOODS.Api_tanty.modules.production.domain.pph.enums.StatutPPH;

import java.util.List;
import java.util.Optional;

public interface PPHRepository {
    PlanProductionHebdomadaire sauvegarder(PlanProductionHebdomadaire pph);
    Optional<PlanProductionHebdomadaire> trouverParReference(String referencePPH);
    List<PlanProductionHebdomadaire> listerParStatut(StatutPPH statut);
    List<PlanProductionHebdomadaire> listerParBC(String referenceBC);
    Optional<PlanProductionHebdomadaire> trouverEnCours();
    List<PlanProductionHebdomadaire> listerTous();
}
