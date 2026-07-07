package com.NTFOODS.Api_tanty.modules.stock.presentation.controller;

import com.NTFOODS.Api_tanty.modules.stock.application.catalogue.dto.response.CatalogueHierarchieResponse;
import com.NTFOODS.Api_tanty.modules.stock.application.catalogue.usecase.lister.ConsulterHierarchieCatalogueUseCase;
import com.NTFOODS.Api_tanty.shared.application.dto.ApiResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Contrôleur REST — Hiérarchie du catalogue (marque → gamme → variété),
 * dérivée des produits. Sert les filtres/sélecteurs du frontend web.
 */
@RestController
@RequestMapping("/api/stock/catalogue")
@Tag(name = "Stock — Catalogue (hiérarchie)", description = "Marques, gammes et variétés dérivées des produits")
@SecurityRequirement(name = "bearerAuth")
public class CatalogueController {

    private final ConsulterHierarchieCatalogueUseCase consulterHierarchieCatalogueUseCase;

    public CatalogueController(ConsulterHierarchieCatalogueUseCase consulterHierarchieCatalogueUseCase) {
        this.consulterHierarchieCatalogueUseCase = consulterHierarchieCatalogueUseCase;
    }

    @GetMapping("/hierarchie")
    @PreAuthorize("isAuthenticated()")
    @Operation(summary = "Hiérarchie catalogue (marques, gammes, variétés)")
    public ResponseEntity<ApiResponse<CatalogueHierarchieResponse>> hierarchie() {
        return ResponseEntity.ok(ApiResponse.succes(
            consulterHierarchieCatalogueUseCase.executer(), "Hiérarchie du catalogue"));
    }
}
