package com.NTFOODS.Api_tanty.modules.commercial.domain.vente.entity;

import java.util.Objects;

/**
 * Ligne d'une vente terrain.
 *
 * <p>La remise est un <strong>pourcentage</strong> de 0 à 100 portant sur la
 * ligne entière. Elle était auparavant un montant en FCFA ; le commanditaire a
 * arrêté le pourcentage le 18/08/2026, et la migration {@code V13} a converti
 * les lignes existantes.</p>
 *
 * <p>Ce choix aligne la vente sur la facture : {@code LigneFacture} exprimait
 * déjà sa remise en pourcentage. Deux lignes décrivant la même marchandise
 * portaient donc, dans le même système, un champ de même nom et de sens
 * opposé.</p>
 */
public class LigneVente {

    private final String codeProduit;
    private final String designationProduit;
    private final String conditionnement;
    private final int quantite;
    private final double prixUnitaire;
    private double remise;

    public LigneVente(String codeProduit, String designationProduit, String conditionnement,
                      int quantite, double prixUnitaire, double remise) {
        this.codeProduit = Objects.requireNonNull(codeProduit);
        this.designationProduit = Objects.requireNonNull(designationProduit);
        this.conditionnement = Objects.requireNonNull(conditionnement);
        if (quantite <= 0) throw new IllegalArgumentException("La quantité doit être positive");
        this.quantite = quantite;
        if (prixUnitaire < 0) throw new IllegalArgumentException("Le prix ne peut pas être négatif");
        this.prixUnitaire = prixUnitaire;
        // Une valeur hors bornes est REFUSÉE, jamais ramenée à zéro : une remise
        // silencieusement annulée fait payer au client un prix qu'on ne lui a pas
        // annoncé, et personne ne s'en aperçoit avant la facture.
        if (remise < 0) {
            throw new IllegalArgumentException("La remise ne peut pas être négative : " + remise + " %");
        }
        if (remise > 100) {
            throw new IllegalArgumentException(
                "La remise ne peut pas excéder 100 % : " + remise + " % demandé"
                + " (au-delà, la ligne deviendrait un revenu négatif)");
        }
        this.remise = remise;
    }

    /** Montant net de la ligne : brut diminué du pourcentage de remise accordé. */
    public double montantHT() {
        return quantite * prixUnitaire * (1 - remise / 100.0);
    }

    /** Montant avant remise, utile aux états qui distinguent brut et net. */
    public double montantBrut() {
        return quantite * prixUnitaire;
    }

    public String getCodeProduit() { return codeProduit; }
    public String getDesignationProduit() { return designationProduit; }
    public String getConditionnement() { return conditionnement; }
    public int getQuantite() { return quantite; }
    public double getPrixUnitaire() { return prixUnitaire; }
    public double getRemise() { return remise; }
}
