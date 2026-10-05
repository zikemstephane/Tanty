package com.NTFOODS.Api_tanty.modules.stock.presentation.controller;

import com.NTFOODS.Api_tanty.modules.stock.application.niveau.dto.response.NiveauStockDetailResponse;
import com.NTFOODS.Api_tanty.modules.stock.application.niveau.usecase.ajuster.AjusterStockUseCase;
import com.NTFOODS.Api_tanty.modules.stock.application.niveau.usecase.consulter.ConsulterNiveauxDetaillesUseCase;
import com.NTFOODS.Api_tanty.modules.stock.application.niveau.usecase.transferer.TransfererVersTamponUseCase;
import com.NTFOODS.Api_tanty.shared.application.dto.ApiResponse;
import com.NTFOODS.Api_tanty.shared.infrastructure.security.AuthenticatedUserResolver;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Contrôleur REST — Niveaux de stock détaillés (écrans Inventaire / Alertes web).
 */
@RestController
@RequestMapping("/api/stock/levels")
@Tag(name = "Stock — Niveaux détaillés", description = "État enrichi, alertes, ajustement et transfert vers tampon")
@SecurityRequirement(name = "bearerAuth")
public class NiveauStockController {

    private final ConsulterNiveauxDetaillesUseCase consulterUseCase;
    private final AjusterStockUseCase ajusterUseCase;
    private final TransfererVersTamponUseCase transfererUseCase;
    private final AuthenticatedUserResolver userResolver;

    public NiveauStockController(ConsulterNiveauxDetaillesUseCase consulterUseCase,
                                 AjusterStockUseCase ajusterUseCase,
                                 TransfererVersTamponUseCase transfererUseCase,
                                 AuthenticatedUserResolver userResolver) {
        this.consulterUseCase = consulterUseCase;
        this.ajusterUseCase = ajusterUseCase;
        this.transfererUseCase = transfererUseCase;
        this.userResolver = userResolver;
    }

    @GetMapping
    @PreAuthorize("isAuthenticated()")
    @Operation(summary = "Niveaux de stock enrichis (filtre optionnel par code entrepôt)")
    public ResponseEntity<ApiResponse<List<NiveauStockDetailResponse>>> lister(
        @RequestParam(required = false) String entrepot) {
        List<NiveauStockDetailResponse> niveaux = consulterUseCase.lister(entrepot);
        return ResponseEntity.ok(ApiResponse.succes(niveaux, niveaux.size() + " ligne(s) de stock"));
    }

    @GetMapping("/warehouse/{entrepotId}")
    @PreAuthorize("isAuthenticated()")
    @Operation(summary = "Niveaux d'un entrepôt par id")
    public ResponseEntity<ApiResponse<List<NiveauStockDetailResponse>>> listerParEntrepot(@PathVariable Long entrepotId) {
        List<NiveauStockDetailResponse> niveaux = consulterUseCase.listerParEntrepotId(entrepotId);
        return ResponseEntity.ok(ApiResponse.succes(niveaux, niveaux.size() + " ligne(s) de stock"));
    }

    @GetMapping("/alerts")
    @PreAuthorize("isAuthenticated()")
    @Operation(summary = "Niveaux en alerte (faible + critique)")
    public ResponseEntity<ApiResponse<List<NiveauStockDetailResponse>>> alertes() {
        List<NiveauStockDetailResponse> niveaux = consulterUseCase.listerAlertes(false);
        return ResponseEntity.ok(ApiResponse.succes(niveaux, niveaux.size() + " alerte(s)"));
    }

    @GetMapping("/alerts/critical")
    @PreAuthorize("isAuthenticated()")
    @Operation(summary = "Niveaux en alerte critique uniquement")
    public ResponseEntity<ApiResponse<List<NiveauStockDetailResponse>>> alertesCritiques() {
        List<NiveauStockDetailResponse> niveaux = consulterUseCase.listerAlertes(true);
        return ResponseEntity.ok(ApiResponse.succes(niveaux, niveaux.size() + " alerte(s) critique(s)"));
    }

    @PostMapping("/adjust")
    @PreAuthorize("hasAuthority('STOCK_INVENTAIRE_GERER')")
    @Operation(summary = "Ajuster la quantité d'un niveau de stock (inventaire)")
    public ResponseEntity<ApiResponse<NiveauStockDetailResponse>> ajuster(@RequestBody AjustementRequest req) {
        NiveauStockDetailResponse maj = ajusterUseCase.executer(
            req.stockLevelId(), req.newQuantity(), req.reason(), userResolver.getMatriculeCourant());
        return ResponseEntity.ok(ApiResponse.succes(maj, "Stock ajusté"));
    }

    @PostMapping("/{id}/transfer-to-buffer")
    @PreAuthorize("hasAuthority('STOCK_TRANSFERT_GERER')")
    @Operation(summary = "Transférer une quantité du central vers le tampon")
    public ResponseEntity<ApiResponse<NiveauStockDetailResponse>> transfererVersTampon(
        @PathVariable Long id, @RequestParam long quantity) {
        NiveauStockDetailResponse maj = transfererUseCase.executer(id, quantity, userResolver.getMatriculeCourant());
        return ResponseEntity.ok(ApiResponse.succes(maj, "Transfert vers tampon effectué"));
    }

    /** Corps de la requête d'ajustement (aligné sur l'appel Angular adjustStock). */
    public record AjustementRequest(Long stockLevelId, long newQuantity, String reason) {}
}
