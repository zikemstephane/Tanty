package com.NTFOODS.Api_tanty.modules.comptabilite.infrastructure.persistence.versement.jpa;

import com.NTFOODS.Api_tanty.modules.comptabilite.domain.versement.enums.StatutVersement;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface VersementJpaRepository extends JpaRepository<VersementJpaEntity, Long> {
    Optional<VersementJpaEntity> findByReferenceVersement(String referenceVersement);
    List<VersementJpaEntity> findByMatriculeCommercial(String matriculeCommercial);
    List<VersementJpaEntity> findByDate(LocalDate date);
    List<VersementJpaEntity> findByDateBetween(LocalDate debut, LocalDate fin);
    List<VersementJpaEntity> findByStatut(StatutVersement statut);
    List<VersementJpaEntity> findByAlerteRougeTrue();
    long countByDateAndMatriculeCommercial(LocalDate date, String matriculeCommercial);
}
