package com.NTFOODS.Api_tanty.modules.production.application.affectation.dto.response;

import com.NTFOODS.Api_tanty.modules.production.domain.affectation.enums.StatutAffectation;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

public record AffectationJournaliereResponse(
    Long id,
    String referencePPH,
    LocalDate date,
    String matriculeAgent,
    StatutAffectation statut,
    List<LigneAffectationResponse> lignes,
    List<SaisieRealisationResponse> saisies,
    List<ClassementPosteResponse> classement,
    LocalDateTime dateCreation,
    LocalDateTime dateModification
) {
    public record LigneAffectationResponse(
        String codePoste,
        String libellePoste,
        int effectifReference,
        int effectifReel,
        String duree,
        double objectifAjuste,
        String unite,
        List<String> matriculesEmployes
    ) {}

    public record SaisieRealisationResponse(
        String codePoste,
        String libellePoste,
        double quantiteRealisee,
        double objectifAjuste,
        double pctRealisation,
        String unite,
        List<ProductionIndividuelleResponse> productionsIndividuelles,
        DetailsRealisationPosteResponse details,
        List<String> warnings,
        LocalDateTime dateEnregistrement
    ) {}

    public record ProductionIndividuelleResponse(
        String matriculeEmploye,
        String nomEmploye,
        double quantiteRealisee,
        String unite
    ) {}

    public record ClassementPosteResponse(
        int rang,
        String codePoste,
        String libellePoste,
        double quantiteRealisee,
        double objectifAjuste,
        double pctRealisation,
        String unite
    ) {}
}
