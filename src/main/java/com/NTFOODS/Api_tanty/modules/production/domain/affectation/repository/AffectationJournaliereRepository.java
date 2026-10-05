package com.NTFOODS.Api_tanty.modules.production.domain.affectation.repository;

import com.NTFOODS.Api_tanty.modules.production.domain.affectation.aggregate.AffectationJournaliere;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface AffectationJournaliereRepository {

    AffectationJournaliere sauvegarder(AffectationJournaliere affectation);

    Optional<AffectationJournaliere> trouverParId(Long id);

    Optional<AffectationJournaliere> trouverParPPHEtDate(String referencePPH, LocalDate date);

    List<AffectationJournaliere> listerParPPH(String referencePPH);

    List<AffectationJournaliere> listerParPeriode(LocalDate debut, LocalDate fin);

    boolean existeParPPHEtDate(String referencePPH, LocalDate date);
}
