package com.NTFOODS.Api_tanty.modules.production.domain.formule.entity;

import java.math.BigDecimal;

/** Résultat du calcul d'une {@link com.NTFOODS.Api_tanty.modules.production.domain.formule.aggregate.FormuleProduction} : la quantité (kg) d'une matière première nécessaire pour un poids total de produit fini donné. */
public record BesoinMatierePremiere(String matierePremiere, BigDecimal quantiteKg) {}
