package com.NTFOODS.Api_tanty.modules.comptabilite.domain.decaissement.repository;

import com.NTFOODS.Api_tanty.modules.comptabilite.domain.decaissement.aggregate.Decaissement;
import com.NTFOODS.Api_tanty.modules.comptabilite.domain.decaissement.enums.StatutDecaissement;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface DecaissementRepository {
    Decaissement sauvegarder(Decaissement decaissement);
    Optional<Decaissement> trouverParNumero(String numero);
    List<Decaissement> listerParStatut(StatutDecaissement statut);
    List<Decaissement> listerParPeriode(LocalDate debut, LocalDate fin);
    List<Decaissement> listerTous();
    double totalDecaissementsParPeriode(LocalDate debut, LocalDate fin);
}
