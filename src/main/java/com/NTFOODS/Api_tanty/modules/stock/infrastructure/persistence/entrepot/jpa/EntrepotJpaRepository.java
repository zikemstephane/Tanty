package com.NTFOODS.Api_tanty.modules.stock.infrastructure.persistence.entrepot.jpa;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface EntrepotJpaRepository extends JpaRepository<EntrepotJpaEntity, Long> {
    Optional<EntrepotJpaEntity> findByCode(String code);
    Optional<EntrepotJpaEntity> findByMatriculeProprietaire(String matriculeProprietaire);
    boolean existsByCode(String code);
    List<EntrepotJpaEntity> findByType(String type);
}
