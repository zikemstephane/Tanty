package com.NTFOODS.Api_tanty.modules.sync.domain.aggregate;

import com.NTFOODS.Api_tanty.modules.sync.domain.enums.SyncEntityType;
import com.NTFOODS.Api_tanty.modules.sync.domain.enums.SyncOperationType;
import com.NTFOODS.Api_tanty.modules.sync.domain.enums.SyncResult;

import java.time.LocalDateTime;
import java.util.Objects;
import java.util.UUID;

/**
 * Agrégat Racine — Journal de Synchronisation (§12).
 *
 * Trace chaque opération de synchronisation (push/pull) effectuée par
 * un client mobile. Permet l'idempotence : un même idempotencyKey ne peut
 * être traité qu'une seule fois.
 */
public class SynchronisationLog {

    private Long id;
    private final String idempotencyKey;
    private final String matriculeCommercial;
    private final SyncEntityType entityType;
    private final SyncOperationType operationType;
    private final String entityRef;
    private final String payloadHash;
    private SyncResult result;
    private String conflictDetail;
    private String serverEntityRef;
    private final LocalDateTime dateReception;
    private LocalDateTime dateTraitement;

    private SynchronisationLog(String idempotencyKey, String matriculeCommercial, SyncEntityType entityType,
                                SyncOperationType operationType, String entityRef, String payloadHash,
                                LocalDateTime dateReception) {
        this.idempotencyKey = Objects.requireNonNull(idempotencyKey);
        this.matriculeCommercial = Objects.requireNonNull(matriculeCommercial);
        this.entityType = Objects.requireNonNull(entityType);
        this.operationType = Objects.requireNonNull(operationType);
        this.entityRef = entityRef;
        this.payloadHash = payloadHash;
        this.dateReception = dateReception;
    }

    public static SynchronisationLog recevoirPush(String idempotencyKey, String matriculeCommercial,
                                                   SyncEntityType entityType, String entityRef, String payloadHash) {
        return new SynchronisationLog(idempotencyKey, matriculeCommercial, entityType,
            SyncOperationType.PUSH, entityRef, payloadHash, LocalDateTime.now());
    }

    public static SynchronisationLog reconstituer(Long id, String idempotencyKey, String matriculeCommercial,
                                                    SyncEntityType entityType, SyncOperationType operationType,
                                                    String entityRef, String payloadHash, SyncResult result,
                                                    String conflictDetail, String serverEntityRef,
                                                    LocalDateTime dateReception, LocalDateTime dateTraitement) {
        SynchronisationLog log = new SynchronisationLog(idempotencyKey, matriculeCommercial, entityType,
            operationType, entityRef, payloadHash, dateReception);
        log.id = id;
        log.result = result;
        log.conflictDetail = conflictDetail;
        log.serverEntityRef = serverEntityRef;
        log.dateTraitement = dateTraitement;
        return log;
    }

    public void marquerSucces(String serverEntityRef) {
        this.result = SyncResult.SUCCESS;
        this.serverEntityRef = serverEntityRef;
        this.dateTraitement = LocalDateTime.now();
    }

    public void marquerConflit(String conflictDetail) {
        this.result = SyncResult.CONFLICT;
        this.conflictDetail = conflictDetail;
        this.dateTraitement = LocalDateTime.now();
    }

    public void marquerRejete(String reason) {
        this.result = SyncResult.REJECTED;
        this.conflictDetail = reason;
        this.dateTraitement = LocalDateTime.now();
    }

    public static String genererIdempotencyKey() {
        return UUID.randomUUID().toString();
    }

    public Long getId() { return id; }
    public String getIdempotencyKey() { return idempotencyKey; }
    public String getMatriculeCommercial() { return matriculeCommercial; }
    public SyncEntityType getEntityType() { return entityType; }
    public SyncOperationType getOperationType() { return operationType; }
    public String getEntityRef() { return entityRef; }
    public String getPayloadHash() { return payloadHash; }
    public SyncResult getResult() { return result; }
    public String getConflictDetail() { return conflictDetail; }
    public String getServerEntityRef() { return serverEntityRef; }
    public LocalDateTime getDateReception() { return dateReception; }
    public LocalDateTime getDateTraitement() { return dateTraitement; }

    @Override public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof SynchronisationLog)) return false;
        return Objects.equals(idempotencyKey, ((SynchronisationLog) o).idempotencyKey);
    }
    @Override public int hashCode() { return Objects.hash(idempotencyKey); }
}
