package com.NTFOODS.Api_tanty.modules.stock.domain.niveau.model;

import com.NTFOODS.Api_tanty.modules.stock.domain.entrepot.enums.TypeEntrepot;
import com.NTFOODS.Api_tanty.shared.kernel.exception.RegleMetierException;

import java.time.LocalDateTime;
import java.util.Objects;

/**
 * Agrégat Racine — Niveau de stock d'un produit dans un entrepôt.
 *
 * La quantité est toujours stockée en UNITÉS DE BASE (pivot), ce qui permet
 * d'afficher ensuite la quantité dans tous les conditionnements via le catalogue.
 * Garantit l'invariant : quantité disponible jamais négative.
 */
public class NiveauStock {

    private Long id;
    private final String codeProduit;
    private final String codeEntrepot;
    private final TypeEntrepot typeEntrepot;
    private long quantiteEnBase;
    private LocalDateTime dateModification;
    private Long version;

    private NiveauStock(String codeProduit, String codeEntrepot, TypeEntrepot typeEntrepot,
                        long quantiteEnBase) {
        this.codeProduit = Objects.requireNonNull(codeProduit);
        this.codeEntrepot = Objects.requireNonNull(codeEntrepot);
        this.typeEntrepot = Objects.requireNonNull(typeEntrepot);
        if (quantiteEnBase < 0) throw new IllegalArgumentException("La quantité initiale ne peut pas être négative");
        this.quantiteEnBase = quantiteEnBase;
        this.version = 0L;
    }

    public static NiveauStock creer(String codeProduit, String codeEntrepot, TypeEntrepot typeEntrepot) {
        return new NiveauStock(codeProduit, codeEntrepot, typeEntrepot, 0L);
    }

    public static NiveauStock reconstituer(Long id, String codeProduit, String codeEntrepot,
                                           TypeEntrepot typeEntrepot, long quantiteEnBase,
                                           LocalDateTime dateModification, Long version) {
        NiveauStock n = new NiveauStock(codeProduit, codeEntrepot, typeEntrepot, quantiteEnBase);
        n.id = id;
        n.dateModification = dateModification;
        n.version = version;
        return n;
    }

    /** Entrée de marchandise (en unités de base). */
    public void entrer(long quantiteEnBase) {
        if (quantiteEnBase <= 0) throw new IllegalArgumentException("La quantité entrante doit être positive");
        this.quantiteEnBase += quantiteEnBase;
        this.dateModification = LocalDateTime.now();
    }

    /** Sortie de marchandise (en unités de base). Refuse si stock insuffisant. */
    public void sortir(long quantiteEnBase) {
        if (quantiteEnBase <= 0) throw new IllegalArgumentException("La quantité sortante doit être positive");
        if (quantiteEnBase > this.quantiteEnBase) {
            throw new RegleMetierException(
                "Stock insuffisant dans " + codeEntrepot + " pour le produit " + codeProduit
                + " (disponible: " + this.quantiteEnBase + ", demandé: " + quantiteEnBase + ")"
            );
        }
        this.quantiteEnBase -= quantiteEnBase;
        this.dateModification = LocalDateTime.now();
    }

    public boolean aAuMoins(long quantiteEnBase) {
        return this.quantiteEnBase >= quantiteEnBase;
    }

    public Long getId() { return id; }
    public String getCodeProduit() { return codeProduit; }
    public String getCodeEntrepot() { return codeEntrepot; }
    public TypeEntrepot getTypeEntrepot() { return typeEntrepot; }
    public long getQuantiteEnBase() { return quantiteEnBase; }
    public LocalDateTime getDateModification() { return dateModification; }
    public Long getVersion() { return version; }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof NiveauStock that)) return false;
        return Objects.equals(codeProduit, that.codeProduit) && Objects.equals(codeEntrepot, that.codeEntrepot);
    }

    @Override
    public int hashCode() { return Objects.hash(codeProduit, codeEntrepot); }
}
