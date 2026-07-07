package com.NTFOODS.Api_tanty.modules.commercial.domain.feuillroute.repository;

import com.NTFOODS.Api_tanty.modules.commercial.domain.feuillroute.aggregate.FeuillRoute;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface FeuillRouteRepository {
    FeuillRoute sauvegarder(FeuillRoute feuillRoute);
    Optional<FeuillRoute> trouverParNumero(String numeroFeuillRoute);
    List<FeuillRoute> listerParCommercial(String matriculeCommercial);
    List<FeuillRoute> listerParCommercialEtDate(String matriculeCommercial, LocalDate date);
    List<FeuillRoute> listerParPeriode(LocalDate debut, LocalDate fin);
}
