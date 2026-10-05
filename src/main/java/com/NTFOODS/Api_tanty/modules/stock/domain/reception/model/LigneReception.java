package com.NTFOODS.Api_tanty.modules.stock.domain.reception.model;

import java.util.Objects;
import java.util.UUID;

/**
 * Entité — ligne d'une réception (appartient à l'agrégat Reception).
 *
 * Les quantités sont exprimées dans le conditionnement {@code sku}.
 * Le gestionnaire enregistre uniquement les quantités physiques devant lui.
 * L'écart (attendu vs reçu) est informatif et ne bloque pas la réception.
 */
public class LigneReception {

    private final String id;
    private final String codeProduit;
    private final String sku;
    private final long quantiteAttendue;
    private final long quantiteRecue;
    private final String idLigneBC;        // lien vers la ligne du BC (réception fournisseur)

    private LigneReception(String id, String codeProduit, String sku, long quantiteAttendue,
                          long quantiteRecue, String idLigneBC) {
        this.id = id;
        this.codeProduit = codeProduit;
        this.sku = sku;
        this.quantiteAttendue = quantiteAttendue;
        this.quantiteRecue = quantiteRecue;
        this.idLigneBC = idLigneBC;
    }

    public static LigneReception creer(String codeProduit, String sku, long quantiteAttendue,
                                       long quantiteRecue, String idLigneBC) {
        Objects.requireNonNull(codeProduit, "Le code produit est obligatoire");
        if (quantiteRecue < 0) throw new IllegalArgumentException("La quantité reçue ne peut pas être négative");
        return new LigneReception(UUID.randomUUID().toString(), codeProduit.trim().toUpperCase(), sku,
            quantiteAttendue, quantiteRecue, idLigneBC);
    }

    /**
     * Crée une ligne où le gestionnaire enregistre uniquement la quantité physique devant lui.
     * La quantité attendue est mise à 0 — la conformité à la commande n'est pas vérifiée par le gestionnaire.
     */
    public static LigneReception creerSimple(String codeProduit, String sku, long quantiteRecue, String idLigneBC) {
        return creer(codeProduit, sku, 0, quantiteRecue, idLigneBC);
    }

    public static LigneReception reconstituer(String id, String codeProduit, String sku, long quantiteAttendue,
                                              long quantiteRecue, String idLigneBC) {
        return new LigneReception(id, codeProduit, sku, quantiteAttendue, quantiteRecue, idLigneBC);
    }

    public long getEcart() {
        return quantiteRecue - quantiteAttendue;
    }

    public double pourcentageEcart() {
        if (quantiteAttendue == 0) return quantiteRecue == 0 ? 0.0 : 100.0;
        return Math.abs(getEcart()) * 100.0 / quantiteAttendue;
    }

    public String getId() { return id; }
    public String getCodeProduit() { return codeProduit; }
    public String getSku() { return sku; }
    public long getQuantiteAttendue() { return quantiteAttendue; }
    public long getQuantiteRecue() { return quantiteRecue; }
    public String getIdLigneBC() { return idLigneBC; }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof LigneReception that)) return false;
        return Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() { return Objects.hash(id); }
}
