package com.NTFOODS.Api_tanty.modules.production.domain.broyage.service;

import com.NTFOODS.Api_tanty.modules.production.domain.broyage.aggregate.SessionBroyage;
import com.NTFOODS.Api_tanty.modules.production.domain.broyage.enums.StatutSessionBroyage;
import com.NTFOODS.Api_tanty.modules.production.domain.broyage.enums.TypePoudre;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.EnumMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;

/**
 * Vue d'ensemble de l'atelier de broyage sur une période : synthèse du jour, synthèse de la période,
 * répartition par poudre et série journalière complète.
 *
 * Définitions (alignées sur le backend existant) :
 * - réalisé = somme des réalisations des machinistes ; taux = réalisé ÷ objectif journalier ;
 * - nette = quantité nette clôturée ; % de pertes = pertes ÷ (nette + pertes), sur les sessions où les
 *   pertes sont renseignées ;
 * - une journée est validée à 85 % de l'objectif, ou avec une justification (pris tel quel de la session).
 *
 * Les taux et pourcentages valent null quand ils ne sont pas calculables (objectif ou brut nul) : le
 * frontend affiche alors « — » au lieu d'un 0 trompeur.
 */
public final class ApercuBroyage {

    private ApercuBroyage() {}

    /** Nombre maximal de jours d'une série, pour borner la réponse. */
    public static final int JOURS_MAX = 366;

    /** Indicateurs d'un ensemble de sessions. */
    public record Synthese(int nbSessions, int nbClotures, int nbValidees, double objectif, double realise,
                           Double taux, double nette, double pertes, Double pctPertes, int machinistes) {}

    /** Répartition d'une poudre sur la période. */
    public record Poudre(String code, double objectif, double realise, double pertes, Double part) {}

    /** Une journée de la série (zéro pour une journée sans session). */
    public record Jour(LocalDate date, int nbSessions, int nbValidees, double objectif, double realise,
                       double nette, double pertes, Double taux, Double pctPertes) {}

    public record Apercu(LocalDate debut, LocalDate fin, LocalDate aujourdhui,
                         Synthese duJour, Synthese periode, List<Poudre> poudres, List<Jour> jours) {}

    /** Erreur de période (fin avant début, ou série trop longue). */
    public static void verifierPeriode(LocalDate debut, LocalDate fin) {
        Objects.requireNonNull(debut);
        Objects.requireNonNull(fin);
        if (fin.isBefore(debut)) throw new IllegalArgumentException("La date de fin doit être après la date de début");
        if (java.time.temporal.ChronoUnit.DAYS.between(debut, fin) + 1 > JOURS_MAX) {
            throw new IllegalArgumentException("La période ne peut pas dépasser " + JOURS_MAX + " jours");
        }
    }

    public static Apercu calculer(List<SessionBroyage> sessions, LocalDate debut, LocalDate fin, LocalDate aujourdhui) {
        verifierPeriode(debut, fin);
        List<SessionBroyage> dansPeriode = sessions.stream()
            .filter(s -> !s.getDate().isBefore(debut) && !s.getDate().isAfter(fin))
            .toList();
        List<SessionBroyage> duJour = dansPeriode.stream().filter(s -> s.getDate().equals(aujourdhui)).toList();

        Synthese synthese = synthese(duJour);
        Synthese periode = synthese(dansPeriode);

        double totalRealise = periode.realise();
        Map<TypePoudre, double[]> parPoudre = new EnumMap<>(TypePoudre.class); // [objectif, réalisé, pertes]
        for (SessionBroyage s : dansPeriode) {
            double[] c = parPoudre.computeIfAbsent(s.getTypePoudre(), k -> new double[3]);
            c[0] += s.getObjectifJournalierKg();
            c[1] += s.totalRealiseKg();
            c[2] += s.getPertesKg() == null ? 0 : s.getPertesKg();
        }
        List<Poudre> poudres = new ArrayList<>();
        for (Map.Entry<TypePoudre, double[]> e : parPoudre.entrySet()) {
            double[] c = e.getValue();
            poudres.add(new Poudre(e.getKey().name(), c[0], c[1], c[2],
                totalRealise > 0 ? pourcent(c[1], totalRealise) : null));
        }

        return new Apercu(debut, fin, aujourdhui, synthese, periode, poudres, serie(dansPeriode, debut, fin));
    }

    static Synthese synthese(List<SessionBroyage> sessions) {
        int clotures = 0;
        int validees = 0;
        double objectif = 0;
        double realise = 0;
        double nette = 0;
        double pertes = 0;
        double netteAvecPertes = 0;
        Set<String> machinistes = new HashSet<>();
        for (SessionBroyage s : sessions) {
            if (s.getStatut() == StatutSessionBroyage.CLOTUREE) clotures++;
            if (s.estJourneeValidee()) validees++;
            objectif += s.getObjectifJournalierKg();
            realise += s.totalRealiseKg();
            if (s.getQuantiteNetteBroyeeKg() != null) nette += s.getQuantiteNetteBroyeeKg();
            if (s.getPertesKg() != null) {
                pertes += s.getPertesKg();
                netteAvecPertes += s.getQuantiteNetteBroyeeKg() == null ? 0 : s.getQuantiteNetteBroyeeKg();
            }
            s.getRealisations().forEach(r -> machinistes.add(r.getMachinisteMatricule()));
        }
        double brut = netteAvecPertes + pertes;
        return new Synthese(sessions.size(), clotures, validees, objectif, realise,
            objectif > 0 ? pourcent(realise, objectif) : null,
            nette, pertes, brut > 0 ? pourcent(pertes, brut) : null, machinistes.size());
    }

    /** Une entrée par jour de la période, y compris les jours sans session (valeurs à zéro). */
    static List<Jour> serie(List<SessionBroyage> sessions, LocalDate debut, LocalDate fin) {
        Map<LocalDate, List<SessionBroyage>> parDate = new java.util.TreeMap<>();
        for (SessionBroyage s : sessions) parDate.computeIfAbsent(s.getDate(), k -> new ArrayList<>()).add(s);
        List<Jour> jours = new ArrayList<>();
        for (LocalDate d = debut; !d.isAfter(fin); d = d.plusDays(1)) {
            List<SessionBroyage> du = parDate.getOrDefault(d, List.of());
            Synthese s = synthese(du);
            jours.add(new Jour(d, s.nbSessions(), s.nbValidees(), s.objectif(), s.realise(), s.nette(), s.pertes(),
                s.taux(), s.pctPertes()));
        }
        return jours;
    }

    private static double pourcent(double part, double total) {
        return Math.round(part / total * 1000) / 10.0;
    }
}
