package com.NTFOODS.Api_tanty.modules.stock.presentation.controller;

import com.NTFOODS.Api_tanty.modules.stock.application.entrepot.dto.request.CreerEntrepotRequest;
import com.NTFOODS.Api_tanty.modules.stock.application.entrepot.dto.response.EntrepotResponse;
import com.NTFOODS.Api_tanty.modules.stock.application.entrepot.usecase.creer.CreerEntrepotUseCase;
import com.NTFOODS.Api_tanty.modules.stock.application.entrepot.usecase.lister.ListerEntrepotsUseCase;
import com.NTFOODS.Api_tanty.shared.application.dto.ApiResponse;
import com.NTFOODS.Api_tanty.shared.infrastructure.security.AuthenticatedUserResolver;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/stock/entrepots")
@Tag(name = "Stock — Entrepôts", description = "Gestion des entrepôts (central, tampon, mobiles)")
@SecurityRequirement(name = "bearerAuth")
public class EntrepotController {

    private final CreerEntrepotUseCase creerEntrepotUseCase;
    private final ListerEntrepotsUseCase listerEntrepotsUseCase;
    private final AuthenticatedUserResolver userResolver;

    public EntrepotController(CreerEntrepotUseCase creerEntrepotUseCase,
                             ListerEntrepotsUseCase listerEntrepotsUseCase,
                             AuthenticatedUserResolver userResolver) {
        this.creerEntrepotUseCase = creerEntrepotUseCase;
        this.listerEntrepotsUseCase = listerEntrepotsUseCase;
        this.userResolver = userResolver;
    }

    @PostMapping
    @PreAuthorize("hasAnyRole('GESTIONNAIRE_STOCK', 'DIRECTEUR_GENERAL', 'ADMIN')")
    @Operation(summary = "Créer un entrepôt")
    public ResponseEntity<ApiResponse<EntrepotResponse>> creer(@Valid @RequestBody CreerEntrepotRequest request) {
        EntrepotResponse response = creerEntrepotUseCase.executer(request, userResolver.getUtilisateurCourant());
        return ResponseEntity.status(HttpStatus.CREATED)
            .body(ApiResponse.succes(response, "Entrepôt créé : " + response.code()));
    }

    @GetMapping
    @PreAuthorize("isAuthenticated()")
    @Operation(summary = "Lister les entrepôts (filtre optionnel par type)")
    public ResponseEntity<ApiResponse<List<EntrepotResponse>>> lister(@RequestParam(required = false) String type) {
        List<EntrepotResponse> entrepots = listerEntrepotsUseCase.executer(type);
        return ResponseEntity.ok(ApiResponse.succes(entrepots, entrepots.size() + " entrepôt(s)"));
    }
}
