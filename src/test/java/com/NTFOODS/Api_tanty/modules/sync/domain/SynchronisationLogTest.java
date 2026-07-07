package com.NTFOODS.Api_tanty.modules.sync.domain;

import com.NTFOODS.Api_tanty.modules.sync.domain.aggregate.SynchronisationLog;
import com.NTFOODS.Api_tanty.modules.sync.domain.enums.SyncEntityType;
import com.NTFOODS.Api_tanty.modules.sync.domain.enums.SyncResult;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class SynchronisationLogTest {

    @Test
    void recevoirPushCreeLogEnAttente() {
        SynchronisationLog log = SynchronisationLog.recevoirPush("key-001", "MAT-COM-001",
            SyncEntityType.VENTE, "VTE-001", "hash123");

        assertNull(log.getResult());
        assertNotNull(log.getDateReception());
    }

    @Test
    void marquerSucces() {
        SynchronisationLog log = SynchronisationLog.recevoirPush("key-002", "MAT-COM-001",
            SyncEntityType.VENTE, "VTE-002", "hash456");
        log.marquerSucces("VTE-002");

        assertEquals(SyncResult.SUCCESS, log.getResult());
        assertEquals("VTE-002", log.getServerEntityRef());
        assertNotNull(log.getDateTraitement());
    }

    @Test
    void marquerConflit() {
        SynchronisationLog log = SynchronisationLog.recevoirPush("key-003", "MAT-COM-001",
            SyncEntityType.VENTE, "VTE-003", "hash789");
        log.marquerConflit("Version en conflit");

        assertEquals(SyncResult.CONFLICT, log.getResult());
        assertEquals("Version en conflit", log.getConflictDetail());
    }

    @Test
    void marquerRejete() {
        SynchronisationLog log = SynchronisationLog.recevoirPush("key-004", "MAT-COM-001",
            SyncEntityType.VENTE, "VTE-004", "hash000");
        log.marquerRejete("Règle métier violée");

        assertEquals(SyncResult.REJECTED, log.getResult());
    }

    @Test
    void idempotencyKeyUnique() {
        String key = SynchronisationLog.genererIdempotencyKey();
        assertNotNull(key);
        assertNotEquals(key, SynchronisationLog.genererIdempotencyKey());
    }
}
