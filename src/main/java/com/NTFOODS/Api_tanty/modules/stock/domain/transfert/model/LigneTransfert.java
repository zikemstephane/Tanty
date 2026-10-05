package com.NTFOODS.Api_tanty.modules.stock.domain.transfert.model;

import java.util.Objects;
import java.util.UUID;

/** Entité — ligne d'un bon de transfert (quantité dans le conditionnement sku). */
public class LigneTransfert {

    private final String id;
    private final String codeProduit;
    private final String sku;
    private final long quantite;

    private LigneTransfert(String id, String codeProduit, String sku, long quantite) {
        this.id = id;
        this.codeProduit = codeProduit;
        this.sku = sku;
        this.quantite = quantite;
    }

    public static LigneTransfert creer(String codeProduit, String sku, long quantite) {
        Objects.requireNonNull(codeProduit, "Le code produit est obligatoire");
        if (quantite <= 0) throw new IllegalArgumentException("La quantité à transférer doit être positive");
        return new LigneTransfert(UUID.randomUUID().toString(), codeProduit.trim().toUpperCase(), sku, quantite);
    }

    public static LigneTransfert reconstituer(String id, String codeProduit, String sku, long quantite) {
        return new LigneTransfert(id, codeProduit, sku, quantite);
    }

    public String getId() { return id; }
    public String getCodeProduit() { return codeProduit; }
    public String getSku() { return sku; }
    public long getQuantite() { return quantite; }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof LigneTransfert that)) return false;
        return Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() { return Objects.hash(id); }
}
