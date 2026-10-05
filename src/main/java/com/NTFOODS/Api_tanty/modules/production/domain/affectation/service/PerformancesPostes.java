package com.NTFOODS.Api_tanty.modules.production.domain.affectation.service;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * Performance d'un poste sur une période, calculée à partir des journées où il était affecté.
 * Le taux et le rendement ne portent que sur les journées saisies, pour comparer objectif et réalisé sur les mêmes jours.
 * Un poste reste dans une seule unité (celle du poste) : les quantités ne sont jamais mélangées entre postes.
 */
public final class PerformancesPostes {

    private PerformancesPostes() {}

    /** Observation d'un poste sur une journée : affecté, avec ou sans réalisation. */
    public record Observation(String codePoste, boolean saisi, double realise, double objectif, int effectifReel) {}

    /**
     * Performance agrégée. rang est renseigné seulement pour les postes ayant au moins une journée saisie ;
     * les autres sont listés sans rang.
     */
    public record Performance(
        String codePoste, int joursAffectes, int joursSaisis,
        double objectifTotal, double realiseTotal, double tauxRealisation,
        double effectifMoyen, double rendementParPersonne, Integer rang
    ) {}

    public static List<Performance> calculer(List<Observation> observations) {
        Map<String, Acc> parPoste = new LinkedHashMap<>();
        for (Observation o : observations) {
            Acc acc = parPoste.computeIfAbsent(o.codePoste(), Acc::new);
            acc.joursAffectes++;
            if (o.saisi()) acc.ajouter(o.realise(), o.objectif(), o.effectifReel());
        }

        List<Acc> saisis = new ArrayList<>();
        List<Acc> sansSaisie = new ArrayList<>();
        parPoste.values().forEach(a -> (a.joursSaisis > 0 ? saisis : sansSaisie).add(a));
        saisis.sort(Comparator.comparingDouble(Acc::taux).reversed()
            .thenComparing(Comparator.comparingInt((Acc a) -> a.joursSaisis).reversed())
            .thenComparing(a -> a.codePoste));
        sansSaisie.sort(Comparator.comparing(a -> a.codePoste));

        List<Performance> resultat = new ArrayList<>(saisis.size() + sansSaisie.size());
        int rang = 1;
        for (Acc a : saisis) resultat.add(a.versPerformance(rang++));
        for (Acc a : sansSaisie) resultat.add(a.versPerformance(null));
        return resultat;
    }

    private static final class Acc {
        final String codePoste;
        int joursAffectes;
        int joursSaisis;
        double objectifTotal;
        double realiseTotal;
        long effectifCumule;

        Acc(String codePoste) {
            this.codePoste = codePoste;
        }

        void ajouter(double realise, double objectif, int effectif) {
            joursSaisis++;
            realiseTotal += realise;
            objectifTotal += objectif;
            effectifCumule += effectif;
        }

        double taux() {
            return objectifTotal > 0 ? realiseTotal / objectifTotal * 100 : 0;
        }

        Performance versPerformance(Integer rang) {
            double effectifMoyen = joursSaisis > 0 ? (double) effectifCumule / joursSaisis : 0;
            double rendement = effectifCumule > 0 ? realiseTotal / effectifCumule : 0;
            return new Performance(codePoste, joursAffectes, joursSaisis, objectifTotal, realiseTotal,
                taux(), effectifMoyen, rendement, rang);
        }
    }
}
