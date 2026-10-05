package com.NTFOODS.Api_tanty.modules.comptabilite.infrastructure.persistence.objectif.jpa;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface ParametrageObjectifJpaRepository extends JpaRepository<ParametrageObjectifJpaEntity, Long> {

    Optional<ParametrageObjectifJpaEntity> findByReference(String reference);

    Optional<ParametrageObjectifJpaEntity> findFirstByDateFinIsNullOrderByDateDebutDesc();

    List<ParametrageObjectifJpaEntity> findAllByOrderByDateDebutDesc();

    @Query("""
        select p from ParametrageObjectifJpaEntity p
        where p.dateDebut <= :date
          and (p.dateFin is null or p.dateFin >= :date)
        order by p.dateDebut desc
        """)
    List<ParametrageObjectifJpaEntity> findApplicables(@Param("date") LocalDate date);
}
