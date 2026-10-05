package com.NTFOODS.Api_tanty.modules.commercial.presentation.controller;

import com.NTFOODS.Api_tanty.modules.commercial.application.tarification.dto.request.AccorderPrixExceptionnelRequest;
import com.NTFOODS.Api_tanty.modules.commercial.application.tarification.dto.request.DefinirGrilleTarifaireRequest;
import com.NTFOODS.Api_tanty.modules.commercial.application.tarification.dto.response.GrilleTarifaireResponse;
import com.NTFOODS.Api_tanty.modules.commercial.application.tarification.dto.response.PrixExceptionnelResponse;
import com.NTFOODS.Api_tanty.modules.commercial.application.tarification.mapper.GrilleTarifaireMapper;
import com.NTFOODS.Api_tanty.modules.commercial.application.tarification.usecase.AccorderPrixExceptionnelUseCase;
import com.NTFOODS.Api_tanty.modules.commercial.application.tarification.usecase.DefinirGrilleTarifaireUseCase;
import com.NTFOODS.Api_tanty.modules.commercial.domain.client.enums.TypeClient;
import com.NTFOODS.Api_tanty.modules.commercial.domain.tarification.repository.GrilleTarifaireRepository;
import com.NTFOODS.Api_tanty.modules.commercial.domain.tarification.repository.PrixExceptionnelRepository;
import com.NTFOODS.Api_tanty.shared.application.dto.ApiResponse;
import com.NTFOODS.Api_tanty.modules.commercial.application.tarification.mapper.PrixExceptionnelMapper;
import com.NTFOODS.Api_tanty.shared.infrastructure.security.AuthenticatedUserResolver;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

/**
 * Contrôleur REST — Tarification Verrouillée (Pilier 3).
 *
 * Les prix appliqués sur le terrain proviennent EXCLUSIVEMENT de la grille
 * tarifaire homologuée par segment de clientèle. Toute dérogation ponctuelle
 * passe par un "prix exceptionnel", accordé à distance par le DG, le
 * Directeur Commercial, la Chargée RP &amp; Commercial ou le Comptable.
 */
@RestController
@RequestMapping("/api/commercial/tarification")
@Tag(name = "Commercial — Tarification", description = "Grille tarifaire homologuée et prix exceptionnels")
@SecurityRequirement(name = "bearerAuth")
public class TarificationController {

    private final DefinirGrilleTarifaireUseCase definirGrilleTarifaireUseCase;
    private final AccorderPrixExceptionnelUseCase accorderPrixExceptionnelUseCase;
    private final GrilleTarifaireRepository grilleTarifaireRepository;
    private final PrixExceptionnelRepository prixExceptionnelRepository;
    private final GrilleTarifaireMapper grilleTarifaireMapper;
    private final PrixExceptionnelMapper prixExceptionnelMapper;
    private final AuthenticatedUserResolver userResolver;

    public TarificationController(DefinirGrilleTarifaireUseCase definirGrilleTarifaireUseCase,
                                   AccorderPrixExceptionnelUseCase accorderPrixExceptionnelUseCase,
                                   GrilleTarifaireRepository grilleTarifaireRepository,
                                   PrixExceptionnelRepository prixExceptionnelRepository,
                                   GrilleTarifaireMapper grilleTarifaireMapper,
                                   PrixExceptionnelMapper prixExceptionnelMapper,
                                   AuthenticatedUserResolver userResolver) {
        this.definirGrilleTarifaireUseCase = definirGrilleTarifaireUseCase;
        this.accorderPrixExceptionnelUseCase = accorderPrixExceptionnelUseCase;
        this.grilleTarifaireRepository = grilleTarifaireRepository;
        this.prixExceptionnelRepository = prixExceptionnelRepository;
        this.grilleTarifaireMapper = grilleTarifaireMapper;
        this.prixExceptionnelMapper = prixExceptionnelMapper;
        this.userResolver = userResolver;
    }

    @PostMapping("/grille")
    @PreAuthorize("hasAuthority('COMMERCIAL_GRILLE_TARIFAIRE_GERER')")
    @Operation(summary = "Définir / réviser un prix homologué par produit et segment client")
    public ResponseEntity<ApiResponse<GrilleTarifaireResponse>> definirGrille(
        @Valid @RequestBody DefinirGrilleTarifaireRequest request) {
        var reponse = definirGrilleTarifaireUseCase.executer(request, userResolver.getUtilisateurCourant());
        return ResponseEntity.status(HttpStatus.CREATED).body(ApiResponse.succes(reponse, "Prix homologué défini"));
    }

    @GetMapping("/grille")
    @PreAuthorize("isAuthenticated()")
    @Operation(summary = "Consulter la grille tarifaire (filtre optionnel par produit ou segment)")
    public ResponseEntity<ApiResponse<List<GrilleTarifaireResponse>>> listerGrille(
        @RequestParam(required = false) String codeProduit,
        @RequestParam(required = false) TypeClient segment) {
        List<GrilleTarifaireResponse> resultats;
        if (codeProduit != null) {
            resultats = grilleTarifaireRepository.listerParProduit(codeProduit).stream()
                .map(grilleTarifaireMapper::versResponse).toList();
        } else if (segment != null) {
            resultats = grilleTarifaireRepository.listerParSegment(segment).stream()
                .map(grilleTarifaireMapper::versResponse).toList();
        } else {
            resultats = grilleTarifaireRepository.listerTous().stream()
                .map(grilleTarifaireMapper::versResponse).toList();
        }
        return ResponseEntity.ok(ApiResponse.succes(resultats, resultats.size() + " tarif(s)"));
    }

    @PostMapping("/prix-exceptionnel")
    @PreAuthorize("hasAuthority('COMMERCIAL_PRIX_EXCEPTIONNEL_GERER')")
    @Operation(summary = "Débloquer un prix exceptionnel à distance")
    public ResponseEntity<ApiResponse<PrixExceptionnelResponse>> accorderPrixExceptionnel(
        @Valid @RequestBody AccorderPrixExceptionnelRequest request) {
        var reponse = accorderPrixExceptionnelUseCase.executer(request, userResolver.getUtilisateurCourant());
        return ResponseEntity.status(HttpStatus.CREATED).body(ApiResponse.succes(reponse, "Prix exceptionnel accordé"));
    }

    @GetMapping("/prix-exceptionnel/client/{codeClient}")
    @PreAuthorize("hasAnyAuthority('COMMERCIAL_PRIX_EXCEPTIONNEL_GERER','AUTH_PERMISSION_CONSULTER')")
    @Operation(summary = "Historique des prix exceptionnels accordés à un client")
    public ResponseEntity<ApiResponse<List<PrixExceptionnelResponse>>> parClient(@PathVariable String codeClient) {
        var r = prixExceptionnelRepository.listerParClient(codeClient).stream()
            .map(prixExceptionnelMapper::versResponse).toList();
        return ResponseEntity.ok(ApiResponse.succes(r, r.size() + " dérogation(s)"));
    }

    @GetMapping("/prix-exceptionnel/periode")
    @PreAuthorize("hasAnyAuthority('COMMERCIAL_PRIX_EXCEPTIONNEL_GERER','AUTH_PERMISSION_CONSULTER')")
    @Operation(summary = "Prix exceptionnels accordés sur une période (surveillance des dérogations tarifaires)")
    public ResponseEntity<ApiResponse<List<PrixExceptionnelResponse>>> parPeriode(
        @RequestParam @org.springframework.format.annotation.DateTimeFormat(iso = org.springframework.format.annotation.DateTimeFormat.ISO.DATE) LocalDate debut,
        @RequestParam @org.springframework.format.annotation.DateTimeFormat(iso = org.springframework.format.annotation.DateTimeFormat.ISO.DATE) LocalDate fin) {
        var r = prixExceptionnelRepository.listerParPeriode(debut, fin).stream()
            .map(prixExceptionnelMapper::versResponse).toList();
        return ResponseEntity.ok(ApiResponse.succes(r, r.size() + " dérogation(s)"));
    }
}
