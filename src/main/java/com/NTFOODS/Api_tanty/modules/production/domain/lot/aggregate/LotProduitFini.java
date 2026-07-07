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
    private final String referencePPH;
    private final String codeProduit;
    private final String designationProduit;
    private final double quantiteKg;
    private final int nbCartons;
    private final LocalDate dateProduction;
    private final LocalDate dlc;
    private final String matriculeChefProduction;
    private StatutLot statut;
    private String matriculeGestionnaireValidation;
    private String motifRejet;
    private final LocalDateTime dateCreation;
    private LocalDateTime dateModification;
    private Long version;

    private final List<Object> domainEvents = new ArrayList<>();

    private LotProduitFini(String numeroLot, String referencePPH, String codeProduit,
                            String designationProduit, double quantiteKg, int nbCartons,
                            LocalDate dateProduction, LocalDate dlc, String matriculeChefProduction,
                            LocalDateTime dateCreation) {
        this.numeroLot = Objects.requireNonNull(numeroLot);
        this.referencePPH = Objects.requireNonNull(referencePPH);
        this.codeProduit = Objects.requireNonNull(codeProduit);
        this.designationProduit = Objects.requireNonNull(designationProduit);
        if (quantiteKg <= 0) throw new IllegalArgumentException("La quantité doit être positive");
        this.quantiteKg = quantiteKg;
        if (nbCartons < 0) throw new IllegalArgumentException("Le nombre de cartons ne peut pas être négatif");
        this.nbCartons = nbCartons;
        this.dateProduction = Objects.requireNonNull(dateProduction);
        this.dlc = Objects.requireNonNull(dlc);
        if (dlc.isBefore(dateProduction)) {
            throw new IllegalArgumentException("La DLC doit être postérieure à la date de production");
        }
        this.matriculeChefProduction = Objects.requireNonNull(matriculeChefProduction);
        this.statut = StatutLot.DECLARE;
        this.dateCreation = dateCreation;
        this.version = 0L;
    }

    public static LotProduitFini declarer(String numeroLot, String referencePPH, String codeProduit,
                                           String designationProduit, double quantiteKg, int nbCartons,
                                           LocalDate dateProduction, LocalDate dlc,
                                           String matriculeChefProduction) {
        LocalDate dateFabrication = appliquerRegleDatage(dateProduction);
        LocalDate dlcAjustee = ajusterDLC(dateFabrication, dlc, dateProduction);
        LotProduitFini lot = new LotProduitFini(
            numeroLot, referencePPH, codeProduit, designationProduit, quantiteKg, nbCartons,
            dateFabrication, dlcAjustee, matriculeChefProduction, LocalDateTime.now()
        );
        lot.domainEvents.add(new LotDeclareEvent(
            numeroLot, codeProduit, quantiteKg, nbCartons, dateFabrication, dlcAjustee, matriculeChefProduction
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
                                               String codeProduit, String designationProduit,
                                               double quantiteKg, int nbCartons, LocalDate dateProduction,
                                               LocalDate dlc, String matriculeChefProduction,
                                               StatutLot statut, String matriculeGestionnaireValidation,
                                               String motifRejet, LocalDateTime dateCreation,
                                               LocalDateTime dateModification, Long version) {
        LotProduitFini lot = new LotProduitFini(
            numeroLot, referencePPH, codeProduit, designationProduit, quantiteKg, nbCartons,
            dateProduction, dlc, matriculeChefProduction, dateCreation
        );
        lot.id = id;
        lot.statut = statut;
        lot.matriculeGestionnaireValidation = matriculeGestionnaireValidation;
        lot.motifRejet = motifRejet;
        lot.dateModification = dateModification;
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
        domainEvents.add(new LotValideParStockEvent(
            numeroLot, referencePPH, codeProduit, quantiteKg, nbCartons, dateProduction, matriculeGestionnaire
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
    public String getCodeProduit() { return codeProduit; }
    public String getDesignationProduit() { return designationProduit; }
    public double getQuantiteKg() { return quantiteKg; }
    public int getNbCartons() { return nbCartons; }
    public LocalDate getDateProduction() { return dateProduction; }
    public LocalDate getDlc() { return dlc; }
    public String getMatriculeChefProduction() { return matriculeChefProduction; }
    public StatutLot getStatut() { return statut; }
    public String getMatriculeGestionnaireValidation() { return matriculeGestionnaireValidation; }
    public String getMotifRejet() { return motifRejet; }
    public LocalDateTime getDateCreation() { return dateCreation; }
    public LocalDateTime getDateModification() { return dateModification; }
    public Long getVersion() { return version; }

    @Override public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof LotProduitFini)) return false;
        return Objects.equals(numeroLot, ((LotProduitFini) o).numeroLot);
    }
    @Override public int hashCode() { return Objects.hash(numeroLot); }
    @Override public String toString() {
        return "LotProduitFini{" + numeroLot + ", " + codeProduit + ", " + quantiteKg + "kg, " + statut + "}";
    }
}
