package com.NTFOODS.Api_tanty.modules.production.domain.pph.entity;

import java.time.LocalDate;
import java.util.List;
import java.util.Objects;

/**
 * Répartition journalière d'un plan de production sur les jours ouvrés.
 */
public class RepartitionJournaliere {

    private final LocalDate date;
    private final List<LignePlanDetail> lignes;
    private final double poudreMaisKg;
    private final double poudreSojaKg;
    private final double poudreArachideKg;
    private final int totalFuts;
    private final int totalSachets;
    private final int totalCouvercles;
    private final int totalScelles;

    public RepartitionJournaliere(LocalDate date, List<LignePlanDetail> lignes,
                                   double poudreMaisKg, double poudreSojaKg, double poudreArachideKg,
                                   int totalFuts, int totalSachets, int totalCouvercles, int totalScelles) {
        this.date = Objects.requireNonNull(date);
        this.lignes = List.copyOf(lignes);
        this.poudreMaisKg = poudreMaisKg;
        this.poudreSojaKg = poudreSojaKg;
        this.poudreArachideKg = poudreArachideKg;
        this.totalFuts = totalFuts;
        this.totalSachets = totalSachets;
        this.totalCouvercles = totalCouvercles;
        this.totalScelles = totalScelles;
    }

    public LocalDate getDate() { return date; }
    public List<LignePlanDetail> getLignes() { return lignes; }
    public double getPoudreMaisKg() { return poudreMaisKg; }
    public double getPoudreSojaKg() { return poudreSojaKg; }
    public double getPoudreArachideKg() { return poudreArachideKg; }
    public int getTotalFuts() { return totalFuts; }
    public int getTotalSachets() { return totalSachets; }
    public int getTotalCouvercles() { return totalCouvercles; }
    public int getTotalScelles() { return totalScelles; }
}
