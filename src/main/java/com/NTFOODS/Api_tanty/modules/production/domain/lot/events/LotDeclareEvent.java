package com.NTFOODS.Api_tanty.modules.production.domain.lot.events;

import com.NTFOODS.Api_tanty.shared.kernel.event.DomainEvent;

import java.time.Instant;
import java.time.LocalDate;
import java.util.UUID;

public class LotDeclareEvent implements DomainEvent {
    private final String eventId = UUID.randomUUID().toString();
    private final Instant occurredOn = Instant.now();
    private final String numeroLot;
    private final String productSku;
    private final double declaredQuantityKg;
    private final int equivalentUnits;
    private final LocalDate dateProduction;
    private final LocalDate dlc;
    private final String matriculeChefProduction;
    private final String conditioningType;
    private final Integer conditioningQty;

    public LotDeclareEvent(String numeroLot, String productSku, double declaredQuantityKg,
                           int equivalentUnits, LocalDate dateProduction, LocalDate dlc,
                           String matriculeChefProduction, String conditioningType, Integer conditioningQty) {
        this.numeroLot = numeroLot;
        this.productSku = productSku;
        this.declaredQuantityKg = declaredQuantityKg;
        this.equivalentUnits = equivalentUnits;
        this.dateProduction = dateProduction;
        this.dlc = dlc;
        this.matriculeChefProduction = matriculeChefProduction;
        this.conditioningType = conditioningType;
        this.conditioningQty = conditioningQty;
    }

    @Override public String getEventId() { return eventId; }
    @Override public Instant getOccurredOn() { return occurredOn; }
    public String getNumeroLot() { return numeroLot; }
    public String getProductSku() { return productSku; }
    public double getDeclaredQuantityKg() { return declaredQuantityKg; }
    public int getEquivalentUnits() { return equivalentUnits; }
    public LocalDate getDateProduction() { return dateProduction; }
    public LocalDate getDlc() { return dlc; }
    public String getMatriculeChefProduction() { return matriculeChefProduction; }
    public String getConditioningType() { return conditioningType; }
    public Integer getConditioningQty() { return conditioningQty; }
}
