package com.NTFOODS.Api_tanty.modules.stock.presentation.controller;

import com.NTFOODS.Api_tanty.modules.stock.application.commande.dto.request.CreerBonCommandeRequest;
import com.NTFOODS.Api_tanty.modules.stock.application.commande.dto.request.ModifierPeriodeProductionRequest;
import com.NTFOODS.Api_tanty.modules.stock.application.commande.dto.response.BonCommandeResponse;
import com.NTFOODS.Api_tanty.modules.stock.application.commande.usecase.consulter.ConsulterBonCommandeUseCase;
import com.NTFOODS.Api_tanty.modules.stock.application.commande.usecase.creer.CreerBonCommandeUseCase;
import com.NTFOODS.Api_tanty.modules.stock.application.commande.usecase.cycle.TransitionBonCommandeUseCase;
import com.NTFOODS.Api_tanty.modules.stock.application.commande.usecase.lister.ListerBonsCommandeUseCase;
import com.NTFOODS.Api_tanty.modules.stock.application.commande.usecase.modifier.ModifierPeriodeProductionBCUseCase;
import com.NTFOODS.Api_tanty.modules.stock.application.commande.usecase.valider.ValiderBonCommandeUseCase;
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
@RequestMapping("/api/stock/bons-commande")
@Tag(name = "Stock — Bons de Commande", description = "Cycle de vie des bons de commande fournisseurs")
@SecurityRequirement(name = "bearerAuth")
public class BonCommandeController {

    private final CreerBonCommandeUseCase creerBonCommandeUseCase;
    private final ValiderBonCommandeUseCase validerBonCommandeUseCase;
    private final TransitionBonCommandeUseCase transitionBonCommandeUseCase;
    private final ListerBonsCommandeUseCase listerBonsCommandeUseCase;
    private final ConsulterBonCommandeUseCase consulterBonCommandeUseCase;
    private final ModifierPeriodeProductionBCUseCase modifierPeriodeProductionBCUseCase;
    private final AuthenticatedUserResolver userResolver;

    public BonCommandeController(CreerBonCommandeUseCase creerBonCommandeUseCase,
                                ValiderBonCommandeUseCase validerBonCommandeUseCase,
                                TransitionBonCommandeUseCase transitionBonCommandeUseCase,
                                ListerBonsCommandeUseCase listerBonsCommandeUseCase,
                                ConsulterBonCommandeUseCase consulterBonCommandeUseCase,
                                ModifierPeriodeProductionBCUseCase modifierPeriodeProductionBCUseCase,
                                AuthenticatedUserResolver userResolver) {
        this.creerBonCommandeUseCase = creerBonCommandeUseCase;
        this.validerBonCommandeUseCase = validerBonCommandeUseCase;
        this.transitionBonCommandeUseCase = transitionBonCommandeUseCase;
        this.listerBonsCommandeUseCase = listerBonsCommandeUseCase;
        this.consulterBonCommandeUseCase = consulterBonCommandeUseCase;
        this.modifierPeriodeProductionBCUseCase = modifierPeriodeProductionBCUseCase;
        this.userResolver = userResolver;
    }

    @PostMapping
    @PreAuthorize("hasAuthority('STOCK_BON_COMMANDE_CREER')")
    @Operation(summary = "Créer un bon de commande (Gestionnaire de Stock ou Chef de Production)")
    public ResponseEntity<ApiResponse<BonCommandeResponse>> creer(@Valid @RequestBody CreerBonCommandeRequest request) {
        BonCommandeResponse response = creerBonCommandeUseCase.executer(request, userResolver.getUtilisateurCourant());
        return ResponseEntity.status(HttpStatus.CREATED)
            .body(ApiResponse.succes(response, "Bon de commande créé : " + response.numero()));
    }

    @GetMapping
    @PreAuthorize("isAuthenticated()")
    @Operation(summary = "Lister les bons de commande (filtres : statut, fournisseur, enRetard, valides)")
    public ResponseEntity<ApiResponse<List<BonCommandeResponse>>> lister(
        @RequestParam(required = false) String statut,
        @RequestParam(required = false) String fournisseur,
        @RequestParam(required = false, defaultValue = "false") boolean enRetard,
        @RequestParam(required = false, defaultValue = "false") boolean valides) {
        List<BonCommandeResponse> bcs = listerBonsCommandeUseCase.executer(statut, fournisseur, enRetard, valides);
        return ResponseEntity.ok(ApiResponse.succes(bcs, bcs.size() + " bon(s) de commande"));
    }

    @GetMapping("/{numero}")
    @PreAuthorize("isAuthenticated()")
    @Operation(summary = "Consulter un bon de commande")
    public ResponseEntity<ApiResponse<BonCommandeResponse>> consulter(@PathVariable String numero) {
        return ResponseEntity.ok(ApiResponse.succes(consulterBonCommandeUseCase.executer(numero)));
    }

    @PostMapping("/{numero}/valider")
    @PreAuthorize("hasAuthority('STOCK_BON_COMMANDE_VALIDER')")
    @Operation(summary = "Valider un bon de commande (Chef de Production uniquement)")
    public ResponseEntity<ApiResponse<BonCommandeResponse>> valider(@PathVariable String numero) {
        BonCommandeResponse response = validerBonCommandeUseCase.executer(numero, userResolver.getUtilisateurCourant());
        return ResponseEntity.ok(ApiResponse.succes(response, "Bon de commande validé"));
    }

    @PostMapping("/{numero}/envoyer")
    @PreAuthorize("hasAuthority('STOCK_BON_COMMANDE_CREER')")
    @Operation(summary = "Marquer le BC comme envoyé au fournisseur")
    public ResponseEntity<ApiResponse<BonCommandeResponse>> envoyer(@PathVariable String numero) {
        BonCommandeResponse response = transitionBonCommandeUseCase.executer(
            numero, TransitionBonCommandeUseCase.Action.ENVOYER, userResolver.getUtilisateurCourant());
        return ResponseEntity.ok(ApiResponse.succes(response, "Bon de commande envoyé"));
    }

    @PostMapping("/{numero}/annuler")
    @PreAuthorize("hasAuthority('STOCK_BON_COMMANDE_CREER')")
    @Operation(summary = "Annuler un bon de commande")
    public ResponseEntity<ApiResponse<BonCommandeResponse>> annuler(@PathVariable String numero) {
        BonCommandeResponse response = transitionBonCommandeUseCase.executer(
            numero, TransitionBonCommandeUseCase.Action.ANNULER, userResolver.getUtilisateurCourant());
        return ResponseEntity.ok(ApiResponse.succes(response, "Bon de commande annulé"));
    }

    @PutMapping("/{numero}/periode-production")
    @PreAuthorize("isAuthenticated()")
    @Operation(summary = "Modifier la période de production d'un BC",
        description = "Autorisé même après validation (Chef de Production, Agent de Production, Machiniste, Admin). "
            + "Ne recalcule pas automatiquement le PPH associé — utiliser ensuite POST /production/pph/{reference}/recalculer.")
    public ResponseEntity<ApiResponse<BonCommandeResponse>> modifierPeriodeProduction(
        @PathVariable String numero, @RequestBody ModifierPeriodeProductionRequest req) {
        BonCommandeResponse response = modifierPeriodeProductionBCUseCase.executer(
            numero, req.dateDebutProduction(), req.dateFinProduction(), userResolver.getUtilisateurCourant());
        return ResponseEntity.ok(ApiResponse.succes(response, "Période de production modifiée"));
    }

    @PostMapping("/{numero}/cloturer")
    @PreAuthorize("hasAuthority('STOCK_BON_COMMANDE_CREER')")
    @Operation(summary = "Clôturer un bon de commande")
    public ResponseEntity<ApiResponse<BonCommandeResponse>> cloturer(@PathVariable String numero) {
        BonCommandeResponse response = transitionBonCommandeUseCase.executer(
            numero, TransitionBonCommandeUseCase.Action.CLOTURER, userResolver.getUtilisateurCourant());
        return ResponseEntity.ok(ApiResponse.succes(response, "Bon de commande clôturé"));
    }
}
