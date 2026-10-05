package com.NTFOODS.Api_tanty.modules.production.domain.lot.aggregate;

import com.NTFOODS.Api_tanty.modules.production.domain.lot.enums.StatutLot;
import com.NTFOODS.Api_tanty.modules.production.domain.lot.events.LotDeclareEvent;
import com.NTFOODS.Api_tanty.modules.production.domain.lot.events.LotValideParStockEvent;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.*;

/**
 * Agrégat Racine — Lot Produit Fini.
 *
 * Déclaré par le Chef de Production, validé physiquement par le Gestionnaire de Stock.
 * Alimente le stock central (produits finis) à la validation.
 *
 * Numérotation : LOT-YYYYMMDD-NNN (ex: LOT-20260625-001)
 */
public class LotProduitFini {

    private Long id;
    private final String numeroLot;
    private String referencePPH;
    private final String productSku;
    private final String productName;
    private String productUnit;
    private final double declaredQuantityKg;
    private final int equivalentUnits;
    private final LocalDate dateProduction;
    private LocalDate dlc;
    private final String matriculeChefProduction;
    private String declaredBy;
    private StatutLot statut;
    private String matriculeGestionnaireValidation;
    private String motifRejet;
    private String conditioningType;
    private Integer conditioningQty;
    private String notes;
    private final LocalDateTime dateCreation;
    private LocalDateTime dateModification;
    private LocalDateTime validatedAt;
    private Long version;

    private final List<Object> domainEvents = new ArrayList<>();

    private LotProduitFini(String numeroLot, String referencePPH, String productSku,
                            String productName, String productUnit, double declaredQuantityKg,
                            int equivalentUnits, LocalDate dateProduction, LocalDate dlc,
                            String matriculeChefProduction, String conditioningType,
                            Integer conditioningQty, String notes, String declaredBy,
                            LocalDateTime dateCreation) {
        this.numeroLot = Objects.requireNonNull(numeroLot);
        this.referencePPH = referencePPH;
        this.productSku = Objects.requireNonNull(productSku);
        this.productName = Objects.requireNonNull(productName);
        this.productUnit = productUnit;
        if (declaredQuantityKg <= 0) throw new IllegalArgumentException("La quantité doit être positive");
        this.declaredQuantityKg = declaredQuantityKg;
        this.equivalentUnits = equivalentUnits;
        this.dateProduction = Objects.requireNonNull(dateProduction);
        this.dlc = dlc;
        this.matriculeChefProduction = matriculeChefProduction;
        this.conditioningType = conditioningType;
        this.conditioningQty = conditioningQty;
        this.notes = notes;
        this.declaredBy = declaredBy;
        this.statut = StatutLot.DECLARE;
        this.dateCreation = dateCreation;
        this.version = 0L;
    }

    public static LotProduitFini declarer(String numeroLot, String referencePPH, String productSku,
                                           String productName, String productUnit, double declaredQuantityKg,
                                           int equivalentUnits, LocalDate dateProduction, LocalDate dlc,
                                           String matriculeChefProduction, String conditioningType,
                                           Integer conditioningQty, String notes, String declaredBy) {
        LocalDate dateFabrication = appliquerRegleDatage(dateProduction);
        LotProduitFini lot = new LotProduitFini(
            numeroLot, referencePPH, productSku, productName, productUnit, declaredQuantityKg,
            equivalentUnits, dateFabrication, dlc, matriculeChefProduction, conditioningType,
            conditioningQty, notes, declaredBy, LocalDateTime.now()
        );
        lot.domainEvents.add(new LotDeclareEvent(
            numeroLot, productSku, declaredQuantityKg, equivalentUnits, dateFabrication, dlc,
            matriculeChefProduction, conditioningType, conditioningQty
        ));
        return lot;
    }

    /**
     * Règle du 1er/15 : la date de fabrication est arrondie au 1er du mois
     * si la production a lieu avant le 15, sinon au 15 du mois.
     */
    static LocalDate appliquerRegleDatage(LocalDate dateProduction) {
        int jour = dateProduction.getDayOfMonth();
        if (jour < 15) {
            return dateProduction.withDayOfMonth(1);
        } else {
            return dateProduction.withDayOfMonth(15);
        }
    }

    /**
     * Ajuste la DLC en fonction du décalage de datage.
     * La DLC est recalculée pour rester cohérente avec la date de fabrication ajustée.
     */
    static LocalDate ajusterDLC(LocalDate dateFabrication, LocalDate dlcOriginale, LocalDate dateProductionOriginale) {
        long decalageJours = java.time.temporal.ChronoUnit.DAYS.between(dateProductionOriginale, dateFabrication);
        return dlcOriginale.plusDays(decalageJours);
    }

    public static LotProduitFini reconstituer(Long id, String numeroLot, String referencePPH,
                                               String productSku, String productName, String productUnit,
                                               double declaredQuantityKg, int equivalentUnits,
                                               LocalDate dateProduction, LocalDate dlc,
                                               String matriculeChefProduction, String declaredBy,
                                               StatutLot statut, String matriculeGestionnaireValidation,
                                               String motifRejet, String conditioningType, Integer conditioningQty,
                                               String notes, LocalDateTime dateCreation,
                                               LocalDateTime dateModification, LocalDateTime validatedAt,
                                               Long version) {
        LotProduitFini lot = new LotProduitFini(
            numeroLot, referencePPH, productSku, productName, productUnit, declaredQuantityKg,
            equivalentUnits, dateProduction, dlc, matriculeChefProduction, conditioningType,
            conditioningQty, notes, declaredBy, dateCreation
        );
        lot.id = id;
        lot.statut = statut;
        lot.matriculeGestionnaireValidation = matriculeGestionnaireValidation;
        lot.motifRejet = motifRejet;
        lot.dateModification = dateModification;
        lot.validatedAt = validatedAt;
        lot.version = version;
        return lot;
    }

    public void validerParStock(String matriculeGestionnaire) {
        if (statut != StatutLot.DECLARE) {
            throw new IllegalStateException("Seul un lot DECLARE peut être validé");
        }
        this.statut = StatutLot.VALIDATED_BY_STOCK;
        this.matriculeGestionnaireValidation = Objects.requireNonNull(matriculeGestionnaire);
        this.dateModification = LocalDateTime.now();
        this.validatedAt = this.dateModification;
        domainEvents.add(new LotValideParStockEvent(
            numeroLot, referencePPH, productSku, declaredQuantityKg, equivalentUnits, dateProduction, matriculeGestionnaire
        ));
    }

    public void rejeter(String motif, String matriculeGestionnaire) {
        if (statut != StatutLot.DECLARE) {
            throw new IllegalStateException("Seul un lot DECLARE peut être rejeté");
        }
        this.statut = StatutLot.REJETE;
        this.motifRejet = Objects.requireNonNull(motif);
        this.matriculeGestionnaireValidation = matriculeGestionnaire;
        this.dateModification = LocalDateTime.now();
    }

    public List<Object> getDomainEvents() { return Collections.unmodifiableList(domainEvents); }
    public void clearDomainEvents() { domainEvents.clear(); }

    public Long getId() { return id; }
    public String getNumeroLot() { return numeroLot; }
    public String getReferencePPH() { return referencePPH; }
    public String getProductSku() { return productSku; }
    public String getProductName() { return productName; }
    public String getProductUnit() { return productUnit; }
    public double getDeclaredQuantityKg() { return declaredQuantityKg; }
    public int getEquivalentUnits() { return equivalentUnits; }
    public LocalDate getDateProduction() { return dateProduction; }
    public LocalDate getDlc() { return dlc; }
    public String getMatriculeChefProduction() { return matriculeChefProduction; }
    public String getDeclaredBy() { return declaredBy; }
    public StatutLot getStatut() { return statut; }
    public String getMatriculeGestionnaireValidation() { return matriculeGestionnaireValidation; }
    public String getMotifRejet() { return motifRejet; }
    public String getConditioningType() { return conditioningType; }
    public Integer getConditioningQty() { return conditioningQty; }
    public String getNotes() { return notes; }
    public LocalDateTime getDateCreation() { return dateCreation; }
    public LocalDateTime getDateModification() { return dateModification; }
    public LocalDateTime getValidatedAt() { return validatedAt; }
    public Long getVersion() { return version; }

    /**
     * Lot validé par le stock dont la validation tombe entre les deux dates (incluses).
     * C'est la mesure de la production d'une période : on compte ce qui a été réellement
     * validé, et non la date de production, qui est ramenée au 1er ou au 15 du mois.
     */
    public boolean estValidePeriode(LocalDate debut, LocalDate fin) {
        if (statut != StatutLot.VALIDATED_BY_STOCK || validatedAt == null) return false;
        LocalDate jour = validatedAt.toLocalDate();
        return !jour.isBefore(debut) && !jour.isAfter(fin);
    }

    @Override public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof LotProduitFini)) return false;
        return Objects.equals(numeroLot, ((LotProduitFini) o).numeroLot);
    }
    @Override public int hashCode() { return Objects.hash(numeroLot); }
    @Override public String toString() {
        return "LotProduitFini{" + numeroLot + ", " + productSku + ", " + declaredQuantityKg + "kg, " + statut + "}";
    }
}
