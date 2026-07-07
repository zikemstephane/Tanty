package com.NTFOODS.Api_tanty.modules.commercial.infrastructure.persistence.carburant.jpa;

import com.NTFOODS.Api_tanty.modules.commercial.domain.carburant.enums.StatutCarburant;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface CarburantJpaRepository extends JpaRepository<CarburantJpaEntity, Long> {
    Optional<CarburantJpaEntity> findByReferenceCarburant(String reference);
    List<CarburantJpaEntity> findByMatriculeCommercial(String matricule);
    List<CarburantJpaEntity> findByDate(LocalDate date);
    List<CarburantJpaEntity> findByDateBetween(LocalDate debut, LocalDate fin);
    List<CarburantJpaEntity> findByStatut(StatutCarburant statut);
    long countByDate(LocalDate date);
}
