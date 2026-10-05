package com.NTFOODS.Api_tanty.modules.production.infrastructure.persistence.of.jpa;

import com.NTFOODS.Api_tanty.modules.production.domain.of.enums.StatutOF;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface OFJpaRepository extends JpaRepository<OFJpaEntity, Long> {
    Optional<OFJpaEntity> findByIdOF(String idOF);
    List<OFJpaEntity> findByReferencePPH(String referencePPH);
    List<OFJpaEntity> findByStatut(StatutOF statut);
}
