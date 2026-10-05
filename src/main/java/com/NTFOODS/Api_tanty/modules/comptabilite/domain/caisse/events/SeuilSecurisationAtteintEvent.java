package com.NTFOODS.Api_tanty.modules.comptabilite.domain.caisse.events;

import com.NTFOODS.Api_tanty.shared.kernel.event.DomainEvent;

import java.time.Instant;
import java.time.LocalDate;
import java.util.UUID;

public class SeuilSecurisationAtteintEvent implements DomainEvent {
    private final String eventId = UUID.randomUUID().toString();
    private final Instant occurredOn = Instant.now();
    private final LocalDate date;
    private final double soldeCaisse;
    private final double seuilSecurisation;

    public SeuilSecurisationAtteintEvent(LocalDate date, double soldeCaisse, double seuilSecurisation) {
        this.date = date;
        this.soldeCaisse = soldeCaisse;
        this.seuilSecurisation = seuilSecurisation;
    }

    @Override public String getEventId() { return eventId; }
    @Override public Instant getOccurredOn() { return occurredOn; }
    public LocalDate getDate() { return date; }
    public double getSoldeCaisse() { return soldeCaisse; }
    public double getSeuilSecurisation() { return seuilSecurisation; }
}
