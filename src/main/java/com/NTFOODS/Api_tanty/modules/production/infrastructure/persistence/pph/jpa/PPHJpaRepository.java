package com.NTFOODS.Api_tanty.modules.production.infrastructure.persistence.pph.jpa;

import com.NTFOODS.Api_tanty.modules.production.domain.pph.enums.StatutPPH;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface PPHJpaRepository extends JpaRepository<PPHJpaEntity, Long> {
    Optional<PPHJpaEntity> findByReferencePPH(String referencePPH);
    List<PPHJpaEntity> findByStatut(StatutPPH statut);
    List<PPHJpaEntity> findByReferenceBC(String referenceBC);
    Optional<PPHJpaEntity> findFirstByStatut(StatutPPH statut);
}
