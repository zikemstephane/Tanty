package com.NTFOODS.Api_tanty.modules.production.infrastructure.persistence.formule.jpa;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface FormuleProductionJpaRepository extends JpaRepository<FormuleProductionJpaEntity, Long> {
    Optional<FormuleProductionJpaEntity> findByCodeProduit(String codeProduit);
}
