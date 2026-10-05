package com.NTFOODS.Api_tanty.modules.comptabilite.infrastructure.persistence.caisse.jpa;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface ParametrageCaisseJpaRepository extends JpaRepository<ParametrageCaisseJpaEntity, Long> {

    Optional<ParametrageCaisseJpaEntity> findByReference(String reference);

    Optional<ParametrageCaisseJpaEntity> findFirstByDateFinIsNullOrderByDateDebutDesc();

    List<ParametrageCaisseJpaEntity> findAllByOrderByDateDebutDesc();

    @Query("""
        select p from ParametrageCaisseJpaEntity p
        where p.dateDebut <= :date
          and (p.dateFin is null or p.dateFin >= :date)
        order by p.dateDebut desc
        """)
    List<ParametrageCaisseJpaEntity> findApplicables(@Param("date") LocalDate date);
}
