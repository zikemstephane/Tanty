package com.NTFOODS.Api_tanty.modules.production.domain.dosage.service;

import com.NTFOODS.Api_tanty.modules.production.domain.dosage.aggregate.SessionDosage;

import java.time.LocalDate;
import java.util.List;
import java.util.Objects;
import java.util.TreeMap;

/**
 * Avancement du dosage d'un plan PPH : fûts dosés, séparés entre sessions validées (clôturées)
 * et sessions encore en cours. Ce calcul est en lecture seule : il ne modifie pas l'avancement
 * de production du plan, qui reste alimenté par les fiches de production.
 */
public final class AvancementDosage {

    private AvancementDosage() {}

    /** Fûts dosés d'une journée. */
    public record Jour(LocalDate date, int futsValides, int futsEnCours) {}

    public record Resultat(String referencePPH, int nbSessions, int nbSessionsValidees,
                           int futsValides, int futsEnCours, List<Jour> parJour) {
        public int futsDoses() { return futsValides + futsEnCours; }
    }

    /** Ne retient que les sessions du plan demandé ; les sessions d'autres plans sont ignorées. */
    public static Resultat calculer(String referencePPH, List<SessionDosage> sessions) {
        Objects.requireNonNull(referencePPH);
        TreeMap<LocalDate, int[]> parDate = new TreeMap<>();
        int nbSessions = 0;
        int nbValidees = 0;
        int futsValides = 0;
        int futsEnCours = 0;
        for (SessionDosage s : sessions) {
            if (!referencePPH.equals(s.getReferencePPH())) continue;
            nbSessions++;
            int[] jour = parDate.computeIfAbsent(s.getDate(), d -> new int[2]);
            if (s.isValidee()) {
                nbValidees++;
                futsValides += s.getNbFutsProduits();
                jour[0] += s.getNbFutsProduits();
            } else {
                futsEnCours += s.getNbFutsProduits();
                jour[1] += s.getNbFutsProduits();
            }
        }
        List<Jour> jours = parDate.entrySet().stream()
            .map(e -> new Jour(e.getKey(), e.getValue()[0], e.getValue()[1]))
            .toList();
        return new Resultat(referencePPH, nbSessions, nbValidees, futsValides, futsEnCours, jours);
    }
}
