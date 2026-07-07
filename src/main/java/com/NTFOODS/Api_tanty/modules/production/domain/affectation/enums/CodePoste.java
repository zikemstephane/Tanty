package com.NTFOODS.Api_tanty.modules.production.domain.affectation.enums;

/**
 * Référentiel fixe des 11 postes de production NTFOODS (Responsable de Salle).
 *
 * Chaque poste porte :
 *   - code court
 *   - libellé
 *   - effectif de référence
 *   - production/heure de référence
 *   - production/jour de référence (8 h)
 *   - unité de mesure de la production
 *
 * Règle d'objectif ajusté :
 *   objectifAjuste = (effectifReel / effectifReference) × productionJournaliere × duree.coefficient
 *
 * Ce référentiel est non modifiable ; il n'y a pas d'écran d'édition côté utilisateur.
 */
public enum CodePoste {

    CARAMEL(                            "CAR",  "Caramel",                                  1, 8.2,    65.6,   UniteProduction.KG),
    CROQUETTE(                          "CRQ",  "Croquette",                                1, 3.6,    28.8,   UniteProduction.KG),
    ARACHIDE_ENROBEE(                   "ARA",  "Arachide enrobée",                         1, 6.1,    48.8,   UniteProduction.KG),
    CHIPS_PLANTAIN(                     "CHP",  "Chips plantain",                           2, 7.5,    60.0,   UniteProduction.KG),
    CONDITIONNEMENT_MINI_GRIGNOTER(     "CMG",  "Conditionnement mini à grignoter (Chips)",   3, 30.0,   2400.0, UniteProduction.SACHET),
    CONDITIONNEMENT_GRIGNOTER(          "CGR",  "Conditionnement à grignoter",              2, 200.0,  1600.0, UniteProduction.SACHET),
    CONDITIONNEMENT_SCEAUX_2L(          "CS2",  "Conditionnement sceaux 2L",                4, 100.0,  800.0,  UniteProduction.SCEAU),
    CONDITIONNEMENT_RASS(               "CRASS","Conditionnement RASS",                     2, 625.0,  5000.0, UniteProduction.SACHET),
    CONDITIONNEMENT_PRESTIGE_CHAP(      "CPR",  "Conditionnement prestige/chap.",           2, 50.0,   400.0,  UniteProduction.ETUI),
    ETIQUETAGE(                         "ETQ",  "Étiquetage",                               1, 225.0,  1800.0, UniteProduction.SCEAU),
    DATAGE(                             "DAT",  "Datage",                                   1, 313.0,  2500.0, UniteProduction.ETIQUETTE);

    private final String code;
    private final String libelle;
    private final int effectifReference;
    private final double productionParHeure;
    private final double productionJournaliere;
    private final UniteProduction unite;

    CodePoste(String code, String libelle, int effectifReference,
              double productionParHeure, double productionJournaliere, UniteProduction unite) {
        this.code = code;
        this.libelle = libelle;
        this.effectifReference = effectifReference;
        this.productionParHeure = productionParHeure;
        this.productionJournaliere = productionJournaliere;
        this.unite = unite;
    }

    /**
     * Calcule l'objectif ajusté selon l'effectif réel et la durée d'affectation.
     * Pour les postes sans taux de référence, retourne 0.
     */
    public double calculerObjectifAjuste(int effectifReel, DureeAffectation duree) {
        if (productionJournaliere == 0 || effectifReference == 0) return 0.0;
        return ((double) effectifReel / effectifReference) * productionJournaliere * duree.getCoefficient();
    }

    public String getCode() { return code; }
    public String getLibelle() { return libelle; }
    public int getEffectifReference() { return effectifReference; }
    public double getProductionParHeure() { return productionParHeure; }
    public double getProductionJournaliere() { return productionJournaliere; }
    public UniteProduction getUnite() { return unite; }

    public boolean hasTauxReference() { return productionJournaliere > 0; }
}
