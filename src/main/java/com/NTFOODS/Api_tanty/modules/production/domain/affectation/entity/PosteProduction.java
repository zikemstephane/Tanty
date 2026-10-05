package com.NTFOODS.Api_tanty.modules.production.domain.affectation.entity;

import com.NTFOODS.Api_tanty.modules.production.domain.affectation.enums.DureeAffectation;

import java.util.Objects;
import java.util.Set;

/**
 * Poste de production : référentiel des effectifs et objectifs de production (§5.10).
 *
 * <p>Autrefois figé dans l'enum {@code CodePoste}, ce référentiel est désormais
 * géré dans le CMS. Le code court reste la clé métier ; effectif de référence,
 * productions horaire/journalière et unité sont modifiables sans redéploiement.</p>
 */
public class PosteProduction {

    private final Long id;
    private final String code;
    private String libelle;
    private int effectifReference;
    private double productionParHeure;
    private double productionJournaliere;
    private String unite;
    private boolean actif;
    private int ordre;
    private final Set<String> categoriesMetier;

    public PosteProduction(Long id, String code, String libelle, int effectifReference,
                           double productionParHeure, double productionJournaliere,
                           String unite, boolean actif, int ordre, Set<String> categoriesMetier) {
        this.id = id;
        this.code = Objects.requireNonNull(code, "Le code est obligatoire");
        this.libelle = Objects.requireNonNull(libelle, "Le libellé est obligatoire");
        this.effectifReference = effectifReference;
        this.productionParHeure = productionParHeure;
        this.productionJournaliere = productionJournaliere;
        this.unite = Objects.requireNonNull(unite);
        this.actif = actif;
        this.ordre = ordre;
        this.categoriesMetier = categoriesMetier != null ? Set.copyOf(categoriesMetier) : Set.of();
    }

    public static PosteProduction creer(String code, String libelle, int effectifReference,
                                          double productionParHeure, double productionJournaliere,
                                          String unite, int ordre, Set<String> categoriesMetier) {
        return new PosteProduction(null, code, libelle, effectifReference, productionParHeure,
            productionJournaliere, unite, true, ordre, categoriesMetier);
    }

    public void mettreAJour(String libelle, int effectifReference, double productionParHeure,
                            double productionJournaliere, String unite, boolean actif, int ordre,
                            Set<String> categoriesMetier) {
        this.libelle = Objects.requireNonNull(libelle);
        this.effectifReference = effectifReference;
        this.productionParHeure = productionParHeure;
        this.productionJournaliere = productionJournaliere;
        this.unite = Objects.requireNonNull(unite);
        this.actif = actif;
        this.ordre = ordre;
    }

    public double calculerObjectifAjuste(int effectifReel, DureeAffectation duree) {
        if (productionJournaliere <= 0 || effectifReference <= 0) return 0.0;
        return (effectifReel / (double) effectifReference)
            * productionJournaliere
            * duree.getCoefficient();
    }

    public Long getId() { return id; }
    public String getCode() { return code; }
    public String getLibelle() { return libelle; }
    public int getEffectifReference() { return effectifReference; }
    public double getProductionParHeure() { return productionParHeure; }
    public double getProductionJournaliere() { return productionJournaliere; }
    public String getUnite() { return unite; }
    public boolean isActif() { return actif; }
    public int getOrdre() { return ordre; }
    public Set<String> getCategoriesMetier() { return categoriesMetier; }
}
