package com.NTFOODS.Api_tanty.modules.comptabilite.infrastructure.persistence.objectif.jpa;

import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface ObjectifCommercialJpaRepository extends JpaRepository<ObjectifCommercialJpaEntity, Long> {
    Optional<ObjectifCommercialJpaEntity> findByMatriculeCommercialAndSemaineDebut(String matriculeCommercial, LocalDate semaineDebut);
    List<ObjectifCommercialJpaEntity> findByMatriculeCommercial(String matriculeCommercial);
    List<ObjectifCommercialJpaEntity> findBySemaineDebut(LocalDate semaineDebut);
}
