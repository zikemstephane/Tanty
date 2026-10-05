package com.NTFOODS.Api_tanty.modules.commercialisation.infrastructure.persistence.jpa;

import com.NTFOODS.Api_tanty.modules.commercialisation.domain.enums.StatutPromotion;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface PromotionJpaRepository extends JpaRepository<PromotionJpaEntity, Long> {
    Optional<PromotionJpaEntity> findByCode(String code);
    List<PromotionJpaEntity> findAllByOrderByDateCreationDesc();
    List<PromotionJpaEntity> findByStatut(StatutPromotion statut);
    List<PromotionJpaEntity> findByCodeProduit(String codeProduit);
}
