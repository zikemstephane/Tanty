package com.NTFOODS.Api_tanty.modules.stock.presentation.controller;

import com.NTFOODS.Api_tanty.modules.stock.application.niveau.dto.response.NiveauStockResponse;
import com.NTFOODS.Api_tanty.modules.stock.application.niveau.usecase.consulter.ConsulterEtatStockUseCase;
import com.NTFOODS.Api_tanty.modules.stock.application.stockmobile.dto.response.StockMobileResponse;
import com.NTFOODS.Api_tanty.modules.stock.application.stockmobile.mapper.StockMobileMapper;
import com.NTFOODS.Api_tanty.modules.stock.domain.stockmobile.model.StockMobile;
import com.NTFOODS.Api_tanty.modules.stock.domain.stockmobile.repository.StockMobileRepository;
import com.NTFOODS.Api_tanty.shared.application.dto.ApiResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

/**
 * Suivi du stock mobile par commercial — réutilise l'état des stocks filtré sur
 * l'entrepôt MOBILE du commercial (convention : MOBILE-{matricule}).
 */
@RestController
@RequestMapping("/api/stock/stock-mobile")
@Tag(name = "Stock — Stock mobile", description = "Suivi temps réel du stock mobile par commercial")
@SecurityRequirement(name = "bearerAuth")
public class StockMobileController {

    private final ConsulterEtatStockUseCase consulterEtatStockUseCase;
    private final StockMobileRepository stockMobileRepository;
    private final StockMobileMapper stockMobileMapper;

    public StockMobileController(ConsulterEtatStockUseCase consulterEtatStockUseCase,
                                  StockMobileRepository stockMobileRepository,
                                  StockMobileMapper stockMobileMapper) {
        this.consulterEtatStockUseCase = consulterEtatStockUseCase;
        this.stockMobileRepository = stockMobileRepository;
        this.stockMobileMapper = stockMobileMapper;
    }

    @GetMapping("/{matriculeCommercial}")
    @PreAuthorize("hasAuthority('STOCK_NIVEAU_CONSULTER')")
    @Operation(summary = "Consulter le stock mobile d'un commercial")
    public ResponseEntity<ApiResponse<List<NiveauStockResponse>>> consulter(@PathVariable String matriculeCommercial) {
        String codeEntrepot = "MOBILE-" + matriculeCommercial;
        List<NiveauStockResponse> etat = consulterEtatStockUseCase.executer(codeEntrepot, null, null);
        return ResponseEntity.ok(ApiResponse.succes(etat, etat.size() + " produit(s) en stock mobile"));
    }

    @GetMapping("/suivi/tous")
    @PreAuthorize("hasAuthority('STOCK_NIVEAU_CONSULTER')")
    @Operation(summary = "Lister tous les stocks mobiles (suivi temps réel)")
    public ResponseEntity<ApiResponse<List<StockMobileResponse>>> listerTous() {
        var list = stockMobileMapper.versResponseList(stockMobileRepository.listerTous());
        return ResponseEntity.ok(ApiResponse.succes(list, list.size() + " stock(s) mobile(s)"));
    }

    @GetMapping("/suivi/commercial/{matricule}")
    @PreAuthorize("hasAuthority('STOCK_NIVEAU_CONSULTER')")
    @Operation(summary = "Historique des stocks mobiles d'un commercial (suivi dans le temps)")
    public ResponseEntity<ApiResponse<List<StockMobileResponse>>> historiqueCommercial(@PathVariable String matricule) {
        var list = stockMobileMapper.versResponseList(stockMobileRepository.listerParCommercial(matricule));
        return ResponseEntity.ok(ApiResponse.succes(list, list.size() + " session(s)"));
    }

    @GetMapping("/suivi/commercial/{matricule}/courant")
    @PreAuthorize("hasAuthority('STOCK_NIVEAU_CONSULTER')")
    @Operation(summary = "Stock mobile courant d'un commercial (temps réel)")
    public ResponseEntity<ApiResponse<StockMobileResponse>> stockCourant(@PathVariable String matricule) {
        return stockMobileRepository.trouverDernierParCommercial(matricule)
            .map(sm -> ResponseEntity.ok(ApiResponse.succes(stockMobileMapper.versResponse(sm), "Stock mobile courant")))
            .orElseGet(() -> ResponseEntity.ok(ApiResponse.succes(null, "Aucun stock mobile pour ce commercial")));
    }

    @GetMapping("/suivi/date/{date}")
    @PreAuthorize("hasAuthority('STOCK_NIVEAU_CONSULTER')")
    @Operation(summary = "Stocks mobiles par date")
    public ResponseEntity<ApiResponse<List<StockMobileResponse>>> listerParDate(@PathVariable String date) {
        var list = stockMobileMapper.versResponseList(stockMobileRepository.listerParDate(LocalDate.parse(date)));
        return ResponseEntity.ok(ApiResponse.succes(list, list.size() + " stock(s) pour cette date"));
    }
}
