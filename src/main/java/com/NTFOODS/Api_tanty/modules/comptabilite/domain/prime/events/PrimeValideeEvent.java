package com.NTFOODS.Api_tanty.modules.comptabilite.domain.prime.events;

import com.NTFOODS.Api_tanty.shared.kernel.event.DomainEvent;

import java.time.Instant;
import java.util.UUID;

public class PrimeValideeEvent implements DomainEvent {
    private final String eventId = UUID.randomUUID().toString();
    private final Instant occurredOn = Instant.now();
    private final String referencePrime;
    private final String matriculeCommercial;
    private final double montantPrime;

    public PrimeValideeEvent(String referencePrime, String matriculeCommercial, double montantPrime) {
        this.referencePrime = referencePrime;
        this.matriculeCommercial = matriculeCommercial;
        this.montantPrime = montantPrime;
    }

    @Override public String getEventId() { return eventId; }
    @Override public Instant getOccurredOn() { return occurredOn; }
    public String getReferencePrime() { return referencePrime; }
    public String getMatriculeCommercial() { return matriculeCommercial; }
    public double getMontantPrime() { return montantPrime; }
}
