package com.NTFOODS.Api_tanty.modules.stock.domain.catalogue.enums;

/**
 * Niveau hiérarchique d'un conditionnement.
 *
 * Hiérarchie croissante : UNITE_BASE < INTERMEDIAIRE < CARTON < PALETTE.
 * Le facteur de conversion d'un conditionnement exprime toujours le nombre
 * d'unités de base qu'il contient, ce qui permet de convertir n'importe quel
 * niveau vers n'importe quel autre.
 */
public enum NiveauConditionnement {

    UNITE_BASE("Unité de base", 1),
    INTERMEDIAIRE("Conditionnement intermédiaire", 2),
    CARTON("Carton", 3),
    PALETTE("Palette", 4);

    private final String libelle;
    private final int rang;

    NiveauConditionnement(String libelle, int rang) {
        this.libelle = libelle;
        this.rang = rang;
    }

    public String getLibelle() { return libelle; }
    public int getRang() { return rang; }

    public boolean estUniteBase() {
        return this == UNITE_BASE;
    }
}
