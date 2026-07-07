package com.NTFOODS.Api_tanty.modules.commercial.infrastructure.persistence.vehicule.jpa;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface VehiculeJpaRepository extends JpaRepository<VehiculeJpaEntity, Long> {
    Optional<VehiculeJpaEntity> findByImmatriculation(String immatriculation);
    List<VehiculeJpaEntity> findByMatriculeCommercial(String matriculeCommercial);
    List<VehiculeJpaEntity> findByActifTrue();
}
