package com.NTFOODS.Api_tanty.modules.sync.application.dto;

import com.NTFOODS.Api_tanty.modules.sync.domain.enums.SyncEntityType;
import com.NTFOODS.Api_tanty.modules.sync.domain.enums.SyncOperationType;
import com.NTFOODS.Api_tanty.modules.sync.domain.enums.SyncResult;

import java.time.LocalDateTime;

public record SyncLogResponse(
    Long id,
    String idempotencyKey,
    String matriculeCommercial,
    SyncEntityType entityType,
    SyncOperationType operationType,
    String entityRef,
    String payloadHash,
    SyncResult result,
    String conflictDetail,
    String serverEntityRef,
    LocalDateTime dateReception,
    LocalDateTime dateTraitement
) {}
