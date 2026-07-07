package com.NTFOODS.Api_tanty.modules.production.infrastructure.persistence.fiche.jpa;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Repository
public interface FicheProduitJpaRepository extends JpaRepository<FicheProduitJpaEntity, Long> {
    Optional<FicheProduitJpaEntity> findByReferencePPHAndDate(String referencePPH, LocalDate date);
    List<FicheProduitJpaEntity> findByReferencePPH(String referencePPH);
    List<FicheProduitJpaEntity> findByDateBetween(LocalDate debut, LocalDate fin);
    boolean existsByReferencePPHAndDate(String referencePPH, LocalDate date);
}
