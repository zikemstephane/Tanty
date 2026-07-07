package com.NTFOODS.Api_tanty.modules.stock.presentation.controller;

import com.NTFOODS.Api_tanty.modules.stock.application.reporting.service.ReportingService;
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
@RequestMapping("/api/stock/rapports")
@Tag(name = "Stock — Rapports & Exports", description = "Génération et export de rapports puissants en Excel ou PDF")
@SecurityRequirement(name = "bearerAuth")
public class ReportingController {

    private final ReportingService reportingService;

    public ReportingController(ReportingService reportingService) {
        this.reportingService = reportingService;
    }

    @GetMapping("/stocks-mobiles/excel")
    @PreAuthorize("hasAnyRole('GESTIONNAIRE_STOCK', 'DIRECTEUR_GENERAL', 'CONTROLEUR_GENERAL', 'ADMIN')")
    @Operation(summary = "Export Excel — Stocks mobiles de tous les commerciaux")
    public ResponseEntity<byte[]> exportStocksMobilesExcel() throws Exception {
        byte[] data = reportingService.rapportStocksMobilesExcel();
        return buildFileResponse(data, "stocks_mobiles.xlsx",
            "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
    }

    @GetMapping("/stocks-mobiles/pdf")
    @PreAuthorize("hasAnyRole('GESTIONNAIRE_STOCK', 'DIRECTEUR_GENERAL', 'CONTROLEUR_GENERAL', 'ADMIN')")
    @Operation(summary = "Export PDF — Stocks mobiles de tous les commerciaux")
    public ResponseEntity<byte[]> exportStocksMobilesPdf() throws Exception {
        byte[] data = reportingService.rapportStocksMobilesPdf();
        return buildFileResponse(data, "stocks_mobiles.pdf", MediaType.APPLICATION_PDF_VALUE);
    }

    @GetMapping("/performances/excel")
    @PreAuthorize("hasAnyRole('GESTIONNAIRE_STOCK', 'DIRECTEUR_GENERAL', 'DIRECTEUR_COMMERCIAL', 'ADMIN')")
    @Operation(summary = "Export Excel — Performances des commerciaux")
    public ResponseEntity<byte[]> exportPerformancesExcel() throws Exception {
        byte[] data = reportingService.rapportPerformancesExcel();
        return buildFileResponse(data, "performances_commerciaux.xlsx",
            "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
    }

    @GetMapping("/performances/pdf")
    @PreAuthorize("hasAnyRole('GESTIONNAIRE_STOCK', 'DIRECTEUR_GENERAL', 'DIRECTEUR_COMMERCIAL', 'ADMIN')")
    @Operation(summary = "Export PDF — Performances des commerciaux")
    public ResponseEntity<byte[]> exportPerformancesPdf() throws Exception {
        byte[] data = reportingService.rapportPerformancesPdf();
        return buildFileResponse(data, "performances_commerciaux.pdf", MediaType.APPLICATION_PDF_VALUE);
    }

    // ─── STOCK CENTRAL ───────────────────────────────────────────────────

    @GetMapping("/stock-central/excel")
    @PreAuthorize("hasAnyRole('GESTIONNAIRE_STOCK', 'DIRECTEUR_GENERAL', 'CONTROLEUR_GENERAL', 'ADMIN')")
    @Operation(summary = "Export Excel — État du stock central")
    public ResponseEntity<byte[]> exportStockCentralExcel() throws Exception {
        byte[] data = reportingService.rapportStockCentralExcel();
        return buildFileResponse(data, "stock_central.xlsx",
            "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
    }

    @GetMapping("/stock-central/pdf")
    @PreAuthorize("hasAnyRole('GESTIONNAIRE_STOCK', 'DIRECTEUR_GENERAL', 'CONTROLEUR_GENERAL', 'ADMIN')")
    @Operation(summary = "Export PDF — État du stock central")
    public ResponseEntity<byte[]> exportStockCentralPdf() throws Exception {
        byte[] data = reportingService.rapportStockCentralPdf();
        return buildFileResponse(data, "stock_central.pdf", MediaType.APPLICATION_PDF_VALUE);
    }

    // ─── TAMPON ──────────────────────────────────────────────────────────

    @GetMapping("/tampon/excel")
    @PreAuthorize("hasAnyRole('GESTIONNAIRE_STOCK', 'DIRECTEUR_GENERAL', 'CONTROLEUR_GENERAL', 'ADMIN')")
    @Operation(summary = "Export Excel — État du tampon")
    public ResponseEntity<byte[]> exportTamponExcel() throws Exception {
        byte[] data = reportingService.rapportTamponExcel();
        return buildFileResponse(data, "tampon.xlsx",
            "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
    }

    @GetMapping("/tampon/pdf")
    @PreAuthorize("hasAnyRole('GESTIONNAIRE_STOCK', 'DIRECTEUR_GENERAL', 'CONTROLEUR_GENERAL', 'ADMIN')")
    @Operation(summary = "Export PDF — État du tampon")
    public ResponseEntity<byte[]> exportTamponPdf() throws Exception {
        byte[] data = reportingService.rapportTamponPdf();
        return buildFileResponse(data, "tampon.pdf", MediaType.APPLICATION_PDF_VALUE);
    }

    // ─── MOUVEMENTS ──────────────────────────────────────────────────────

    @GetMapping("/mouvements/excel")
    @PreAuthorize("hasAnyRole('GESTIONNAIRE_STOCK', 'DIRECTEUR_GENERAL', 'CONTROLEUR_GENERAL', 'ADMIN')")
    @Operation(summary = "Export Excel — Mouvements de stock par période")
    public ResponseEntity<byte[]> exportMouvementsExcel(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate debut,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate fin) throws Exception {
        byte[] data = reportingService.rapportMouvementsExcel(debut, fin);
        return buildFileResponse(data, "mouvements_stock.xlsx",
            "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
    }

    // ─── VALORISATION ────────────────────────────────────────────────────

    @GetMapping("/valorisation/excel")
    @PreAuthorize("hasAnyRole('DIRECTEUR_GENERAL', 'CONTROLEUR_GENERAL', 'COMPTABLE', 'ADMIN')")
    @Operation(summary = "Export Excel — Valorisation du stock")
    public ResponseEntity<byte[]> exportValorisationExcel() throws Exception {
        byte[] data = reportingService.rapportValorisationExcel();
        return buildFileResponse(data, "valorisation_stock.xlsx",
            "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
    }

    // ─── DOTATIONS VS VENTES ─────────────────────────────────────────────

    @GetMapping("/dotations-ventes/excel")
    @PreAuthorize("hasAnyRole('GESTIONNAIRE_STOCK', 'DIRECTEUR_GENERAL', 'DIRECTEUR_COMMERCIAL', 'ADMIN')")
    @Operation(summary = "Export Excel — Dotations vs ventes (taux de retour)")
    public ResponseEntity<byte[]> exportDotationsVsVentesExcel() throws Exception {
        byte[] data = reportingService.rapportDotationsVsVentesExcel();
        return buildFileResponse(data, "dotations_vs_ventes.xlsx",
            "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
    }

    // ─── ÉCARTS D'INVENTAIRE ─────────────────────────────────────────────

    @GetMapping("/ecarts-inventaire/excel")
    @PreAuthorize("hasAnyRole('GESTIONNAIRE_STOCK', 'DIRECTEUR_GENERAL', 'CONTROLEUR_GENERAL', 'ADMIN')")
    @Operation(summary = "Export Excel — Écarts d'inventaire (théorique vs compté)")
    public ResponseEntity<byte[]> exportEcartsInventaireExcel() throws Exception {
        byte[] data = reportingService.rapportEcartsInventaireExcel();
        return buildFileResponse(data, "ecarts_inventaire.xlsx",
            "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
    }

    // ─── ROTATION DES STOCKS ─────────────────────────────────────────────

    @GetMapping("/rotation/excel")
    @PreAuthorize("hasAnyRole('GESTIONNAIRE_STOCK', 'DIRECTEUR_GENERAL', 'DIRECTEUR_COMMERCIAL', 'ADMIN')")
    @Operation(summary = "Export Excel — Rotation des stocks (sorties / stock actuel)")
    public ResponseEntity<byte[]> exportRotationStockExcel() throws Exception {
        byte[] data = reportingService.rapportRotationStockExcel();
        return buildFileResponse(data, "rotation_stock.xlsx",
            "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
    }

    // ─── EXPORTS CSV ─────────────────────────────────────────────────────

    @GetMapping("/stock-central/csv")
    @PreAuthorize("hasAnyRole('GESTIONNAIRE_STOCK', 'DIRECTEUR_GENERAL', 'CONTROLEUR_GENERAL', 'ADMIN')")
    @Operation(summary = "Export CSV — État du stock central")
    public ResponseEntity<byte[]> exportStockCentralCsv() throws Exception {
        byte[] data = reportingService.rapportStockCentralCsv();
        return buildFileResponse(data, "stock_central.csv", "text/csv");
    }

    @GetMapping("/tampon/csv")
    @PreAuthorize("hasAnyRole('GESTIONNAIRE_STOCK', 'DIRECTEUR_GENERAL', 'CONTROLEUR_GENERAL', 'ADMIN')")
    @Operation(summary = "Export CSV — État du tampon")
    public ResponseEntity<byte[]> exportTamponCsv() throws Exception {
        byte[] data = reportingService.rapportTamponCsv();
        return buildFileResponse(data, "tampon.csv", "text/csv");
    }

    @GetMapping("/mouvements/csv")
    @PreAuthorize("hasAnyRole('GESTIONNAIRE_STOCK', 'DIRECTEUR_GENERAL', 'CONTROLEUR_GENERAL', 'ADMIN')")
    @Operation(summary = "Export CSV — Mouvements de stock par période")
    public ResponseEntity<byte[]> exportMouvementsCsv(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate debut,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate fin) throws Exception {
        byte[] data = reportingService.rapportMouvementsCsv(debut, fin);
        return buildFileResponse(data, "mouvements_stock.csv", "text/csv");
    }

    private ResponseEntity<byte[]> buildFileResponse(byte[] data, String filename, String contentType) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.parseMediaType(contentType));
        headers.setContentDispositionFormData("attachment", filename);
        headers.setContentLength(data.length);
        return ResponseEntity.ok().headers(headers).body(data);
    }
}
