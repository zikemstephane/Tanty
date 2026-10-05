package com.NTFOODS.Api_tanty.modules.production.application.rapport.usecase;

import com.NTFOODS.Api_tanty.modules.production.domain.broyage.aggregate.SessionBroyage;
import com.NTFOODS.Api_tanty.modules.production.domain.broyage.repository.SessionBroyageRepository;
import com.NTFOODS.Api_tanty.modules.production.domain.dosage.aggregate.SessionDosage;
import com.NTFOODS.Api_tanty.modules.production.domain.dosage.repository.SessionDosageRepository;
import com.NTFOODS.Api_tanty.modules.production.domain.fiche.aggregate.FicheProduitJournaliere;
import com.NTFOODS.Api_tanty.modules.production.domain.fiche.repository.FicheProduitRepository;
import com.NTFOODS.Api_tanty.modules.production.domain.lot.aggregate.LotProduitFini;
import com.NTFOODS.Api_tanty.modules.production.domain.lot.enums.StatutLot;
import com.NTFOODS.Api_tanty.modules.production.domain.lot.repository.LotRepository;
import com.NTFOODS.Api_tanty.modules.production.domain.pph.aggregate.PlanProductionHebdomadaire;
import com.NTFOODS.Api_tanty.modules.production.domain.pph.repository.PPHRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Use Case — Générer les rapports hebdomadaires de production automatiquement.
 *
 * Per §8 du Workflow doc :
 * - Rapport Hebdomadaire de Production : quantités produites par référence, taux d'avancement du BC,
 *   récapitulatif des pertes machiniste, bilan des fûts, comparaison prévu/réalisé sur la semaine.
 * - Rapport Hebdomadaire des Pertes Machiniste : synthèse des pertes par étape, par type de poudre,
 *   avec les pourcentages de perte cumulés sur la semaine.
 *
 * Les rapports hebdomadaires sont générés automatiquement par la plateforme à partir des saisies journalières.
 * Aucune saisie manuelle de synthèse n'est requise.
 */
@Service
public class GenererRapportHebdomadaireUseCase {

    private final FicheProduitRepository ficheRepository;
    private final SessionBroyageRepository broyageRepository;
    private final SessionDosageRepository dosageRepository;
    private final LotRepository lotRepository;
    private final PPHRepository pphRepository;

    public GenererRapportHebdomadaireUseCase(FicheProduitRepository ficheRepository,
                                              SessionBroyageRepository broyageRepository,
                                              SessionDosageRepository dosageRepository,
                                              LotRepository lotRepository,
                                              PPHRepository pphRepository) {
        this.ficheRepository = ficheRepository;
        this.broyageRepository = broyageRepository;
        this.dosageRepository = dosageRepository;
        this.lotRepository = lotRepository;
        this.pphRepository = pphRepository;
    }

    public RapportHebdomadaireProduction executer(LocalDate debutSemaine, LocalDate finSemaine) {
        List<FicheProduitJournaliere> fiches = ficheRepository.listerParPeriode(debutSemaine, finSemaine);
        List<SessionBroyage> sessionsBroyage = broyageRepository.listerParPeriode(debutSemaine, finSemaine);
        List<SessionDosage> sessionsDosage = dosageRepository.listerParPeriode(debutSemaine, finSemaine);
        // Production de la période = lots validés par le stock pendant la période (date de validation)
        List<LotProduitFini> lots = lotRepository.listerParStatut(StatutLot.VALIDATED_BY_STOCK).stream()
            .filter(l -> l.estValidePeriode(debutSemaine, finSemaine))
            .toList();

        Map<String, Integer> quantitesParReference = new HashMap<>();
        for (FicheProduitJournaliere fiche : fiches) {
            for (var ligne : fiche.getLignes()) {
                if (ligne.getCodeProduit() == null) continue;
                quantitesParReference.merge(ligne.getCodeProduit(), (int) Math.round(ligne.getQuantiteProduite()), Integer::sum);
            }
        }

        Map<String, Double> pertesParTypePoudre = new HashMap<>();
        Map<String, Double> quantiteBroyeeParTypePoudre = new HashMap<>();
        for (SessionBroyage session : sessionsBroyage) {
            if (session.getTypePoudre() == null) continue;
            String typePoudre = session.getTypePoudre().name();
            if (session.getQuantiteNetteBroyeeKg() != null) {
                quantiteBroyeeParTypePoudre.merge(typePoudre, session.getQuantiteNetteBroyeeKg(), Double::sum);
            }
            if (session.getPertesKg() != null) {
                pertesParTypePoudre.merge(typePoudre, session.getPertesKg(), Double::sum);
            }
        }

        int totalFutsProduits = sessionsDosage.stream().mapToInt(SessionDosage::getNbFutsProduits).sum();
        int totalFutsNets = totalFutsProduits;

        int totalLotsValides = (int) lots.stream()
            .filter(l -> l.getStatut() != null && l.getStatut().name().equals("VALIDATED_BY_STOCK"))
            .count();
        double totalQuantiteLots = lots.stream()
            .filter(l -> l.getStatut() != null && l.getStatut().name().equals("VALIDATED_BY_STOCK"))
            .mapToDouble(LotProduitFini::getDeclaredQuantityKg).sum();

        Map<String, Integer> tauxAvancementParBC = new HashMap<>();
        Map<String, List<PlanProductionHebdomadaire>> pphsParBC = new HashMap<>();
        for (FicheProduitJournaliere fiche : fiches) {
            pphRepository.trouverParReference(fiche.getReferencePPH()).ifPresent(pph -> {
                if (pph.getReferenceBC() != null) {
                    pphsParBC.computeIfAbsent(pph.getReferenceBC(), k -> new java.util.ArrayList<>()).add(pph);
                }
            });
        }
        for (var entry : pphsParBC.entrySet()) {
            int avgTaux = (int) entry.getValue().stream().mapToInt(PlanProductionHebdomadaire::tauxAvancementGlobal).average().orElse(0);
            tauxAvancementParBC.put(entry.getKey(), avgTaux);
        }

        return new RapportHebdomadaireProduction(
            debutSemaine, finSemaine,
            quantitesParReference, tauxAvancementParBC,
            pertesParTypePoudre, quantiteBroyeeParTypePoudre,
            totalFutsProduits, totalFutsNets,
            totalLotsValides, totalQuantiteLots,
            fiches.size(), sessionsBroyage.size(), sessionsDosage.size()
        );
    }

    public record RapportHebdomadaireProduction(
        LocalDate debutSemaine,
        LocalDate finSemaine,
        Map<String, Integer> quantitesParReference,
        Map<String, Integer> tauxAvancementParBC,
        Map<String, Double> pertesParTypePoudre,
        Map<String, Double> quantiteBroyeeParTypePoudre,
        int totalFutsProduits,
        int totalFutsNets,
        int totalLotsValides,
        double totalQuantiteLots,
        int nbFiches,
        int nbSessionsBroyage,
        int nbSessionsDosage
    ) {
        public double getTauxPertesGlobal() {
            double totalPertes = pertesParTypePoudre.values().stream().mapToDouble(Double::doubleValue).sum();
            double totalBroye = quantiteBroyeeParTypePoudre.values().stream().mapToDouble(Double::doubleValue).sum();
            double totalBrut = totalBroye + totalPertes;
            return totalBrut > 0 ? (totalPertes / totalBrut) * 100.0 : 0.0;
        }

        public Map<String, Double> getPctPertesParTypePoudre() {
            Map<String, Double> result = new HashMap<>();
            for (var entry : pertesParTypePoudre.entrySet()) {
                double broye = quantiteBroyeeParTypePoudre.getOrDefault(entry.getKey(), 0.0);
                double brut = broye + entry.getValue();
                result.put(entry.getKey(), brut > 0 ? (entry.getValue() / brut) * 100.0 : 0.0);
            }
            return result;
        }
    }
}
