package com.NTFOODS.Api_tanty.modules.stock.presentation.controller;

import com.NTFOODS.Api_tanty.modules.stock.application.catalogue.dto.request.AjouterConditionnementRequest;
import com.NTFOODS.Api_tanty.modules.stock.application.catalogue.dto.request.CreerProduitRequest;
import com.NTFOODS.Api_tanty.modules.stock.application.catalogue.dto.request.ModifierProduitRequest;
import com.NTFOODS.Api_tanty.modules.stock.application.catalogue.dto.response.ProduitResponse;
import com.NTFOODS.Api_tanty.modules.stock.application.catalogue.usecase.consulter.ConsulterProduitUseCase;
import com.NTFOODS.Api_tanty.modules.stock.application.catalogue.usecase.creer.CreerProduitUseCase;
import com.NTFOODS.Api_tanty.modules.stock.application.catalogue.usecase.lister.ListerProduitsUseCase;
import com.NTFOODS.Api_tanty.modules.stock.application.catalogue.usecase.modifier.AjouterConditionnementUseCase;
import com.NTFOODS.Api_tanty.modules.stock.application.catalogue.usecase.modifier.ModifierProduitUseCase;
import com.NTFOODS.Api_tanty.modules.stock.application.catalogue.usecase.supprimer.DesactiverProduitUseCase;
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

/**
 * Contrôleur REST — Catalogue produit (Stock).
 *
 * POST   /api/stock/produits                                → Créer un produit
 * GET    /api/stock/produits                                → Lister / rechercher
 * GET    /api/stock/produits/{code}                         → Consulter
 * PUT    /api/stock/produits/{code}                         → Modifier les infos
 * POST   /api/stock/produits/{code}/conditionnements        → Ajouter un conditionnement
 * DELETE /api/stock/produits/{code}                         → Désactiver
 */
@RestController
@RequestMapping("/api/stock/produits")
@Tag(name = "Stock — Catalogue", description = "Gestion des produits et conditionnements multi-niveaux")
@SecurityRequirement(name = "bearerAuth")
public class ProduitController {

    private final CreerProduitUseCase creerProduitUseCase;
    private final ListerProduitsUseCase listerProduitsUseCase;
    private final ConsulterProduitUseCase consulterProduitUseCase;
    private final ModifierProduitUseCase modifierProduitUseCase;
    private final AjouterConditionnementUseCase ajouterConditionnementUseCase;
    private final DesactiverProduitUseCase desactiverProduitUseCase;
    private final AuthenticatedUserResolver userResolver;

    public ProduitController(CreerProduitUseCase creerProduitUseCase,
                             ListerProduitsUseCase listerProduitsUseCase,
                             ConsulterProduitUseCase consulterProduitUseCase,
                             ModifierProduitUseCase modifierProduitUseCase,
                             AjouterConditionnementUseCase ajouterConditionnementUseCase,
                             DesactiverProduitUseCase desactiverProduitUseCase,
                             AuthenticatedUserResolver userResolver) {
        this.creerProduitUseCase = creerProduitUseCase;
        this.listerProduitsUseCase = listerProduitsUseCase;
        this.consulterProduitUseCase = consulterProduitUseCase;
        this.modifierProduitUseCase = modifierProduitUseCase;
        this.ajouterConditionnementUseCase = ajouterConditionnementUseCase;
        this.desactiverProduitUseCase = desactiverProduitUseCase;
        this.userResolver = userResolver;
    }

    @PostMapping
    @PreAuthorize("hasAnyRole('GESTIONNAIRE_STOCK', 'DIRECTEUR_GENERAL', 'ADMIN')")
    @Operation(summary = "Créer un produit avec ses conditionnements")
    public ResponseEntity<ApiResponse<ProduitResponse>> creer(@Valid @RequestBody CreerProduitRequest request) {
        ProduitResponse response = creerProduitUseCase.executer(request, userResolver.getUtilisateurCourant());
        return ResponseEntity.status(HttpStatus.CREATED)
            .body(ApiResponse.succes(response, "Produit créé : " + response.code()));
    }

    @GetMapping
    @PreAuthorize("isAuthenticated()")
    @Operation(summary = "Lister / rechercher les produits")
    public ResponseEntity<ApiResponse<List<ProduitResponse>>> lister(
        @RequestParam(required = false) String marque,
        @RequestParam(required = false) String gamme,
        @RequestParam(required = false) String variete,
        @RequestParam(required = false) Boolean actif) {
        List<ProduitResponse> produits = listerProduitsUseCase.executer(marque, gamme, variete, actif);
        return ResponseEntity.ok(ApiResponse.succes(produits, produits.size() + " produit(s) trouvé(s)"));
    }

    @GetMapping("/{code}")
    @PreAuthorize("isAuthenticated()")
    @Operation(summary = "Consulter un produit par son code")
    public ResponseEntity<ApiResponse<ProduitResponse>> consulter(@PathVariable String code) {
        return ResponseEntity.ok(ApiResponse.succes(consulterProduitUseCase.executer(code)));
    }

    @PutMapping("/{code}")
    @PreAuthorize("hasAnyRole('GESTIONNAIRE_STOCK', 'DIRECTEUR_GENERAL', 'ADMIN')")
    @Operation(summary = "Modifier les informations descriptives d'un produit")
    public ResponseEntity<ApiResponse<ProduitResponse>> modifier(
        @PathVariable String code, @RequestBody ModifierProduitRequest request) {
        ProduitResponse response = modifierProduitUseCase.executer(code, request, userResolver.getUtilisateurCourant());
        return ResponseEntity.ok(ApiResponse.succes(response, "Produit modifié"));
    }

    @PostMapping("/{code}/conditionnements")
    @PreAuthorize("hasAnyRole('GESTIONNAIRE_STOCK', 'DIRECTEUR_GENERAL', 'ADMIN')")
    @Operation(summary = "Ajouter un conditionnement à un produit")
    public ResponseEntity<ApiResponse<ProduitResponse>> ajouterConditionnement(
        @PathVariable String code, @Valid @RequestBody AjouterConditionnementRequest request) {
        ProduitResponse response = ajouterConditionnementUseCase.executer(code, request, userResolver.getUtilisateurCourant());
        return ResponseEntity.status(HttpStatus.CREATED)
            .body(ApiResponse.succes(response, "Conditionnement ajouté"));
    }

    @DeleteMapping("/{code}")
    @PreAuthorize("hasAnyRole('GESTIONNAIRE_STOCK', 'DIRECTEUR_GENERAL', 'ADMIN')")
    @Operation(summary = "Désactiver un produit")
    public ResponseEntity<ApiResponse<Void>> desactiver(@PathVariable String code) {
        desactiverProduitUseCase.executer(code, userResolver.getUtilisateurCourant());
        return ResponseEntity.ok(ApiResponse.succes(null, "Produit désactivé : " + code));
    }
}
