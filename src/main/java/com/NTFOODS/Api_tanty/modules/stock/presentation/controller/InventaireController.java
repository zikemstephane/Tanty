package com.NTFOODS.Api_tanty.modules.stock.presentation.controller;

import com.NTFOODS.Api_tanty.modules.stock.application.inventaire.dto.request.OuvrirInventaireRequest;
import com.NTFOODS.Api_tanty.modules.stock.application.inventaire.dto.request.SaisirComptageRequest;
import com.NTFOODS.Api_tanty.modules.stock.application.inventaire.dto.response.InventaireResponse;
import com.NTFOODS.Api_tanty.modules.stock.application.inventaire.usecase.cloturer.CloturerComptageUseCase;
import com.NTFOODS.Api_tanty.modules.stock.application.inventaire.usecase.consulter.ConsulterInventaireUseCase;
import com.NTFOODS.Api_tanty.modules.stock.application.inventaire.usecase.lister.ListerInventairesUseCase;
import com.NTFOODS.Api_tanty.modules.stock.application.inventaire.usecase.ouvrir.OuvrirInventaireUseCase;
import com.NTFOODS.Api_tanty.modules.stock.application.inventaire.usecase.saisir.SaisirComptageUseCase;
import com.NTFOODS.Api_tanty.modules.stock.application.inventaire.usecase.valider.ValiderInventaireUseCase;
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
@RequestMapping("/api/stock/inventaires")
@Tag(name = "Stock — Inventaires", description = "Inventaires complet / tournant / ciblé / surprise")
@SecurityRequirement(name = "bearerAuth")
public class InventaireController {

    private final OuvrirInventaireUseCase ouvrirInventaireUseCase;
    private final SaisirComptageUseCase saisirComptageUseCase;
    private final CloturerComptageUseCase cloturerComptageUseCase;
    private final ValiderInventaireUseCase validerInventaireUseCase;
    private final ListerInventairesUseCase listerInventairesUseCase;
    private final ConsulterInventaireUseCase consulterInventaireUseCase;
    private final AuthenticatedUserResolver userResolver;

    public InventaireController(OuvrirInventaireUseCase ouvrirInventaireUseCase,
                              SaisirComptageUseCase saisirComptageUseCase,
                              CloturerComptageUseCase cloturerComptageUseCase,
                              ValiderInventaireUseCase validerInventaireUseCase,
                              ListerInventairesUseCase listerInventairesUseCase,
                              ConsulterInventaireUseCase consulterInventaireUseCase,
                              AuthenticatedUserResolver userResolver) {
        this.ouvrirInventaireUseCase = ouvrirInventaireUseCase;
        this.saisirComptageUseCase = saisirComptageUseCase;
        this.cloturerComptageUseCase = cloturerComptageUseCase;
        this.validerInventaireUseCase = validerInventaireUseCase;
        this.listerInventairesUseCase = listerInventairesUseCase;
        this.consulterInventaireUseCase = consulterInventaireUseCase;
        this.userResolver = userResolver;
    }

    @PostMapping
    @PreAuthorize("hasAuthority('STOCK_INVENTAIRE_GERER')")
    @Operation(summary = "Ouvrir une session d'inventaire")
    public ResponseEntity<ApiResponse<InventaireResponse>> ouvrir(@Valid @RequestBody OuvrirInventaireRequest request) {
        InventaireResponse response = ouvrirInventaireUseCase.executer(request, userResolver.getUtilisateurCourant());
        return ResponseEntity.status(HttpStatus.CREATED).body(ApiResponse.succes(response, "Inventaire ouvert : " + response.numero()));
    }

    @PatchMapping("/{numero}/comptage")
    @PreAuthorize("hasAuthority('STOCK_INVENTAIRE_GERER')")
    @Operation(summary = "Saisir le comptage d'une ligne")
    public ResponseEntity<ApiResponse<InventaireResponse>> saisirComptage(
        @PathVariable String numero, @Valid @RequestBody SaisirComptageRequest request) {
        return ResponseEntity.ok(ApiResponse.succes(saisirComptageUseCase.executer(numero, request), "Comptage enregistré"));
    }

    @PostMapping("/{numero}/cloturer-comptage")
    @PreAuthorize("hasAuthority('STOCK_INVENTAIRE_GERER')")
    @Operation(summary = "Clôturer le comptage (passe à l'état compté)")
    public ResponseEntity<ApiResponse<InventaireResponse>> cloturerComptage(@PathVariable String numero) {
        return ResponseEntity.ok(ApiResponse.succes(cloturerComptageUseCase.executer(numero), "Comptage clôturé"));
    }

    @PostMapping("/{numero}/valider")
    @PreAuthorize("hasAuthority('STOCK_INVENTAIRE_GERER')")
    @Operation(summary = "Valider l'inventaire (DG) — ajuste le stock")
    public ResponseEntity<ApiResponse<InventaireResponse>> valider(
        @PathVariable String numero, @RequestParam(required = false) String justification) {
        InventaireResponse response = validerInventaireUseCase.executer(numero, justification, userResolver.getUtilisateurCourant());
        return ResponseEntity.ok(ApiResponse.succes(response, "Inventaire validé — stock ajusté"));
    }

    @GetMapping
    @PreAuthorize("isAuthenticated()")
    @Operation(summary = "Lister les inventaires (filtre optionnel par statut)")
    public ResponseEntity<ApiResponse<List<InventaireResponse>>> lister(@RequestParam(required = false) String statut) {
        List<InventaireResponse> inventaires = listerInventairesUseCase.executer(statut);
        return ResponseEntity.ok(ApiResponse.succes(inventaires, inventaires.size() + " inventaire(s)"));
    }

    @GetMapping("/{numero}")
    @PreAuthorize("isAuthenticated()")
    @Operation(summary = "Consulter un inventaire")
    public ResponseEntity<ApiResponse<InventaireResponse>> consulter(@PathVariable String numero) {
        return ResponseEntity.ok(ApiResponse.succes(consulterInventaireUseCase.executer(numero)));
    }
}
