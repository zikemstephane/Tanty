package com.NTFOODS.Api_tanty.modules.production.application.dosage.usecase;

import com.NTFOODS.Api_tanty.modules.production.application.dosage.dto.response.StatistiquesFutsResponse;
import com.NTFOODS.Api_tanty.modules.production.domain.dosage.aggregate.SessionDosage;
import com.NTFOODS.Api_tanty.modules.production.domain.dosage.repository.SessionDosageRepository;
import com.NTFOODS.Api_tanty.modules.production.domain.pph.enums.RecetteBouillie;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;

/**
 * Use Case — Statistiques de fûts sur une période avec comparaison N-1.
 *
 * Le nombre de jours de la période courante détermine la période précédente
 * (même durée, juste avant).
 */
@Service
@Transactional(readOnly = true)
public class StatistiquesFutsUseCase {

    private final SessionDosageRepository repository;

    public StatistiquesFutsUseCase(SessionDosageRepository repository) {
        this.repository = repository;
    }

    public StatistiquesFutsResponse executer(LocalDate debut, LocalDate fin) {
        if (debut.isAfter(fin)) {
            throw new IllegalArgumentException("La date de début doit être avant la date de fin");
        }

        PeriodeStats courante = calculerPeriode(debut, fin);

        long nbJours = ChronoUnit.DAYS.between(debut, fin) + 1;
        LocalDate debutPrecedent = debut.minusDays(nbJours);
        LocalDate finPrecedent = debut.minusDays(1);
        PeriodeStats precedente = calculerPeriode(debutPrecedent, finPrecedent);

        double variation = precedente.futsNets > 0
            ? ((courante.futsNets - precedente.futsNets) * 100.0 / precedente.futsNets)
            : 0.0;

        String periodeLibelle = debut + " / " + fin;

        return new StatistiquesFutsResponse(
            debut, fin, periodeLibelle,
            courante.futsProduits, courante.futsNets,
            courante.mais, courante.soja, courante.arachide,
            new StatistiquesFutsResponse.ComparaisonPeriode(
                debutPrecedent, finPrecedent, precedente.futsNets, variation
            ),
            courante.nbSessions
        );
    }

    private PeriodeStats calculerPeriode(LocalDate debut, LocalDate fin) {
        var sessions = repository.listerParPeriode(debut, fin);

        int futsProduits = 0;
        int futsNets = 0;
        double mais = 0.0;
        double soja = 0.0;
        double arachide = 0.0;

        for (SessionDosage s : sessions) {
            futsProduits += s.getNbFutsProduits();
            futsNets += s.getNbFutsNets();

            // Prise en compte des matières réelles saisies par l'Agent Doseur.
            boolean matieresSaisies = !s.getMatieresUtilisees().isEmpty();
            for (SessionDosage.MatierePremiereUtilisee m : s.getMatieresUtilisees()) {
                switch (m.getTypeMatiere().toUpperCase()) {
                    case "MAIS" -> mais += m.getQuantiteKg();
                    case "SOJA" -> soja += m.getQuantiteKg();
                    case "ARACHIDE" -> arachide += m.getQuantiteKg();
                }
            }

            // Si aucune matière réelle n'est saisie, on estime selon la recette standard 40/9/1.
            if (!matieresSaisies) {
                int futsAConsiderer = s.getNbFutsNets();
                mais += futsAConsiderer * RecetteBouillie.SIMPLE_PORRIDGE.getProportionMais();
                soja += futsAConsiderer * RecetteBouillie.SIMPLE_PORRIDGE.getProportionSoja();
                arachide += futsAConsiderer * RecetteBouillie.SIMPLE_PORRIDGE.getProportionArachide();
            }
        }

        return new PeriodeStats(futsProduits, futsNets, mais, soja, arachide, sessions.size());
    }

    private record PeriodeStats(
        int futsProduits, int futsNets,
        double mais, double soja, double arachide,
        int nbSessions
    ) {}
}
