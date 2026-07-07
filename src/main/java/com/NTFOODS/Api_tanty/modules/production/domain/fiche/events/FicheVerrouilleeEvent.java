package com.NTFOODS.Api_tanty.modules.production.domain.fiche.events;

import com.NTFOODS.Api_tanty.shared.kernel.event.DomainEvent;

import java.time.Instant;
import java.time.LocalDate;
import java.util.UUID;

/** Publié quand une fiche de production journalière devient verrouillée (Lock Numérique). */
public class FicheVerrouilleeEvent implements DomainEvent {
    private final String eventId = UUID.randomUUID().toString();
    private final Instant occurredOn = Instant.now();
    private final String referencePPH;
    private final LocalDate date;
    private final String matriculeAgentProduction;

    public FicheVerrouilleeEvent(String referencePPH, LocalDate date, String matriculeAgentProduction) {
        this.referencePPH = referencePPH;
        this.date = date;
        this.matriculeAgentProduction = matriculeAgentProduction;
    }

    @Override public String getEventId() { return eventId; }
    @Override public Instant getOccurredOn() { return occurredOn; }
    public String getReferencePPH() { return referencePPH; }
    public LocalDate getDate() { return date; }
    public String getMatriculeAgentProduction() { return matriculeAgentProduction; }
}
