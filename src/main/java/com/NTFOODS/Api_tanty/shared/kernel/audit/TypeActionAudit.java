package com.NTFOODS.Api_tanty.shared.kernel.audit;

/**
 * Nature d'une action tracée dans le Journal d'Audit (Pilier 1 — Immuabilité).
 *
 * Chaque action, impression ou tentative de modification effectuée sur le
 * système doit être tracée numériquement (qui, quoi, quand) pour permettre
 * des audits futurs, quel que soit le module concerné.
 */
public enum TypeActionAudit {
    CONSULTATION,
    CREATION,
    MODIFICATION,
    VALIDATION,
    SUPPRESSION_TENTATIVE,
    ANNULATION,
    IMPRESSION_EXPORT,
    CONNEXION,
    ECHEC_CONNEXION,
    DEBLOCAGE_PRIX,
    AUTRE
}
