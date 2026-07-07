package com.NTFOODS.Api_tanty.modules.production.presentation.controller;

import com.NTFOODS.Api_tanty.modules.production.application.rapport.usecase.GenererRapportComparatifBCUseCase;
import com.NTFOODS.Api_tanty.modules.production.application.rapport.usecase.GenererRapportHebdomadaireUseCase;
import com.NTFOODS.Api_tanty.modules.production.application.rapport.usecase.GenererRapportLotsUseCase;
import com.NTFOODS.Api_tanty.shared.application.dto.ApiResponse;
import com.NTFOODS.Api_tanty.shared.application.rapport.FormatRapport;
import com.NTFOODS.Api_tanty.shared.application.rapport.RapportFichier;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;

@RestController
@RequestMapping("/api/production/rapports")
@Tag(name = "Production — Rapports", description = "Rapports de production exportables (journaliers et hebdomadaires)")
@SecurityRequirement(name = "bearerAuth")
public class RapportProductionController {

    private final GenererRapportLotsUseCase genererRapportLotsUseCase;
    private final GenererRapportHebdomadaireUseCase genererRapportHebdomadaireUseCase;
    private final GenererRapportComparatifBCUseCase genererRapportComparatifBCUseCase;

    public RapportProductionController(GenererRapportLotsUseCase genererRapportLotsUseCase,
                                        GenererRapportHebdomadaireUseCase genererRapportHebdomadaireUseCase,
                                        GenererRapportComparatifBCUseCase genererRapportComparatifBCUseCase) {
        this.genererRapportLotsUseCase = genererRapportLotsUseCase;
        this.genererRapportHebdomadaireUseCase = genererRapportHebdomadaireUseCase;
        this.genererRapportComparatifBCUseCase = genererRapportComparatifBCUseCase;
    }

    @GetMapping("/lots")
    @PreAuthorize("hasAnyRole('CHEF_PRODUCTION', 'DIRECTEUR_GENERAL', 'GESTIONNAIRE_STOCK', 'ADMIN')")
    @Operation(summary = "Rapport des lots de production (format = pdf|xlsx|csv|json)")
    public ResponseEntity<byte[]> lots(
        @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate debut,
        @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate fin,
        @RequestParam(required = false, defaultValue = "xlsx") String format) {
        RapportFichier f = genererRapportLotsUseCase.executer(debut, fin, FormatRapport.depuis(format));
        return ResponseEntity.ok()
            .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + f.nomFichier() + "\"")
            .contentType(MediaType.parseMediaType(f.contentType()))
            .body(f.contenu());
    }

    @GetMapping("/hebdomadaire")
    @PreAuthorize("hasAnyRole('CHEF_PRODUCTION', 'RESPONSABLE_SALLE', 'DIRECTEUR_GENERAL', 'ADMIN')")
    @Operation(summary = "Rapport hebdomadaire de production (généré automatiquement à partir des saisies journalières)",
        description = "Inclut : quantités par référence, taux d'avancement BC, pertes machiniste, bilan des fûts, comparaison prévu/réalisé")
    public ResponseEntity<ApiResponse<GenererRapportHebdomadaireUseCase.RapportHebdomadaireProduction>> hebdomadaire(
        @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate debut,
        @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate fin) {
        var rapport = genererRapportHebdomadaireUseCase.executer(debut, fin);
        return ResponseEntity.ok(ApiResponse.succes(rapport, "Rapport hebdomadaire généré"));
    }

    @GetMapping("/comparatif-bc")
    @PreAuthorize("hasAnyRole('CHEF_PRODUCTION', 'DIRECTEUR_GENERAL', 'ADMIN')")
    @Operation(summary = "Rapport comparatif BC vs Production",
        description = "Compare les quantités commandées (via les PPH liés à un BC) avec les quantités réellement produites (via les lots validés). Met en évidence les écarts.")
    public ResponseEntity<ApiResponse<GenererRapportComparatifBCUseCase.RapportComparatifBC>> comparatifBC(
        @RequestParam String referenceBC) {
        var rapport = genererRapportComparatifBCUseCase.executer(referenceBC);
        return ResponseEntity.ok(ApiResponse.succes(rapport, "Rapport comparatif BC généré"));
    }

    @GetMapping("/responsable-salle")
    @PreAuthorize("hasAnyRole('CHEF_PRODUCTION', 'RESPONSABLE_SALLE', 'DIRECTEUR_GENERAL', 'ADMIN')")
    @Operation(summary = "Rapport du responsable de salle",
        description = "Vue hebdomadaire filtrée pour le responsable de salle : performance équipe, délais, volumes et écarts prévu/réalisé.")
    public ResponseEntity<ApiResponse<GenererRapportHebdomadaireUseCase.RapportHebdomadaireProduction>> responsableSalle(
        @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate debut,
        @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate fin) {
        var rapport = genererRapportHebdomadaireUseCase.executer(debut, fin);
        return ResponseEntity.ok(ApiResponse.succes(rapport, "Rapport du responsable de salle généré"));
    }
}
