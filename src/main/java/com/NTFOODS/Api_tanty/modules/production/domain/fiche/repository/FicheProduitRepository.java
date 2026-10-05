package com.NTFOODS.Api_tanty.modules.production.domain.fiche.repository;

import com.NTFOODS.Api_tanty.modules.production.domain.fiche.aggregate.FicheProduitJournaliere;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface FicheProduitRepository {
    FicheProduitJournaliere sauvegarder(FicheProduitJournaliere fiche);
    Optional<FicheProduitJournaliere> trouverParPPHEtDate(String referencePPH, LocalDate date);
    List<FicheProduitJournaliere> listerParPPH(String referencePPH);
    List<FicheProduitJournaliere> listerParPeriode(LocalDate debut, LocalDate fin);
    boolean existeParPPHEtDate(String referencePPH, LocalDate date);
}
