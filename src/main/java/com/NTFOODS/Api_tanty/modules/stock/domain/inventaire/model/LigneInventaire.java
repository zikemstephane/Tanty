package com.NTFOODS.Api_tanty.modules.stock.domain.inventaire.model;

import java.util.Objects;
import java.util.UUID;

/**
 * Entité — ligne de comptage d'inventaire (quantités en unités de base).
 * L'écart théorique vs compté est calculé automatiquement.
 */
public class LigneInventaire {

    private final String id;
    private final String codeProduit;
    private final String emplacement;
    private final long quantiteTheorique;
    private Long quantiteComptee;          // null tant que non comptée

    private LigneInventaire(String id, String codeProduit, String emplacement,
                           long quantiteTheorique, Long quantiteComptee) {
        this.id = id;
        this.codeProduit = codeProduit;
        this.emplacement = emplacement;
        this.quantiteTheorique = quantiteTheorique;
        this.quantiteComptee = quantiteComptee;
    }

    public static LigneInventaire creer(String codeProduit, String emplacement, long quantiteTheorique) {
        Objects.requireNonNull(codeProduit, "Le code produit est obligatoire");
        return new LigneInventaire(UUID.randomUUID().toString(), codeProduit.trim().toUpperCase(),
            emplacement, quantiteTheorique, null);
    }

    public static LigneInventaire reconstituer(String id, String codeProduit, String emplacement,
                                               long quantiteTheorique, Long quantiteComptee) {
        return new LigneInventaire(id, codeProduit, emplacement, quantiteTheorique, quantiteComptee);
    }

    void saisirComptage(long quantiteComptee) {
        if (quantiteComptee < 0) throw new IllegalArgumentException("La quantité comptée ne peut pas être négative");
        this.quantiteComptee = quantiteComptee;
    }

    public boolean estComptee() {
        return quantiteComptee != null;
    }

    public long getEcart() {
        return quantiteComptee == null ? 0 : quantiteComptee - quantiteTheorique;
    }

    public String getId() { return id; }
    public String getCodeProduit() { return codeProduit; }
    public String getEmplacement() { return emplacement; }
    public long getQuantiteTheorique() { return quantiteTheorique; }
    public Long getQuantiteComptee() { return quantiteComptee; }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof LigneInventaire that)) return false;
        return Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() { return Objects.hash(id); }
}
