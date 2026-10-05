package com.NTFOODS.Api_tanty.modules.production.application.dosage.usecase;

import com.NTFOODS.Api_tanty.modules.production.application.dosage.dto.response.StatistiquesFutsResponse;
import com.NTFOODS.Api_tanty.modules.production.application.dosage.service.ResolveurFormuleParSessionService;
import com.NTFOODS.Api_tanty.modules.production.domain.dosage.aggregate.SessionDosage;
import com.NTFOODS.Api_tanty.modules.production.domain.dosage.repository.SessionDosageRepository;
import com.NTFOODS.Api_tanty.modules.production.domain.formule.aggregate.FormuleProduction;
import com.NTFOODS.Api_tanty.modules.production.domain.formule.entity.BesoinMatierePremiere;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.text.Normalizer;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Optional;

/**
 * Use Case — Statistiques de fûts sur une période avec comparaison N-1.
 *
 * Le nombre de jours de la période courante détermine la période précédente
 * (même durée, juste avant).
 */
@Service
@Transactional(readOnly = true)
public class StatistiquesFutsUseCase {

    private static final double POUDRE_FUT_KG = 50.0;

    private final SessionDosageRepository repository;
    private final ResolveurFormuleParSessionService resolveurFormule;

    public StatistiquesFutsUseCase(SessionDosageRepository repository, ResolveurFormuleParSessionService resolveurFormule) {
        this.repository = repository;
        this.resolveurFormule = resolveurFormule;
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

        List<StatistiquesFutsResponse.MatierePremiereTotal> matieres = courante.matieres.entrySet().stream()
            .map(e -> new StatistiquesFutsResponse.MatierePremiereTotal(e.getKey(), e.getValue().doubleValue()))
            .toList();

        return new StatistiquesFutsResponse(
            debut, fin, periodeLibelle,
            courante.futsProduits, courante.futsNets,
            matieres, courante.sessionsNonEstimees,
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
        Map<String, BigDecimal> matieres = new LinkedHashMap<>();
        int sessionsNonEstimees = 0;

        for (SessionDosage s : sessions) {
            futsProduits += s.getNbFutsProduits();
            futsNets += s.getNbFutsNets();

            // Prise en compte des matières réelles saisies par l'Agent Doseur.
            boolean matieresSaisies = !s.getMatieresUtilisees().isEmpty();
            for (SessionDosage.MatierePremiereUtilisee m : s.getMatieresUtilisees()) {
                matieres.merge(normaliser(m.getTypeMatiere()), BigDecimal.valueOf(m.getQuantiteKg()), BigDecimal::add);
            }

            // Si aucune matière réelle n'est saisie, on estime via la formule de
            // production des produits du bon de commande associé — uniquement
            // quand un unique jeu de proportions est identifiable sans ambiguïté
            // pour cette session (sinon : signalé en non-estimée, jamais une
            // recette par défaut inventée).
            if (!matieresSaisies) {
                int futsAConsiderer = s.getNbFutsNets();
                Optional<FormuleProduction> formuleUnique = resolveurFormule.resoudre(s.getReferencePPH());
                if (formuleUnique.isPresent() && futsAConsiderer > 0) {
                    List<BesoinMatierePremiere> besoins = formuleUnique.get()
                        .calculerBesoins(BigDecimal.valueOf(POUDRE_FUT_KG * futsAConsiderer));
                    for (BesoinMatierePremiere b : besoins) {
                        matieres.merge(normaliser(b.matierePremiere()), b.quantiteKg(), BigDecimal::add);
                    }
                } else if (futsAConsiderer > 0) {
                    sessionsNonEstimees++;
                }
            }
        }

        return new PeriodeStats(futsProduits, futsNets, matieres, sessionsNonEstimees, sessions.size());
    }

    /** Normalise un nom de matière première (majuscules, sans accents) pour agréger sans dupliquer les libellés. */
    private String normaliser(String nom) {
        String sansAccents = Normalizer.normalize(nom, Normalizer.Form.NFD).replaceAll("\\p{M}", "");
        return sansAccents.trim().toUpperCase(Locale.ROOT);
    }

    private record PeriodeStats(
        int futsProduits, int futsNets,
        Map<String, BigDecimal> matieres,
        int sessionsNonEstimees,
        int nbSessions
    ) {}
}
