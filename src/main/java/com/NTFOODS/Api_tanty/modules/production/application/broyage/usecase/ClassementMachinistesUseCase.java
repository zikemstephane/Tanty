package com.NTFOODS.Api_tanty.modules.production.application.broyage.usecase;

import com.NTFOODS.Api_tanty.modules.production.application.broyage.dto.response.ClassementMachinisteResponse;
import com.NTFOODS.Api_tanty.modules.production.domain.broyage.aggregate.SessionBroyage;
import com.NTFOODS.Api_tanty.modules.production.domain.broyage.entity.RealisationMachiniste;
import com.NTFOODS.Api_tanty.modules.production.domain.broyage.repository.SessionBroyageRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.*;

/**
 * Use Case — Classement des machinistes sur une période.
 *
 * Métriques :
 * - productivité : quantité totale traitée (kg)
 * - taux de réalisation moyen : moyenne des % de réalisation par ligne
 * - efficacité : ratio pondéré réalisé/objectif (totalRealise / totalObjectif)
 */
@Service
@Transactional(readOnly = true)
public class ClassementMachinistesUseCase {

    private final SessionBroyageRepository repository;

    public ClassementMachinistesUseCase(SessionBroyageRepository repository) {
        this.repository = repository;
    }

    public ClassementMachinisteResponse executer(LocalDate debut, LocalDate fin) {
        List<SessionBroyage> sessions = repository.listerParPeriode(debut, fin);

        Map<String, StatsMachiniste> statsParMachiniste = new HashMap<>();

        for (SessionBroyage session : sessions) {
            for (RealisationMachiniste r : session.getRealisations()) {
                if (r == null || r.getMachinisteMatricule() == null || r.getOperation() == null) {
                    continue;
                }
                String nom = r.getNomMachiniste() != null ? r.getNomMachiniste() : r.getMachinisteMatricule();
                StatsMachiniste stats = statsParMachiniste.computeIfAbsent(
                    r.getMachinisteMatricule(),
                    k -> new StatsMachiniste(nom)
                );
                stats.ajouter(session.getDate(), r.getOperation().name(), r.getObjectifKg(), r.getRealiseKg(), r.getPctRealisation());
            }
        }

        List<ClassementMachinisteResponse.LigneClassement> lignes = statsParMachiniste.entrySet().stream()
            .map(e -> {
                StatsMachiniste s = e.getValue();
                double efficacite = s.objectifTotal > 0 ? s.realiseTotal / s.objectifTotal : 0.0;
                return new ClassementMachinisteResponse.LigneClassement(
                    0, e.getKey(), s.nom,
                    s.realiseTotal, s.objectifTotal,
                    s.tauxRealisationMoyen(), Math.min(efficacite, 2.0),
                    s.nbJoursDistincts(),
                    s.operations.stream().distinct().toList()
                );
            })
            .sorted(Comparator.comparing(ClassementMachinisteResponse.LigneClassement::efficacite).reversed()
                .thenComparing(Comparator.comparing(ClassementMachinisteResponse.LigneClassement::productiviteKg).reversed()))
            .toList();

        // Attribuer les rangs
        List<ClassementMachinisteResponse.LigneClassement> classements = new ArrayList<>();
        for (int i = 0; i < lignes.size(); i++) {
            ClassementMachinisteResponse.LigneClassement l = lignes.get(i);
            classements.add(new ClassementMachinisteResponse.LigneClassement(
                i + 1, l.machinisteMatricule(), l.nomMachiniste(),
                l.productiviteKg(), l.objectifTotalKg(), l.tauxRealisationMoyen(),
                l.efficacite(), l.nbJours(), l.operations()
            ));
        }

        String periode = debut.format(DateTimeFormatter.ISO_DATE) + " / " + fin.format(DateTimeFormatter.ISO_DATE);
        return new ClassementMachinisteResponse(classements, periode);
    }

    private static class StatsMachiniste {
        final String nom;
        double objectifTotal;
        double realiseTotal;
        double sommePctRealisation;
        int nbLignes;
        final Set<LocalDate> jours = new HashSet<>();
        final Set<String> operations = new HashSet<>();

        StatsMachiniste(String nom) {
            this.nom = nom;
        }

        void ajouter(LocalDate date, String operation, double objectif, double realise, double pctRealisation) {
            jours.add(date);
            operations.add(operation);
            objectifTotal += objectif;
            realiseTotal += realise;
            sommePctRealisation += pctRealisation;
            nbLignes++;
        }

        double tauxRealisationMoyen() {
            return nbLignes > 0 ? sommePctRealisation / nbLignes : 0.0;
        }

        int nbJoursDistincts() {
            return jours.size();
        }
    }
}
