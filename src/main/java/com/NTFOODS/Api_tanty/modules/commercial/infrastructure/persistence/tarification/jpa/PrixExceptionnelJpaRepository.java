package com.NTFOODS.Api_tanty.modules.commercial.infrastructure.persistence.tarification.jpa;

import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface PrixExceptionnelJpaRepository extends JpaRepository<PrixExceptionnelJpaEntity, Long> {
    Optional<PrixExceptionnelJpaEntity> findByReference(String reference);
    List<PrixExceptionnelJpaEntity> findByCodeClient(String codeClient);
    List<PrixExceptionnelJpaEntity> findByMatriculeAutorisateur(String matriculeAutorisateur);
    List<PrixExceptionnelJpaEntity> findByDateAutorisationBetween(LocalDateTime debut, LocalDateTime fin);
    long countByDateCreationAfter(LocalDateTime depuis);
}
