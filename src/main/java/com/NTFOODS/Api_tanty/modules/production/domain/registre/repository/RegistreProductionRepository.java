package com.NTFOODS.Api_tanty.modules.production.domain.registre.repository;

import com.NTFOODS.Api_tanty.modules.production.domain.registre.aggregate.RegistreProduction;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface RegistreProductionRepository {
    RegistreProduction sauvegarder(RegistreProduction registre);
    Optional<RegistreProduction> trouverParPPHEtDate(String referencePPH, LocalDate date);
    List<RegistreProduction> listerParPPH(String referencePPH);
    List<RegistreProduction> listerParPeriode(LocalDate debut, LocalDate fin);
    boolean existeParPPHEtDate(String referencePPH, LocalDate date);
}
