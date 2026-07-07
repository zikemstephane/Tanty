package com.NTFOODS.Api_tanty.modules.commercial.application.rapport.usecase;

import com.NTFOODS.Api_tanty.modules.commercial.domain.vente.aggregate.Vente;
import com.NTFOODS.Api_tanty.modules.commercial.domain.vente.repository.VenteRepository;
import com.NTFOODS.Api_tanty.modules.comptabilite.domain.objectif.aggregate.ObjectifCommercial;
import com.NTFOODS.Api_tanty.modules.comptabilite.domain.objectif.repository.ObjectifCommercialRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.*;

/**
 * Use Case — Calcul des Primes Hebdomadaires (RG-10).
 *
 * Prime de 12 000 CFA si ET SEULEMENT si :
 * - Condition 1 (globale) : taux atteinte global = (CA réalisé / CA cible) × 100 >= 80 %
 * - Condition 2 (par gamme) : taux atteinte par gamme >= 75 % pour CHAQUE gamme assignée
 *
 * Les DEUX conditions doivent être SIMULTANÉMENT satisfaites.
 * Si une seule gamme est en dessous de 75 % : PRIME NON VERSÉE.
 */
@Service
@Transactional(readOnly = true)
public class CalculerPrimesHebdoUseCase {

    public static final double MONTANT_PRIME = 12_000.0;
    public static final double SEUIL_GLOBAL_POURCENT = 80.0;
    public static final double SEUIL_PAR_GAMME_POURCENT = 75.0;

    private final ObjectifCommercialRepository objectifRepository;
    private final VenteRepository venteRepository;

    public CalculerPrimesHebdoUseCase(ObjectifCommercialRepository objectifRepository,
                                       VenteRepository venteRepository) {
        this.objectifRepository = objectifRepository;
        this.venteRepository = venteRepository;
    }

    public List<PrimeResultat> executer(LocalDate semaineDebut, LocalDate semaineFin) {
        List<ObjectifCommercial> objectifs = objectifRepository.listerParSemaine(semaineDebut);
        List<PrimeResultat> resultats = new ArrayList<>();

        for (ObjectifCommercial objectif : objectifs) {
            String matricule = objectif.getMatriculeCommercial();
            List<Vente> ventes = venteRepository.listerParPeriode(semaineDebut, semaineFin).stream()
                .filter(v -> v.getMatriculeCommercial().equals(matricule))
                .toList();

            double caRealise = ventes.stream().mapToDouble(Vente::montantTotal).sum();
            double caCible = objectif.getObjectifGlobalFCFA();
            double tauxGlobal = caCible > 0 ? (caRealise / caCible) * 100.0 : 0.0;

            Map<String, Double> caParGamme = calculerCAParGamme(ventes);
            Map<String, Double> objectifsParGamme = objectif.getObjectifsParGammeFCFA();

            Map<String, Double> tauxParGamme = new LinkedHashMap<>();
            List<String> gammesBloquantes = new ArrayList<>();
            boolean toutesGammesOK = true;

            for (Map.Entry<String, Double> entry : objectifsParGamme.entrySet()) {
                String gamme = entry.getKey();
                double cibleGamme = entry.getValue();
                double caGamme = caParGamme.getOrDefault(gamme, 0.0);
                double tauxGamme = cibleGamme > 0 ? (caGamme / cibleGamme) * 100.0 : 0.0;
                tauxParGamme.put(gamme, tauxGamme);

                if (tauxGamme < SEUIL_PAR_GAMME_POURCENT) {
                    toutesGammesOK = false;
                    gammesBloquantes.add(gamme);
                }
            }

            boolean conditionGlobale = tauxGlobal >= SEUIL_GLOBAL_POURCENT;
            boolean eligible = conditionGlobale && toutesGammesOK;

            resultats.add(new PrimeResultat(matricule, caRealise, caCible, tauxGlobal,
                tauxParGamme, eligible, eligible ? MONTANT_PRIME : 0.0,
                gammesBloquantes, conditionGlobale, toutesGammesOK));
        }

        resultats.sort(Comparator.comparingDouble(PrimeResultat::tauxGlobal).reversed());
        return resultats;
    }

    private Map<String, Double> calculerCAParGamme(List<Vente> ventes) {
        Map<String, Double> caParGamme = new HashMap<>();
        for (Vente vente : ventes) {
            for (var ligne : vente.getLignes()) {
                String gamme = extraireGamme(ligne.getCodeProduit());
                caParGamme.merge(gamme, ligne.montantHT(), Double::sum);
            }
        }
        return caParGamme;
    }

    private String extraireGamme(String codeProduit) {
        if (codeProduit == null || codeProduit.isBlank()) return "INCONNU";
        String upper = codeProduit.toUpperCase();
        if (upper.contains("BOUILLIE") || upper.contains("BOU")) return "BOUILLIE";
        if (upper.contains("SNACK") || upper.contains("SNK")) return "SNACKING";
        if (upper.contains("CHOCO") || upper.contains("CHC")) return "CHOCO";
        if (upper.contains("SEAU") || upper.contains("STU")) return "SEAU";
        if (upper.contains("ENRO") || upper.contains("ENR")) return "ENRO";
        if (upper.contains("CROO") || upper.contains("CRO")) return "CROO";
        if (upper.contains("CHIPS") || upper.contains("CHP")) return "CHIPS";
        if (upper.contains("CARAM") || upper.contains("CRM")) return "CARAM";
        return "AUTRE";
    }

    public record PrimeResultat(
        String matriculeCommercial,
        double caRealise,
        double caCible,
        double tauxGlobal,
        Map<String, Double> tauxParGamme,
        boolean eligible,
        double montantPrime,
        List<String> gammesBloquantes,
        boolean conditionGlobaleOK,
        boolean conditionGammesOK
    ) {}
}
