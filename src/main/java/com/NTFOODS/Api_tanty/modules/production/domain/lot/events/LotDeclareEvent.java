package com.NTFOODS.Api_tanty.modules.production.domain.lot.events;

import com.NTFOODS.Api_tanty.shared.kernel.event.DomainEvent;

import java.time.Instant;
import java.time.LocalDate;
import java.util.UUID;

public class LotDeclareEvent implements DomainEvent {
    private final String eventId = UUID.randomUUID().toString();
    private final Instant occurredOn = Instant.now();
    private final String numeroLot;
    private final String codeProduit;
    private final double quantiteKg;
    private final int nbCartons;
    private final LocalDate dateProduction;
    private final LocalDate dlc;
    private final String matriculeChefProduction;

    public LotDeclareEvent(String numeroLot, String codeProduit, double quantiteKg,
                           int nbCartons, LocalDate dateProduction, LocalDate dlc,
                           String matriculeChefProduction) {
        this.numeroLot = numeroLot;
        this.codeProduit = codeProduit;
        this.quantiteKg = quantiteKg;
        this.nbCartons = nbCartons;
        this.dateProduction = dateProduction;
        this.dlc = dlc;
        this.matriculeChefProduction = matriculeChefProduction;
    }

    @Override public String getEventId() { return eventId; }
    @Override public Instant getOccurredOn() { return occurredOn; }
    public String getNumeroLot() { return numeroLot; }
    public String getCodeProduit() { return codeProduit; }
    public double getQuantiteKg() { return quantiteKg; }
    public int getNbCartons() { return nbCartons; }
    public LocalDate getDateProduction() { return dateProduction; }
    public LocalDate getDlc() { return dlc; }
    public String getMatriculeChefProduction() { return matriculeChefProduction; }
}
