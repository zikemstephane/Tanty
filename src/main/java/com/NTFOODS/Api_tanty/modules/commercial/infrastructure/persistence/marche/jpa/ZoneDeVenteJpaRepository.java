package com.NTFOODS.Api_tanty.modules.commercial.infrastructure.persistence.marche.jpa;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface ZoneDeVenteJpaRepository extends JpaRepository<ZoneDeVenteJpaEntity, Long> {
    Optional<ZoneDeVenteJpaEntity> findByNom(String nom);
    List<ZoneDeVenteJpaEntity> findByActifTrue();
}
