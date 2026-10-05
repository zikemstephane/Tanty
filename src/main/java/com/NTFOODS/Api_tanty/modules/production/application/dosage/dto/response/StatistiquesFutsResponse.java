package com.NTFOODS.Api_tanty.modules.production.application.dosage.dto.response;

import java.time.LocalDate;
import java.util.List;

/**
 * Statistiques de fûts produits sur une période, avec comparaison N-1.
 *
 * Les quantités de matières premières sont génériques (une entrée par nom de
 * matière réellement rencontrée) et non plus figées à maïs/soja/arachide :
 * elles combinent les saisies réelles de l'Agent Doseur et, pour les sessions
 * sans saisie réelle, une estimation via la {@link com.NTFOODS.Api_tanty.modules.production.domain.formule.aggregate.FormuleProduction}
 * des produits du bon de commande — uniquement quand un unique jeu de
 * proportions peut être déterminé sans ambiguïté pour la session ;
 * {@link #nbSessionsNonEstimees()} signale les sessions exclues de cette
 * estimation (aucune formule ou plusieurs formules différentes en jeu),
 * plutôt que de leur appliquer silencieusement une recette par défaut.
 */
public record StatistiquesFutsResponse(
    LocalDate debut,
    LocalDate fin,
    String periodeLibelle,
    int nbFutsProduits,
    int nbFutsNets,
    List<MatierePremiereTotal> matieresPremieres,
    int nbSessionsNonEstimees,
    ComparaisonPeriode comparaison,
    int nbSessions
) {
    public record MatierePremiereTotal(String matierePremiere, double quantiteKg) {}

    public record ComparaisonPeriode(
        LocalDate debutPeriodePrecedente,
        LocalDate finPeriodePrecedente,
        int nbFutsNetsPeriodePrecedente,
        double variationPourcentage
    ) {}
}
