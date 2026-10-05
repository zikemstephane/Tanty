package com.NTFOODS.Api_tanty.modules.commercial.domain.precommande.events;

import com.NTFOODS.Api_tanty.modules.commercial.domain.precommande.enums.StatutPreCommande;
import com.NTFOODS.Api_tanty.shared.kernel.event.DomainEvent;

import java.time.Instant;
import java.time.LocalDate;
import java.util.UUID;

public class PreCommandeValideeEvent implements DomainEvent {
    private final String eventId = UUID.randomUUID().toString();
    private final Instant occurredOn = Instant.now();
    private final String numeroPreCommande;
    private final String matriculeCommercial;
    private final StatutPreCommande statut;
    private final LocalDate dateEffet;

    public PreCommandeValideeEvent(String numeroPreCommande, String matriculeCommercial,
                                    StatutPreCommande statut, LocalDate dateEffet) {
        this.numeroPreCommande = numeroPreCommande;
        this.matriculeCommercial = matriculeCommercial;
        this.statut = statut;
        this.dateEffet = dateEffet;
    }

    @Override public String getEventId() { return eventId; }
    @Override public Instant getOccurredOn() { return occurredOn; }
    public String getNumeroPreCommande() { return numeroPreCommande; }
    public String getMatriculeCommercial() { return matriculeCommercial; }
    public StatutPreCommande getStatut() { return statut; }
    public LocalDate getDateEffet() { return dateEffet; }
}
