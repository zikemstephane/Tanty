package com.NTFOODS.Api_tanty.modules.stock.domain.retour.model;

import java.util.Objects;
import java.util.UUID;

/**
 * Entité — ligne d'un constat d'invendus.
 *
 * <p>Deux quantités coexistent volontairement : celle que le commercial déclare
 * rapporter et celle que le gestionnaire compte effectivement. La seconde ne
 * remplace jamais la première — c'est leur différence qui constitue l'information
 * exploitable, et l'écraser reviendrait à effacer le constat.</p>
 */
public class LigneRetour {

    private final String id;
    private final String codeProduit;
    private final String sku;
    private final long quantiteDeclaree;
    private final String motif;

    /** Renseignée au comptage seulement ; {@code null} tant que rien n'a été compté. */
    private Long quantiteComptee;

    private LigneRetour(String id, String codeProduit, String sku, long quantiteDeclaree,
                        String motif, Long quantiteComptee) {
        this.id = id;
        this.codeProduit = codeProduit;
        this.sku = sku;
        this.quantiteDeclaree = quantiteDeclaree;
        this.motif = motif;
        this.quantiteComptee = quantiteComptee;
    }

    public static LigneRetour creer(String codeProduit, String sku, long quantiteDeclaree, String motif) {
        Objects.requireNonNull(codeProduit, "Le code produit est obligatoire");
        if (quantiteDeclaree <= 0) throw new IllegalArgumentException("La quantité déclarée doit être positive");
        return new LigneRetour(UUID.randomUUID().toString(), codeProduit.trim().toUpperCase(), sku,
            quantiteDeclaree, motif, null);
    }

    public static LigneRetour reconstituer(String id, String codeProduit, String sku, long quantiteDeclaree,
                                           String motif, Long quantiteComptee) {
        return new LigneRetour(id, codeProduit, sku, quantiteDeclaree, motif, quantiteComptee);
    }

    /**
     * Enregistre la quantité constatée par le gestionnaire.
     *
     * <p>Une quantité nulle est acceptée : constater qu'un produit annoncé n'a en
     * réalité pas été rapporté est une information, pas une absence de saisie.</p>
     */
    void enregistrerComptage(long quantite) {
        if (quantite < 0) {
            throw new IllegalArgumentException("La quantité comptée ne peut pas être négative");
        }
        this.quantiteComptee = quantite;
    }

    /**
     * Différence entre le compté et le déclaré, ou {@code null} si rien n'a été compté.
     * Négative lorsqu'il manque des invendus par rapport à l'annonce.
     */
    public Long ecart() {
        return quantiteComptee == null ? null : quantiteComptee - quantiteDeclaree;
    }

    public boolean estComptee() {
        return quantiteComptee != null;
    }

    public String getId() { return id; }
    public String getCodeProduit() { return codeProduit; }
    public String getSku() { return sku; }
    public long getQuantiteDeclaree() { return quantiteDeclaree; }
    public Long getQuantiteComptee() { return quantiteComptee; }
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
