package com.NTFOODS.Api_tanty.modules.production.domain.affectation.entity;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

/**
 * Saisie de la réalisation journalière pour un poste de production.
 * Le poste est identifié par son code métier (String), l'unité est portée
 * par la réponse pour rester indépendante du référentiel dans le domaine.
 */
public class SaisieRealisationPoste {

    private final String poste;
    private final String libellePoste;
    private final String unite;
    private final double quantiteRealisee;
    private final double objectifAjuste;
    private final List<ProductionIndividuelle> productionsIndividuelles;
    private final DetailsRealisationPoste details;
    private final List<String> warnings;
    private final LocalDateTime dateEnregistrement;

    public SaisieRealisationPoste(String poste, String libellePoste, String unite, double quantiteRealisee,
                                   double objectifAjuste, List<ProductionIndividuelle> productionsIndividuelles,
                                   DetailsRealisationPoste details, List<String> warnings) {
        if (quantiteRealisee < 0) throw new IllegalArgumentException("La quantité réalisée ne peut être négative");
        this.poste = Objects.requireNonNull(poste);
        this.libellePoste = libellePoste != null ? libellePoste : poste;
        this.unite = unite != null ? unite : "";
        this.quantiteRealisee = quantiteRealisee;
        this.objectifAjuste = objectifAjuste;
        this.productionsIndividuelles = productionsIndividuelles != null
            ? List.copyOf(productionsIndividuelles) : List.of();
        this.details = details != null ? details : DetailsRealisationPoste.vide();
        this.warnings = warnings != null ? List.copyOf(warnings) : List.of();
        this.dateEnregistrement = LocalDateTime.now();
    }

    /** % de réalisation par rapport à l'objectif ajusté, plafonné à 200 %. */
    public double getPctRealisation() {
        if (objectifAjuste <= 0) return 0.0;
        return Math.min((quantiteRealisee / objectifAjuste) * 100.0, 200.0);
    }

    public String getUnite() { return unite; }

    public String getPoste() { return poste; }
    public String getLibellePoste() { return libellePoste; }
    public double getQuantiteRealisee() { return quantiteRealisee; }
    public double getObjectifAjuste() { return objectifAjuste; }
    public List<ProductionIndividuelle> getProductionsIndividuelles() { return Collections.unmodifiableList(productionsIndividuelles); }
    public DetailsRealisationPoste getDetails() { return details; }
    public List<String> getWarnings() { return Collections.unmodifiableList(warnings); }
    public LocalDateTime getDateEnregistrement() { return dateEnregistrement; }
}
