package com.NTFOODS.Api_tanty.modules.stock.domain.catalogue.model;

import com.NTFOODS.Api_tanty.modules.stock.domain.catalogue.enums.TypeEmballage;

/** Résultat de {@link Conditionnement#calculerComposantsEmballage} : la quantité d'un composant d'emballage nécessaire pour honorer une commande. */
public record BesoinEmballage(TypeEmballage type, long quantite) {}
