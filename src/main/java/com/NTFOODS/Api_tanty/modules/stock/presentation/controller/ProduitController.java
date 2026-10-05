package com.NTFOODS.Api_tanty.modules.stock.presentation.controller;

import com.NTFOODS.Api_tanty.modules.stock.application.catalogue.dto.request.AjouterConditionnementRequest;
import com.NTFOODS.Api_tanty.modules.stock.application.catalogue.dto.request.CreerProduitRequest;
import com.NTFOODS.Api_tanty.modules.stock.application.catalogue.dto.request.ModifierConditionnementRequest;
import com.NTFOODS.Api_tanty.modules.stock.application.catalogue.dto.request.ModifierProduitRequest;
import com.NTFOODS.Api_tanty.modules.stock.application.catalogue.dto.response.ProduitResponse;
import com.NTFOODS.Api_tanty.modules.stock.application.catalogue.usecase.consulter.ConsulterProduitUseCase;
import com.NTFOODS.Api_tanty.modules.stock.application.catalogue.usecase.creer.CreerProduitUseCase;
import com.NTFOODS.Api_tanty.modules.stock.application.catalogue.usecase.lister.ListerProduitsUseCase;
import com.NTFOODS.Api_tanty.modules.stock.application.catalogue.usecase.modifier.AjouterConditionnementUseCase;
import com.NTFOODS.Api_tanty.modules.stock.application.catalogue.usecase.modifier.ModifierConditionnementUseCase;
import com.NTFOODS.Api_tanty.modules.stock.application.catalogue.usecase.modifier.ModifierProduitUseCase;
import com.NTFOODS.Api_tanty.modules.stock.application.catalogue.usecase.supprimer.DesactiverProduitUseCase;
import com.NTFOODS.Api_tanty.modules.stock.application.catalogue.usecase.supprimer.SupprimerConditionnementUseCase;
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
 * PUT    /api/stock/produits/{code}/conditionnements/{id}   → Modifier un conditionnement
 * DELETE /api/stock/produits/{code}/conditionnements/{id}   → Supprimer un conditionnement
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
    private final ModifierConditionnementUseCase modifierConditionnementUseCase;
    private final SupprimerConditionnementUseCase supprimerConditionnementUseCase;
    private final DesactiverProduitUseCase desactiverProduitUseCase;
    private final AuthenticatedUserResolver userResolver;

    public ProduitController(CreerProduitUseCase creerProduitUseCase,
                             ListerProduitsUseCase listerProduitsUseCase,
                             ConsulterProduitUseCase consulterProduitUseCase,
                             ModifierProduitUseCase modifierProduitUseCase,
                             AjouterConditionnementUseCase ajouterConditionnementUseCase,
                             ModifierConditionnementUseCase modifierConditionnementUseCase,
                             SupprimerConditionnementUseCase supprimerConditionnementUseCase,
                             DesactiverProduitUseCase desactiverProduitUseCase,
                             AuthenticatedUserResolver userResolver) {
        this.creerProduitUseCase = creerProduitUseCase;
        this.listerProduitsUseCase = listerProduitsUseCase;
        this.consulterProduitUseCase = consulterProduitUseCase;
        this.modifierProduitUseCase = modifierProduitUseCase;
        this.ajouterConditionnementUseCase = ajouterConditionnementUseCase;
        this.modifierConditionnementUseCase = modifierConditionnementUseCase;
        this.supprimerConditionnementUseCase = supprimerConditionnementUseCase;
        this.desactiverProduitUseCase = desactiverProduitUseCase;
        this.userResolver = userResolver;
    }

    @PostMapping
    @PreAuthorize("hasAuthority('STOCK_PRODUIT_CREER')")
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
    @PreAuthorize("hasAuthority('STOCK_PRODUIT_MODIFIER')")
    @Operation(summary = "Modifier les informations descriptives d'un produit")
    public ResponseEntity<ApiResponse<ProduitResponse>> modifier(
        @PathVariable String code, @RequestBody ModifierProduitRequest request) {
        ProduitResponse response = modifierProduitUseCase.executer(code, request, userResolver.getUtilisateurCourant());
        return ResponseEntity.ok(ApiResponse.succes(response, "Produit modifié"));
    }

    @PostMapping("/{code}/conditionnements")
    @PreAuthorize("hasAuthority('STOCK_CONDITIONNEMENT_GERER')")
    @Operation(summary = "Ajouter un conditionnement à un produit")
    public ResponseEntity<ApiResponse<ProduitResponse>> ajouterConditionnement(
        @PathVariable String code, @Valid @RequestBody AjouterConditionnementRequest request) {
        ProduitResponse response = ajouterConditionnementUseCase.executer(code, request, userResolver.getUtilisateurCourant());
        return ResponseEntity.status(HttpStatus.CREATED)
            .body(ApiResponse.succes(response, "Conditionnement ajouté"));
    }

    @PutMapping("/{code}/conditionnements/{conditionnementId}")
    @PreAuthorize("hasAuthority('STOCK_CONDITIONNEMENT_GERER')")
    @Operation(summary = "Modifier un conditionnement existant d'un produit")
    public ResponseEntity<ApiResponse<ProduitResponse>> modifierConditionnement(
        @PathVariable String code, @PathVariable String conditionnementId,
        @RequestBody ModifierConditionnementRequest request) {
        ProduitResponse response = modifierConditionnementUseCase.executer(code, conditionnementId, request, userResolver.getUtilisateurCourant());
        return ResponseEntity.ok(ApiResponse.succes(response, "Conditionnement modifié"));
    }

    @DeleteMapping("/{code}/conditionnements/{conditionnementId}")
    @PreAuthorize("hasAuthority('STOCK_CONDITIONNEMENT_GERER')")
    @Operation(summary = "Supprimer un conditionnement d'un produit")
    public ResponseEntity<ApiResponse<ProduitResponse>> supprimerConditionnement(
        @PathVariable String code, @PathVariable String conditionnementId) {
        ProduitResponse response = supprimerConditionnementUseCase.executer(code, conditionnementId, userResolver.getUtilisateurCourant());
        return ResponseEntity.ok(ApiResponse.succes(response, "Conditionnement supprimé"));
    }

    @DeleteMapping("/{code}")
    @PreAuthorize("hasAuthority('STOCK_PRODUIT_DESACTIVER')")
    @Operation(summary = "Désactiver un produit")
    public ResponseEntity<ApiResponse<Void>> desactiver(@PathVariable String code) {
        desactiverProduitUseCase.executer(code, userResolver.getUtilisateurCourant());
        return ResponseEntity.ok(ApiResponse.succes(null, "Produit désactivé : " + code));
    }
}
