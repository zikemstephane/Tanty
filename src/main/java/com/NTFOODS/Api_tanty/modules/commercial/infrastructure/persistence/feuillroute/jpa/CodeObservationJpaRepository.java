package com.NTFOODS.Api_tanty.modules.commercial.infrastructure.persistence.feuillroute.jpa;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CodeObservationJpaRepository extends JpaRepository<CodeObservationJpaEntity, Long> {

    Optional<CodeObservationJpaEntity> findByCode(String code);

    List<CodeObservationJpaEntity> findByActifTrueOrderByOrdreAsc();

    List<CodeObservationJpaEntity> findAllByOrderByOrdreAsc();
}
