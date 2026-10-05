package com.NTFOODS.Api_tanty.modules.comptabilite.domain.caisse.repository;

import com.NTFOODS.Api_tanty.modules.comptabilite.domain.caisse.aggregate.ParametrageCaisse;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface ParametrageCaisseRepository {

    ParametrageCaisse sauvegarder(ParametrageCaisse parametrage);

    Optional<ParametrageCaisse> trouverParReference(String reference);

    Optional<ParametrageCaisse> trouverApplicableA(LocalDate date);

    Optional<ParametrageCaisse> trouverCourant();

    List<ParametrageCaisse> listerHistorique();
}
