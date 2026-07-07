package com.NTFOODS.Api_tanty.modules.comptabilite.domain.objectif.repository;

import com.NTFOODS.Api_tanty.modules.comptabilite.domain.objectif.aggregate.ObjectifCommercial;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface ObjectifCommercialRepository {
    ObjectifCommercial sauvegarder(ObjectifCommercial objectif);
    Optional<ObjectifCommercial> trouver(String matriculeCommercial, LocalDate semaineDebut);
    List<ObjectifCommercial> listerParCommercial(String matriculeCommercial);
    List<ObjectifCommercial> listerParSemaine(LocalDate semaineDebut);
}
