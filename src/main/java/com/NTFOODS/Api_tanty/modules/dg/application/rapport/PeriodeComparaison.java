package com.NTFOODS.Api_tanty.modules.dg.application.rapport;

/**
 * Niveau de comparaison temporelle pour les rapports de pilotage DG.
 * Chaque niveau compare la période "à ce jour" (du début de la période à la
 * date de référence) à la même tranche relative de la période précédente —
 * permet de comparer équitablement un mois ou une année en cours.
 */
public enum PeriodeComparaison {
    JOUR, SEMAINE, MOIS, ANNEE
}
