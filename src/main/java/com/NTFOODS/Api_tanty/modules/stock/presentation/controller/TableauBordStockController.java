package com.NTFOODS.Api_tanty.modules.stock.presentation.controller;

import com.NTFOODS.Api_tanty.modules.stock.application.dashboard.dto.response.TableauBordEnrichiResponse;
import com.NTFOODS.Api_tanty.modules.stock.application.dashboard.dto.response.TableauBordStockResponse;
import com.NTFOODS.Api_tanty.modules.stock.application.dashboard.usecase.ConsulterTableauBordEnrichiUseCase;
import com.NTFOODS.Api_tanty.modules.stock.application.dashboard.usecase.ConsulterTableauBordStockUseCase;
import com.NTFOODS.Api_tanty.shared.application.dto.ApiResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/stock/dashboard")
@Tag(name = "Stock — Tableau de bord", description = "KPIs agrégés du module Stock")
@SecurityRequirement(name = "bearerAuth")
public class TableauBordStockController {

    private final ConsulterTableauBordStockUseCase consulterTableauBordStockUseCase;
    private final ConsulterTableauBordEnrichiUseCase consulterTableauBordEnrichiUseCase;

    public TableauBordStockController(ConsulterTableauBordStockUseCase consulterTableauBordStockUseCase,
                                       ConsulterTableauBordEnrichiUseCase consulterTableauBordEnrichiUseCase) {
        this.consulterTableauBordStockUseCase = consulterTableauBordStockUseCase;
        this.consulterTableauBordEnrichiUseCase = consulterTableauBordEnrichiUseCase;
    }

    @GetMapping
    @PreAuthorize("hasAuthority('STOCK_RAPPORT_CONSULTER')")
    @Operation(summary = "KPIs du tableau de bord Stock (valeur, alertes, réceptions, mouvements, lots)")
    public ResponseEntity<ApiResponse<TableauBordStockResponse>> consulter() {
        return ResponseEntity.ok(ApiResponse.succes(
            consulterTableauBordStockUseCase.executer(), "Tableau de bord stock"));
    }

    @GetMapping("/enrichi")
    @PreAuthorize("hasAuthority('STOCK_RAPPORT_CONSULTER')")
    @Operation(summary = "Tableau de bord enrichi — stats percutantes, tampon, central, mobile, performances, invendus, actions rapides")
    public ResponseEntity<ApiResponse<TableauBordEnrichiResponse>> consulterEnrichi() {
        return ResponseEntity.ok(ApiResponse.succes(
            consulterTableauBordEnrichiUseCase.executer(), "Tableau de bord enrichi"));
    }
}
