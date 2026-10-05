package com.NTFOODS.Api_tanty.modules.stock.domain.reception.event;

import com.NTFOODS.Api_tanty.modules.stock.domain.reception.enums.SourceReception;
import com.NTFOODS.Api_tanty.shared.kernel.event.DomainEvent;

import java.time.Instant;
import java.util.UUID;

/** Événement — une réception a été validée (le stock central est mis à jour). */
public class ReceptionValideeEvent implements DomainEvent {

    private final String eventId = UUID.randomUUID().toString();
    private final Instant occurredOn = Instant.now();
    private final String numero;
    private final SourceReception source;
    private final String codeEntrepotCible;
    private final String numeroBonCommande;

    public ReceptionValideeEvent(String numero, SourceReception source, String codeEntrepotCible, String numeroBonCommande) {
        this.numero = numero;
        this.source = source;
        this.codeEntrepotCible = codeEntrepotCible;
        this.numeroBonCommande = numeroBonCommande;
    }

    @Override public String getEventId() { return eventId; }
    @Override public Instant getOccurredOn() { return occurredOn; }
    public String getNumero() { return numero; }
    public SourceReception getSource() { return source; }
    public String getCodeEntrepotCible() { return codeEntrepotCible; }
    public String getNumeroBonCommande() { return numeroBonCommande; }
}
