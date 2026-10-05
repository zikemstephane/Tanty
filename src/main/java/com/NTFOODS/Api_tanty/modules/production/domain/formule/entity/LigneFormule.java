package com.NTFOODS.Api_tanty.modules.production.domain.formule.entity;

import java.math.BigDecimal;
import java.util.Objects;

/**
 * Ligne d'une {@link com.NTFOODS.Api_tanty.modules.production.domain.formule.aggregate.FormuleProduction} :
 * une matière première et la quantité (en kg) qu'elle représente pour la
 * base de la formule (ex. 40 kg de maïs pour une base de 50 kg de produit fini).
 *
 * Le nom de la matière première est libre — la formule n'est pas limitée aux
 * trois poudres historiques (maïs/soja/arachide) et couvre donc aussi bien
 * les catégories Chocolat que À Grignoter, par la donnée plutôt que par du code.
 */
public class LigneFormule {

    private final String matierePremiere;
    private final BigDecimal proportionKg;

    public LigneFormule(String matierePremiere, BigDecimal proportionKg) {
        this.matierePremiere = Objects.requireNonNull(matierePremiere, "La matière première est obligatoire").trim();
        if (this.matierePremiere.isBlank()) {
            throw new IllegalArgumentException("La matière première est obligatoire");
        }
        Objects.requireNonNull(proportionKg, "La proportion est obligatoire");
        if (proportionKg.signum() < 0) {
            throw new IllegalArgumentException("La proportion de « " + matierePremiere + " » ne peut pas être négative");
        }
        this.proportionKg = proportionKg;
    }

    public String getMatierePremiere() { return matierePremiere; }
    public BigDecimal getProportionKg() { return proportionKg; }
}
