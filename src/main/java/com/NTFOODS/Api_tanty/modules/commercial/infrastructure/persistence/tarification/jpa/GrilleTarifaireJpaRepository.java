package com.NTFOODS.Api_tanty.modules.commercial.infrastructure.persistence.tarification.jpa;

import com.NTFOODS.Api_tanty.modules.commercial.domain.client.enums.TypeClient;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface GrilleTarifaireJpaRepository extends JpaRepository<GrilleTarifaireJpaEntity, Long> {
    Optional<GrilleTarifaireJpaEntity> findByCodeProduitAndSegment(String codeProduit, TypeClient segment);
    List<GrilleTarifaireJpaEntity> findByCodeProduit(String codeProduit);
    List<GrilleTarifaireJpaEntity> findBySegment(TypeClient segment);
}
