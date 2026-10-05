package com.NTFOODS.Api_tanty.modules.commercial.application.rapport.usecase;

import com.NTFOODS.Api_tanty.modules.commercial.domain.vente.aggregate.Vente;
import com.NTFOODS.Api_tanty.modules.commercial.domain.vente.repository.VenteRepository;
import com.NTFOODS.Api_tanty.modules.comptabilite.domain.objectif.aggregate.ObjectifCommercial;
import com.NTFOODS.Api_tanty.modules.comptabilite.domain.objectif.repository.ObjectifCommercialRepository;
import com.NTFOODS.Api_tanty.modules.stock.domain.catalogue.repository.ProduitRepository;
import com.NTFOODS.Api_tanty.modules.stock.domain.catalogue.valueobject.CodeProduit;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;

/**
 * Use Case — Calcul des Primes Hebdomadaires (règle réelle NTFoods).
 *
 * Éligibilité de base : taux atteinte global = (CA réalisé / CA cible) × 100 >= 80 %.
 * Si non atteinte, la prime est nulle quel que soit le détail par gamme.
 *
 * Si éligible : 3 000 FCFA par gamme (Bouillies, À Grignoter, Choco, Ingrédients)
 * ayant atteint >= 75 % de son objectif, plafonné à {@link #MONTANT_PRIME_MAX}
 * (12 000 FCFA pour les 4 gammes). Payée moitié en cash, moitié en nature.
 *
 * Règle des 60 % journaliers : une journée ne compte dans les totaux hebdomadaires
 * (global et par gamme) que si le CA réalisé ce jour-là atteint au moins 60 % de
 * l'objectif quotidien (objectif hebdo ÷ nombre de jours ouvrés). Les journées
 * sous ce seuil sont exclues du calcul, mais tracées (joursValides/joursTotal)
 * pour la transparence envers le commercial.
 */
@Service
@Transactional(readOnly = true)
public class CalculerPrimesHebdoUseCase {

    public static final double MONTANT_PAR_GAMME = 3_000.0;
    public static final double MONTANT_PRIME_MAX = 12_000.0;
    public static final double SEUIL_GLOBAL_POURCENT = 80.0;
    public static final double SEUIL_PAR_GAMME_POURCENT = 75.0;
    public static final double SEUIL_JOURNALIER_POURCENT = 60.0;

    private final ObjectifCommercialRepository objectifRepository;
    private final VenteRepository venteRepository;
    private final ProduitRepository produitRepository;

    public CalculerPrimesHebdoUseCase(ObjectifCommercialRepository objectifRepository,
                                       VenteRepository venteRepository,
                                       ProduitRepository produitRepository) {
        this.objectifRepository = objectifRepository;
        this.venteRepository = venteRepository;
        this.produitRepository = produitRepository;
    }

    public List<PrimeResultat> executer(LocalDate semaineDebut, LocalDate semaineFin) {
        List<ObjectifCommercial> objectifs = objectifRepository.listerParSemaine(semaineDebut);
        List<PrimeResultat> resultats = new ArrayList<>();

        for (ObjectifCommercial objectif : objectifs) {
            String matricule = objectif.getMatriculeCommercial();
            List<Vente> ventes = venteRepository.listerParPeriode(semaineDebut, semaineFin).stream()
                .filter(v -> v.getMatriculeCommercial().equals(matricule))
                .toList();

            JoursValides jv = filtrerJoursValides(ventes, objectif.objectifQuotidienGlobal());

            double caRealise = jv.ventes().stream().mapToDouble(Vente::montantTotal).sum();
            double caCible = objectif.getObjectifGlobalFCFA();
            double tauxGlobal = caCible > 0 ? (caRealise / caCible) * 100.0 : 0.0;

            Map<String, Double> caParGamme = calculerCAParGamme(jv.ventes());
            Map<String, Double> objectifsParGamme = objectif.getObjectifsParGammeFCFA();

            Map<String, Double> tauxParGamme = new LinkedHashMap<>();
            List<String> gammesValidees = new ArrayList<>();
            List<String> gammesBloquantes = new ArrayList<>();

            for (Map.Entry<String, Double> entry : objectifsParGamme.entrySet()) {
                String gamme = entry.getKey();
                double cibleGamme = entry.getValue();
                double caGamme = caParGamme.getOrDefault(gamme, 0.0);
                double tauxGamme = cibleGamme > 0 ? (caGamme / cibleGamme) * 100.0 : 0.0;
                tauxParGamme.put(gamme, tauxGamme);

                if (tauxGamme >= SEUIL_PAR_GAMME_POURCENT) {
                    gammesValidees.add(gamme);
                } else {
                    gammesBloquantes.add(gamme);
                }
            }

            boolean conditionGlobale = tauxGlobal >= SEUIL_GLOBAL_POURCENT;
            double montantPrime = conditionGlobale
                ? Math.min(MONTANT_PAR_GAMME * gammesValidees.size(), MONTANT_PRIME_MAX)
                : 0.0;
            boolean eligible = conditionGlobale && montantPrime > 0;
            double montantCashFCFA = montantPrime / 2.0;
            double montantNatureFCFA = montantPrime - montantCashFCFA;

            resultats.add(new PrimeResultat(matricule, caRealise, caCible, tauxGlobal,
                tauxParGamme, eligible, montantPrime, montantCashFCFA, montantNatureFCFA,
                gammesValidees, gammesBloquantes, conditionGlobale, jv.joursValides(), jv.joursTotal()));
        }

        resultats.sort(Comparator.comparingDouble(PrimeResultat::tauxGlobal).reversed());
        return resultats;
    }

    /**
     * Ne conserve, pour le calcul hebdomadaire, que les ventes des journées ayant
     * atteint le seuil des 60 % de l'objectif quotidien (règle de validation
     * journalière). Un objectif quotidien nul (objectif hebdo non défini) valide
     * toute journée par défaut plutôt que de tout exclure.
     */
    private JoursValides filtrerJoursValides(List<Vente> ventes, double objectifQuotidien) {
        Map<LocalDate, List<Vente>> parJour = ventes.stream().collect(Collectors.groupingBy(Vente::getDate));
        List<Vente> ventesValidees = new ArrayList<>();
        int joursValides = 0;
        for (List<Vente> ventesDuJour : parJour.values()) {
            double caJour = ventesDuJour.stream().mapToDouble(Vente::montantTotal).sum();
            boolean jourValide = objectifQuotidien <= 0 || (caJour / objectifQuotidien) * 100.0 >= SEUIL_JOURNALIER_POURCENT;
            if (jourValide) {
                joursValides++;
                ventesValidees.addAll(ventesDuJour);
            }
        }
        return new JoursValides(ventesValidees, joursValides, parJour.size());
    }

    private record JoursValides(List<Vente> ventes, int joursValides, int joursTotal) {}

    private Map<String, Double> calculerCAParGamme(List<Vente> ventes) {
        Map<String, Double> caParGamme = new HashMap<>();
        for (Vente vente : ventes) {
            for (var ligne : vente.getLignes()) {
                String gamme = trouverGamme(ligne.getCodeProduit());
                caParGamme.merge(gamme, ligne.montantHT(), Double::sum);
            }
        }
        return caParGamme;
    }

    /** Gamme réelle du produit (catalogue), et non plus devinée depuis le code. */
    private String trouverGamme(String codeProduit) {
        if (codeProduit == null || codeProduit.isBlank()) return "INCONNU";
        try {
            return produitRepository.trouverParCode(CodeProduit.de(codeProduit))
                .map(p -> p.getGamme())
                .orElse("INCONNU");
        } catch (RuntimeException e) {
            return "INCONNU";
        }
    }

    public record PrimeResultat(
        String matriculeCommercial,
        double caRealise,
        double caCible,
        double tauxGlobal,
        Map<String, Double> tauxParGamme,
        boolean eligible,
        double montantPrime,
        double montantCashFCFA,
        double montantNatureFCFA,
        List<String> gammesValidees,
        List<String> gammesBloquantes,
        boolean conditionGlobaleOK,
        int joursValides,
        int joursTotal
    ) {}
}
