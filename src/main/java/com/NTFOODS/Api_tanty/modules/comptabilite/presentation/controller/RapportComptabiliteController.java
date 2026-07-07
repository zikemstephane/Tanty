package com.NTFOODS.Api_tanty.modules.comptabilite.presentation.controller;

import com.NTFOODS.Api_tanty.modules.comptabilite.application.rapport.usecase.GenererRapportDecaissementsUseCase;
import com.NTFOODS.Api_tanty.modules.comptabilite.application.rapport.usecase.GenererRapportFacturesUseCase;
import com.NTFOODS.Api_tanty.modules.comptabilite.application.rapport.usecase.GenererRapportPrimesUseCase;
import com.NTFOODS.Api_tanty.modules.comptabilite.application.rapport.usecase.GenererRapportVersementsUseCase;
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
@RequestMapping("/api/comptabilite/rapports")
@Tag(name = "Comptabilité — Rapports", description = "Rapports comptables exportables")
@SecurityRequirement(name = "bearerAuth")
public class RapportComptabiliteController {

    private final GenererRapportFacturesUseCase genererRapportFacturesUseCase;
    private final GenererRapportDecaissementsUseCase genererRapportDecaissementsUseCase;
    private final GenererRapportVersementsUseCase genererRapportVersementsUseCase;
    private final GenererRapportPrimesUseCase genererRapportPrimesUseCase;

    public RapportComptabiliteController(GenererRapportFacturesUseCase genererRapportFacturesUseCase,
                                          GenererRapportDecaissementsUseCase genererRapportDecaissementsUseCase,
                                          GenererRapportVersementsUseCase genererRapportVersementsUseCase,
                                          GenererRapportPrimesUseCase genererRapportPrimesUseCase) {
        this.genererRapportFacturesUseCase = genererRapportFacturesUseCase;
        this.genererRapportDecaissementsUseCase = genererRapportDecaissementsUseCase;
        this.genererRapportVersementsUseCase = genererRapportVersementsUseCase;
        this.genererRapportPrimesUseCase = genererRapportPrimesUseCase;
    }

    @GetMapping("/factures")
    @PreAuthorize("hasAnyRole('COMMERCIAL', 'COMPTABLE', 'CONTROLEUR_GENERAL', 'DIRECTEUR_GENERAL', 'ADMIN')")
    @Operation(summary = "Rapport des factures (format = pdf|xlsx|csv|json)")
    public ResponseEntity<byte[]> factures(
        @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate debut,
        @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate fin,
        @RequestParam(required = false, defaultValue = "xlsx") String format) {
        RapportFichier f = genererRapportFacturesUseCase.executer(debut, fin, FormatRapport.depuis(format));
        return ResponseEntity.ok()
            .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + f.nomFichier() + "\"")
            .contentType(MediaType.parseMediaType(f.contentType()))
            .body(f.contenu());
    }

    @GetMapping("/decaissements")
    @PreAuthorize("hasAnyRole('COMMERCIAL', 'COMPTABLE', 'CONTROLEUR_GENERAL', 'DIRECTEUR_GENERAL', 'ADMIN')")
    @Operation(summary = "Rapport des décaissements (format = pdf|xlsx|csv|json)")
    public ResponseEntity<byte[]> decaissements(
        @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate debut,
        @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate fin,
        @RequestParam(required = false, defaultValue = "xlsx") String format) {
        RapportFichier f = genererRapportDecaissementsUseCase.executer(debut, fin, FormatRapport.depuis(format));
        return ResponseEntity.ok()
            .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + f.nomFichier() + "\"")
            .contentType(MediaType.parseMediaType(f.contentType()))
            .body(f.contenu());
    }

    @GetMapping("/versements")
    @PreAuthorize("hasAnyRole('COMMERCIAL', 'COMPTABLE', 'CONTROLEUR_GENERAL', 'DIRECTEUR_GENERAL', 'ADMIN')")
    @Operation(summary = "Rapport des versements (format = pdf|xlsx|csv|json)")
    public ResponseEntity<byte[]> versements(
        @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate debut,
        @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate fin,
        @RequestParam(required = false, defaultValue = "xlsx") String format) {
        RapportFichier f = genererRapportVersementsUseCase.executer(debut, fin, FormatRapport.depuis(format));
        return ResponseEntity.ok()
            .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + f.nomFichier() + "\"")
            .contentType(MediaType.parseMediaType(f.contentType()))
            .body(f.contenu());
    }

    @GetMapping("/primes")
    @PreAuthorize("hasAnyRole('COMMERCIAL', 'COMPTABLE', 'CONTROLEUR_GENERAL', 'DIRECTEUR_GENERAL', 'ADMIN')")
    @Operation(summary = "Rapport des primes pour une semaine (format = pdf|xlsx|csv|json)")
    public ResponseEntity<byte[]> primes(
        @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate semaineDebut,
        @RequestParam(required = false, defaultValue = "xlsx") String format) {
        RapportFichier f = genererRapportPrimesUseCase.executer(semaineDebut, FormatRapport.depuis(format));
        return ResponseEntity.ok()
            .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + f.nomFichier() + "\"")
            .contentType(MediaType.parseMediaType(f.contentType()))
            .body(f.contenu());
    }
}
