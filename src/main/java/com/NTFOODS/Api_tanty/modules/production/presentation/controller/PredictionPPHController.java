package com.NTFOODS.Api_tanty.modules.production.presentation.controller;

import com.NTFOODS.Api_tanty.shared.infrastructure.security.AuthenticatedUserResolver;
import com.NTFOODS.Api_tanty.modules.production.application.prediction.dto.request.CalculerPredictionRequest;
import com.NTFOODS.Api_tanty.modules.production.application.prediction.dto.response.PredictionPPHResponse;
import com.NTFOODS.Api_tanty.modules.production.application.prediction.usecase.PredictionPPHUseCase;
import com.NTFOODS.Api_tanty.shared.application.dto.ApiResponse;
import jakarta.validation.Valid;
import io.swagger.v3.oas.annotations.Operation;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Prédictions de production par plan PPH. Calculées par l'agent doseur à partir du nombre de fûts,
 * puis lues par les autres rôles : c'est la référence unique de la prédiction d'un plan.
 */
@RestController
@RequestMapping("/api/production/predictions")
public class PredictionPPHController {

    private final PredictionPPHUseCase useCase;
    private final AuthenticatedUserResolver userResolver;

    public PredictionPPHController(PredictionPPHUseCase useCase, AuthenticatedUserResolver userResolver) {
        this.useCase = useCase;
        this.userResolver = userResolver;
    }

    @PutMapping("/{referencePPH}")
    @PreAuthorize("hasAuthority('PRODUCTION_DOSAGE_GERER')")
    @Operation(summary = "Calculer et enregistrer la prédiction d'un plan à partir du nombre de fûts")
    public ResponseEntity<ApiResponse<PredictionPPHResponse>> calculer(@PathVariable String referencePPH,
                                                                       @RequestBody @Valid CalculerPredictionRequest req) {
        var prediction = useCase.calculer(referencePPH, req.nbFuts(), userResolver.getUtilisateurCourant().getMatricule().getValeur());
        return ResponseEntity.ok(ApiResponse.succes(PredictionPPHResponse.de(prediction), "Prédiction enregistrée"));
    }

    @GetMapping("/{referencePPH}")
    @PreAuthorize("hasAuthority('PRODUCTION_REGISTRE_CONSULTER')")
    @Operation(summary = "Lire la prédiction enregistrée d'un plan")
    public ResponseEntity<ApiResponse<PredictionPPHResponse>> lire(@PathVariable String referencePPH) {
        return ResponseEntity.ok(ApiResponse.succes(PredictionPPHResponse.de(useCase.lire(referencePPH)), "Prédiction"));
    }

    @GetMapping
    @PreAuthorize("hasAuthority('PRODUCTION_REGISTRE_CONSULTER')")
    @Operation(summary = "Lister les prédictions enregistrées")
    public ResponseEntity<ApiResponse<List<PredictionPPHResponse>>> lister() {
        var liste = useCase.lister().stream().map(PredictionPPHResponse::de).toList();
        return ResponseEntity.ok(ApiResponse.succes(liste, liste.size() + " prédiction(s)"));
    }
}
