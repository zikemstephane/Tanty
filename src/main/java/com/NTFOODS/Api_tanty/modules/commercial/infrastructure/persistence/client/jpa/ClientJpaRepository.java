package com.NTFOODS.Api_tanty.modules.commercial.infrastructure.persistence.client.jpa;

import com.NTFOODS.Api_tanty.modules.commercial.domain.client.enums.TypeClient;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface ClientJpaRepository extends JpaRepository<ClientJpaEntity, Long> {
    Optional<ClientJpaEntity> findByCodeClient(String codeClient);
    List<ClientJpaEntity> findByMatriculeCommercialReferent(String matricule);
    List<ClientJpaEntity> findByType(TypeClient type);
    List<ClientJpaEntity> findByActifTrue();
    boolean existsByCodeClient(String codeClient);
    long countByMatriculeCommercialReferent(String matricule);
    List<ClientJpaEntity> findByNomContainingIgnoreCase(String nomPartiel);
    List<ClientJpaEntity> findByLocaliteIgnoreCaseAndActifTrue(String localite);
}
