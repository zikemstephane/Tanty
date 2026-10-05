package com.NTFOODS.Api_tanty.modules.commercial.domain.tarification.event;

import com.NTFOODS.Api_tanty.shared.kernel.event.DomainEvent;

import java.time.Instant;
import java.util.UUID;

/**
 * Publié lorsqu'un DG ou Directeur Commercial accorde un prix exceptionnel.
 * Consommé par le module Notifications (traçabilité de la décision) et par
 * l'Audit Trail pour le suivi des dérogations tarifaires.
 */
public class PrixExceptionnelAccordeEvent implements DomainEvent {
    private final String eventId = UUID.randomUUID().toString();
    private final Instant occurredOn = Instant.now();
    private final String reference;
    private final String codeProduit;
    private final String codeClient;
    private final double prixHomologue;
    private final double prixApplique;
    private final String matriculeAutorisateur;

    public PrixExceptionnelAccordeEvent(String reference, String codeProduit, String codeClient,
                                         double prixHomologue, double prixApplique, String matriculeAutorisateur) {
        this.reference = reference;
        this.codeProduit = codeProduit;
        this.codeClient = codeClient;
        this.prixHomologue = prixHomologue;
        this.prixApplique = prixApplique;
        this.matriculeAutorisateur = matriculeAutorisateur;
    }

    @Override public String getEventId() { return eventId; }
    @Override public Instant getOccurredOn() { return occurredOn; }
    public String getReference() { return reference; }
    public String getCodeProduit() { return codeProduit; }
    public String getCodeClient() { return codeClient; }
    public double getPrixHomologue() { return prixHomologue; }
    public double getPrixApplique() { return prixApplique; }
    public String getMatriculeAutorisateur() { return matriculeAutorisateur; }
}
