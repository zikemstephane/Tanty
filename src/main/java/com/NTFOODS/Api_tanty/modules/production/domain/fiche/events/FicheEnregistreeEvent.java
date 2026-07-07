package com.NTFOODS.Api_tanty.modules.production.domain.fiche.events;

import com.NTFOODS.Api_tanty.shared.kernel.event.DomainEvent;

import java.time.Instant;
import java.time.LocalDate;
import java.util.UUID;

public class FicheEnregistreeEvent implements DomainEvent {
    private final String eventId = UUID.randomUUID().toString();
    private final Instant occurredOn = Instant.now();
    private final String referencePPH;
    private final LocalDate date;
    private final String matriculeAgent;
    private final int nombreLignes;

    public FicheEnregistreeEvent(String referencePPH, LocalDate date, String matriculeAgent, int nombreLignes) {
        this.referencePPH = referencePPH;
        this.date = date;
        this.matriculeAgent = matriculeAgent;
        this.nombreLignes = nombreLignes;
    }

    @Override public String getEventId() { return eventId; }
    @Override public Instant getOccurredOn() { return occurredOn; }
    public String getReferencePPH() { return referencePPH; }
    public LocalDate getDate() { return date; }
    public String getMatriculeAgent() { return matriculeAgent; }
    public int getNombreLignes() { return nombreLignes; }
}
