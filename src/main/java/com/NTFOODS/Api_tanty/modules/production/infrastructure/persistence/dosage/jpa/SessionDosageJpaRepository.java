package com.NTFOODS.Api_tanty.modules.production.infrastructure.persistence.dosage.jpa;

import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface SessionDosageJpaRepository extends JpaRepository<SessionDosageJpaEntity, Long> {
    Optional<SessionDosageJpaEntity> findByReferencePPHAndDate(String referencePPH, LocalDate date);
    List<SessionDosageJpaEntity> findByReferencePPH(String referencePPH);
    List<SessionDosageJpaEntity> findByDate(LocalDate date);
    List<SessionDosageJpaEntity> findByDateBetween(LocalDate debut, LocalDate fin);
}
