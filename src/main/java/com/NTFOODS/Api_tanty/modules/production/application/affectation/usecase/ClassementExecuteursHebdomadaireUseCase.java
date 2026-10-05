package com.NTFOODS.Api_tanty.modules.production.application.affectation.usecase;

import com.NTFOODS.Api_tanty.modules.production.application.affectation.dto.response.ClassementExecuteurResponse;
import com.NTFOODS.Api_tanty.modules.production.application.affectation.dto.response.ClassementExecuteurResponse.QuantiteParUnite;
import com.NTFOODS.Api_tanty.modules.production.domain.affectation.aggregate.AffectationJournaliere;
import com.NTFOODS.Api_tanty.modules.production.domain.affectation.entity.ProductionIndividuelle;
import com.NTFOODS.Api_tanty.modules.production.domain.affectation.entity.SaisieRealisationPoste;
import com.NTFOODS.Api_tanty.modules.production.domain.affectation.repository.AffectationJournaliereRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;

/**
 * Classe les exécutants d'une période sur leur taux de réalisation individuel moyen.
 * Le taux de chaque production est rapporté à l'objectif individuel (objectif ajusté du poste réparti entre
 * les employés affectés), ce qui le rend comparable d'un poste à l'autre malgré des unités différentes.
 * Les quantités ne sont jamais additionnées entre unités.
 */
@Service
@Transactional(readOnly = true)
public class ClassementExecuteursHebdomadaireUseCase {

    private final AffectationJournaliereRepository repository;

    public ClassementExecuteursHebdomadaireUseCase(AffectationJournaliereRepository repository) {
        this.repository = repository;
    }

    public List<ClassementExecuteurResponse> executer(LocalDate debut, LocalDate fin) {
        Map<String, StatsExecuteur> stats = new HashMap<>();
        for (AffectationJournaliere a : repository.listerParPeriode(debut, fin)) {
            for (SaisieRealisationPoste s : a.getSaisies()) {
                double objectifIndividuel = objectifIndividuel(a, s.getPoste());
                for (ProductionIndividuelle p : s.getProductionsIndividuelles()) {
                    stats.computeIfAbsent(p.getMatriculeEmploye(),
                            k -> new StatsExecuteur(p.getMatriculeEmploye(), p.getNomEmploye()))
                        .ajouter(s.getUnite(), p.getQuantiteRealisee(), objectifIndividuel, a.getDate());
                }
            }
        }

        List<StatsExecuteur> tries = new ArrayList<>(stats.values());
        tries.sort(Comparator
            .comparingDouble(StatsExecuteur::tauxMoyen).reversed()
            .thenComparing(Comparator.comparingInt(StatsExecuteur::nbJours).reversed())
            .thenComparing(s -> s.nom == null ? "" : s.nom));

        List<ClassementExecuteurResponse> resultat = new ArrayList<>(tries.size());
        for (int i = 0; i < tries.size(); i++) {
            StatsExecuteur s = tries.get(i);
            resultat.add(new ClassementExecuteurResponse(
                i + 1, s.matricule, s.nom, s.quantitePrincipale(), s.unitePrincipale(),
                s.tauxMoyen(), s.nbJours(), s.quantitesParUnite()));
        }
        return resultat;
    }

    /** Objectif ajusté du poste réparti entre les employés affectés à ce poste (0 si aucun employé). */
    private double objectifIndividuel(AffectationJournaliere a, String codePoste) {
        return a.getLignePourPoste(codePoste)
            .filter(l -> !l.getMatriculesEmployes().isEmpty())
            .map(l -> l.getObjectifAjuste() / l.getMatriculesEmployes().size())
            .orElse(0.0);
    }

    private static class StatsExecuteur {
        final String matricule;
        final String nom;
        final Map<String, Double> quantiteParUnite = new TreeMap<>();
        final Map<String, Integer> productionsParUnite = new TreeMap<>();
        final Set<LocalDate> jours = new HashSet<>();
        double tauxCumule;
        int nbTaux;

        StatsExecuteur(String matricule, String nom) {
            this.matricule = matricule;
            this.nom = nom;
        }

        void ajouter(String unite, double quantite, double objectifIndividuel, LocalDate date) {
            String u = unite == null ? "" : unite;
            quantiteParUnite.merge(u, quantite, Double::sum);
            productionsParUnite.merge(u, 1, Integer::sum);
            if (objectifIndividuel > 0) {
                tauxCumule += quantite / objectifIndividuel * 100;
                nbTaux++;
            }
            jours.add(date);
        }

        double tauxMoyen() { return nbTaux > 0 ? tauxCumule / nbTaux : 0.0; }
        int nbJours() { return jours.size(); }

        /** Unité la plus fréquente de l'exécutant. */
        String unitePrincipale() {
            return productionsParUnite.entrySet().stream()
                .max(Map.Entry.comparingByValue())
                .map(Map.Entry::getKey)
                .orElse("");
        }

        double quantitePrincipale() { return quantiteParUnite.getOrDefault(unitePrincipale(), 0.0); }

        List<QuantiteParUnite> quantitesParUnite() {
            return quantiteParUnite.entrySet().stream()
                .map(e -> new QuantiteParUnite(e.getKey(), e.getValue()))
                .toList();
        }
    }
}
