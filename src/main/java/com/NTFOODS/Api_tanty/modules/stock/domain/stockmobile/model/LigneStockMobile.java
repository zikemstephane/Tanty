package com.NTFOODS.Api_tanty.modules.stock.domain.stockmobile.model;

import java.time.LocalDateTime;
import java.util.Objects;

/**
 * Entité — Ligne de stock mobile d'un commercial pour un produit donné.
 * Suivi en temps réel des quantités disponibles chez le commercial.
 * Les restes non vendus restent dans le stock mobile (pas de retour au stock central).
 */
public class LigneStockMobile {

    private Long id;
    private final String codeProduit;
    private final String designation;
    private final String conditionnement;
    private long quantiteDisponible;
    private long quantiteInitiale;
    private long quantiteVendue;
    private long quantiteAvariee;
    private long quantiteDonnee;
    private LocalDateTime dateDernierMouvement;

    public LigneStockMobile(String codeProduit, String designation, String conditionnement, long quantiteInitiale) {
        this.codeProduit = Objects.requireNonNull(codeProduit);
        this.designation = Objects.requireNonNull(designation);
        this.conditionnement = Objects.requireNonNull(conditionnement);
        if (quantiteInitiale < 0) throw new IllegalArgumentException("La quantité initiale ne peut pas être négative");
        this.quantiteInitiale = quantiteInitiale;
        this.quantiteDisponible = quantiteInitiale;
        this.quantiteVendue = 0;
        this.quantiteAvariee = 0;
        this.quantiteDonnee = 0;
        this.dateDernierMouvement = LocalDateTime.now();
    }

    public static LigneStockMobile reconstituer(Long id, String codeProduit, String designation,
                                                  String conditionnement, long quantiteDisponible,
                                                  long quantiteInitiale, long quantiteVendue,
                                                  long quantiteAvariee, long quantiteDonnee,
                                                  LocalDateTime dateDernierMouvement) {
        LigneStockMobile l = new LigneStockMobile(codeProduit, designation, conditionnement, quantiteInitiale);
        l.id = id;
        l.quantiteDisponible = quantiteDisponible;
        l.quantiteVendue = quantiteVendue;
        l.quantiteAvariee = quantiteAvariee;
        l.quantiteDonnee = quantiteDonnee;
        l.dateDernierMouvement = dateDernierMouvement;
        return l;
    }

    public void crediterDotation(long quantite) {
        if (quantite <= 0) throw new IllegalArgumentException("La quantité de dotation doit être positive");
        this.quantiteDisponible += quantite;
        this.quantiteInitiale += quantite;
        this.dateDernierMouvement = LocalDateTime.now();
    }

    public void debiterVente(long quantite) {
        if (quantite <= 0) throw new IllegalArgumentException("La quantité vendue doit être positive");
        if (quantite > quantiteDisponible) {
            throw new IllegalStateException("Stock mobile insuffisant pour " + codeProduit
                + " : disponible=" + quantiteDisponible + ", demandé=" + quantite);
        }
        this.quantiteDisponible -= quantite;
        this.quantiteVendue += quantite;
        this.dateDernierMouvement = LocalDateTime.now();
    }

    public void debiterAvarie(long quantite) {
        if (quantite <= 0) throw new IllegalArgumentException("La quantité d'avarie doit être positive");
        if (quantite > quantiteDisponible) {
            throw new IllegalStateException("Stock mobile insuffisant pour avarie de " + codeProduit);
        }
        this.quantiteDisponible -= quantite;
        this.quantiteAvariee += quantite;
        this.dateDernierMouvement = LocalDateTime.now();
    }

    public void debiterDon(long quantite) {
        if (quantite <= 0) throw new IllegalArgumentException("La quantité de don doit être positive");
        if (quantite > quantiteDisponible) {
            throw new IllegalStateException("Stock mobile insuffisant pour don de " + codeProduit);
        }
        this.quantiteDisponible -= quantite;
        this.quantiteDonnee += quantite;
        this.dateDernierMouvement = LocalDateTime.now();
    }

    public long getQuantiteRestante() {
        return quantiteDisponible;
    }

    public boolean estEnSurstock(long seuilSurstock) {
        return quantiteDisponible > seuilSurstock;
    }

    public Long getId() { return id; }
    public String getCodeProduit() { return codeProduit; }
    public String getDesignation() { return designation; }
    public String getConditionnement() { return conditionnement; }
    public long getQuantiteDisponible() { return quantiteDisponible; }
    public long getQuantiteInitiale() { return quantiteInitiale; }
    public long getQuantiteVendue() { return quantiteVendue; }
    public long getQuantiteAvariee() { return quantiteAvariee; }
    public long getQuantiteDonnee() { return quantiteDonnee; }
    public LocalDateTime getDateDernierMouvement() { return dateDernierMouvement; }
}
