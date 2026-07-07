package com.NTFOODS.Api_tanty.modules.stock.domain.catalogue.event;

import com.NTFOODS.Api_tanty.modules.stock.domain.catalogue.enums.CategorieProduit;
import com.NTFOODS.Api_tanty.shared.kernel.event.DomainEvent;

import java.time.Instant;
import java.util.UUID;

/** Événement — un nouveau produit a été créé dans le catalogue. */
public class ProduitCreeEvent implements DomainEvent {

    private final String eventId = UUID.randomUUID().toString();
    private final Instant occurredOn = Instant.now();
    private final String codeProduit;
    private final String marque;
    private final String gamme;
    private final String variete;
    private final String designation;
    private final CategorieProduit categorie;

    public ProduitCreeEvent(String codeProduit, String marque, String gamme, String variete,
                            String designation, CategorieProduit categorie) {
        this.codeProduit = codeProduit;
        this.marque = marque;
        this.gamme = gamme;
        this.variete = variete;
        this.designation = designation;
        this.categorie = categorie;
    }

    @Override public String getEventId() { return eventId; }
    @Override public Instant getOccurredOn() { return occurredOn; }
    public String getCodeProduit() { return codeProduit; }
    public String getMarque() { return marque; }
    public String getGamme() { return gamme; }
    public String getVariete() { return variete; }
    public String getDesignation() { return designation; }
    public CategorieProduit getCategorie() { return categorie; }
}
