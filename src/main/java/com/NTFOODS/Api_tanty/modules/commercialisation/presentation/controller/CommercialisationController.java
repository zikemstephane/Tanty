package com.NTFOODS.Api_tanty.modules.commercialisation.presentation.controller;

import com.NTFOODS.Api_tanty.modules.commercialisation.application.dto.CreerPromotionRequest;
import com.NTFOODS.Api_tanty.modules.commercialisation.application.dto.PromotionResponse;
import com.NTFOODS.Api_tanty.modules.commercialisation.application.usecase.GererPromotionUseCase;
import com.NTFOODS.Api_tanty.modules.commercialisation.domain.enums.StatutPromotion;
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
 * Contrôleur REST — Commercialisation (promotions).
 *
 * <p>Les gardes sont posées par permission, endpoint par endpoint. Le garde de
 * classe précédent raisonnait par rôle — {@code DIRECTEUR_GENERAL},
 * {@code DIRECTEUR_COMMERCIAL}, {@code ADMIN} — et excluait la Chargée RP, qui
 * détient pourtant {@code COMMERCIALISATION_PROMOTION_GERER} depuis qu'elle
 * assure le rôle fonctionnel de Directeur Commercial. Elle possédait donc un
 * droit qu'aucun endpoint ne lisait.</p>
 *
 * <p>Ce n'était pas sans conséquence sur le terrain : les promotions déterminent
 * les remises que le serveur accepte sur une vente. Aucune promotion ne pouvant
 * être créée, toute remise saisie par un commercial était refusée.</p>
 *
 * <p>Les campagnes marketing ont été retirées : elles sont hors périmètre du
 * projet.</p>
 *
 * <pre>
 * POST /promotions                    créer
 * GET  /promotions                    lister (filtre par statut)
 * GET  /promotions/{code}             consulter
 * PUT  /promotions/{code}/activer     activer
 * PUT  /promotions/{code}/suspendre   suspendre
 * PUT  /promotions/{code}/annuler     annuler
 * </pre>
 */
@RestController
@RequestMapping("/api/commercialisation")
@Tag(name = "Commercialisation — Promotions", description = "Gestion des promotions commerciales")
@SecurityRequirement(name = "bearerAuth")
public class CommercialisationController {

    private final GererPromotionUseCase promotionUseCase;
    private final AuthenticatedUserResolver userResolver;

    public CommercialisationController(GererPromotionUseCase promotionUseCase,
                                        AuthenticatedUserResolver userResolver) {
        this.promotionUseCase = promotionUseCase;
        this.userResolver = userResolver;
    }

    @PostMapping("/promotions")
    @PreAuthorize("hasAuthority('COMMERCIALISATION_PROMOTION_GERER')")
    @Operation(summary = "Créer une promotion",
        description = "Une promotion active sur un couple produit × segment client ouvre droit à une remise " +
                      "sur les ventes terrain. Sans promotion, aucune remise n'est acceptée.")
    public ResponseEntity<ApiResponse<PromotionResponse>> creerPromotion(@RequestBody @Valid CreerPromotionRequest request) {
        var r = promotionUseCase.creer(request, userResolver.getMatriculeCourant());
        return ResponseEntity.status(HttpStatus.CREATED).body(ApiResponse.succes(r, "Promotion créée"));
    }

    @GetMapping("/promotions")
    @PreAuthorize("hasAnyAuthority('COMMERCIALISATION_PROMOTION_GERER','COMMERCIAL_VENTE_CONSULTER')")
    @Operation(summary = "Lister les promotions (filtre par statut)")
    public ResponseEntity<ApiResponse<List<PromotionResponse>>> listerPromotions(
        @RequestParam(required = false) StatutPromotion statut,
        @RequestParam(required = false, defaultValue = "false") boolean activesSeulement) {
        List<PromotionResponse> r = (activesSeulement)
            ? promotionUseCase.listerActives()
            : (statut != null) ? promotionUseCase.listerParStatut(statut) : promotionUseCase.listerTous();
        return ResponseEntity.ok(ApiResponse.succes(r, r.size() + " promotion(s)"));
    }

    @GetMapping("/promotions/{code}")
    @PreAuthorize("hasAnyAuthority('COMMERCIALISATION_PROMOTION_GERER','COMMERCIAL_VENTE_CONSULTER')")
    @Operation(summary = "Consulter une promotion")
    public ResponseEntity<ApiResponse<PromotionResponse>> consulterPromotion(@PathVariable String code) {
        return ResponseEntity.ok(ApiResponse.succes(promotionUseCase.consulter(code), "Promotion"));
    }

    @PutMapping("/promotions/{code}/activer")
    @PreAuthorize("hasAuthority('COMMERCIALISATION_PROMOTION_GERER')")
    @Operation(summary = "Activer une promotion")
    public ResponseEntity<ApiResponse<PromotionResponse>> activerPromotion(@PathVariable String code) {
        return ResponseEntity.ok(ApiResponse.succes(promotionUseCase.activer(code), "Promotion activée"));
    }

    @PutMapping("/promotions/{code}/suspendre")
    @PreAuthorize("hasAuthority('COMMERCIALISATION_PROMOTION_GERER')")
    @Operation(summary = "Suspendre une promotion")
    public ResponseEntity<ApiResponse<PromotionResponse>> suspendrePromotion(@PathVariable String code) {
        return ResponseEntity.ok(ApiResponse.succes(promotionUseCase.suspendre(code), "Promotion suspendue"));
    }

    @PutMapping("/promotions/{code}/annuler")
    @PreAuthorize("hasAuthority('COMMERCIALISATION_PROMOTION_GERER')")
    @Operation(summary = "Annuler une promotion")
    public ResponseEntity<ApiResponse<PromotionResponse>> annulerPromotion(@PathVariable String code) {
        return ResponseEntity.ok(ApiResponse.succes(promotionUseCase.annuler(code), "Promotion annulée"));
    }
}
