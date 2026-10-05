package com.NTFOODS.Api_tanty.modules.production.application.broyage.dto.response;

import com.NTFOODS.Api_tanty.modules.production.domain.broyage.service.ApercuBroyage;

import java.time.LocalDate;
import java.util.List;

/** Vue d'ensemble de l'atelier de broyage : synthèses, répartition par poudre et série journalière. */
public record ApercuBroyageResponse(
    LocalDate debut,
    LocalDate fin,
    Synthese duJour,
    Synthese periode,
    List<Poudre> poudres,
    List<Jour> jours,
    String planEnCours,
    int plansValides
) {
    public record Synthese(int nbSessions, int nbClotures, int nbValidees, double objectif, double realise,
                           Double taux, double nette, double pertes, Double pctPertes, int machinistes) {}

    public record Poudre(String code, double objectif, double realise, double pertes, Double part) {}

    public record Jour(LocalDate date, int nbSessions, int nbValidees, double objectif, double realise,
                       double nette, double pertes, Double taux, Double pctPertes) {}

    public static ApercuBroyageResponse de(ApercuBroyage.Apercu a, String planEnCours, int plansValides) {
        return new ApercuBroyageResponse(a.debut(), a.fin(),
            synthese(a.duJour()), synthese(a.periode()),
            a.poudres().stream().map(p -> new Poudre(p.code(), p.objectif(), p.realise(), p.pertes(), p.part())).toList(),
            a.jours().stream().map(j -> new Jour(j.date(), j.nbSessions(), j.nbValidees(), j.objectif(), j.realise(),
                j.nette(), j.pertes(), j.taux(), j.pctPertes())).toList(),
            planEnCours, plansValides);
    }

    private static Synthese synthese(ApercuBroyage.Synthese s) {
        return new Synthese(s.nbSessions(), s.nbClotures(), s.nbValidees(), s.objectif(), s.realise(),
            s.taux(), s.nette(), s.pertes(), s.pctPertes(), s.machinistes());
    }
}
