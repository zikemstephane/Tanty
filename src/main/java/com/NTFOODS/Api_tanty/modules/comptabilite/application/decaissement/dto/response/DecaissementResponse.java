package com.NTFOODS.Api_tanty.modules.comptabilite.application.decaissement.dto.response;

import com.NTFOODS.Api_tanty.modules.comptabilite.domain.decaissement.enums.StatutDecaissement;
import com.NTFOODS.Api_tanty.modules.comptabilite.domain.decaissement.enums.TypeDepense;

import java.time.LocalDate;
import java.time.LocalDateTime;

public record DecaissementResponse(
    Long id,
    String numeroDecaissement,
    String typeDepense,
    String statut,
    String matriculeProposant,
    String matriculeValidateurCG,
    String matriculeValidateurDG,
    String matriculeExecuteur,
    double montantFCFA,
    String beneficiaire,
    LocalDate dateDepense,
    String motif,
    String motifAnnulation,
    LocalDateTime dateCreation
) {}
