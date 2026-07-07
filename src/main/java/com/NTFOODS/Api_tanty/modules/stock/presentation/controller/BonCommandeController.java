package com.NTFOODS.Api_tanty.modules.stock.presentation.controller;

import com.NTFOODS.Api_tanty.modules.stock.application.commande.dto.request.CreerBonCommandeRequest;
import com.NTFOODS.Api_tanty.modules.stock.application.commande.dto.response.BonCommandeResponse;
import com.NTFOODS.Api_tanty.modules.stock.application.commande.usecase.consulter.ConsulterBonCommandeUseCase;
import com.NTFOODS.Api_tanty.modules.stock.application.commande.usecase.creer.CreerBonCommandeUseCase;
import com.NTFOODS.Api_tanty.modules.stock.application.commande.usecase.cycle.TransitionBonCommandeUseCase;
import com.NTFOODS.Api_tanty.modules.stock.application.commande.usecase.lister.ListerBonsCommandeUseCase;
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
    private final AuthenticatedUserResolver userResolver;

    public BonCommandeController(CreerBonCommandeUseCase creerBonCommandeUseCase,
                                ValiderBonCommandeUseCase validerBonCommandeUseCase,
                                TransitionBonCommandeUseCase transitionBonCommandeUseCase,
                                ListerBonsCommandeUseCase listerBonsCommandeUseCase,
                                ConsulterBonCommandeUseCase consulterBonCommandeUseCase,
                                AuthenticatedUserResolver userResolver) {
        this.creerBonCommandeUseCase = creerBonCommandeUseCase;
        this.validerBonCommandeUseCase = validerBonCommandeUseCase;
        this.transitionBonCommandeUseCase = transitionBonCommandeUseCase;
        this.listerBonsCommandeUseCase = listerBonsCommandeUseCase;
        this.consulterBonCommandeUseCase = consulterBonCommandeUseCase;
        this.userResolver = userResolver;
    }

    @PostMapping
    @PreAuthorize("hasAnyRole('GESTIONNAIRE_STOCK', 'CHEF_PRODUCTION', 'DIRECTEUR_GENERAL', 'ADMIN')")
    @Operation(summary = "Créer un bon de commande (Gestionnaire de Stock ou Chef de Production)")
    public ResponseEntity<ApiResponse<BonCommandeResponse>> creer(@Valid @RequestBody CreerBonCommandeRequest request) {
        BonCommandeResponse response = creerBonCommandeUseCase.executer(request, userResolver.getUtilisateurCourant());
        return ResponseEntity.status(HttpStatus.CREATED)
            .body(ApiResponse.succes(response, "Bon de commande créé : " + response.numero()));
    }

    @GetMapping
    @PreAuthorize("isAuthenticated()")
    @Operation(summary = "Lister les bons de commande (filtres : statut, fournisseur, enRetard)")
    public ResponseEntity<ApiResponse<List<BonCommandeResponse>>> lister(
        @RequestParam(required = false) String statut,
        @RequestParam(required = false) String fournisseur,
        @RequestParam(required = false, defaultValue = "false") boolean enRetard) {
        List<BonCommandeResponse> bcs = listerBonsCommandeUseCase.executer(statut, fournisseur, enRetard);
        return ResponseEntity.ok(ApiResponse.succes(bcs, bcs.size() + " bon(s) de commande"));
    }

    @GetMapping("/{numero}")
    @PreAuthorize("isAuthenticated()")
    @Operation(summary = "Consulter un bon de commande")
    public ResponseEntity<ApiResponse<BonCommandeResponse>> consulter(@PathVariable String numero) {
        return ResponseEntity.ok(ApiResponse.succes(consulterBonCommandeUseCase.executer(numero)));
    }

    @PostMapping("/{numero}/valider")
    @PreAuthorize("hasAnyRole('CHEF_PRODUCTION', 'ADMIN')")
    @Operation(summary = "Valider un bon de commande (Chef de Production uniquement)")
    public ResponseEntity<ApiResponse<BonCommandeResponse>> valider(@PathVariable String numero) {
        BonCommandeResponse response = validerBonCommandeUseCase.executer(numero, userResolver.getUtilisateurCourant());
        return ResponseEntity.ok(ApiResponse.succes(response, "Bon de commande validé"));
    }

    @PostMapping("/{numero}/envoyer")
    @PreAuthorize("hasAnyRole('CHEF_PRODUCTION', 'GESTIONNAIRE_STOCK', 'DIRECTEUR_GENERAL', 'ADMIN')")
    @Operation(summary = "Marquer le BC comme envoyé / Passer la commande")
    public ResponseEntity<ApiResponse<BonCommandeResponse>> envoyer(@PathVariable String numero) {
        BonCommandeResponse response = transitionBonCommandeUseCase.executer(
            numero, TransitionBonCommandeUseCase.Action.ENVOYER, userResolver.getUtilisateurCourant());
        return ResponseEntity.ok(ApiResponse.succes(response, "Bon de commande envoyé"));
    }

    @DeleteMapping("/{numero}")
    @PreAuthorize("hasAnyRole('CHEF_PRODUCTION', 'ADMIN')")
    @Operation(summary = "Supprimer un bon de commande (Chef de Production)")
    public ResponseEntity<ApiResponse<Void>> supprimer(@PathVariable String numero) {
        transitionBonCommandeUseCase.executer(
            numero, TransitionBonCommandeUseCase.Action.SUPPRIMER, userResolver.getUtilisateurCourant());
        return ResponseEntity.ok(ApiResponse.succes(null, "Bon de commande supprimé"));
    }

    @PostMapping("/{numero}/annuler")
    @PreAuthorize("hasAnyRole('GESTIONNAIRE_STOCK', 'DIRECTEUR_GENERAL', 'ADMIN')")
    @Operation(summary = "Annuler un bon de commande")
    public ResponseEntity<ApiResponse<BonCommandeResponse>> annuler(@PathVariable String numero) {
        BonCommandeResponse response = transitionBonCommandeUseCase.executer(
            numero, TransitionBonCommandeUseCase.Action.ANNULER, userResolver.getUtilisateurCourant());
        return ResponseEntity.ok(ApiResponse.succes(response, "Bon de commande annulé"));
    }

    @PostMapping("/{numero}/cloturer")
    @PreAuthorize("hasAnyRole('GESTIONNAIRE_STOCK', 'DIRECTEUR_GENERAL', 'ADMIN')")
    @Operation(summary = "Clôturer un bon de commande")
    public ResponseEntity<ApiResponse<BonCommandeResponse>> cloturer(@PathVariable String numero) {
        BonCommandeResponse response = transitionBonCommandeUseCase.executer(
            numero, TransitionBonCommandeUseCase.Action.CLOTURER, userResolver.getUtilisateurCourant());
        return ResponseEntity.ok(ApiResponse.succes(response, "Bon de commande clôturé"));
    }
}
