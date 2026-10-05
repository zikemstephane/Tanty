package com.NTFOODS.Api_tanty.modules.comptabilite.domain.versement.events;

import com.NTFOODS.Api_tanty.shared.kernel.event.DomainEvent;

import java.time.Instant;
import java.util.UUID;

public class AlerteRougeVersementEvent implements DomainEvent {
    private final String eventId = UUID.randomUUID().toString();
    private final Instant occurredOn = Instant.now();
    private final String referenceVersement;
    private final String matriculeCommercial;
    private final double montantAttendu;
    private final double cashVerse;
    private final double ecart;

    public AlerteRougeVersementEvent(String referenceVersement, String matriculeCommercial,
                                      double montantAttendu, double cashVerse, double ecart) {
        this.referenceVersement = referenceVersement;
        this.matriculeCommercial = matriculeCommercial;
        this.montantAttendu = montantAttendu;
        this.cashVerse = cashVerse;
        this.ecart = ecart;
    }

    @Override public String getEventId() { return eventId; }
    @Override public Instant getOccurredOn() { return occurredOn; }
    public String getReferenceVersement() { return referenceVersement; }
    public String getMatriculeCommercial() { return matriculeCommercial; }
    public double getMontantAttendu() { return montantAttendu; }
    public double getCashVerse() { return cashVerse; }
    public double getEcart() { return ecart; }
}
