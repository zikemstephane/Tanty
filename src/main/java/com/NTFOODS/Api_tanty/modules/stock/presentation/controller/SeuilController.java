package com.NTFOODS.Api_tanty.modules.stock.presentation.controller;

import com.NTFOODS.Api_tanty.modules.stock.application.alerte.dto.response.AlerteResponse;
import com.NTFOODS.Api_tanty.modules.stock.application.seuil.dto.request.DefinirSeuilRequest;
import com.NTFOODS.Api_tanty.modules.stock.application.seuil.dto.response.HistoriqueSeuilResponse;
import com.NTFOODS.Api_tanty.modules.stock.application.seuil.dto.response.SeuilResponse;
import com.NTFOODS.Api_tanty.modules.stock.application.seuil.usecase.DefinirSeuilUseCase;
import com.NTFOODS.Api_tanty.modules.stock.application.seuil.usecase.EvaluerSeuilsUseCase;
import com.NTFOODS.Api_tanty.modules.stock.application.seuil.usecase.ListerSeuilsUseCase;
import com.NTFOODS.Api_tanty.modules.stock.infrastructure.persistence.seuil.jpa.HistoriqueSeuilJpaEntity;
import com.NTFOODS.Api_tanty.modules.stock.infrastructure.persistence.seuil.jpa.HistoriqueSeuilJpaRepository;
import com.NTFOODS.Api_tanty.shared.application.dto.ApiResponse;
import com.NTFOODS.Api_tanty.shared.infrastructure.security.AuthenticatedUserResolver;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/stock/seuils")
@Tag(name = "Stock — Seuils", description = "Seuils de réapprovisionnement (gouvernance exclusive du DG) et évaluation des alertes")
@SecurityRequirement(name = "bearerAuth")
public class SeuilController {

    private final DefinirSeuilUseCase definirSeuilUseCase;
    private final ListerSeuilsUseCase listerSeuilsUseCase;
    private final EvaluerSeuilsUseCase evaluerSeuilsUseCase;
    private final AuthenticatedUserResolver userResolver;
    private final HistoriqueSeuilJpaRepository historiqueRepository;

    public SeuilController(DefinirSeuilUseCase definirSeuilUseCase, ListerSeuilsUseCase listerSeuilsUseCase,
                          EvaluerSeuilsUseCase evaluerSeuilsUseCase, AuthenticatedUserResolver userResolver,
                          HistoriqueSeuilJpaRepository historiqueRepository) {
        this.definirSeuilUseCase = definirSeuilUseCase;
        this.listerSeuilsUseCase = listerSeuilsUseCase;
        this.evaluerSeuilsUseCase = evaluerSeuilsUseCase;
        this.userResolver = userResolver;
        this.historiqueRepository = historiqueRepository;
    }

    @PostMapping
    @PreAuthorize("hasAuthority('STOCK_SEUIL_GERER')")
    @Operation(summary = "Définir / mettre à jour un seuil", description = "Réservé au Directeur Général : gouvernance exclusive des seuils.")
    public ResponseEntity<ApiResponse<SeuilResponse>> definir(@Valid @RequestBody DefinirSeuilRequest request) {
        SeuilResponse response = definirSeuilUseCase.executer(request, userResolver.getUtilisateurCourant());
        return ResponseEntity.ok(ApiResponse.succes(response, "Seuil défini"));
    }

    @GetMapping
    @PreAuthorize("isAuthenticated()")
    @Operation(summary = "Lister les seuils")
    public ResponseEntity<ApiResponse<List<SeuilResponse>>> lister() {
        List<SeuilResponse> seuils = listerSeuilsUseCase.executer();
        return ResponseEntity.ok(ApiResponse.succes(seuils, seuils.size() + " seuil(s)"));
    }

    @GetMapping("/historique")
    @PreAuthorize("hasAuthority('STOCK_SEUIL_GERER')")
    @Operation(summary = "Historique des modifications de seuils (qui, quand, ancienne → nouvelle valeur)")
    public ResponseEntity<ApiResponse<List<HistoriqueSeuilResponse>>> historique(
        @RequestParam(required = false) String codeProduit) {
        List<HistoriqueSeuilJpaEntity> entites = (codeProduit != null && !codeProduit.isBlank())
            ? historiqueRepository.findByCodeProduitOrderByDateModificationDesc(codeProduit.trim().toUpperCase())
            : historiqueRepository.findAllByOrderByDateModificationDesc();
        List<HistoriqueSeuilResponse> r = entites.stream()
            .map(e -> new HistoriqueSeuilResponse(e.getId(), e.getCodeProduit(), e.getTypeEntrepot(),
                e.getAncienneValeur(), e.getNouvelleValeur(), e.getMatriculeAuteur(), e.getDateModification()))
            .toList();
        return ResponseEntity.ok(ApiResponse.succes(r, r.size() + " modification(s)"));
    }

    @PostMapping("/evaluer")
    @PreAuthorize("hasAuthority('STOCK_SEUIL_GERER')")
    @Operation(summary = "Évaluer les seuils et générer les alertes")
    public ResponseEntity<ApiResponse<List<AlerteResponse>>> evaluer() {
        List<AlerteResponse> alertes = evaluerSeuilsUseCase.executer();
        return ResponseEntity.ok(ApiResponse.succes(alertes, alertes.size() + " alerte(s) générée(s)"));
    }
}
