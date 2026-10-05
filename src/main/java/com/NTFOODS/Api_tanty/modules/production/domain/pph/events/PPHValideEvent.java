package com.NTFOODS.Api_tanty.modules.production.domain.pph.events;

import com.NTFOODS.Api_tanty.shared.kernel.event.DomainEvent;

import java.time.Instant;
import java.time.LocalDate;
import java.util.UUID;

public class PPHValideEvent implements DomainEvent {
    private final String eventId = UUID.randomUUID().toString();
    private final Instant occurredOn = Instant.now();
    private final String referencePPH;
    private final String semaine;
    private final LocalDate dateDebut;
    private final String matriculeChefProduction;

    public PPHValideEvent(String referencePPH, String semaine, LocalDate dateDebut, String matriculeChefProduction) {
        this.referencePPH = referencePPH;
        this.semaine = semaine;
        this.dateDebut = dateDebut;
        this.matriculeChefProduction = matriculeChefProduction;
    }

    @Override public String getEventId() { return eventId; }
    @Override public Instant getOccurredOn() { return occurredOn; }
    public String getReferencePPH() { return referencePPH; }
    public String getSemaine() { return semaine; }
    public LocalDate getDateDebut() { return dateDebut; }
    public String getMatriculeChefProduction() { return matriculeChefProduction; }
}
