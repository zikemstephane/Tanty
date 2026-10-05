package com.NTFOODS.Api_tanty.modules.production.presentation.controller;

import com.NTFOODS.Api_tanty.modules.production.application.besoin.dto.request.SignalerBesoinRequest;
import com.NTFOODS.Api_tanty.modules.production.application.besoin.dto.response.ExpressionBesoinResponse;
import com.NTFOODS.Api_tanty.modules.production.application.besoin.mapper.ExpressionBesoinMapper;
import com.NTFOODS.Api_tanty.modules.production.application.besoin.usecase.SignalerBesoinUseCase;
import com.NTFOODS.Api_tanty.modules.production.application.besoin.usecase.TraiterBesoinUseCase;
import com.NTFOODS.Api_tanty.modules.production.domain.besoin.enums.StatutBesoin;
import com.NTFOODS.Api_tanty.modules.production.domain.besoin.repository.ExpressionBesoinRepository;
import com.NTFOODS.Api_tanty.shared.application.dto.ApiResponse;
import com.NTFOODS.Api_tanty.shared.infrastructure.security.AuthenticatedUserResolver;
import com.NTFOODS.Api_tanty.shared.kernel.exception.EntiteNonTrouveeException;
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
@RequestMapping("/api/production/besoins")
@Tag(name = "Production — Expressions de besoin", description = "Signalement de ruptures de matières premières ou emballages")
@SecurityRequirement(name = "bearerAuth")
public class ExpressionBesoinController {

    private final SignalerBesoinUseCase signalerUseCase;
    private final TraiterBesoinUseCase traiterUseCase;
    private final ExpressionBesoinRepository repository;
    private final ExpressionBesoinMapper mapper;
    private final AuthenticatedUserResolver userResolver;

    public ExpressionBesoinController(SignalerBesoinUseCase signalerUseCase, TraiterBesoinUseCase traiterUseCase,
                                       ExpressionBesoinRepository repository, ExpressionBesoinMapper mapper,
                                       AuthenticatedUserResolver userResolver) {
        this.signalerUseCase = signalerUseCase;
        this.traiterUseCase = traiterUseCase;
        this.repository = repository;
        this.mapper = mapper;
        this.userResolver = userResolver;
    }

    @PostMapping
    @PreAuthorize("hasAuthority('PRODUCTION_EXPRESSION_BESOIN_GERER')")
    @Operation(summary = "Signaler une expression de besoin (rupture matière première ou emballage)")
    public ResponseEntity<ApiResponse<ExpressionBesoinResponse>> signaler(
            @RequestBody @Valid SignalerBesoinRequest req) {
        ExpressionBesoinResponse response = signalerUseCase.executer(req, userResolver.getUtilisateurCourant());
        return ResponseEntity.status(HttpStatus.CREATED)
            .body(ApiResponse.succes(response, "Expression de besoin signalée"));
    }

    @PatchMapping("/{numero}/valider")
    @PreAuthorize("hasAuthority('PRODUCTION_EXPRESSION_BESOIN_GERER')")
    @Operation(summary = "Valider une expression de besoin")
    public ResponseEntity<ApiResponse<ExpressionBesoinResponse>> valider(@PathVariable String numero) {
        return ResponseEntity.ok(ApiResponse.succes(
            traiterUseCase.valider(numero, userResolver.getMatriculeCourant()), "Besoin validé"));
    }

    @PatchMapping("/{numero}/rejeter")
    @PreAuthorize("hasAuthority('PRODUCTION_EXPRESSION_BESOIN_GERER')")
    @Operation(summary = "Rejeter une expression de besoin")
    public ResponseEntity<ApiResponse<ExpressionBesoinResponse>> rejeter(
            @PathVariable String numero, @RequestParam String motif) {
        return ResponseEntity.ok(ApiResponse.succes(
            traiterUseCase.rejeter(numero, userResolver.getMatriculeCourant(), motif), "Besoin rejeté"));
    }

    @GetMapping
    @PreAuthorize("hasAuthority('PRODUCTION_EXPRESSION_BESOIN_GERER')")
    @Operation(summary = "Lister les expressions de besoin (filtre optionnel par statut)")
    public ResponseEntity<ApiResponse<List<ExpressionBesoinResponse>>> lister(
            @RequestParam(required = false) StatutBesoin statut) {
        List<ExpressionBesoinResponse> list = (statut != null)
            ? repository.listerParStatut(statut).stream().map(mapper::versResponse).toList()
            : repository.listerTous().stream().map(mapper::versResponse).toList();
        return ResponseEntity.ok(ApiResponse.succes(list, list.size() + " besoin(s)"));
    }

    @GetMapping("/{numero}")
    @PreAuthorize("hasAuthority('PRODUCTION_EXPRESSION_BESOIN_GERER')")
    @Operation(summary = "Consulter une expression de besoin")
    public ResponseEntity<ApiResponse<ExpressionBesoinResponse>> consulter(@PathVariable String numero) {
        return repository.trouverParNumero(numero)
            .map(b -> ResponseEntity.ok(ApiResponse.succes(mapper.versResponse(b), "Besoin trouvé")))
            .orElseThrow(() -> EntiteNonTrouveeException.de("Expression de besoin", numero));
    }
}
