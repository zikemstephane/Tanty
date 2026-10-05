package com.NTFOODS.Api_tanty.modules.commercial.infrastructure.persistence.vente.jpa;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface FactureAvoirJpaRepository extends JpaRepository<FactureAvoirJpaEntity, Long> {
    Optional<FactureAvoirJpaEntity> findByNumeroAvoir(String numeroAvoir);
    List<FactureAvoirJpaEntity> findByNumeroVenteOriginale(String numeroVenteOriginale);
    long countByDateAvoir(java.time.LocalDate date);
}
