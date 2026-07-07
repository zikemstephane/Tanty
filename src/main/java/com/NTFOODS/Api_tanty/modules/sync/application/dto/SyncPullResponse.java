package com.NTFOODS.Api_tanty.modules.sync.application.dto;

import com.NTFOODS.Api_tanty.modules.sync.domain.enums.SyncEntityType;

import java.time.LocalDateTime;
import java.util.List;

public record SyncPullResponse(
    LocalDateTime serverTime,
    String matriculeCommercial,
    List<EntityBatch> batches
) {
    public record EntityBatch(
        SyncEntityType entityType,
        LocalDateTime lastSync,
        List<EntityPayload> entities
    ) {}

    public record EntityPayload(
        String ref,
        String payload,
        LocalDateTime lastModified
    ) {}
}
