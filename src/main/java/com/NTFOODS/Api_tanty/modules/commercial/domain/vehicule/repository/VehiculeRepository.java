package com.NTFOODS.Api_tanty.modules.commercial.domain.vehicule.repository;

import com.NTFOODS.Api_tanty.modules.commercial.domain.vehicule.aggregate.Vehicule;

import java.util.List;
import java.util.Optional;

public interface VehiculeRepository {
    Vehicule sauvegarder(Vehicule vehicule);
    Optional<Vehicule> trouverParImmatriculation(String immatriculation);
    List<Vehicule> listerTous();
    List<Vehicule> listerParCommercial(String matriculeCommercial);
    List<Vehicule> listerActifs();
}
