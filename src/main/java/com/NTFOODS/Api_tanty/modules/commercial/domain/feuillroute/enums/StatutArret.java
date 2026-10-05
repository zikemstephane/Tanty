package com.NTFOODS.Api_tanty.modules.commercial.domain.feuillroute.enums;

/**
 * État d'un arrêt de tournée.
 *
 * <p>Le partage porte sur le <strong>passage</strong>, et non sur la vente : le
 * commercial constate qu'il est passé chez le client, ou qu'il n'a pas pu. Une
 * visite sans commande reste une visite.</p>
 *
 * <p>Les valeurs s'appelaient auparavant {@code VISE} et {@code NON_VISE}, et
 * {@code VISE} exigeait un numéro de vente. Conserver ces mots avec un sens neuf
 * aurait laissé deux lectures possibles d'une même colonne.</p>
 */
public enum StatutArret {

    /** Arrêt inscrit à la tournée, pas encore tranché. */
    PLANIFIE,

    /** Le commercial est passé chez le client. */
    VISITE,

    /** Le commercial n'a pas pu passer ; le motif est porté par la saisie InfoClient. */
    NON_VISITE
}
