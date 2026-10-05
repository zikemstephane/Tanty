package com.NTFOODS.Api_tanty.modules.commercial.infrastructure.persistence.feuillroute.jpa;

import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface FeuillRouteJpaRepository extends JpaRepository<FeuillRouteJpaEntity, Long> {
    Optional<FeuillRouteJpaEntity> findByNumeroFeuillRoute(String numero);
    List<FeuillRouteJpaEntity> findByMatriculeCommercial(String matricule);
    List<FeuillRouteJpaEntity> findByMatriculeCommercialAndDate(String matricule, LocalDate date);
    List<FeuillRouteJpaEntity> findByDateBetween(LocalDate debut, LocalDate fin);
}
