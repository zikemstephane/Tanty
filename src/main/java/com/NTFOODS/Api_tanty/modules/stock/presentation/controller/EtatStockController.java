package com.NTFOODS.Api_tanty.modules.stock.presentation.controller;

import com.NTFOODS.Api_tanty.modules.stock.application.niveau.dto.response.NiveauStockResponse;
import com.NTFOODS.Api_tanty.modules.stock.application.niveau.usecase.consulter.ConsulterEtatStockUseCase;
import com.NTFOODS.Api_tanty.shared.application.dto.ApiResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/stock/etat")
@Tag(name = "Stock — État des stocks", description = "Consultation temps réel par entrepôt / produit / type")
@SecurityRequirement(name = "bearerAuth")
public class EtatStockController {

    private final ConsulterEtatStockUseCase consulterEtatStockUseCase;

    public EtatStockController(ConsulterEtatStockUseCase consulterEtatStockUseCase) {
        this.consulterEtatStockUseCase = consulterEtatStockUseCase;
    }

    @GetMapping
    @PreAuthorize("isAuthenticated()")
    @Operation(summary = "État des stocks (filtres : entrepot, produit, type)")
    public ResponseEntity<ApiResponse<List<NiveauStockResponse>>> consulter(
        @RequestParam(required = false) String entrepot,
        @RequestParam(required = false) String produit,
        @RequestParam(required = false) String type) {
        List<NiveauStockResponse> etat = consulterEtatStockUseCase.executer(entrepot, produit, type);
        return ResponseEntity.ok(ApiResponse.succes(etat, etat.size() + " ligne(s) de stock"));
    }
}
