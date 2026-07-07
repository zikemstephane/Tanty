package com.NTFOODS.Api_tanty.modules.dg.application.rapport;

import com.NTFOODS.Api_tanty.modules.commercial.infrastructure.persistence.vente.jpa.VenteJpaEntity;
import com.NTFOODS.Api_tanty.modules.commercial.infrastructure.persistence.vente.jpa.VenteJpaRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.List;

/**
 * Service applicatif — Analyse Comparative (Pilier 4).
 *
 * Les interfaces de direction doivent toujours présenter les données en
 * VALEUR ABSOLUE (jamais seulement en pourcentage), avec une comparaison
 * instantanée à la période précédente équivalente — disponible à TOUS les
 * niveaux : jour, semaine, mois, année.
 *
 * Méthode "à ce jour" (to-date) : compare la période en cours, du début de
 * la période jusqu'à la date de référence, à la même tranche relative de la
 * période précédente. Cela permet de comparer équitablement un mois ou une
 * année encore en cours (ex: "1er au 3 juillet 2026" vs "1er au 3 juin 2026").
 */
@Service
@Transactional(readOnly = true)
public class RapportComparatifService {

    private final VenteJpaRepository venteJpaRepository;

    public RapportComparatifService(VenteJpaRepository venteJpaRepository) {
        this.venteJpaRepository = venteJpaRepository;
    }

    public RapportComparatifResponse comparerCA(PeriodeComparaison periode, LocalDate dateReference) {
        LocalDate ref = dateReference != null ? dateReference : LocalDate.now();

        LocalDate debutActuelle = debutPeriode(periode, ref);
        LocalDate finActuelle = ref;

        long joursEcoules = ChronoUnit.DAYS.between(debutActuelle, finActuelle);
        LocalDate debutPrecedente = debutPeriodePrecedente(periode, debutActuelle);
        LocalDate finPrecedente = debutPrecedente.plusDays(joursEcoules);

        RapportComparatifResponse.Tranche actuelle = calculerTranche(debutActuelle, finActuelle);
        RapportComparatifResponse.Tranche precedente = calculerTranche(debutPrecedente, finPrecedente);

        double variationAbsolue = actuelle.caFCFA() - precedente.caFCFA();
        double variationPourcent = precedente.caFCFA() > 0 ? (variationAbsolue / precedente.caFCFA()) * 100.0 : 0.0;

        return new RapportComparatifResponse(periode, actuelle, precedente, variationAbsolue, variationPourcent);
    }

    private RapportComparatifResponse.Tranche calculerTranche(LocalDate debut, LocalDate fin) {
        List<VenteJpaEntity> ventes = venteJpaRepository.findByDateBetween(debut, fin);
        double ca = ventes.stream()
            .flatMap(v -> v.getLignes().stream())
            .mapToDouble(l -> l.getPrixUnitaire() * l.getQuantite() * (1.0 - l.getRemise() / 100.0))
            .sum();
        return new RapportComparatifResponse.Tranche(debut, fin, ca, ventes.size());
    }

    private LocalDate debutPeriode(PeriodeComparaison periode, LocalDate ref) {
        return switch (periode) {
            case JOUR -> ref;
            case SEMAINE -> ref.with(DayOfWeek.MONDAY);
            case MOIS -> ref.withDayOfMonth(1);
            case ANNEE -> ref.withDayOfYear(1);
        };
    }

    private LocalDate debutPeriodePrecedente(PeriodeComparaison periode, LocalDate debutActuelle) {
        return switch (periode) {
            case JOUR -> debutActuelle.minusDays(1);
            case SEMAINE -> debutActuelle.minusWeeks(1);
            case MOIS -> debutActuelle.minusMonths(1);
            case ANNEE -> debutActuelle.minusYears(1);
        };
    }
}
