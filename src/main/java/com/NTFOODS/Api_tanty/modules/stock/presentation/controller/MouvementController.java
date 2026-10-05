package com.NTFOODS.Api_tanty.modules.stock.presentation.controller;

import com.NTFOODS.Api_tanty.modules.stock.application.mouvement.dto.response.MouvementResponse;
import com.NTFOODS.Api_tanty.modules.stock.application.mouvement.usecase.lister.ListerMouvementsUseCase;
import com.NTFOODS.Api_tanty.shared.application.dto.ApiResponse;
import com.NTFOODS.Api_tanty.shared.application.dto.PageResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;

@RestController
@RequestMapping("/api/stock/mouvements")
@Tag(name = "Stock — Mouvements", description = "Historique des mouvements de stock (registre)")
@SecurityRequirement(name = "bearerAuth")
public class MouvementController {

    private final ListerMouvementsUseCase listerMouvementsUseCase;

    public MouvementController(ListerMouvementsUseCase listerMouvementsUseCase) {
        this.listerMouvementsUseCase = listerMouvementsUseCase;
    }

    @GetMapping
    @PreAuthorize("hasAuthority('STOCK_MOUVEMENT_CONSULTER')")
    @Operation(summary = "Historique des mouvements (paginé — filtres : produit, entrepot, type, document, période)",
        description = "Retourne une page de mouvements. Paramètres optionnels: page (déf. 0), size (déf. 20), sort (ex: horodatage,desc). " +
                      "Si page/size omis, retourne les 20 derniers mouvements.")
    public ResponseEntity<ApiResponse<PageResponse<MouvementResponse>>> lister(
        @RequestParam(required = false) String produit,
        @RequestParam(required = false) String entrepot,
        @RequestParam(required = false) String type,
        @RequestParam(required = false) String document,
        @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate debut,
        @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate fin,
        @PageableDefault(size = 20, sort = "horodatage", direction = Sort.Direction.DESC) Pageable pageable) {
        Page<MouvementResponse> page = listerMouvementsUseCase.executerPagine(produit, entrepot, type, document, debut, fin, pageable);
        return ResponseEntity.ok(ApiResponse.succes(PageResponse.de(page), page.getTotalElements() + " mouvement(s)"));
    }
}
