package com.NTFOODS.Api_tanty.modules.comptabilite.domain.versement.events;

import com.NTFOODS.Api_tanty.shared.kernel.event.DomainEvent;

import java.time.Instant;
import java.util.UUID;

public class VersementValideEvent implements DomainEvent {
    private final String eventId = UUID.randomUUID().toString();
    private final Instant occurredOn = Instant.now();
    private final String referenceVersement;
    private final String matriculeCommercial;
    private final double montantValide;

    public VersementValideEvent(String referenceVersement, String matriculeCommercial, double montantValide) {
        this.referenceVersement = referenceVersement;
        this.matriculeCommercial = matriculeCommercial;
        this.montantValide = montantValide;
    }

    @Override public String getEventId() { return eventId; }
    @Override public Instant getOccurredOn() { return occurredOn; }
    public String getReferenceVersement() { return referenceVersement; }
    public String getMatriculeCommercial() { return matriculeCommercial; }
    public double getMontantValide() { return montantValide; }
}
