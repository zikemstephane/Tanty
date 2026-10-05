package com.NTFOODS.Api_tanty.modules.commercial.domain.vente.events;

import com.NTFOODS.Api_tanty.shared.kernel.event.DomainEvent;

import java.time.Instant;
import java.time.LocalDate;
import java.util.UUID;

public class VenteFinaliseEvent implements DomainEvent {
    private final String eventId = UUID.randomUUID().toString();
    private final Instant occurredOn = Instant.now();
    private final String numeroVente;
    private final String matriculeCommercial;
    private final String codeClient;
    private final double montantTotal;
    private final LocalDate date;

    public VenteFinaliseEvent(String numeroVente, String matriculeCommercial, String codeClient,
                               double montantTotal, LocalDate date) {
        this.numeroVente = numeroVente;
        this.matriculeCommercial = matriculeCommercial;
        this.codeClient = codeClient;
        this.montantTotal = montantTotal;
        this.date = date;
    }

    @Override public String getEventId() { return eventId; }
    @Override public Instant getOccurredOn() { return occurredOn; }
    public String getNumeroVente() { return numeroVente; }
    public String getMatriculeCommercial() { return matriculeCommercial; }
    public String getCodeClient() { return codeClient; }
    public double getMontantTotal() { return montantTotal; }
    public LocalDate getDate() { return date; }
}
