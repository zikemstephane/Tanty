package com.NTFOODS.Api_tanty.modules.commercial.domain.vente.repository;

import com.NTFOODS.Api_tanty.modules.commercial.domain.vente.aggregate.ReglesAvarie;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface ReglesAvarieRepository {

    ReglesAvarie sauvegarder(ReglesAvarie regles);

    Optional<ReglesAvarie> trouverParReference(String reference);

    Optional<ReglesAvarie> trouverApplicableA(LocalDate date);

    Optional<ReglesAvarie> trouverCourant();

    List<ReglesAvarie> listerHistorique();
}
