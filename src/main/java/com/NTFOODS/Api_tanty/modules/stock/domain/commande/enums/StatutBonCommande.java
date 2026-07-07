package com.NTFOODS.Api_tanty.modules.stock.domain.commande.enums;

/**
 * Cycle de vie d'un Bon de Commande fournisseur.
 *
 * BROUILLON → VALIDE → ENVOYE → RECU_PARTIEL → RECU_TOTAL → CLOTURE
 * Annulation possible tant que non envoyé/reçu.
 */
public enum StatutBonCommande {

    BROUILLON("Brouillon"),
    VALIDE("Validé"),
    ENVOYE("Envoyé au fournisseur"),
    RECU_PARTIEL("Reçu partiellement"),
    RECU_TOTAL("Reçu en totalité"),
    CLOTURE("Clôturé"),
    ANNULE("Annulé");

    private final String libelle;

    StatutBonCommande(String libelle) {
        this.libelle = libelle;
    }

    public String getLibelle() { return libelle; }

    public boolean peutTransitionnerVers(StatutBonCommande cible) {
        return switch (this) {
            case BROUILLON -> cible == VALIDE || cible == ANNULE;
            case VALIDE -> cible == ENVOYE || cible == ANNULE;
            case ENVOYE -> cible == RECU_PARTIEL || cible == RECU_TOTAL || cible == ANNULE;
            case RECU_PARTIEL -> cible == RECU_PARTIEL || cible == RECU_TOTAL;
            case RECU_TOTAL -> cible == CLOTURE;
            case CLOTURE, ANNULE -> false;
        };
    }

    public boolean estModifiable() {
        return this == BROUILLON;
    }

    public boolean estTerminal() {
        return this == CLOTURE || this == ANNULE;
    }
}
