package com.NTFOODS.Api_tanty.modules.sync.infrastructure.persistence.repository;

import com.NTFOODS.Api_tanty.modules.sync.domain.aggregate.SynchronisationLog;
import com.NTFOODS.Api_tanty.modules.sync.domain.repository.SynchronisationLogRepository;
import com.NTFOODS.Api_tanty.modules.sync.infrastructure.persistence.jpa.SynchronisationLogJpaEntity;
import com.NTFOODS.Api_tanty.modules.sync.infrastructure.persistence.jpa.SynchronisationLogJpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public class SynchronisationLogRepositoryImpl implements SynchronisationLogRepository {

    private final SynchronisationLogJpaRepository jpaRepository;

    public SynchronisationLogRepositoryImpl(SynchronisationLogJpaRepository jpaRepository) {
        this.jpaRepository = jpaRepository;
    }

    @Override
    public SynchronisationLog sauvegarder(SynchronisationLog log) {
        return toDomain(jpaRepository.save(toEntity(log)));
    }

    @Override
    public Optional<SynchronisationLog> trouverParIdempotencyKey(String key) {
        return jpaRepository.findByIdempotencyKey(key).map(this::toDomain);
    }

    private SynchronisationLogJpaEntity toEntity(SynchronisationLog log) {
        return SynchronisationLogJpaEntity.builder()
            .id(log.getId()).idempotencyKey(log.getIdempotencyKey())
            .matriculeCommercial(log.getMatriculeCommercial())
            .entityType(log.getEntityType()).operationType(log.getOperationType())
            .entityRef(log.getEntityRef()).payloadHash(log.getPayloadHash())
            .result(log.getResult()).conflictDetail(log.getConflictDetail())
            .serverEntityRef(log.getServerEntityRef())
            .dateReception(log.getDateReception()).dateTraitement(log.getDateTraitement())
            .build();
    }

    private SynchronisationLog toDomain(SynchronisationLogJpaEntity e) {
        return SynchronisationLog.reconstituer(e.getId(), e.getIdempotencyKey(),
            e.getMatriculeCommercial(), e.getEntityType(), e.getOperationType(),
            e.getEntityRef(), e.getPayloadHash(), e.getResult(), e.getConflictDetail(),
            e.getServerEntityRef(), e.getDateReception(), e.getDateTraitement());
    }
}
