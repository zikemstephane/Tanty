package com.NTFOODS.Api_tanty.modules.dg.infrastructure.persistence.jpa;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.Set;

@Repository
public interface DgValidationResolutionRepository extends JpaRepository<DgValidationResolutionEntity, Long> {

    Optional<DgValidationResolutionEntity> findByReference(String reference);

    Set<DgValidationResolutionEntity> findByReferenceIn(Set<String> references);
}
