package com.NTFOODS.Api_tanty.modules.stock.domain.mouvement.model;

import com.NTFOODS.Api_tanty.modules.stock.domain.mouvement.enums.TypeMouvement;

import java.time.LocalDateTime;
import java.util.Objects;

/**
 * Agrégat Racine — Mouvement de stock (entrée du registre, immuable).
 *
 * Chaque variation physique de stock produit un mouvement horodaté et tracé
 * (acteur, document source, conditionnement concerné). Sert de piste d'audit
 * et de base à la valorisation par le module Comptabilité.
 */
public class MouvementStock {

    private Long id;
    private final String reference;
    private final TypeMouvement type;
    private final String codeProduit;
    private final String sku;                 // conditionnement exact concerné
    private final long quantiteEnBase;
    private final String codeEntrepotSource;  // nul pour une entrée pure
    private final String codeEntrepotCible;   // nul pour une sortie pure
    private final String matriculeActeur;
    private final String referenceDocument;   // BR, BT, BD, BRetour, inventaire…
    private final String motif;
    private final LocalDateTime horodatage;

    private MouvementStock(String reference, TypeMouvement type, String codeProduit, String sku,
                           long quantiteEnBase, String codeEntrepotSource, String codeEntrepotCible,
                           String matriculeActeur, String referenceDocument, String motif,
                           LocalDateTime horodatage) {
        this.reference = Objects.requireNonNull(reference);
        this.type = Objects.requireNonNull(type);
        this.codeProduit = Objects.requireNonNull(codeProduit);
        this.sku = sku;
        if (quantiteEnBase <= 0) throw new IllegalArgumentException("La quantité d'un mouvement doit être positive");
        this.quantiteEnBase = quantiteEnBase;
        this.codeEntrepotSource = codeEntrepotSource;
        this.codeEntrepotCible = codeEntrepotCible;
        this.matriculeActeur = Objects.requireNonNull(matriculeActeur);
        this.referenceDocument = referenceDocument;
        this.motif = motif;
        this.horodatage = horodatage;
    }

    public static MouvementStock enregistrer(String reference, TypeMouvement type, String codeProduit,
                                             String sku, long quantiteEnBase, String codeEntrepotSource,
                                             String codeEntrepotCible, String matriculeActeur,
                                             String referenceDocument, String motif) {
        return new MouvementStock(reference, type, codeProduit, sku, quantiteEnBase,
            codeEntrepotSource, codeEntrepotCible, matriculeActeur, referenceDocument, motif,
            LocalDateTime.now());
    }

    public static MouvementStock reconstituer(Long id, String reference, TypeMouvement type, String codeProduit,
                                              String sku, long quantiteEnBase, String codeEntrepotSource,
                                              String codeEntrepotCible, String matriculeActeur,
                                              String referenceDocument, String motif, LocalDateTime horodatage) {
        MouvementStock m = new MouvementStock(reference, type, codeProduit, sku, quantiteEnBase,
            codeEntrepotSource, codeEntrepotCible, matriculeActeur, referenceDocument, motif, horodatage);
        m.id = id;
        return m;
    }

    public Long getId() { return id; }
    public String getReference() { return reference; }
    public TypeMouvement getType() { return type; }
    public String getCodeProduit() { return codeProduit; }
    public String getSku() { return sku; }
    public long getQuantiteEnBase() { return quantiteEnBase; }
    public String getCodeEntrepotSource() { return codeEntrepotSource; }
    public String getCodeEntrepotCible() { return codeEntrepotCible; }
    public String getMatriculeActeur() { return matriculeActeur; }
    public String getReferenceDocument() { return referenceDocument; }
    public String getMotif() { return motif; }
    public LocalDateTime getHorodatage() { return horodatage; }
}
