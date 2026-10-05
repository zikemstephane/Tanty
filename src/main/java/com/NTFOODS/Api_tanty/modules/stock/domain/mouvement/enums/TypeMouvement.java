package com.NTFOODS.Api_tanty.modules.stock.domain.mouvement.enums;

/**
 * Type de mouvement de stock — trace toute variation physique des entrepôts.
 */
public enum TypeMouvement {

    ENTREE_RECEPTION_FOURNISSEUR("Réception fournisseur", SensMouvement.ENTREE),
    ENTREE_RECEPTION_PRODUCTION("Réception production (produits finis)", SensMouvement.ENTREE),
    TRANSFERT_CENTRAL_VERS_TAMPON("Transfert central → tampon", SensMouvement.TRANSFERT),
    SORTIE_DOTATION("Sortie dotation commerciale", SensMouvement.SORTIE),
    RETOUR_COMMERCIAL("Retour invendus commercial", SensMouvement.ENTREE),
    AJUSTEMENT_INVENTAIRE("Ajustement d'inventaire", SensMouvement.ENTREE),

    // Transactions non marchandes déclarées depuis le terrain. Elles font varier le
    // stock mobile sans générer d'encaissement.
    SORTIE_AVARIE("Sortie pour avarie", SensMouvement.SORTIE),
    SORTIE_DON("Sortie pour don", SensMouvement.SORTIE),
    ECHANGE_REMISE_CLIENT("Échange — produit remis au client", SensMouvement.SORTIE),
    ECHANGE_REPRISE_CLIENT("Échange — produit repris au client", SensMouvement.ENTREE);

    private final String libelle;
    private final SensMouvement sens;

    TypeMouvement(String libelle, SensMouvement sens) {
        this.libelle = libelle;
        this.sens = sens;
    }

    public String getLibelle() { return libelle; }
    public SensMouvement getSens() { return sens; }
}
