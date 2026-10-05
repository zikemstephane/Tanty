package com.NTFOODS.Api_tanty.modules.production.domain.broyage.service;

import com.NTFOODS.Api_tanty.modules.production.domain.broyage.aggregate.SessionBroyage;
import com.NTFOODS.Api_tanty.modules.production.domain.broyage.enums.OperationBroyage;
import com.NTFOODS.Api_tanty.modules.production.domain.broyage.enums.StatutSessionBroyage;
import com.NTFOODS.Api_tanty.modules.production.domain.formule.entity.BesoinMatierePremiere;

import java.math.BigDecimal;
import java.text.Normalizer;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.TreeMap;

/**
 * Avancement du broyage d'un plan PPH, par matière première (maïs, soja, arachide).
 *
 * Prévu : besoins en matières du plan (issus des formules, sur toute la période).
 * Écrasé : nette broyée des sessions clôturées ; en cours : poste écrasage des sessions encore ouvertes.
 *
 * Lecture seule : le plan garde ses prévisions d'origine, et ce calcul n'écrit rien.
 */
public final class AvancementBroyage {

    private AvancementBroyage() {}

    /**
     * Avancement d'une matière. {@code suivie} est faux pour une matière qu'aucune session de broyage ne
     * peut écraser (ex. une matière de formule hors maïs, soja, arachide) : son écrasé reste à zéro et
     * elle ne doit pas être présentée comme un retard de broyage.
     */
    public record Matiere(String code, boolean suivie, double prevuKg, double ecraseKg, double enCoursKg) {
        public double restantKg() { return suivie ? Math.max(0, prevuKg - ecraseKg) : 0; }
        public double ecartKg() { return ecraseKg - prevuKg; }
        public double pourcent() { return suivie && prevuKg > 0 ? Math.min(100, ecraseKg / prevuKg * 100) : 0; }
    }

    public record Resultat(String referencePPH, int nbSessions, int nbSessionsCloturees, List<Matiere> matieres) {}

    /** Matières qu'une session de broyage peut écraser (les types de poudre du broyage). */
    private static final Set<String> MATIERES_SUIVIES = Set.of("MAIS", "SOJA", "ARACHIDE");

    /** Variantes courantes de noms de matières, ramenées au code du type de poudre. */
    private static final Map<String, String> ALIAS = Map.of(
        "MAIZE", "MAIS", "CORN", "MAIS",
        "SOYA", "SOJA", "SOYBEAN", "SOJA",
        "ARACHIDES", "ARACHIDE", "PEANUT", "ARACHIDE", "PEANUTS", "ARACHIDE");

    /** Code canonique d'une matière : sans accents, en majuscules, variantes ramenées au type de poudre. */
    public static String codeMatiere(String nom) {
        Objects.requireNonNull(nom);
        String code = Normalizer.normalize(nom, Normalizer.Form.NFD).replaceAll("\\p{M}", "")
            .trim().replaceAll("\\s+", " ").toUpperCase();
        return ALIAS.getOrDefault(code, code);
    }

    public static boolean estSuivie(String code) {
        return MATIERES_SUIVIES.contains(code);
    }

    /**
     * @param besoinsPrevus besoins en matières du plan (toutes les journées confondues)
     * @param sessions sessions de broyage du plan ; celles d'un autre plan sont ignorées
     */
    public static Resultat calculer(String referencePPH, List<BesoinMatierePremiere> besoinsPrevus,
                                    List<SessionBroyage> sessions) {
        Objects.requireNonNull(referencePPH);
        TreeMap<String, double[]> cumul = new TreeMap<>(); // [prévu, écrasé, en cours]

        for (BesoinMatierePremiere b : besoinsPrevus) {
            cumul.computeIfAbsent(codeMatiere(b.matierePremiere()), c -> new double[3])[0] += quantite(b.quantiteKg());
        }

        int nbSessions = 0;
        int nbCloturees = 0;
        for (SessionBroyage s : sessions) {
            if (!referencePPH.equals(s.getReferencePPH())) continue;
            nbSessions++;
            String code = s.getTypePoudre().name();
            double[] ligne = cumul.computeIfAbsent(code, c -> new double[3]);
            if (s.getStatut() == StatutSessionBroyage.CLOTUREE) {
                nbCloturees++;
                ligne[1] += s.getQuantiteNetteBroyeeKg() == null ? 0 : s.getQuantiteNetteBroyeeKg();
            } else {
                ligne[2] += s.getRealisations().stream()
                    .filter(r -> r.getOperation() == OperationBroyage.ECRASAGE)
                    .mapToDouble(r -> r.getRealiseKg())
                    .sum();
            }
        }

        List<Matiere> matieres = cumul.entrySet().stream()
            .map(e -> new Matiere(e.getKey(), estSuivie(e.getKey()),
                e.getValue()[0], e.getValue()[1], e.getValue()[2]))
            .toList();
        return new Resultat(referencePPH, nbSessions, nbCloturees, matieres);
    }

    private static double quantite(BigDecimal kg) {
        return kg == null ? 0 : kg.doubleValue();
    }
}
