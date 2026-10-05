package com.NTFOODS.Api_tanty.modules.comptabilite.domain.objectif.repository;

import com.NTFOODS.Api_tanty.modules.comptabilite.domain.objectif.aggregate.ParametrageObjectif;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface ParametrageObjectifRepository {

    ParametrageObjectif sauvegarder(ParametrageObjectif parametrage);

    Optional<ParametrageObjectif> trouverParReference(String reference);

    Optional<ParametrageObjectif> trouverApplicableA(LocalDate date);

    Optional<ParametrageObjectif> trouverCourant();

    List<ParametrageObjectif> listerHistorique();
}
