package com.NTFOODS.Api_tanty.modules.commercialisation.domain.repository;

import com.NTFOODS.Api_tanty.modules.commercialisation.domain.aggregate.CampagneMarketing;
import com.NTFOODS.Api_tanty.modules.commercialisation.domain.enums.StatutCampagne;

import java.util.List;
import java.util.Optional;

public interface CampagneMarketingRepository {
    CampagneMarketing sauvegarder(CampagneMarketing campagne);
    Optional<CampagneMarketing> trouverParCode(String code);
    List<CampagneMarketing> listerTous();
    List<CampagneMarketing> listerParStatut(StatutCampagne statut);
    List<CampagneMarketing> listerParPeriode(java.time.LocalDate debut, java.time.LocalDate fin);
}
