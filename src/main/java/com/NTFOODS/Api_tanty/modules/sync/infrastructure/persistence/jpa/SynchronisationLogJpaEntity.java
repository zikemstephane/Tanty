package com.NTFOODS.Api_tanty.modules.sync.infrastructure.persistence.jpa;

import com.NTFOODS.Api_tanty.modules.sync.domain.enums.SyncEntityType;
import com.NTFOODS.Api_tanty.modules.sync.domain.enums.SyncOperationType;
import com.NTFOODS.Api_tanty.modules.sync.domain.enums.SyncResult;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Entity
@Table(name = "sync_logs")
@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class SynchronisationLogJpaEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String idempotencyKey;

    @Column(nullable = false)
    private String matriculeCommercial;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private SyncEntityType entityType;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private SyncOperationType operationType;

    private String entityRef;
    private String payloadHash;

    @Enumerated(EnumType.STRING)
    private SyncResult result;

    @Column(columnDefinition = "TEXT")
    private String conflictDetail;

    private String serverEntityRef;

    @Column(nullable = false, updatable = false)
    private LocalDateTime dateReception;

    private LocalDateTime dateTraitement;
}
