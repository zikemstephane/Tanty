package com.NTFOODS.Api_tanty.modules.production.presentation.controller;

import com.NTFOODS.Api_tanty.modules.production.application.fiche.dto.request.EnregistrerFicheRequest;
import com.NTFOODS.Api_tanty.modules.production.application.fiche.dto.request.ModifierFicheRequest;
import com.NTFOODS.Api_tanty.modules.production.application.fiche.dto.response.FicheResponse;
import com.NTFOODS.Api_tanty.modules.production.application.fiche.mapper.FicheMapper;
import com.NTFOODS.Api_tanty.modules.production.application.fiche.usecase.enregistrer.EnregistrerFicheUseCase;
import com.NTFOODS.Api_tanty.modules.production.application.fiche.usecase.modifier.ModifierFicheUseCase;
import com.NTFOODS.Api_tanty.modules.production.application.fiche.usecase.recalculer.RecalculerPredictionsFicheUseCase;
import com.NTFOODS.Api_tanty.modules.production.domain.fiche.repository.FicheProduitRepository;
import com.NTFOODS.Api_tanty.shared.application.dto.ApiResponse;
import com.NTFOODS.Api_tanty.shared.infrastructure.security.AuthenticatedUserResolver;
import com.NTFOODS.Api_tanty.shared.kernel.exception.EntiteNonTrouveeException;
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

@RestController
@RequestMapping("/api/production/fiches")
@Tag(name = "Production — Fiches journalières", description = "Saisie des fiches produit journalières (verrouillées après validation stock)")
@SecurityRequirement(name = "bearerAuth")
public class FicheController {

    private final EnregistrerFicheUseCase enregistrerFicheUseCase;
    private final ModifierFicheUseCase modifierFicheUseCase;
    private final FicheProduitRepository ficheRepository;
    private final FicheMapper ficheMapper;
    private final AuthenticatedUserResolver userResolver;
    private final RecalculerPredictionsFicheUseCase recalculerPredictionsUseCase;

    public FicheController(EnregistrerFicheUseCase enregistrerFicheUseCase, ModifierFicheUseCase modifierFicheUseCase,
                         FicheProduitRepository ficheRepository, FicheMapper ficheMapper,
                         AuthenticatedUserResolver userResolver,
                         RecalculerPredictionsFicheUseCase recalculerPredictionsUseCase) {
        this.enregistrerFicheUseCase = enregistrerFicheUseCase;
        this.modifierFicheUseCase = modifierFicheUseCase;
        this.recalculerPredictionsUseCase = recalculerPredictionsUseCase;
        this.ficheRepository = ficheRepository;
        this.ficheMapper = ficheMapper;
        this.userResolver = userResolver;
    }

    @PostMapping
    @PreAuthorize("hasAuthority('PRODUCTION_FICHE_PRODUIT_GERER')")
    @Operation(summary = "Enregistrer une fiche produit journalière")
    public ResponseEntity<ApiResponse<FicheResponse>> enregistrer(@RequestBody @Valid EnregistrerFicheRequest req) {
        FicheResponse response = enregistrerFicheUseCase.executer(req, userResolver.getUtilisateurCourant());
        return ResponseEntity.status(HttpStatus.CREATED).body(ApiResponse.succes(response, "Fiche enregistrée"));
    }

    @PatchMapping("/pph/{referencePPH}/date/{date}")
    @PreAuthorize("hasAuthority('PRODUCTION_FICHE_PRODUIT_GERER')")
    @Operation(summary = "Corriger les chiffres d'une fiche",
        description = "Refusé si la fiche est verrouillée (Lock Numérique déclenché par la validation stock du lot).")
    public ResponseEntity<ApiResponse<FicheResponse>> modifier(
        @PathVariable String referencePPH,
        @PathVariable @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date,
        @Valid @RequestBody ModifierFicheRequest request) {
        FicheResponse response = modifierFicheUseCase.executer(referencePPH, date, request,
            userResolver.getUtilisateurCourant());
        return ResponseEntity.ok(ApiResponse.succes(response, "Fiche corrigée"));
    }

    @PostMapping("/pph/{referencePPH}/date/{date}/predictions")
    @PreAuthorize("hasAuthority('PRODUCTION_FICHE_PRODUIT_GERER')")
    @Operation(summary = "Recalculer les prédictions d'une fiche",
        description = "Prédiction dosage (bouillie, fûts) et prédiction postes (grignoter, réalisations). Refusé si la fiche est verrouillée.")
    public ResponseEntity<ApiResponse<FicheResponse>> recalculerPredictions(
        @PathVariable String referencePPH,
        @PathVariable @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date) {
        FicheResponse response = recalculerPredictionsUseCase.executer(referencePPH, date);
        return ResponseEntity.ok(ApiResponse.succes(response, "Prédictions recalculées"));
    }

    @GetMapping("/pph/{referencePPH}")
    @PreAuthorize("hasAuthority('PRODUCTION_REGISTRE_CONSULTER')")
    @Operation(summary = "Lister les fiches d'un PPH")
    public ResponseEntity<ApiResponse<List<FicheResponse>>> parPPH(@PathVariable String referencePPH) {
        var fiches = ficheRepository.listerParPPH(referencePPH).stream().map(ficheMapper::versResponse).toList();
        return ResponseEntity.ok(ApiResponse.succes(fiches, fiches.size() + " fiche(s)"));
    }

    @GetMapping("/pph/{referencePPH}/date/{date}")
    @PreAuthorize("hasAuthority('PRODUCTION_REGISTRE_CONSULTER')")
    @Operation(summary = "Consulter une fiche précise (statut de verrouillage inclus)")
    public ResponseEntity<ApiResponse<FicheResponse>> parPPHEtDate(
        @PathVariable String referencePPH,
        @PathVariable @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date) {
        var fiche = ficheRepository.trouverParPPHEtDate(referencePPH, date)
            .orElseThrow(() -> EntiteNonTrouveeException.de("Fiche de production", referencePPH + " / " + date));
        return ResponseEntity.ok(ApiResponse.succes(ficheMapper.versResponse(fiche), "Fiche trouvée"));
    }
}
