package com.NTFOODS.Api_tanty.modules.sync.application.dto;

import com.NTFOODS.Api_tanty.modules.sync.domain.enums.SyncEntityType;
import com.NTFOODS.Api_tanty.modules.sync.domain.enums.SyncResult;

import java.time.LocalDateTime;

public record SyncPushResponse(
    String idempotencyKey,
    String status,
    String serverRef,
    String conflictDetail,
    LocalDateTime dateTraitement
) {}
