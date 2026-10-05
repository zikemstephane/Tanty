package com.NTFOODS.Api_tanty.modules.sync.infrastructure.persistence.jpa;

import com.NTFOODS.Api_tanty.modules.sync.domain.enums.SyncEntityType;
import com.NTFOODS.Api_tanty.modules.sync.domain.enums.SyncResult;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface SynchronisationLogJpaRepository extends JpaRepository<SynchronisationLogJpaEntity, Long> {
    Optional<SynchronisationLogJpaEntity> findByIdempotencyKey(String key);

    List<SynchronisationLogJpaEntity> findAllByOrderByDateReceptionDesc();

    List<SynchronisationLogJpaEntity> findByMatriculeCommercialOrderByDateReceptionDesc(String matricule);

    List<SynchronisationLogJpaEntity> findByEntityTypeOrderByDateReceptionDesc(SyncEntityType entityType);

    List<SynchronisationLogJpaEntity> findByResultOrderByDateReceptionDesc(SyncResult result);

    List<SynchronisationLogJpaEntity> findByDateReceptionBetweenOrderByDateReceptionDesc(
        LocalDateTime debut, LocalDateTime fin);
}
