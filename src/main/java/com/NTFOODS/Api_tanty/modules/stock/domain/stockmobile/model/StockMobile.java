package com.NTFOODS.Api_tanty.modules.stock.domain.stockmobile.model;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.*;

/**
 * Agrégat Racine — Stock Mobile d'un commercial.
 *
 * Suit en temps réel les quantités de produits disponibles chez un commercial.
 * Les restes non vendus restent dans le stock mobile (pas de retour au stock central).
 * Le gestionnaire de stock consulte et gère ces stocks dans le temps et en temps réel.
 */
public class StockMobile {

    private Long id;
    private final String matriculeCommercial;
    private final Map<String, LigneStockMobile> lignes;
    private LocalDate dateSession;
    private LocalDateTime dateCreation;
    private LocalDateTime dateModification;
    private Long version;

    public StockMobile(String matriculeCommercial, LocalDate dateSession) {
        this.matriculeCommercial = Objects.requireNonNull(matriculeCommercial);
        this.dateSession = Objects.requireNonNull(dateSession);
        this.lignes = new LinkedHashMap<>();
        this.dateCreation = LocalDateTime.now();
        this.version = 0L;
    }

    public static StockMobile reconstituer(Long id, String matriculeCommercial, LocalDate dateSession,
                                            List<LigneStockMobile> lignesList,
                                            LocalDateTime dateCreation, LocalDateTime dateModification, Long version) {
        StockMobile sm = new StockMobile(matriculeCommercial, dateSession);
        sm.id = id;
        for (LigneStockMobile l : lignesList) {
            sm.lignes.put(l.getCodeProduit(), l);
        }
        sm.dateCreation = dateCreation;
        sm.dateModification = dateModification;
        sm.version = version;
        return sm;
    }

    public void recevoirDotation(String codeProduit, String designation, String conditionnement, long quantite) {
        LigneStockMobile ligne = lignes.get(codeProduit);
        if (ligne == null) {
            ligne = new LigneStockMobile(codeProduit, designation, conditionnement, 0);
            lignes.put(codeProduit, ligne);
        }
        ligne.crediterDotation(quantite);
        this.dateModification = LocalDateTime.now();
    }

    public void enregistrerVente(String codeProduit, long quantite) {
        LigneStockMobile ligne = lignes.get(codeProduit);
        if (ligne == null) {
            throw new IllegalStateException("Produit " + codeProduit + " absent du stock mobile du commercial");
        }
        ligne.debiterVente(quantite);
        this.dateModification = LocalDateTime.now();
    }

    public void enregistrerAvarie(String codeProduit, long quantite) {
        LigneStockMobile ligne = lignes.get(codeProduit);
        if (ligne == null) {
            throw new IllegalStateException("Produit " + codeProduit + " absent du stock mobile du commercial");
        }
        ligne.debiterAvarie(quantite);
        this.dateModification = LocalDateTime.now();
    }

    public void enregistrerDon(String codeProduit, long quantite) {
        LigneStockMobile ligne = lignes.get(codeProduit);
        if (ligne == null) {
            throw new IllegalStateException("Produit " + codeProduit + " absent du stock mobile du commercial");
        }
        ligne.debiterDon(quantite);
        this.dateModification = LocalDateTime.now();
    }

    public long quantiteDisponible(String codeProduit) {
        LigneStockMobile ligne = lignes.get(codeProduit);
        return ligne != null ? ligne.getQuantiteDisponible() : 0;
    }

    public boolean peutRecevoirDotation(String codeProduit, long quantiteDemandee, long seuilSurstock) {
        long disponible = quantiteDisponible(codeProduit);
        return (disponible + quantiteDemandee) <= seuilSurstock;
    }

    public List<LigneStockMobile> getLignes() {
        return Collections.unmodifiableList(new ArrayList<>(lignes.values()));
    }

    public Optional<LigneStockMobile> trouverLigne(String codeProduit) {
        return Optional.ofNullable(lignes.get(codeProduit));
    }

    public long totalVentes() {
        return lignes.values().stream().mapToLong(LigneStockMobile::getQuantiteVendue).sum();
    }

    public long totalRestant() {
        return lignes.values().stream().mapToLong(LigneStockMobile::getQuantiteDisponible).sum();
    }

    public long totalAvaries() {
        return lignes.values().stream().mapToLong(LigneStockMobile::getQuantiteAvariee).sum();
    }

    public Long getId() { return id; }
    public String getMatriculeCommercial() { return matriculeCommercial; }
    public LocalDate getDateSession() { return dateSession; }
    public LocalDateTime getDateCreation() { return dateCreation; }
    public LocalDateTime getDateModification() { return dateModification; }
    public Long getVersion() { return version; }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof StockMobile that)) return false;
        return Objects.equals(matriculeCommercial, that.matriculeCommercial)
            && Objects.equals(dateSession, that.dateSession);
    }

    @Override
    public int hashCode() { return Objects.hash(matriculeCommercial, dateSession); }
}
