package com.NTFOODS.Api_tanty.modules.sync.domain.repository;

import com.NTFOODS.Api_tanty.modules.sync.domain.aggregate.SynchronisationLog;

import java.util.Optional;

public interface SynchronisationLogRepository {
    SynchronisationLog sauvegarder(SynchronisationLog log);
    Optional<SynchronisationLog> trouverParIdempotencyKey(String key);
}
