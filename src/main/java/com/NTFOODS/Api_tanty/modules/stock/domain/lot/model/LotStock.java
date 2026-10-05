package com.NTFOODS.Api_tanty.modules.stock.domain.lot.model;

import com.NTFOODS.Api_tanty.modules.stock.domain.lot.enums.StatutLot;
import com.NTFOODS.Api_tanty.shared.kernel.exception.RegleMetierException;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.Objects;

/**
 * Agrégat Racine — Lot de stock (traçabilité et péremption).
 *
 * <h3>Ce qu'un lot est, et ce qu'il n'est pas</h3>
 * Un lot dit <em>de quel arrivage</em> proviennent des marchandises et
 * <em>jusqu'à quand</em> elles sont consommables. Il ne dit pas combien
 * l'entreprise possède : cette autorité appartient à {@code niveaux_stock},
 * alimenté par les mouvements de stock.
 *
 * Autrement dit, le lot est une couche de traçabilité posée sur le stock
 * existant, pas un second stock. Créer un lot ne crédite rien ; en consommer
 * ne débite rien. C'est le mouvement de stock qui fait varier les quantités,
 * et le lot qui retient d'où elles venaient.
 *
 * <h3>Sortie au plus proche de la péremption (FEFO)</h3>
 * Quand plusieurs lots d'un même produit coexistent au même endroit, celui
 * qui périme le premier sort le premier — à défaut de date de péremption, le
 * plus ancien. Cette règle est appliquée par le use case de consommation, qui
 * seul voit l'ensemble des lots.
 */
public class LotStock {

    private Long id;
    private final String numeroLot;
    private final String codeProduit;
    private final String sku;
    private final String designation;
    private final String codeFournisseur;
    private final String codeEntrepot;
    private final LocalDate dateFabrication;
    private final LocalDate datePeremption;
    private final long quantiteInitiale;
    private long quantiteRestante;
    private StatutLot statut;
    private String observations;
    private final LocalDateTime dateCreation;
    private LocalDateTime dateDernierMouvement;
    private Long version;

    private LotStock(String numeroLot, String codeProduit, String sku, String designation,
                     String codeFournisseur, String codeEntrepot, LocalDate dateFabrication,
                     LocalDate datePeremption, long quantiteInitiale, String observations,
                     LocalDateTime dateCreation) {
        this.numeroLot = Objects.requireNonNull(numeroLot, "Le numéro de lot est obligatoire");
        this.codeProduit = Objects.requireNonNull(codeProduit, "Le produit est obligatoire");
        this.sku = sku;
        this.designation = designation;
        this.codeFournisseur = codeFournisseur;
        this.codeEntrepot = Objects.requireNonNull(codeEntrepot, "L'entrepôt est obligatoire");
        this.dateFabrication = dateFabrication;
        this.datePeremption = datePeremption;
        this.quantiteInitiale = quantiteInitiale;
        this.quantiteRestante = quantiteInitiale;
        this.observations = observations;
        this.dateCreation = dateCreation;
        this.version = 0L;
    }

    public static LotStock creer(String numeroLot, String codeProduit, String sku, String designation,
                                 String codeFournisseur, String codeEntrepot, LocalDate dateFabrication,
                                 LocalDate datePeremption, long quantiteInitiale, String observations) {
        if (quantiteInitiale <= 0) {
            throw new RegleMetierException("La quantité d'un lot doit être strictement positive : " + quantiteInitiale);
        }
        if (dateFabrication != null && datePeremption != null && datePeremption.isBefore(dateFabrication)) {
            throw new RegleMetierException(
                "La date de péremption (" + datePeremption + ") précède la date de fabrication (" + dateFabrication + ")");
        }
        LotStock lot = new LotStock(numeroLot, codeProduit, sku, designation, codeFournisseur, codeEntrepot,
            dateFabrication, datePeremption, quantiteInitiale, observations, LocalDateTime.now());
        // Un lot déjà périmé à sa création ne se déclare pas disponible : c'est
        // le cas d'un arrivage dont la date a été mal saisie, ou d'une reprise
        // d'historique. Il entre périmé, et cela se voit.
        lot.statut = lot.estPerime() ? StatutLot.PERIME : StatutLot.DISPONIBLE;
        return lot;
    }

    public static LotStock reconstituer(Long id, String numeroLot, String codeProduit, String sku, String designation,
                                        String codeFournisseur, String codeEntrepot, LocalDate dateFabrication,
                                        LocalDate datePeremption, long quantiteInitiale, long quantiteRestante,
                                        StatutLot statut, String observations, LocalDateTime dateCreation,
                                        LocalDateTime dateDernierMouvement, Long version) {
        LotStock lot = new LotStock(numeroLot, codeProduit, sku, designation, codeFournisseur, codeEntrepot,
            dateFabrication, datePeremption, quantiteInitiale, observations, dateCreation);
        lot.id = id;
        lot.quantiteRestante = quantiteRestante;
        lot.statut = statut;
        lot.dateDernierMouvement = dateDernierMouvement;
        lot.version = version;
        return lot;
    }

    /**
     * Un lot est périmé le jour même de sa date de péremption, pas le lendemain.
     * En agroalimentaire, la date portée sur l'emballage est le dernier jour de
     * validité inclus ; consommer ce jour-là est déjà hors délai pour un stock
     * destiné à la revente.
     */
    public boolean estPerime() {
        return datePeremption != null && !LocalDate.now().isBefore(datePeremption);
    }

    /** Périme dans les {@code jours} à venir, sans l'être déjà. */
    public boolean perimeBientot(int jours) {
        if (datePeremption == null || estPerime()) return false;
        return !datePeremption.isAfter(LocalDate.now().plusDays(jours));
    }

    /** Jours restants avant péremption ; négatif si la date est passée, null sans date. */
    public Long joursAvantPeremption() {
        if (datePeremption == null) return null;
        return ChronoUnit.DAYS.between(LocalDate.now(), datePeremption);
    }

    public void consommer(long quantite) {
        if (quantite <= 0) {
            throw new RegleMetierException("La quantité consommée doit être strictement positive : " + quantite);
        }
        if (!statut.permetConsommation()) {
            throw new RegleMetierException(
                "Le lot " + numeroLot + " est " + statut.getLibelle().toLowerCase() + " et ne peut pas être consommé");
        }
        if (estPerime()) {
            throw new RegleMetierException(
                "Le lot " + numeroLot + " est périmé depuis le " + datePeremption + " et ne peut pas être consommé");
        }
        if (quantite > quantiteRestante) {
            throw new RegleMetierException(
                "Le lot " + numeroLot + " ne contient que " + quantiteRestante + " unité(s), " + quantite + " demandée(s)");
        }
        this.quantiteRestante -= quantite;
        this.dateDernierMouvement = LocalDateTime.now();
        if (quantiteRestante == 0) {
            this.statut = StatutLot.EPUISE;
        }
    }

    /** Marque le lot périmé. Sans effet s'il l'est déjà, ou s'il est épuisé ou retiré. */
    public void marquerPerime() {
        if (statut == StatutLot.EPUISE || statut == StatutLot.RETIRE || statut == StatutLot.PERIME) return;
        this.statut = StatutLot.PERIME;
        this.dateDernierMouvement = LocalDateTime.now();
    }

    public void changerStatut(StatutLot nouveau) {
        Objects.requireNonNull(nouveau, "Le statut est obligatoire");
        if (statut == StatutLot.RETIRE) {
            throw new RegleMetierException("Le lot " + numeroLot + " a été retiré ; son statut ne change plus");
        }
        if (nouveau == StatutLot.EPUISE && quantiteRestante > 0) {
            throw new RegleMetierException(
                "Le lot " + numeroLot + " porte encore " + quantiteRestante + " unité(s) : il ne peut être déclaré épuisé");
        }
        this.statut = nouveau;
        this.dateDernierMouvement = LocalDateTime.now();
    }

    /**
     * Retire le lot du suivi sans l'effacer. Refusé tant qu'il reste de la
     * marchandise : retirer un lot non vide reviendrait à faire disparaître de
     * la traçabilité des produits qui sont encore dans l'entrepôt.
     */
    public void retirer() {
        if (quantiteRestante > 0) {
            throw new RegleMetierException(
                "Le lot " + numeroLot + " contient encore " + quantiteRestante + " unité(s) et ne peut être retiré");
        }
        this.statut = StatutLot.RETIRE;
        this.dateDernierMouvement = LocalDateTime.now();
    }

    public void modifierObservations(String observations) {
        this.observations = observations;
        this.dateDernierMouvement = LocalDateTime.now();
    }

    public Long getId() { return id; }
    public String getNumeroLot() { return numeroLot; }
    public String getCodeProduit() { return codeProduit; }
    public String getSku() { return sku; }
    public String getDesignation() { return designation; }
    public String getCodeFournisseur() { return codeFournisseur; }
    public String getCodeEntrepot() { return codeEntrepot; }
    public LocalDate getDateFabrication() { return dateFabrication; }
    public LocalDate getDatePeremption() { return datePeremption; }
    public long getQuantiteInitiale() { return quantiteInitiale; }
    public long getQuantiteRestante() { return quantiteRestante; }
    public StatutLot getStatut() { return statut; }
    public String getObservations() { return observations; }
    public LocalDateTime getDateCreation() { return dateCreation; }
    public LocalDateTime getDateDernierMouvement() { return dateDernierMouvement; }
    public Long getVersion() { return version; }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof LotStock that)) return false;
        return Objects.equals(numeroLot, that.numeroLot) && Objects.equals(codeEntrepot, that.codeEntrepot);
    }

    @Override
    public int hashCode() { return Objects.hash(numeroLot, codeEntrepot); }
}
