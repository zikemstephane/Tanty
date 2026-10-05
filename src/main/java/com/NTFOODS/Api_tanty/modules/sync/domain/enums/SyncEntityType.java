package com.NTFOODS.Api_tanty.modules.sync.domain.enums;

/**
 * Nature de l'entité rejouée par la synchronisation.
 *
 * <p>{@code TRANSACTION_NON_MARCHANDE} couvre avaries, dons, dotations et
 * échanges : ils empruntent un autre chemin que la vente marchande — ils font
 * varier le stock sans rien encaisser — et ne peuvent donc pas être rejoués par
 * la même logique.</p>
 */
public enum SyncEntityType {
    VENTE,
    TRANSACTION_NON_MARCHANDE,
    PRECOMMANDE,
    FEUILL_ROUTE,
    ARRET,
    CLOTURE_JOURNEE
}
