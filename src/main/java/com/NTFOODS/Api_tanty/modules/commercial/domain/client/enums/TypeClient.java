package com.NTFOODS.Api_tanty.modules.commercial.domain.client.enums;

/**
 * Segment commercial d'un client.
 *
 * <p>La valeur {@code PROSPECT} a été retirée avec la prospection, sortie du
 * périmètre du projet. Les deux clients qui la portaient ont été requalifiés en
 * {@code DETAIL} par la migration V4 — c'est le repli déjà appliqué par le code
 * à un segment non reconnu, et l'un et l'autre sont de petits commerces.</p>
 */
public enum TypeClient {
    DISTRIBUTEUR,
    DETAIL,
    GROSSISTE,
    SEMI_GROSSISTE,
    REVENDEUR
}
