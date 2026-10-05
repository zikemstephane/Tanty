package com.NTFOODS.Api_tanty.modules.production.domain.dashboard.service;

import com.NTFOODS.Api_tanty.modules.production.domain.affectation.entity.LigneAffectationPoste;
import com.NTFOODS.Api_tanty.modules.production.domain.affectation.entity.SaisieRealisationPoste;
import com.NTFOODS.Api_tanty.modules.production.domain.pph.entity.LignePPH;

import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.HashSet;

/**
 * Calculs purs du tableau de bord du responsable de salle : avancement d'un plan et résumé de
 * l'affectation du jour. Aucune dépendance d'infrastructure, donc testables sans base.
 */
public final class SyntheseResponsableSalle {

    private SyntheseResponsableSalle() {}

    /** Avancement d'un plan : réalisé ÷ objectif sur toutes ses lignes. pct est null sans objectif. */
    public record Avancement(int nbProduits, long objectif, long realise, Double pct) {}

    /** Résumé de l'affectation d'une journée. */
    public record Affectation(boolean existe, String statut, int postes, int employesAffectes,
                              int saisies, int objectifsAtteints, int ecarts, double quantiteRealisee) {
        public static Affectation absente() {
            return new Affectation(false, null, 0, 0, 0, 0, 0, 0);
        }
    }

    public static Avancement avancementPph(List<LignePPH> lignes) {
        Objects.requireNonNull(lignes);
        long objectif = lignes.stream().mapToLong(LignePPH::getObjectifSemaine).sum();
        long realise = lignes.stream().mapToLong(LignePPH::getProductionRealisee).sum();
        Double pct = objectif > 0 ? Math.round(realise * 1000.0 / objectif) / 10.0 : null;
        return new Avancement(lignes.size(), objectif, realise, pct);
    }

    /**
     * @param statut libellé du statut de l'affectation (BROUILLON / VALIDEE)
     * @param lignes postes affectés avec leurs employés
     * @param saisies réalisations saisies par poste
     */
    public static Affectation resumeAffectation(String statut, List<LigneAffectationPoste> lignes,
                                                List<SaisieRealisationPoste> saisies) {
        Set<String> employes = new HashSet<>();
        lignes.forEach(l -> employes.addAll(l.getMatriculesEmployes()));
        int atteints = (int) saisies.stream().filter(s -> s.getPctRealisation() >= 100).count();
        int ecarts = saisies.stream().mapToInt(s -> s.getWarnings().size()).sum();
        double quantite = saisies.stream().mapToDouble(SaisieRealisationPoste::getQuantiteRealisee).sum();
        return new Affectation(true, statut, lignes.size(), employes.size(), saisies.size(), atteints, ecarts, quantite);
    }
}
