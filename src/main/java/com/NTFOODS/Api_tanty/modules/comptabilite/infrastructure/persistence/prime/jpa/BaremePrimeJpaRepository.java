package com.NTFOODS.Api_tanty.modules.comptabilite.infrastructure.persistence.prime.jpa;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface BaremePrimeJpaRepository extends JpaRepository<BaremePrimeJpaEntity, Long> {

    Optional<BaremePrimeJpaEntity> findByReference(String reference);

    Optional<BaremePrimeJpaEntity> findFirstByDateFinIsNullOrderByDateDebutDesc();

    List<BaremePrimeJpaEntity> findAllByOrderByDateDebutDesc();

    /**
     * Barème encadrant la date : celui dont la prise d'effet la précède et dont la
     * clôture, si elle existe, ne la précède pas.
     */
    @Query("""
        select b from BaremePrimeJpaEntity b
        where b.dateDebut <= :date
          and (b.dateFin is null or b.dateFin >= :date)
        order by b.dateDebut desc
        """)
    List<BaremePrimeJpaEntity> findApplicables(@Param("date") LocalDate date);
}
