package com.NTFOODS.Api_tanty.modules.commercial.infrastructure.persistence.vente.jpa;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface ReglesAvarieJpaRepository extends JpaRepository<ReglesAvarieJpaEntity, Long> {

    Optional<ReglesAvarieJpaEntity> findByReference(String reference);

    Optional<ReglesAvarieJpaEntity> findFirstByDateFinIsNullOrderByDateDebutDesc();

    List<ReglesAvarieJpaEntity> findAllByOrderByDateDebutDesc();

    @Query("""
        select r from ReglesAvarieJpaEntity r
        where r.dateDebut <= :date
          and (r.dateFin is null or r.dateFin >= :date)
        order by r.dateDebut desc
        """)
    List<ReglesAvarieJpaEntity> findApplicables(@Param("date") LocalDate date);
}
