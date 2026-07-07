package com.NTFOODS.Api_tanty.modules.production.domain.dosage.repository;

import com.NTFOODS.Api_tanty.modules.production.domain.dosage.aggregate.SessionDosage;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface SessionDosageRepository {
    SessionDosage sauvegarder(SessionDosage session);
    Optional<SessionDosage> trouverParId(Long id);
    Optional<SessionDosage> trouverParPPHEtDate(String referencePPH, LocalDate date);
    List<SessionDosage> listerParPPH(String referencePPH);
    List<SessionDosage> listerParDate(LocalDate date);
    List<SessionDosage> listerParPeriode(LocalDate debut, LocalDate fin);
    List<SessionDosage> listerTous();
}
