package com.NTFOODS.Api_tanty.modules.sync.application.usecase;

import com.NTFOODS.Api_tanty.modules.sync.application.dto.SyncLogResponse;
import com.NTFOODS.Api_tanty.modules.sync.domain.enums.SyncEntityType;
import com.NTFOODS.Api_tanty.modules.sync.domain.enums.SyncResult;
import com.NTFOODS.Api_tanty.modules.sync.infrastructure.persistence.jpa.SynchronisationLogJpaEntity;
import com.NTFOODS.Api_tanty.modules.sync.infrastructure.persistence.jpa.SynchronisationLogJpaRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
@Transactional(readOnly = true)
public class ConsulterHistoriqueSyncUseCase {

    private final SynchronisationLogJpaRepository jpaRepository;

    public ConsulterHistoriqueSyncUseCase(SynchronisationLogJpaRepository jpaRepository) {
        this.jpaRepository = jpaRepository;
    }

    public List<SyncLogResponse> executer(String matricule,
                                           SyncEntityType entityType,
                                           SyncResult result,
                                           LocalDateTime debut,
                                           LocalDateTime fin,
                                           int limite) {
        int max = Math.min(limite, 500);
        List<SynchronisationLogJpaEntity> entries;

        if (matricule != null && !matricule.isBlank()) {
            entries = jpaRepository.findByMatriculeCommercialOrderByDateReceptionDesc(matricule);
        } else if (debut != null && fin != null) {
            entries = jpaRepository.findByDateReceptionBetweenOrderByDateReceptionDesc(debut, fin);
        } else if (entityType != null) {
            entries = jpaRepository.findByEntityTypeOrderByDateReceptionDesc(entityType);
        } else if (result != null) {
            entries = jpaRepository.findByResultOrderByDateReceptionDesc(result);
        } else {
            entries = jpaRepository.findAllByOrderByDateReceptionDesc();
        }

        return entries.stream()
            .limit(max)
            .map(this::versResponse)
            .toList();
    }

    private SyncLogResponse versResponse(SynchronisationLogJpaEntity e) {
        return new SyncLogResponse(
            e.getId(), e.getIdempotencyKey(), e.getMatriculeCommercial(),
            e.getEntityType(), e.getOperationType(), e.getEntityRef(),
            e.getPayloadHash(), e.getResult(), e.getConflictDetail(),
            e.getServerEntityRef(), e.getDateReception(), e.getDateTraitement()
        );
    }
}
