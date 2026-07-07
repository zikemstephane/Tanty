package com.NTFOODS.Api_tanty.modules.stock.domain.alerte.event;

import com.NTFOODS.Api_tanty.modules.stock.domain.alerte.enums.TypeAlerte;
import com.NTFOODS.Api_tanty.shared.kernel.event.DomainEvent;

import java.time.Instant;
import java.util.UUID;

/** Événement — une alerte a été créée (poussée en temps réel via WebSocket). */
public class AlerteCreeeEvent implements DomainEvent {

    private final String eventId = UUID.randomUUID().toString();
    private final Instant occurredOn = Instant.now();
    private final String reference;
    private final TypeAlerte type;
    private final String message;
    private final String destinataireRole;
    private final String priorite;

    public AlerteCreeeEvent(String reference, TypeAlerte type, String message, String destinataireRole) {
        this.reference = reference;
        this.type = type;
        this.message = message;
        this.destinataireRole = destinataireRole;
        this.priorite = type.getPriorite().name();
    }

    @Override public String getEventId() { return eventId; }
    @Override public Instant getOccurredOn() { return occurredOn; }
    public String getReference() { return reference; }
    public TypeAlerte getType() { return type; }
    public String getMessage() { return message; }
    public String getDestinataireRole() { return destinataireRole; }
    public String getPriorite() { return priorite; }
}
