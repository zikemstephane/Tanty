package com.NTFOODS.Api_tanty.modules.stock.domain.commande.event;

import com.NTFOODS.Api_tanty.shared.kernel.event.DomainEvent;

import java.time.Instant;
import java.time.LocalDate;
import java.util.UUID;

/** Événement — un Bon de Commande a été validé (prêt à être lié à une réception). */
public class BonCommandeValideEvent implements DomainEvent {

    private final String eventId = UUID.randomUUID().toString();
    private final Instant occurredOn = Instant.now();
    private final String numero;
    private final String codeFournisseur;
    private final LocalDate dateLivraisonPrevue;

    public BonCommandeValideEvent(String numero, String codeFournisseur, LocalDate dateLivraisonPrevue) {
        this.numero = numero;
        this.codeFournisseur = codeFournisseur;
        this.dateLivraisonPrevue = dateLivraisonPrevue;
    }

    @Override public String getEventId() { return eventId; }
    @Override public Instant getOccurredOn() { return occurredOn; }
    public String getNumero() { return numero; }
    public String getCodeFournisseur() { return codeFournisseur; }
    public LocalDate getDateLivraisonPrevue() { return dateLivraisonPrevue; }
}
