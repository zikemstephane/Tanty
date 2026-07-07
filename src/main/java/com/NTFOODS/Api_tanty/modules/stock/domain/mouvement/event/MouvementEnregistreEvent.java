package com.NTFOODS.Api_tanty.modules.stock.domain.mouvement.event;

import com.NTFOODS.Api_tanty.modules.stock.domain.mouvement.enums.TypeMouvement;
import com.NTFOODS.Api_tanty.shared.kernel.event.DomainEvent;

import java.time.Instant;
import java.util.UUID;

/**
 * Événement — un mouvement de stock a été enregistré.
 * Publié sur le bus (stock.mouvement.enregistre) pour la valorisation Comptabilité.
 */
public class MouvementEnregistreEvent implements DomainEvent {

    private final String eventId = UUID.randomUUID().toString();
    private final Instant occurredOn = Instant.now();
    private final String reference;
    private final TypeMouvement type;
    private final String codeProduit;
    private final String sku;
    private final long quantiteEnBase;
    private final String codeEntrepotSource;
    private final String codeEntrepotCible;
    private final String matriculeActeur;

    public MouvementEnregistreEvent(String reference, TypeMouvement type, String codeProduit, String sku,
                                    long quantiteEnBase, String codeEntrepotSource, String codeEntrepotCible,
                                    String matriculeActeur) {
        this.reference = reference;
        this.type = type;
        this.codeProduit = codeProduit;
        this.sku = sku;
        this.quantiteEnBase = quantiteEnBase;
        this.codeEntrepotSource = codeEntrepotSource;
        this.codeEntrepotCible = codeEntrepotCible;
        this.matriculeActeur = matriculeActeur;
    }

    @Override public String getEventId() { return eventId; }
    @Override public Instant getOccurredOn() { return occurredOn; }
    public String getReference() { return reference; }
    public TypeMouvement getType() { return type; }
    public String getCodeProduit() { return codeProduit; }
    public String getSku() { return sku; }
    public long getQuantiteEnBase() { return quantiteEnBase; }
    public String getCodeEntrepotSource() { return codeEntrepotSource; }
    public String getCodeEntrepotCible() { return codeEntrepotCible; }
    public String getMatriculeActeur() { return matriculeActeur; }
}
