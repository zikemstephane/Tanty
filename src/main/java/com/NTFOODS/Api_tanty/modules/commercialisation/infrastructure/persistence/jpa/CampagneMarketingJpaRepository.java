package com.NTFOODS.Api_tanty.modules.commercialisation.infrastructure.persistence.jpa;

import com.NTFOODS.Api_tanty.modules.commercialisation.domain.enums.StatutCampagne;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface CampagneMarketingJpaRepository extends JpaRepository<CampagneMarketingJpaEntity, Long> {
    Optional<CampagneMarketingJpaEntity> findByCode(String code);
    List<CampagneMarketingJpaEntity> findAllByOrderByDateCreationDesc();
    List<CampagneMarketingJpaEntity> findByStatut(StatutCampagne statut);
    List<CampagneMarketingJpaEntity> findByDateDebutBetween(LocalDate debut, LocalDate fin);
}
