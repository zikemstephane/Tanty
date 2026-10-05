package com.NTFOODS.Api_tanty.modules.stock.domain.catalogue.enums;

/**
 * Type physique d'un élément d'emballage.
 *
 * Sert à la fois à qualifier le conditionnement d'un produit dans son
 * ensemble (ex. un produit vendu en SACHET, un autre en SEAU) et à
 * identifier chaque composant de sa composition d'emballage
 * (ex. un conditionnement de type SACHET peut avoir pour composants
 * SACHET, COUVERCLE et SCELLE, dans des proportions distinctes).
 *
 * Volontairement une liste ouverte : ajouter un nouveau type ne casse rien
 * d'existant (aucun code ne fait de {@code switch} exhaustif dessus), il
 * suffit d'ajouter la constante ici et d'élargir la contrainte CHECK
 * correspondante par migration (cf. StatutOF.EN_PAUSE pour le précédent).
 */
public enum TypeEmballage {
    SACHET,
    COUVERCLE,
    SCELLE,
    CARTON,
    SEAU,
    GAINE,
    ETUI
}
