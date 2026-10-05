package com.NTFOODS.Api_tanty.modules.stock.domain.dotation.model;

import java.util.Objects;
import java.util.UUID;

/**
 * Entité — ligne d'une dotation. Le gestionnaire peut modifier la quantité
 * approuvée et/ou substituer le conditionnement (sku) par rapport à la demande.
 */
public class LigneDotation {

    private final String id;
    private final String codeProduit;
    private String sku;                    // conditionnement (peut être substitué)
    private final long quantiteDemandee;
    private long quantiteApprouvee;
    private String motifModification;

    private LigneDotation(String id, String codeProduit, String sku, long quantiteDemandee,
                         long quantiteApprouvee, String motifModification) {
        this.id = id;
        this.codeProduit = codeProduit;
        this.sku = sku;
        this.quantiteDemandee = quantiteDemandee;
        this.quantiteApprouvee = quantiteApprouvee;
        this.motifModification = motifModification;
    }

    public static LigneDotation creer(String codeProduit, String sku, long quantiteDemandee) {
        Objects.requireNonNull(codeProduit, "Le code produit est obligatoire");
        if (quantiteDemandee <= 0) throw new IllegalArgumentException("La quantité demandée doit être positive");
        return new LigneDotation(UUID.randomUUID().toString(), codeProduit.trim().toUpperCase(), sku,
            quantiteDemandee, quantiteDemandee, null);
    }

    public static LigneDotation reconstituer(String id, String codeProduit, String sku, long quantiteDemandee,
                                             long quantiteApprouvee, String motifModification) {
        return new LigneDotation(id, codeProduit, sku, quantiteDemandee, quantiteApprouvee, motifModification);
    }

    void modifierQuantite(long nouvelleQuantite, String motif) {
        if (nouvelleQuantite < 0) throw new IllegalArgumentException("La quantité approuvée ne peut pas être négative");
        this.quantiteApprouvee = nouvelleQuantite;
        this.motifModification = motif;
    }

    void substituerConditionnement(String nouveauSku, String motif) {
        this.sku = nouveauSku;
        this.motifModification = motif;
    }

    public String getId() { return id; }
    public String getCodeProduit() { return codeProduit; }
    public String getSku() { return sku; }
    public long getQuantiteDemandee() { return quantiteDemandee; }
    public long getQuantiteApprouvee() { return quantiteApprouvee; }
    public String getMotifModification() { return motifModification; }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof LigneDotation that)) return false;
        return Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() { return Objects.hash(id); }
}
