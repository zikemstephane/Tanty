package com.NTFOODS.Api_tanty.modules.production.application.affectation.usecase;

import com.NTFOODS.Api_tanty.modules.production.application.affectation.dto.response.ClassementExecuteurResponse;
import com.NTFOODS.Api_tanty.modules.production.domain.affectation.aggregate.AffectationJournaliere;
import com.NTFOODS.Api_tanty.modules.production.domain.affectation.entity.LigneAffectationPoste;
import com.NTFOODS.Api_tanty.modules.production.domain.affectation.entity.ProductionIndividuelle;
import com.NTFOODS.Api_tanty.modules.production.domain.affectation.entity.SaisieRealisationPoste;
import com.NTFOODS.Api_tanty.modules.production.domain.affectation.repository.AffectationJournaliereRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

/**
 * Agrège les réalisations individuelles par employé sur une semaine et établit un classement.
 */
@Service
@Transactional(readOnly = true)
public class ClassementExecuteursHebdomadaireUseCase {

    private final AffectationJournaliereRepository repository;

    public ClassementExecuteursHebdomadaireUseCase(AffectationJournaliereRepository repository) {
        this.repository = repository;
    }

    public List<ClassementExecuteurResponse> executer(LocalDate debut, LocalDate fin) {
        List<AffectationJournaliere> affectations = repository.listerParPeriode(debut, fin);

        Map<String, StatsExecuteur> stats = new HashMap<>();
        for (AffectationJournaliere a : affectations) {
            for (SaisieRealisationPoste s : a.getSaisies()) {
                for (ProductionIndividuelle p : s.getProductionsIndividuelles()) {
                    StatsExecuteur st = stats.computeIfAbsent(p.getMatriculeEmploye(),
                        k -> new StatsExecuteur(p.getMatriculeEmploye(), p.getNomEmploye(), s.getUnite().getLibelle()));
                    st.ajouter(p.getQuantiteRealisee(), s.getPctRealisation(), a.getDate());
                }
            }
        }

        AtomicInteger rang = new AtomicInteger(1);
        return stats.values().stream()
            .sorted(Comparator.comparingDouble(StatsExecuteur::quantiteTotale).reversed())
            .map(s -> new ClassementExecuteurResponse(
                rang.getAndIncrement(), s.matricule, s.nom, s.quantiteTotale(), s.unite,
                s.tauxMoyen(), s.joursAffectes.size()))
            .toList();
    }

    private static class StatsExecuteur {
        final String matricule;
        final String nom;
        final String unite;
        double quantite;
        double tauxCumule;
        int nbTaux;
        final Set<LocalDate> joursAffectes = new HashSet<>();

        StatsExecuteur(String matricule, String nom, String unite) {
            this.matricule = matricule;
            this.nom = nom;
            this.unite = unite;
        }

        void ajouter(double qte, double taux, LocalDate date) {
            this.quantite += qte;
            if (taux > 0) {
                this.tauxCumule += taux;
                this.nbTaux++;
            }
            this.joursAffectes.add(date);
        }

        double quantiteTotale() { return quantite; }
        double tauxMoyen() { return nbTaux > 0 ? tauxCumule / nbTaux : 0.0; }
    }
}
