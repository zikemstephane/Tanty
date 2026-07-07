package com.NTFOODS.Api_tanty.modules.auth.domain.utilisateur.events;

import com.NTFOODS.Api_tanty.modules.auth.domain.utilisateur.enums.Role;
import com.NTFOODS.Api_tanty.shared.kernel.event.DomainEvent;

import java.time.Instant;
import java.util.UUID;

public class UtilisateurCreeEvent implements DomainEvent {
    private final String eventId = UUID.randomUUID().toString();
    private final Instant occurredOn = Instant.now();
    private final String matricule;
    private final String nomComplet;
    private final String email;
    private final Role role;

    public UtilisateurCreeEvent(String matricule, String nomComplet, String email, Role role) {
        this.matricule = matricule;
        this.nomComplet = nomComplet;
        this.email = email;
        this.role = role;
    }

    @Override public String getEventId() { return eventId; }
    @Override public Instant getOccurredOn() { return occurredOn; }
    public String getMatricule() { return matricule; }
    public String getNomComplet() { return nomComplet; }
    public String getEmail() { return email; }
    public Role getRole() { return role; }
}
