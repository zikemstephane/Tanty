package com.NTFOODS.Api_tanty.modules.stock.presentation.controller;

import com.NTFOODS.Api_tanty.modules.stock.application.fournisseur.dto.request.CreerFournisseurRequest;
import com.NTFOODS.Api_tanty.modules.stock.application.fournisseur.dto.response.FournisseurResponse;
import com.NTFOODS.Api_tanty.modules.stock.application.fournisseur.usecase.creer.CreerFournisseurUseCase;
import com.NTFOODS.Api_tanty.modules.stock.application.fournisseur.usecase.lister.ListerFournisseursUseCase;
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
@RequestMapping("/api/stock/fournisseurs")
@Tag(name = "Stock — Fournisseurs", description = "Gestion des fournisseurs")
@SecurityRequirement(name = "bearerAuth")
public class FournisseurController {

    private final CreerFournisseurUseCase creerFournisseurUseCase;
    private final ListerFournisseursUseCase listerFournisseursUseCase;
    private final AuthenticatedUserResolver userResolver;

    public FournisseurController(CreerFournisseurUseCase creerFournisseurUseCase,
                                ListerFournisseursUseCase listerFournisseursUseCase,
                                AuthenticatedUserResolver userResolver) {
        this.creerFournisseurUseCase = creerFournisseurUseCase;
        this.listerFournisseursUseCase = listerFournisseursUseCase;
        this.userResolver = userResolver;
    }

    @PostMapping
    @PreAuthorize("hasAnyRole('GESTIONNAIRE_STOCK', 'DIRECTEUR_GENERAL', 'ADMIN')")
    @Operation(summary = "Créer un fournisseur")
    public ResponseEntity<ApiResponse<FournisseurResponse>> creer(@Valid @RequestBody CreerFournisseurRequest request) {
        FournisseurResponse response = creerFournisseurUseCase.executer(request, userResolver.getUtilisateurCourant());
        return ResponseEntity.status(HttpStatus.CREATED)
            .body(ApiResponse.succes(response, "Fournisseur créé : " + response.code()));
    }

    @GetMapping
    @PreAuthorize("isAuthenticated()")
    @Operation(summary = "Lister / rechercher les fournisseurs")
    public ResponseEntity<ApiResponse<List<FournisseurResponse>>> lister(@RequestParam(required = false) String recherche) {
        List<FournisseurResponse> fournisseurs = listerFournisseursUseCase.executer(recherche);
        return ResponseEntity.ok(ApiResponse.succes(fournisseurs, fournisseurs.size() + " fournisseur(s)"));
    }
}
