package com.NTFOODS.Api_tanty.modules.comptabilite.domain.prime.repository;

import com.NTFOODS.Api_tanty.modules.comptabilite.domain.prime.aggregate.PrimeCommerciale;
import com.NTFOODS.Api_tanty.modules.comptabilite.domain.prime.enums.StatutPrime;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface PrimeRepository {
    PrimeCommerciale sauvegarder(PrimeCommerciale prime);
    Optional<PrimeCommerciale> trouverParReference(String referencePrime);
    List<PrimeCommerciale> listerParCommercial(String matriculeCommercial);
    List<PrimeCommerciale> listerParStatut(StatutPrime statut);
    List<PrimeCommerciale> listerParSemaine(LocalDate semaineDebut);
    String genererReference(String matriculeCommercial, LocalDate semaineDebut);
}
