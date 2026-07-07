package com.NTFOODS.Api_tanty.modules.commercial.domain.carburant.repository;

import com.NTFOODS.Api_tanty.modules.commercial.domain.carburant.aggregate.DemandeCarburant;
import com.NTFOODS.Api_tanty.modules.commercial.domain.carburant.enums.StatutCarburant;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface CarburantRepository {
    DemandeCarburant sauvegarder(DemandeCarburant demande);
    Optional<DemandeCarburant> trouverParReference(String reference);
    List<DemandeCarburant> listerParStatut(StatutCarburant statut);
    List<DemandeCarburant> listerParCommercial(String matriculeCommercial);
    List<DemandeCarburant> listerParPeriode(LocalDate debut, LocalDate fin);
    String genererReference(LocalDate date);
}
