package com.NTFOODS.Api_tanty.shared.application.audit.dto;

import com.NTFOODS.Api_tanty.shared.kernel.audit.TypeActionAudit;

import java.time.LocalDateTime;

public record JournalAuditResponse(
    Long id,
    String matricule,
    String role,
    TypeActionAudit typeAction,
    String module,
    String ressource,
    String referenceEntite,
    String methodeHttp,
    String endpoint,
    String details,
    boolean succes,
    String messageErreur,
    String adresseIp,
    long dureeMs,
    LocalDateTime horodatage
) {}
