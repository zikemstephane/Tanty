package com.NTFOODS.Api_tanty.modules.comptabilite.infrastructure.persistence.caisse.jpa;

import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface JournalCaisseJpaRepository extends JpaRepository<JournalCaisseJpaEntity, Long> {
    Optional<JournalCaisseJpaEntity> findByDate(LocalDate date);
    List<JournalCaisseJpaEntity> findByDateBetween(LocalDate debut, LocalDate fin);
    boolean existsByDate(LocalDate date);
}
