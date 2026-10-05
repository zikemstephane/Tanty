package com.NTFOODS.Api_tanty.modules.production.domain.affectation.entity;

/**
 * Détails optionnels de saisie d'une réalisation, selon le poste de production.
 *
 * Champs utilisés :
 * - nbRegimesEpluches / nbRegimesDecoupes : Chips plantain
 * - nbSceauxEtiquetes : Étiquetage
 * - nbEtiquettesDatees / nbSachetsDates : Datage
 * - nbCartonsProduits : Conditionnement (5 lignes)
 * - kgChipsProduits : Chips plantain (découpe/friture)
 */
public class DetailsRealisationPoste {

    private final Integer nbRegimesEpluches;
    private final Integer nbRegimesDecoupes;
    private final Integer nbSceauxEtiquetes;
    private final Integer nbEtiquettesDatees;
    private final Integer nbSachetsDates;
    private final Integer nbCartonsProduits;
    private final Double kgChipsProduits;

    public static DetailsRealisationPoste vide() {
        return new DetailsRealisationPoste(null, null, null, null, null, null, null);
    }

    public DetailsRealisationPoste(Integer nbRegimesEpluches, Integer nbRegimesDecoupes,
                                    Integer nbSceauxEtiquetes, Integer nbEtiquettesDatees,
                                    Integer nbSachetsDates, Integer nbCartonsProduits,
                                    Double kgChipsProduits) {
        this.nbRegimesEpluches = nbRegimesEpluches;
        this.nbRegimesDecoupes = nbRegimesDecoupes;
        this.nbSceauxEtiquetes = nbSceauxEtiquetes;
        this.nbEtiquettesDatees = nbEtiquettesDatees;
        this.nbSachetsDates = nbSachetsDates;
        this.nbCartonsProduits = nbCartonsProduits;
        this.kgChipsProduits = kgChipsProduits;
    }

    public Integer getNbRegimesEpluches() { return nbRegimesEpluches; }
    public Integer getNbRegimesDecoupes() { return nbRegimesDecoupes; }
    public Integer getNbSceauxEtiquetes() { return nbSceauxEtiquetes; }
    public Integer getNbEtiquettesDatees() { return nbEtiquettesDatees; }
    public Integer getNbSachetsDates() { return nbSachetsDates; }
    public Integer getNbCartonsProduits() { return nbCartonsProduits; }
    public Double getKgChipsProduits() { return kgChipsProduits; }
}
