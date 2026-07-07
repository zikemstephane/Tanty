package com.NTFOODS.Api_tanty.modules.auth.domain.utilisateur.events;

import com.NTFOODS.Api_tanty.shared.kernel.event.DomainEvent;

import java.time.Instant;
import java.util.UUID;

/**
 * Publié lorsqu'un compte est automatiquement bloqué après 3 tentatives de
 * connexion erronées. Consommé par le module Notifications pour alerter
 * l'administrateur et le DG (comportement à risque potentiel).
 */
public class CompteBloqueEvent implements DomainEvent {
    private final String eventId = UUID.randomUUID().toString();
    private final Instant occurredOn = Instant.now();
    private final String matricule;
    private final String nomComplet;

    public CompteBloqueEvent(String matricule, String nomComplet) {
        this.matricule = matricule;
        this.nomComplet = nomComplet;
    }

    @Override public String getEventId() { return eventId; }
    @Override public Instant getOccurredOn() { return occurredOn; }
    public String getMatricule() { return matricule; }
    public String getNomComplet() { return nomComplet; }
}
