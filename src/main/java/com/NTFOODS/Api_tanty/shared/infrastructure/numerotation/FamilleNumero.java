package com.NTFOODS.Api_tanty.shared.infrastructure.numerotation;

/**
 * Familles de numéros métier disposant d'une séquence PostgreSQL dédiée.
 *
 * <p>Une famille = un préfixe fonctionnel + une séquence. Deux familles ne
 * partagent jamais la même séquence, afin qu'une forte volumétrie sur l'une
 * n'entraîne pas de sauts de numérotation sur l'autre.</p>
 */
public enum FamilleNumero {

    VENTE("VTE", "seq_numero_vente"),
    PRECOMMANDE("PCM", "seq_numero_precommande"),
    CONSTAT_INVENDUS("BRET", "seq_numero_retour"),
    TRANSACTION_NON_MARCHANDE("NM", "seq_numero_non_marchand"),
    MOUVEMENT_STOCK("MVT", "seq_numero_mouvement"),
    FEUILLE_ROUTE("FR", "seq_numero_feuille_route");

    private final String prefixe;
    private final String nomSequence;

    FamilleNumero(String prefixe, String nomSequence) {
        this.prefixe = prefixe;
        this.nomSequence = nomSequence;
    }

    public String getPrefixe() {
        return prefixe;
    }

    public String getNomSequence() {
        return nomSequence;
    }
}
