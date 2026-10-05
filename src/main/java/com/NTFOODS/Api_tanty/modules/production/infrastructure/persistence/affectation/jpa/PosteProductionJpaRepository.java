package com.NTFOODS.Api_tanty.modules.production.infrastructure.persistence.affectation.jpa;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface PosteProductionJpaRepository extends JpaRepository<PosteProductionJpaEntity, Long> {

    Optional<PosteProductionJpaEntity> findByCode(String code);

    List<PosteProductionJpaEntity> findByActifTrueOrderByOrdreAsc();

    List<PosteProductionJpaEntity> findAllByOrderByOrdreAsc();
}
