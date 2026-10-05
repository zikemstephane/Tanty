package com.NTFOODS.Api_tanty.modules.production.domain.pph.entity;

import com.NTFOODS.Api_tanty.modules.production.domain.formule.entity.BesoinMatierePremiere;
import com.NTFOODS.Api_tanty.modules.stock.domain.catalogue.enums.TypeEmballage;
import com.NTFOODS.Api_tanty.modules.stock.domain.catalogue.model.BesoinEmballage;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Objects;

/**
 * Répartition journalière d'un plan de production sur les jours ouvrés.
 *
 * Les besoins en matières premières et en composants d'emballage sont agrégés
 * ici — sommés sur toutes les lignes du jour, par matière première / par
 * composant — plutôt que figés sur maïs/soja/arachide et sachet/couvercle/scellé.
 */
public class RepartitionJournaliere {

    private final LocalDate date;
    private final List<LignePlanDetail> lignes;
    private final List<BesoinMatierePremiere> besoinsMatieresPremiere;
    private final List<BesoinEmballage> besoinsEmballage;
    private final double totalFuts;

    public RepartitionJournaliere(LocalDate date, List<LignePlanDetail> lignes,
                                   List<BesoinMatierePremiere> besoinsMatieresPremiere,
                                   List<BesoinEmballage> besoinsEmballage,
                                   double totalFuts) {
        this.date = Objects.requireNonNull(date);
        this.lignes = List.copyOf(lignes);
        this.besoinsMatieresPremiere = besoinsMatieresPremiere == null ? List.of() : List.copyOf(besoinsMatieresPremiere);
        this.besoinsEmballage = besoinsEmballage == null ? List.of() : List.copyOf(besoinsEmballage);
        this.totalFuts = totalFuts;
    }

    public LocalDate getDate() { return date; }
    public List<LignePlanDetail> getLignes() { return lignes; }
    public List<BesoinMatierePremiere> getBesoinsMatieresPremiere() { return besoinsMatieresPremiere; }
    public List<BesoinEmballage> getBesoinsEmballage() { return besoinsEmballage; }
    public double getTotalFuts() { return totalFuts; }

    public BigDecimal getTotalMatieresPremieresKg() {
        return besoinsMatieresPremiere.stream()
            .map(BesoinMatierePremiere::quantiteKg)
            .reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    public BigDecimal getBesoinMatierePremiere(String matierePremiere) {
        return besoinsMatieresPremiere.stream()
            .filter(b -> b.matierePremiere().equalsIgnoreCase(matierePremiere))
            .map(BesoinMatierePremiere::quantiteKg)
            .findFirst()
            .orElse(BigDecimal.ZERO);
    }

    public long getBesoinEmballage(TypeEmballage type) {
        return besoinsEmballage.stream()
            .filter(b -> b.type() == type)
            .mapToLong(BesoinEmballage::quantite)
            .findFirst()
            .orElse(0L);
    }
}
