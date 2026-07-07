package com.NTFOODS.Api_tanty.modules.production.application.rapport.usecase;

import com.NTFOODS.Api_tanty.modules.production.domain.lot.aggregate.LotProduitFini;
import com.NTFOODS.Api_tanty.modules.production.domain.lot.enums.StatutLot;
import com.NTFOODS.Api_tanty.modules.production.domain.lot.repository.LotRepository;
import com.NTFOODS.Api_tanty.modules.production.domain.pph.aggregate.PlanProductionHebdomadaire;
import com.NTFOODS.Api_tanty.modules.production.domain.pph.entity.LignePPH;
import com.NTFOODS.Api_tanty.modules.production.domain.pph.repository.PPHRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.*;

/**
 * Use Case — Rapport comparatif BC vs Production.
 *
 * Compare les quantités commandées (via les PPH liés à un BC) avec les quantités
 * réellement produites (via les lots validés). Met en évidence les écarts.
 */
@Service
public class GenererRapportComparatifBCUseCase {

    private final PPHRepository pphRepository;
    private final LotRepository lotRepository;

    public GenererRapportComparatifBCUseCase(PPHRepository pphRepository, LotRepository lotRepository) {
        this.pphRepository = pphRepository;
        this.lotRepository = lotRepository;
    }

    public RapportComparatifBC executer(String referenceBC) {
        List<PlanProductionHebdomadaire> pphs = pphRepository.listerParBC(referenceBC);
        if (pphs.isEmpty()) {
            return new RapportComparatifBC(referenceBC, List.of(), 0, 0, 0, 0.0);
        }

        Map<String, LigneComparatif> lignesParProduit = new LinkedHashMap<>();
        for (PlanProductionHebdomadaire pph : pphs) {
            for (LignePPH ligne : pph.getLignes()) {
                lignesParProduit.computeIfAbsent(ligne.getCodeProduit(),
                    k -> new LigneComparatif(ligne.getCodeProduit(), ligne.getNomProduit(),
                        ligne.getObjectifSemaine(), 0, 0));
                LigneComparatif lc = lignesParProduit.get(ligne.getCodeProduit());
                lc.quantiteProduite += ligne.getProductionRealisee();
            }
        }

        List<LotProduitFini> lots = lotRepository.listerParPeriode(
            pphs.stream().map(PlanProductionHebdomadaire::getDateDebut).min(Comparator.naturalOrder()).orElse(LocalDate.now()),
            pphs.stream().map(PlanProductionHebdomadaire::getDateFin).max(Comparator.naturalOrder()).orElse(LocalDate.now())
        );
        for (LotProduitFini lot : lots) {
            if (lot.getStatut() == StatutLot.VALIDATED_BY_STOCK) {
                LigneComparatif lc = lignesParProduit.get(lot.getCodeProduit());
                if (lc != null) {
                    lc.quantiteLotsValidesKg += lot.getQuantiteKg();
                    lc.nbCartonsValides += lot.getNbCartons();
                }
            }
        }

        List<LigneComparatif> lignes = new ArrayList<>(lignesParProduit.values());
        int totalPlanifie = lignes.stream().mapToInt(l -> l.quantitePlanifiee).sum();
        int totalProduit = lignes.stream().mapToInt(l -> l.quantiteProduite).sum();
        int totalCartons = lignes.stream().mapToInt(l -> l.nbCartonsValides).sum();
        double totalKg = lignes.stream().mapToDouble(l -> l.quantiteLotsValidesKg).sum();

        return new RapportComparatifBC(referenceBC, lignes, totalPlanifie, totalProduit, totalCartons, totalKg);
    }

    public record RapportComparatifBC(
        String referenceBC,
        List<LigneComparatif> lignes,
        int totalQuantitePlanifiee,
        int totalQuantiteProduite,
        int totalCartonsValides,
        double totalKgValides
    ) {
        public int tauxAvancementGlobal() {
            return totalQuantitePlanifiee > 0
                ? Math.min(100, (int) ((totalQuantiteProduite * 100.0) / totalQuantitePlanifiee))
                : 0;
        }
    }

    public static class LigneComparatif {
        public final String codeProduit;
        public final String designation;
        public final int quantitePlanifiee;
        public int quantiteProduite;
        public double quantiteLotsValidesKg;
        public int nbCartonsValides;

        public LigneComparatif(String codeProduit, String designation, int quantitePlanifiee,
                               int quantiteProduite, int nbCartonsValides) {
            this.codeProduit = codeProduit;
            this.designation = designation;
            this.quantitePlanifiee = quantitePlanifiee;
            this.quantiteProduite = quantiteProduite;
            this.quantiteLotsValidesKg = 0;
            this.nbCartonsValides = nbCartonsValides;
        }

        public int tauxAvancement() {
            return quantitePlanifiee > 0
                ? Math.min(100, (int) ((quantiteProduite * 100.0) / quantitePlanifiee))
                : 0;
        }

        public int ecart() {
            return quantiteProduite - quantitePlanifiee;
        }
    }
}
