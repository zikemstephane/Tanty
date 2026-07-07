package com.NTFOODS.Api_tanty.modules.stock.domain.dotation.event;

import com.NTFOODS.Api_tanty.shared.kernel.event.DomainEvent;

import java.time.Instant;
import java.time.LocalDate;
import java.util.UUID;

/**
 * Événement — une dotation a été approuvée (publié sur stock.dotation.approuvee
 * pour les modules Caisse / Comptabilité qui gèrent la valorisation financière).
 */
public class DotationApprouveeEvent implements DomainEvent {

    private final String eventId = UUID.randomUUID().toString();
    private final Instant occurredOn = Instant.now();
    private final String numero;
    private final String matriculeCommercial;
    private final String matriculeGestionnaire;
    private final LocalDate dateEffet;

    public DotationApprouveeEvent(String numero, String matriculeCommercial, String matriculeGestionnaire,
                                  LocalDate dateEffet) {
        this.numero = numero;
        this.matriculeCommercial = matriculeCommercial;
        this.matriculeGestionnaire = matriculeGestionnaire;
        this.dateEffet = dateEffet;
    }

    @Override public String getEventId() { return eventId; }
    @Override public Instant getOccurredOn() { return occurredOn; }
    public String getNumero() { return numero; }
    public String getMatriculeCommercial() { return matriculeCommercial; }
    public String getMatriculeGestionnaire() { return matriculeGestionnaire; }
    public LocalDate getDateEffet() { return dateEffet; }
}
