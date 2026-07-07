package com.NTFOODS.Api_tanty.modules.stock.domain.inventaire.event;

import com.NTFOODS.Api_tanty.modules.stock.domain.inventaire.enums.TypeInventaire;
import com.NTFOODS.Api_tanty.shared.kernel.event.DomainEvent;

import java.time.Instant;
import java.util.UUID;

/** Événement — un inventaire a été validé par le DG (le stock est ajusté). */
public class InventaireValideEvent implements DomainEvent {

    private final String eventId = UUID.randomUUID().toString();
    private final Instant occurredOn = Instant.now();
    private final String numero;
    private final String codeEntrepot;
    private final TypeInventaire type;

    public InventaireValideEvent(String numero, String codeEntrepot, TypeInventaire type) {
        this.numero = numero;
        this.codeEntrepot = codeEntrepot;
        this.type = type;
    }

    @Override public String getEventId() { return eventId; }
    @Override public Instant getOccurredOn() { return occurredOn; }
    public String getNumero() { return numero; }
    public String getCodeEntrepot() { return codeEntrepot; }
    public TypeInventaire getType() { return type; }
}
