package com.NTFOODS.Api_tanty.modules.production.infrastructure.persistence.affectation.jpa;

import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface AffectationJournaliereJpaRepository extends JpaRepository<AffectationJournaliereJpaEntity, Long> {

    Optional<AffectationJournaliereJpaEntity> findByReferencePPHAndDate(String referencePPH, LocalDate date);

    boolean existsByReferencePPHAndDate(String referencePPH, LocalDate date);

    List<AffectationJournaliereJpaEntity> findByReferencePPHOrderByDateDesc(String referencePPH);

    List<AffectationJournaliereJpaEntity> findByDateBetweenOrderByDateDesc(LocalDate debut, LocalDate fin);
}
