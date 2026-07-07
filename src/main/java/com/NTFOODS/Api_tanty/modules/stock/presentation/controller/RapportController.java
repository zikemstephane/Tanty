package com.NTFOODS.Api_tanty.modules.stock.presentation.controller;

import com.NTFOODS.Api_tanty.shared.application.rapport.RapportFichier;
import com.NTFOODS.Api_tanty.shared.application.rapport.FormatRapport;
import com.NTFOODS.Api_tanty.modules.stock.application.rapport.usecase.GenererRapportEtatStockUseCase;
import com.NTFOODS.Api_tanty.modules.stock.application.rapport.usecase.GenererRapportMouvementsUseCase;
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

/**
 * Contrôleur REST — moteur de rapports Stock (PDF, Excel, CSV, JSON).
 */
@RestController
@RequestMapping("/api/stock/rapports")
@Tag(name = "Stock — Rapports", description = "Génération de rapports exportables (PDF/Excel/CSV/JSON)")
@SecurityRequirement(name = "bearerAuth")
public class RapportController {

    private final GenererRapportEtatStockUseCase genererRapportEtatStockUseCase;
    private final GenererRapportMouvementsUseCase genererRapportMouvementsUseCase;

    public RapportController(GenererRapportEtatStockUseCase genererRapportEtatStockUseCase,
                           GenererRapportMouvementsUseCase genererRapportMouvementsUseCase) {
        this.genererRapportEtatStockUseCase = genererRapportEtatStockUseCase;
        this.genererRapportMouvementsUseCase = genererRapportMouvementsUseCase;
    }

    @GetMapping("/etat-stock")
    @PreAuthorize("hasAnyRole('GESTIONNAIRE_STOCK', 'DIRECTEUR_GENERAL', 'CONTROLEUR_GENERAL', 'COMPTABLE', 'ADMIN')")
    @Operation(summary = "Rapport État des stocks (format = pdf|xlsx|csv|json)")
    public ResponseEntity<byte[]> etatStock(
        @RequestParam(required = false) String entrepot,
        @RequestParam(required = false) String produit,
        @RequestParam(required = false) String type,
        @RequestParam(required = false, defaultValue = "xlsx") String format) {
        RapportFichier fichier = genererRapportEtatStockUseCase.executer(entrepot, produit, type, FormatRapport.depuis(format));
        return reponseFichier(fichier);
    }

    @GetMapping("/mouvements")
    @PreAuthorize("hasAnyRole('GESTIONNAIRE_STOCK', 'DIRECTEUR_GENERAL', 'CONTROLEUR_GENERAL', 'COMPTABLE', 'ADMIN')")
    @Operation(summary = "Rapport Historique des mouvements (format = pdf|xlsx|csv|json)")
    public ResponseEntity<byte[]> mouvements(
        @RequestParam(required = false) String produit,
        @RequestParam(required = false) String entrepot,
        @RequestParam(required = false) String type,
        @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate debut,
        @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate fin,
        @RequestParam(required = false, defaultValue = "xlsx") String format) {
        RapportFichier fichier = genererRapportMouvementsUseCase.executer(produit, entrepot, type, debut, fin, FormatRapport.depuis(format));
        return reponseFichier(fichier);
    }

    private ResponseEntity<byte[]> reponseFichier(RapportFichier fichier) {
        return ResponseEntity.ok()
            .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + fichier.nomFichier() + "\"")
            .contentType(MediaType.parseMediaType(fichier.contentType()))
            .body(fichier.contenu());
    }
}
