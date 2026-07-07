package com.NTFOODS.Api_tanty.modules.commercialisation.presentation.controller;

import com.NTFOODS.Api_tanty.modules.commercialisation.application.dto.CampagneResponse;
import com.NTFOODS.Api_tanty.modules.commercialisation.application.dto.CreerCampagneRequest;
import com.NTFOODS.Api_tanty.modules.commercialisation.application.dto.CreerPromotionRequest;
import com.NTFOODS.Api_tanty.modules.commercialisation.application.dto.PromotionResponse;
import com.NTFOODS.Api_tanty.modules.commercialisation.application.usecase.GererCampagneUseCase;
import com.NTFOODS.Api_tanty.modules.commercialisation.application.usecase.GererPromotionUseCase;
import com.NTFOODS.Api_tanty.modules.commercialisation.domain.enums.StatutCampagne;
import com.NTFOODS.Api_tanty.modules.commercialisation.domain.enums.StatutPromotion;
import com.NTFOODS.Api_tanty.shared.application.dto.ApiResponse;
import com.NTFOODS.Api_tanty.shared.infrastructure.security.AuthenticatedUserResolver;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;

/**
 * Contrôleur REST — Commercialisation (promotions & campagnes marketing).
 *
 * Promotions:
 * POST   /api/commercialisation/promotions               → créer une promotion
 * GET    /api/commercialisation/promotions                → lister (filtre par statut)
 * GET    /api/commercialisation/promotions/{code}         → consulter
 * PUT    /api/commercialisation/promotions/{code}/activer → activer
 * PUT    /api/commercialisation/promotions/{code}/suspendre → suspendre
 * PUT    /api/commercialisation/promotions/{code}/annuler → annuler
 *
 * Campagnes:
 * POST   /api/commercialisation/campagnes                 → créer une campagne
 * GET    /api/commercialisation/campagnes                 → lister (filtre par statut/période)
 * GET    /api/commercialisation/campagnes/{code}          → consulter
 * PUT    /api/commercialisation/campagnes/{code}/demarrer → démarrer
 * PUT    /api/commercialisation/campagnes/{code}/terminer → terminer
 * PUT    /api/commercialisation/campagnes/{code}/annuler  → annuler
 * PUT    /api/commercialisation/campagnes/{code}/depense  → enregistrer une dépense
 * PUT    /api/commercialisation/campagnes/{code}/promotion → lier une promotion
 */
@RestController
@RequestMapping("/api/commercialisation")
@Tag(name = "Commercialisation — Promotions & Campagnes", description = "Gestion des promotions et campagnes marketing")
@SecurityRequirement(name = "bearerAuth")
@PreAuthorize("hasAnyRole('DIRECTEUR_GENERAL', 'DIRECTEUR_COMMERCIAL', 'ADMIN')")
public class CommercialisationController {

    private final GererPromotionUseCase promotionUseCase;
    private final GererCampagneUseCase campagneUseCase;
    private final AuthenticatedUserResolver userResolver;

    public CommercialisationController(GererPromotionUseCase promotionUseCase,
                                        GererCampagneUseCase campagneUseCase,
                                        AuthenticatedUserResolver userResolver) {
        this.promotionUseCase = promotionUseCase;
        this.campagneUseCase = campagneUseCase;
        this.userResolver = userResolver;
    }

    @PostMapping("/promotions")
    @Operation(summary = "Créer une promotion")
    public ResponseEntity<ApiResponse<PromotionResponse>> creerPromotion(@RequestBody @Valid CreerPromotionRequest request) {
        var r = promotionUseCase.creer(request, userResolver.getMatriculeCourant());
        return ResponseEntity.status(HttpStatus.CREATED).body(ApiResponse.succes(r, "Promotion créée"));
    }

    @GetMapping("/promotions")
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
    @Operation(summary = "Consulter une promotion")
    public ResponseEntity<ApiResponse<PromotionResponse>> consulterPromotion(@PathVariable String code) {
        return ResponseEntity.ok(ApiResponse.succes(promotionUseCase.consulter(code), "Promotion"));
    }

    @PutMapping("/promotions/{code}/activer")
    @Operation(summary = "Activer une promotion")
    public ResponseEntity<ApiResponse<PromotionResponse>> activerPromotion(@PathVariable String code) {
        return ResponseEntity.ok(ApiResponse.succes(promotionUseCase.activer(code), "Promotion activée"));
    }

    @PutMapping("/promotions/{code}/suspendre")
    @Operation(summary = "Suspendre une promotion")
    public ResponseEntity<ApiResponse<PromotionResponse>> suspendrePromotion(@PathVariable String code) {
        return ResponseEntity.ok(ApiResponse.succes(promotionUseCase.suspendre(code), "Promotion suspendue"));
    }

    @PutMapping("/promotions/{code}/annuler")
    @Operation(summary = "Annuler une promotion")
    public ResponseEntity<ApiResponse<PromotionResponse>> annulerPromotion(@PathVariable String code) {
        return ResponseEntity.ok(ApiResponse.succes(promotionUseCase.annuler(code), "Promotion annulée"));
    }

    @PostMapping("/campagnes")
    @Operation(summary = "Créer une campagne marketing")
    public ResponseEntity<ApiResponse<CampagneResponse>> creerCampagne(@RequestBody @Valid CreerCampagneRequest request) {
        var r = campagneUseCase.creer(request, userResolver.getMatriculeCourant());
        return ResponseEntity.status(HttpStatus.CREATED).body(ApiResponse.succes(r, "Campagne créée"));
    }

    @GetMapping("/campagnes")
    @Operation(summary = "Lister les campagnes (filtre par statut ou période)")
    public ResponseEntity<ApiResponse<List<CampagneResponse>>> listerCampagnes(
        @RequestParam(required = false) StatutCampagne statut,
        @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate debut,
        @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate fin) {
        List<CampagneResponse> r;
        if (debut != null && fin != null) {
            r = campagneUseCase.listerParPeriode(debut, fin);
        } else if (statut != null) {
            r = campagneUseCase.listerParStatut(statut);
        } else {
            r = campagneUseCase.listerTous();
        }
        return ResponseEntity.ok(ApiResponse.succes(r, r.size() + " campagne(s)"));
    }

    @GetMapping("/campagnes/{code}")
    @Operation(summary = "Consulter une campagne")
    public ResponseEntity<ApiResponse<CampagneResponse>> consulterCampagne(@PathVariable String code) {
        return ResponseEntity.ok(ApiResponse.succes(campagneUseCase.consulter(code), "Campagne"));
    }

    @PutMapping("/campagnes/{code}/demarrer")
    @Operation(summary = "Démarrer une campagne planifiée")
    public ResponseEntity<ApiResponse<CampagneResponse>> demarrerCampagne(@PathVariable String code) {
        return ResponseEntity.ok(ApiResponse.succes(campagneUseCase.demarrer(code), "Campagne démarrée"));
    }

    @PutMapping("/campagnes/{code}/terminer")
    @Operation(summary = "Terminer une campagne en cours")
    public ResponseEntity<ApiResponse<CampagneResponse>> terminerCampagne(@PathVariable String code) {
        return ResponseEntity.ok(ApiResponse.succes(campagneUseCase.terminer(code), "Campagne terminée"));
    }

    @PutMapping("/campagnes/{code}/annuler")
    @Operation(summary = "Annuler une campagne")
    public ResponseEntity<ApiResponse<CampagneResponse>> annulerCampagne(@PathVariable String code) {
        return ResponseEntity.ok(ApiResponse.succes(campagneUseCase.annuler(code), "Campagne annulée"));
    }

    @PutMapping("/campagnes/{code}/depense")
    @Operation(summary = "Enregistrer une dépense sur une campagne")
    public ResponseEntity<ApiResponse<CampagneResponse>> enregistrerDepense(
        @PathVariable String code,
        @RequestBody Map<String, Double> body) {
        return ResponseEntity.ok(ApiResponse.succes(
            campagneUseCase.enregistrerDepense(code, body.get("montantFCFA")), "Dépense enregistrée"));
    }

    @PutMapping("/campagnes/{code}/promotion")
    @Operation(summary = "Lier une promotion à une campagne")
    public ResponseEntity<ApiResponse<CampagneResponse>> lierPromotion(
        @PathVariable String code,
        @RequestBody Map<String, String> body) {
        return ResponseEntity.ok(ApiResponse.succes(
            campagneUseCase.lierPromotion(code, body.get("codePromotion")), "Promotion liée à la campagne"));
    }
}
