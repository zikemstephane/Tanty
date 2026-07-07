package com.NTFOODS.Api_tanty.modules.stock.presentation.controller;

import com.NTFOODS.Api_tanty.modules.stock.application.reception.dto.request.CreerReceptionFournisseurRequest;
import com.NTFOODS.Api_tanty.modules.stock.application.reception.dto.request.CreerReceptionProductionRequest;
import com.NTFOODS.Api_tanty.modules.stock.application.reception.dto.response.ReceptionResponse;
import com.NTFOODS.Api_tanty.modules.stock.application.reception.usecase.consulter.ConsulterReceptionUseCase;
import com.NTFOODS.Api_tanty.modules.stock.application.reception.usecase.creer.CreerReceptionFournisseurUseCase;
import com.NTFOODS.Api_tanty.modules.stock.application.reception.usecase.creer.CreerReceptionProductionUseCase;
import com.NTFOODS.Api_tanty.modules.stock.application.reception.usecase.lister.ListerReceptionsUseCase;
import com.NTFOODS.Api_tanty.modules.stock.application.reception.usecase.rejeter.RejeterReceptionUseCase;
import com.NTFOODS.Api_tanty.modules.stock.application.reception.usecase.valider.ValiderReceptionEtape1UseCase;
import com.NTFOODS.Api_tanty.modules.stock.application.reception.usecase.valider.ValiderReceptionEtape2UseCase;
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
@RequestMapping("/api/stock/receptions")
@Tag(name = "Stock — Réceptions", description = "Réceptions fournisseur (double validation) et production")
@SecurityRequirement(name = "bearerAuth")
public class ReceptionController {

    private final CreerReceptionFournisseurUseCase creerFournisseurUseCase;
    private final CreerReceptionProductionUseCase creerProductionUseCase;
    private final ValiderReceptionEtape1UseCase validerEtape1UseCase;
    private final ValiderReceptionEtape2UseCase validerEtape2UseCase;
    private final RejeterReceptionUseCase rejeterReceptionUseCase;
    private final ListerReceptionsUseCase listerReceptionsUseCase;
    private final ConsulterReceptionUseCase consulterReceptionUseCase;
    private final AuthenticatedUserResolver userResolver;

    public ReceptionController(CreerReceptionFournisseurUseCase creerFournisseurUseCase,
                              CreerReceptionProductionUseCase creerProductionUseCase,
                              ValiderReceptionEtape1UseCase validerEtape1UseCase,
                              ValiderReceptionEtape2UseCase validerEtape2UseCase,
                              RejeterReceptionUseCase rejeterReceptionUseCase,
                              ListerReceptionsUseCase listerReceptionsUseCase,
                              ConsulterReceptionUseCase consulterReceptionUseCase,
                              AuthenticatedUserResolver userResolver) {
        this.creerFournisseurUseCase = creerFournisseurUseCase;
        this.creerProductionUseCase = creerProductionUseCase;
        this.validerEtape1UseCase = validerEtape1UseCase;
        this.validerEtape2UseCase = validerEtape2UseCase;
        this.rejeterReceptionUseCase = rejeterReceptionUseCase;
        this.listerReceptionsUseCase = listerReceptionsUseCase;
        this.consulterReceptionUseCase = consulterReceptionUseCase;
        this.userResolver = userResolver;
    }

    @PostMapping("/fournisseur")
    @PreAuthorize("hasAnyRole('GESTIONNAIRE_STOCK', 'ADMIN')")
    @Operation(summary = "Créer une réception fournisseur (liée à un BC)")
    public ResponseEntity<ApiResponse<ReceptionResponse>> creerFournisseur(
        @Valid @RequestBody CreerReceptionFournisseurRequest request) {
        ReceptionResponse response = creerFournisseurUseCase.executer(request, userResolver.getUtilisateurCourant());
        return ResponseEntity.status(HttpStatus.CREATED).body(ApiResponse.succes(response, "Réception créée : " + response.numero()));
    }

    @PostMapping("/production")
    @PreAuthorize("hasAnyRole('GESTIONNAIRE_STOCK', 'ADMIN')")
    @Operation(summary = "Créer une réception de produits finis (lot production)")
    public ResponseEntity<ApiResponse<ReceptionResponse>> creerProduction(
        @Valid @RequestBody CreerReceptionProductionRequest request) {
        ReceptionResponse response = creerProductionUseCase.executer(request, userResolver.getUtilisateurCourant());
        return ResponseEntity.status(HttpStatus.CREATED).body(ApiResponse.succes(response, "Réception créée : " + response.numero()));
    }

    @PostMapping("/{numero}/valider-etape1")
    @PreAuthorize("hasAnyRole('GESTIONNAIRE_STOCK', 'CHEF_PRODUCTION', 'ADMIN')")
    @Operation(summary = "1ʳᵉ validation (vérification physique)")
    public ResponseEntity<ApiResponse<ReceptionResponse>> validerEtape1(@PathVariable String numero) {
        ReceptionResponse response = validerEtape1UseCase.executer(numero, userResolver.getUtilisateurCourant());
        return ResponseEntity.ok(ApiResponse.succes(response, "1ʳᵉ validation effectuée"));
    }

    @PostMapping("/{numero}/valider-etape2")
    @PreAuthorize("hasAnyRole('CHEF_PRODUCTION', 'CONTROLEUR_GENERAL', 'GESTIONNAIRE_STOCK', 'ADMIN')")
    @Operation(summary = "2ᵉ validation (entrée en stock central)")
    public ResponseEntity<ApiResponse<ReceptionResponse>> validerEtape2(@PathVariable String numero) {
        ReceptionResponse response = validerEtape2UseCase.executer(numero, userResolver.getUtilisateurCourant());
        return ResponseEntity.ok(ApiResponse.succes(response, "Réception validée — stock central mis à jour"));
    }

    @PostMapping("/{numero}/rejeter")
    @PreAuthorize("hasAnyRole('CHEF_PRODUCTION', 'CONTROLEUR_GENERAL', 'GESTIONNAIRE_STOCK', 'ADMIN')")
    @Operation(summary = "Rejeter une réception")
    public ResponseEntity<ApiResponse<ReceptionResponse>> rejeter(@PathVariable String numero, @RequestParam String motif) {
        ReceptionResponse response = rejeterReceptionUseCase.executer(numero, motif, userResolver.getUtilisateurCourant());
        return ResponseEntity.ok(ApiResponse.succes(response, "Réception rejetée"));
    }

    @GetMapping
    @PreAuthorize("isAuthenticated()")
    @Operation(summary = "Lister les réceptions (filtres : statut, bon de commande)")
    public ResponseEntity<ApiResponse<List<ReceptionResponse>>> lister(
        @RequestParam(required = false) String statut,
        @RequestParam(required = false) String bonCommande) {
        List<ReceptionResponse> receptions = listerReceptionsUseCase.executer(statut, bonCommande);
        return ResponseEntity.ok(ApiResponse.succes(receptions, receptions.size() + " réception(s)"));
    }

    @GetMapping("/{numero}")
    @PreAuthorize("isAuthenticated()")
    @Operation(summary = "Consulter une réception")
    public ResponseEntity<ApiResponse<ReceptionResponse>> consulter(@PathVariable String numero) {
        return ResponseEntity.ok(ApiResponse.succes(consulterReceptionUseCase.executer(numero)));
    }
}
