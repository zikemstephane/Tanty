package com.NTFOODS.Api_tanty.modules.stock.domain.transfert.event;

import com.NTFOODS.Api_tanty.shared.kernel.event.DomainEvent;

import java.time.Instant;
import java.util.UUID;

/** Événement — un transfert central → tampon a été exécuté. */
public class TransfertExecuteEvent implements DomainEvent {

    private final String eventId = UUID.randomUUID().toString();
    private final Instant occurredOn = Instant.now();
    private final String numero;
    private final String codeEntrepotSource;
    private final String codeEntrepotCible;

    public TransfertExecuteEvent(String numero, String codeEntrepotSource, String codeEntrepotCible) {
        this.numero = numero;
        this.codeEntrepotSource = codeEntrepotSource;
        this.codeEntrepotCible = codeEntrepotCible;
    }

    @Override public String getEventId() { return eventId; }
    @Override public Instant getOccurredOn() { return occurredOn; }
    public String getNumero() { return numero; }
    public String getCodeEntrepotSource() { return codeEntrepotSource; }
    public String getCodeEntrepotCible() { return codeEntrepotCible; }
}
