package com.NTFOODS.Api_tanty.modules.stock.domain.dotation.event;

import com.NTFOODS.Api_tanty.shared.kernel.event.DomainEvent;

import java.time.Instant;
import java.time.LocalDate;
import java.util.UUID;

/** Événement — une dotation a été soumise (notification immédiate au gestionnaire). */
public class DotationSoumiseEvent implements DomainEvent {

    private final String eventId = UUID.randomUUID().toString();
    private final Instant occurredOn = Instant.now();
    private final String numero;
    private final String matriculeCommercial;
    private final LocalDate dateEffet;

    public DotationSoumiseEvent(String numero, String matriculeCommercial, LocalDate dateEffet) {
        this.numero = numero;
        this.matriculeCommercial = matriculeCommercial;
        this.dateEffet = dateEffet;
    }

    @Override public String getEventId() { return eventId; }
    @Override public Instant getOccurredOn() { return occurredOn; }
    public String getNumero() { return numero; }
    public String getMatriculeCommercial() { return matriculeCommercial; }
    public LocalDate getDateEffet() { return dateEffet; }
}
