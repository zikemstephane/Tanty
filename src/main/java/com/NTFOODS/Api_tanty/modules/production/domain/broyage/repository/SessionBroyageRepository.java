package com.NTFOODS.Api_tanty.modules.production.domain.broyage.repository;

import com.NTFOODS.Api_tanty.modules.production.domain.broyage.aggregate.SessionBroyage;
import com.NTFOODS.Api_tanty.modules.production.domain.broyage.enums.TypePoudre;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface SessionBroyageRepository {

    SessionBroyage sauvegarder(SessionBroyage session);

    Optional<SessionBroyage> trouverParId(Long id);

    Optional<SessionBroyage> trouverParPPHDateEtType(String referencePPH, LocalDate date, TypePoudre typePoudre);

    List<SessionBroyage> listerParPPH(String referencePPH);

    List<SessionBroyage> listerParPeriode(LocalDate debut, LocalDate fin);

    List<SessionBroyage> listerParMachinisteEtPeriode(String machinisteMatricule, LocalDate debut, LocalDate fin);

    boolean existeParPPHDateEtType(String referencePPH, LocalDate date, TypePoudre typePoudre);
}
