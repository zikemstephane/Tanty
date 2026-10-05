package com.NTFOODS.Api_tanty.modules.stock.presentation.controller;

import com.NTFOODS.Api_tanty.modules.stock.application.reception.dto.request.CreerReceptionFournisseurRequest;
import com.NTFOODS.Api_tanty.modules.stock.application.reception.dto.request.CreerReceptionProductionRequest;
import com.NTFOODS.Api_tanty.modules.stock.application.reception.dto.request.ValiderReceptionRequest;
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
    @PreAuthorize("hasAuthority('STOCK_RECEPTION_GERER')")
    @Operation(summary = "Créer une réception fournisseur (liée à un BC)")
    public ResponseEntity<ApiResponse<ReceptionResponse>> creerFournisseur(
        @Valid @RequestBody CreerReceptionFournisseurRequest request) {
        ReceptionResponse response = creerFournisseurUseCase.executer(request, userResolver.getUtilisateurCourant());
        return ResponseEntity.status(HttpStatus.CREATED).body(ApiResponse.succes(response, "Réception créée : " + response.numero()));
    }

    @PostMapping("/production")
    @PreAuthorize("hasAuthority('STOCK_RECEPTION_GERER')")
    @Operation(summary = "Créer une réception de produits finis (lot production)")
    public ResponseEntity<ApiResponse<ReceptionResponse>> creerProduction(
        @Valid @RequestBody CreerReceptionProductionRequest request) {
        ReceptionResponse response = creerProductionUseCase.executer(request, userResolver.getUtilisateurCourant());
        return ResponseEntity.status(HttpStatus.CREATED).body(ApiResponse.succes(response, "Réception créée : " + response.numero()));
    }

    @PostMapping("/{numero}/valider-etape1")
    @PreAuthorize("hasAuthority('STOCK_RECEPTION_GERER')")
    @Operation(summary = "1ʳᵉ validation (vérification physique)",
        description = "Le corps est facultatif et ne porte que les observations du validateur, " +
                      "conservées avec la réception.")
    public ResponseEntity<ApiResponse<ReceptionResponse>> validerEtape1(
        @PathVariable String numero,
        @Valid @RequestBody(required = false) ValiderReceptionRequest request) {
        String notes = (request != null ? request : ValiderReceptionRequest.vide()).notesOuNull();
        ReceptionResponse response = validerEtape1UseCase.executer(numero, userResolver.getUtilisateurCourant(), notes);
        return ResponseEntity.ok(ApiResponse.succes(response, "1ʳᵉ validation effectuée"));
    }

    @PostMapping("/{numero}/valider-etape2")
    @PreAuthorize("hasAuthority('STOCK_RECEPTION_VALIDER_ETAPE2')")
    @Operation(summary = "2ᵉ validation (entrée en stock central)",
        description = "Réservée au Comptable pour les matières premières, au Contrôleur Général " +
                      "pour les consommables, au Gestionnaire de Stock pour les produits finis. " +
                      "Le garde ouvre la porte aux trois ; Reception.verifierRoleEtape2 trie ensuite " +
                      "selon la catégorie de la réception. Le corps est facultatif et ne porte " +
                      "que les observations du validateur, conservées avec la réception.")
    public ResponseEntity<ApiResponse<ReceptionResponse>> validerEtape2(
        @PathVariable String numero,
        @Valid @RequestBody(required = false) ValiderReceptionRequest request) {
        String notes = (request != null ? request : ValiderReceptionRequest.vide()).notesOuNull();
        ReceptionResponse response = validerEtape2UseCase.executer(numero, userResolver.getUtilisateurCourant(), notes);
        return ResponseEntity.ok(ApiResponse.succes(response, "Réception validée — stock central mis à jour"));
    }

    @PostMapping("/{numero}/rejeter")
    @PreAuthorize("hasAuthority('STOCK_RECEPTION_VALIDER_ETAPE2')")
    @Operation(summary = "Rejeter une réception",
        description = "Rejeter est l'autre issue du contrôle de second niveau : " +
                      "Reception.rejeter applique la même vérification de rôle que la 2ᵉ validation. " +
                      "Le garde est donc le même — les séparer aurait permis de refuser " +
                      "ce que l'on n'avait pas le droit d'accepter.")
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
