package com.NTFOODS.Api_tanty.modules.comptabilite.application.objectif.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

import java.time.LocalDate;
import java.util.Map;

public record DefinirObjectifRequest(
    @NotBlank String matriculeCommercial,
    @NotNull LocalDate semaineDebut,
    @NotNull LocalDate semaineFin,
    @NotNull @Positive Double objectifGlobalFCFA,
    /** Clé = nom de la gamme (ex: "Bouillies", "Snacks", "Choco", "Ingrédients"), valeur = objectif FCFA. */
    Map<String, Double> objectifsParGammeFCFA
) {}
