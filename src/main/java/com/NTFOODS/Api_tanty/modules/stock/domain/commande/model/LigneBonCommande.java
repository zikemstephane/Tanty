package com.NTFOODS.Api_tanty.modules.stock.domain.commande.model;

import java.math.BigDecimal;
import java.util.Objects;
import java.util.UUID;

/**
 * Entité — ligne d'un Bon de Commande (appartient à l'agrégat BonCommande).
 * Suit la quantité commandée vs la quantité réellement reçue (par conditionnement).
 */
public class LigneBonCommande {

    private final String id;
    private final String codeProduit;
    private final String designation;
    private final String sku;                 // conditionnement commandé
    private final long quantiteCommandee;
    private long quantiteRecue;
    private final BigDecimal prixUnitaireHT;

    private LigneBonCommande(String id, String codeProduit, String designation, String sku,
                            long quantiteCommandee, long quantiteRecue, BigDecimal prixUnitaireHT) {
        this.id = id;
        this.codeProduit = codeProduit;
        this.designation = designation;
        this.sku = sku;
        this.quantiteCommandee = quantiteCommandee;
        this.quantiteRecue = quantiteRecue;
        this.prixUnitaireHT = prixUnitaireHT;
    }

    public static LigneBonCommande creer(String codeProduit, String designation, String sku,
                                         long quantiteCommandee, BigDecimal prixUnitaireHT) {
        Objects.requireNonNull(codeProduit, "Le code produit est obligatoire");
        if (quantiteCommandee <= 0) throw new IllegalArgumentException("La quantité commandée doit être positive");
        return new LigneBonCommande(UUID.randomUUID().toString(), codeProduit.trim().toUpperCase(),
            designation, sku, quantiteCommandee, 0L, prixUnitaireHT != null ? prixUnitaireHT : BigDecimal.ZERO);
    }

    public static LigneBonCommande reconstituer(String id, String codeProduit, String designation, String sku,
                                                long quantiteCommandee, long quantiteRecue, BigDecimal prixUnitaireHT) {
        return new LigneBonCommande(id, codeProduit, designation, sku, quantiteCommandee, quantiteRecue, prixUnitaireHT);
    }

    void enregistrerReception(long quantite) {
        if (quantite <= 0) throw new IllegalArgumentException("La quantité reçue doit être positive");
        this.quantiteRecue += quantite;
    }

    public boolean estCompletementRecue() {
        return quantiteRecue >= quantiteCommandee;
    }

    public BigDecimal montantLigneHT() {
        return prixUnitaireHT.multiply(BigDecimal.valueOf(quantiteCommandee));
    }

    public String getId() { return id; }
    public String getCodeProduit() { return codeProduit; }
    public String getDesignation() { return designation; }
    public String getSku() { return sku; }
    public long getQuantiteCommandee() { return quantiteCommandee; }
    public long getQuantiteRecue() { return quantiteRecue; }
    public BigDecimal getPrixUnitaireHT() { return prixUnitaireHT; }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof LigneBonCommande that)) return false;
        return Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() { return Objects.hash(id); }
}
