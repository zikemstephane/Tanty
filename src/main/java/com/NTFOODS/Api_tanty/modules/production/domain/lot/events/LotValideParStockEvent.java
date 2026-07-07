package com.NTFOODS.Api_tanty.modules.production.domain.lot.events;

import com.NTFOODS.Api_tanty.shared.kernel.event.DomainEvent;

import java.time.Instant;
import java.time.LocalDate;
import java.util.UUID;

/**
 * Publié quand le Gestionnaire de Stock valide physiquement un lot de produit
 * fini. Déclenche le "Lock Numérique" (Pilier 1 / 3) : la fiche de production
 * journalière correspondante (même referencePPH + même date) devient
 * immuable — l'responsable de salle ne peut plus en modifier les chiffres.
 */
public class LotValideParStockEvent implements DomainEvent {
    private final String eventId = UUID.randomUUID().toString();
    private final Instant occurredOn = Instant.now();
    private final String numeroLot;
    private final String referencePPH;
    private final String codeProduit;
    private final double quantiteKg;
    private final int nbCartons;
    private final LocalDate dateProduction;
    private final String matriculeGestionnaire;

    public LotValideParStockEvent(String numeroLot, String referencePPH, String codeProduit, double quantiteKg,
                                   int nbCartons, LocalDate dateProduction, String matriculeGestionnaire) {
        this.numeroLot = numeroLot;
        this.referencePPH = referencePPH;
        this.codeProduit = codeProduit;
        this.quantiteKg = quantiteKg;
        this.nbCartons = nbCartons;
        this.dateProduction = dateProduction;
        this.matriculeGestionnaire = matriculeGestionnaire;
    }

    @Override public String getEventId() { return eventId; }
    @Override public Instant getOccurredOn() { return occurredOn; }
    public String getNumeroLot() { return numeroLot; }
    public String getReferencePPH() { return referencePPH; }
    public String getCodeProduit() { return codeProduit; }
    public double getQuantiteKg() { return quantiteKg; }
    public int getNbCartons() { return nbCartons; }
    public LocalDate getDateProduction() { return dateProduction; }
    public String getMatriculeGestionnaire() { return matriculeGestionnaire; }
}
