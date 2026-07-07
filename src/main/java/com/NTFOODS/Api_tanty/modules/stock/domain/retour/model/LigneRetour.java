package com.NTFOODS.Api_tanty.modules.stock.domain.retour.model;

import java.util.Objects;
import java.util.UUID;

/** Entité — ligne d'un bon de retour (invendus retournés par un commercial). */
public class LigneRetour {

    private final String id;
    private final String codeProduit;
    private final String sku;
    private final long quantite;
    private final String motif;

    private LigneRetour(String id, String codeProduit, String sku, long quantite, String motif) {
        this.id = id;
        this.codeProduit = codeProduit;
        this.sku = sku;
        this.quantite = quantite;
        this.motif = motif;
    }

    public static LigneRetour creer(String codeProduit, String sku, long quantite, String motif) {
        Objects.requireNonNull(codeProduit, "Le code produit est obligatoire");
        if (quantite <= 0) throw new IllegalArgumentException("La quantité retournée doit être positive");
        return new LigneRetour(UUID.randomUUID().toString(), codeProduit.trim().toUpperCase(), sku, quantite, motif);
    }

    public static LigneRetour reconstituer(String id, String codeProduit, String sku, long quantite, String motif) {
        return new LigneRetour(id, codeProduit, sku, quantite, motif);
    }

    public String getId() { return id; }
    public String getCodeProduit() { return codeProduit; }
    public String getSku() { return sku; }
    public long getQuantite() { return quantite; }
    public String getMotif() { return motif; }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof LigneRetour that)) return false;
        return Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() { return Objects.hash(id); }
}
