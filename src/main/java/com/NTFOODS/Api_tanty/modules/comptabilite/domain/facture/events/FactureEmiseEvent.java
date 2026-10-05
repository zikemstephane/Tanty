package com.NTFOODS.Api_tanty.modules.comptabilite.domain.facture.events;

import com.NTFOODS.Api_tanty.shared.kernel.event.DomainEvent;

import java.time.Instant;
import java.util.UUID;

public class FactureEmiseEvent implements DomainEvent {
    private final String eventId = UUID.randomUUID().toString();
    private final Instant occurredOn = Instant.now();
    private final String numeroFacture;
    private final String codeClient;
    private final double montantTTC;

    public FactureEmiseEvent(String numeroFacture, String codeClient, double montantTTC) {
        this.numeroFacture = numeroFacture;
        this.codeClient = codeClient;
        this.montantTTC = montantTTC;
    }

    @Override public String getEventId() { return eventId; }
    @Override public Instant getOccurredOn() { return occurredOn; }
    public String getNumeroFacture() { return numeroFacture; }
    public String getCodeClient() { return codeClient; }
    public double getMontantTTC() { return montantTTC; }
}
