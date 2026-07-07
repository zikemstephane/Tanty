package com.NTFOODS.Api_tanty.modules.comptabilite.domain.caisse.repository;

import com.NTFOODS.Api_tanty.modules.comptabilite.domain.caisse.aggregate.JournalCaisse;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface JournalCaisseRepository {
    JournalCaisse sauvegarder(JournalCaisse journal);
    Optional<JournalCaisse> trouverParDate(LocalDate date);
    List<JournalCaisse> listerParPeriode(LocalDate debut, LocalDate fin);
    boolean existeParDate(LocalDate date);
}
