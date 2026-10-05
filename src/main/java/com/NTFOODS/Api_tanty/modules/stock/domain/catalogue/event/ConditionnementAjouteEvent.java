package com.NTFOODS.Api_tanty.modules.stock.domain.catalogue.event;

import com.NTFOODS.Api_tanty.modules.stock.domain.catalogue.enums.NiveauConditionnement;
import com.NTFOODS.Api_tanty.shared.kernel.event.DomainEvent;

import java.time.Instant;
import java.util.UUID;

/** Événement — un conditionnement a été ajouté à un produit. */
public class ConditionnementAjouteEvent implements DomainEvent {

    private final String eventId = UUID.randomUUID().toString();
    private final Instant occurredOn = Instant.now();
    private final String codeProduit;
    private final String sku;
    private final NiveauConditionnement niveau;

    public ConditionnementAjouteEvent(String codeProduit, String sku, NiveauConditionnement niveau) {
        this.codeProduit = codeProduit;
        this.sku = sku;
        this.niveau = niveau;
    }

    @Override public String getEventId() { return eventId; }
    @Override public Instant getOccurredOn() { return occurredOn; }
    public String getCodeProduit() { return codeProduit; }
    public String getSku() { return sku; }
    public NiveauConditionnement getNiveau() { return niveau; }
}
