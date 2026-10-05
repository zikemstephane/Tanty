package com.NTFOODS.Api_tanty.modules.comptabilite.domain.versement.repository;

import com.NTFOODS.Api_tanty.modules.comptabilite.domain.versement.aggregate.Versement;
import com.NTFOODS.Api_tanty.modules.comptabilite.domain.versement.enums.StatutVersement;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface VersementRepository {
    Versement sauvegarder(Versement versement);
    Optional<Versement> trouverParReference(String referenceVersement);
    List<Versement> listerParCommercial(String matriculeCommercial);
    List<Versement> listerParDate(LocalDate date);
    List<Versement> listerParPeriode(LocalDate debut, LocalDate fin);
    List<Versement> listerParStatut(StatutVersement statut);
    List<Versement> listerAlertesRouges();
    String genererReference(LocalDate date);
}
