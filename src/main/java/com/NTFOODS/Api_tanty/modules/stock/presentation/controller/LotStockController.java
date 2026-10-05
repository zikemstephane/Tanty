package com.NTFOODS.Api_tanty.modules.stock.presentation.controller;

import com.NTFOODS.Api_tanty.modules.stock.application.lot.dto.request.ConsommerLotsRequest;
import com.NTFOODS.Api_tanty.modules.stock.application.lot.dto.request.CreerLotRequest;
import com.NTFOODS.Api_tanty.modules.stock.application.lot.dto.response.LotStockResponse;
import com.NTFOODS.Api_tanty.modules.stock.application.lot.usecase.GererLotsUseCase;
import com.NTFOODS.Api_tanty.modules.stock.domain.lot.enums.StatutLot;
import com.NTFOODS.Api_tanty.shared.application.dto.ApiResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Contrôleur REST — Lots de stock (traçabilité et péremption).
 *
 * À ne pas confondre avec {@code production.presentation.controller.LotController},
 * qui suit les lots de fabrication : celui-là dit ce que l'usine a produit,
 * celui-ci ce qu'il reste en entrepôt et jusqu'à quand.
 *
 * Un lot rattache des marchandises à leur arrivage et à leur date limite. Il
 * ne porte pas l'autorité sur les quantités détenues : celle-ci reste aux
 * niveaux de stock, alimentés par les mouvements.
 */
@RestController
@RequestMapping("/api/stock/lots")
@Tag(name = "Stock — Lots", description = "Traçabilité par lot et suivi des péremptions (sortie FEFO)")
@SecurityRequirement(name = "bearerAuth")
public class LotStockController {

    private final GererLotsUseCase gererLotsUseCase;

    public LotStockController(GererLotsUseCase gererLotsUseCase) {
        this.gererLotsUseCase = gererLotsUseCase;
    }

    @GetMapping
    @PreAuthorize("isAuthenticated()")
    @Operation(summary = "Lister les lots (filtres : produit, sku, entrepôt)")
    public ResponseEntity<ApiResponse<List<LotStockResponse>>> lister(
        @RequestParam(required = false) String produit,
        @RequestParam(required = false) String sku,
        @RequestParam(required = false) String entrepot) {
        List<LotStockResponse> lots;
        if (produit != null && !produit.isBlank()) {
            lots = gererLotsUseCase.listerParProduit(produit);
        } else if (sku != null && !sku.isBlank()) {
            lots = gererLotsUseCase.listerParSku(sku);
        } else if (entrepot != null && !entrepot.isBlank()) {
            lots = gererLotsUseCase.listerParEntrepot(entrepot);
        } else {
            lots = gererLotsUseCase.lister();
        }
        return ResponseEntity.ok(ApiResponse.succes(lots, lots.size() + " lot(s)"));
    }

    @GetMapping("/perimant-bientot")
    @PreAuthorize("isAuthenticated()")
    @Operation(summary = "Lots qui périment dans les prochains jours (défaut : 30)")
    public ResponseEntity<ApiResponse<List<LotStockResponse>>> perimantBientot(
        @RequestParam(defaultValue = "30") int jours) {
        List<LotStockResponse> lots = gererLotsUseCase.listerPerimantBientot(jours);
        return ResponseEntity.ok(ApiResponse.succes(lots,
            lots.size() + " lot(s) périmant dans les " + jours + " jours"));
    }

    @GetMapping("/perimes")
    @PreAuthorize("isAuthenticated()")
    @Operation(summary = "Lots dont la date de péremption est atteinte ou dépassée")
    public ResponseEntity<ApiResponse<List<LotStockResponse>>> perimes() {
        List<LotStockResponse> lots = gererLotsUseCase.listerPerimes();
        return ResponseEntity.ok(ApiResponse.succes(lots, lots.size() + " lot(s) périmé(s)"));
    }

    @GetMapping("/{numeroLot}")
    @PreAuthorize("isAuthenticated()")
    @Operation(summary = "Consulter un lot")
    public ResponseEntity<ApiResponse<LotStockResponse>> consulter(@PathVariable String numeroLot) {
        return ResponseEntity.ok(ApiResponse.succes(gererLotsUseCase.consulter(numeroLot)));
    }

    @PostMapping
    @PreAuthorize("hasAuthority('STOCK_LOT_GERER')")
    @Operation(summary = "Déclarer un lot")
    public ResponseEntity<ApiResponse<LotStockResponse>> creer(@Valid @RequestBody CreerLotRequest request) {
        LotStockResponse response = gererLotsUseCase.creer(request);
        return ResponseEntity.status(HttpStatus.CREATED)
            .body(ApiResponse.succes(response, "Lot déclaré : " + response.numeroLot()));
    }

    @PostMapping("/consommer")
    @PreAuthorize("hasAuthority('STOCK_LOT_GERER')")
    @Operation(summary = "Prélever une quantité au plus proche de la péremption (FEFO)",
        description = "Rend les lots effectivement entamés. Ne modifie pas les niveaux de stock : " +
                      "la traçabilité dit de quel lot vient la marchandise, le mouvement dit combien il en reste.")
    public ResponseEntity<ApiResponse<List<LotStockResponse>>> consommer(
        @Valid @RequestBody ConsommerLotsRequest request) {
        List<LotStockResponse> touches = gererLotsUseCase.consommer(request);
        return ResponseEntity.ok(ApiResponse.succes(touches,
            request.quantite() + " unité(s) prélevée(s) sur " + touches.size() + " lot(s)"));
    }

    @PatchMapping("/{numeroLot}/statut")
    @PreAuthorize("hasAuthority('STOCK_LOT_GERER')")
    @Operation(summary = "Changer le statut d'un lot (mise en quarantaine, remise à disposition…)")
    public ResponseEntity<ApiResponse<LotStockResponse>> changerStatut(
        @PathVariable String numeroLot, @RequestParam StatutLot statut) {
        return ResponseEntity.ok(ApiResponse.succes(
            gererLotsUseCase.changerStatut(numeroLot, statut), "Statut du lot mis à jour"));
    }

    @DeleteMapping("/{numeroLot}")
    @PreAuthorize("hasAuthority('STOCK_LOT_GERER')")
    @Operation(summary = "Retirer un lot du suivi",
        description = "Le lot reste en base — on ne supprime pas une trace. Refusé s'il reste de la marchandise.")
    public ResponseEntity<ApiResponse<LotStockResponse>> retirer(@PathVariable String numeroLot) {
        return ResponseEntity.ok(ApiResponse.succes(gererLotsUseCase.retirer(numeroLot), "Lot retiré du suivi"));
    }

    @PostMapping("/marquer-perimes")
    @PreAuthorize("hasAuthority('STOCK_LOT_GERER')")
    @Operation(summary = "Aligner le statut des lots dont la date est dépassée")
    public ResponseEntity<ApiResponse<Integer>> marquerPerimes() {
        int nombre = gererLotsUseCase.marquerPerimes();
        return ResponseEntity.ok(ApiResponse.succes(nombre, nombre + " lot(s) passé(s) en périmé"));
    }
}
