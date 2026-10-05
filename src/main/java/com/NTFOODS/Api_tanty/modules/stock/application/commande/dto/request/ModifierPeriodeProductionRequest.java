package com.NTFOODS.Api_tanty.modules.stock.application.commande.dto.request;

import java.time.LocalDate;

/** Les deux dates sont facultatives mais solidaires (les deux, ou aucune — cf. BonCommande.definirPeriodeProduction). */
public record ModifierPeriodeProductionRequest(
    LocalDate dateDebutProduction,
    LocalDate dateFinProduction
) {}
