package com.NTFOODS.Api_tanty.modules.production.presentation.controller;

import com.NTFOODS.Api_tanty.modules.production.application.lot.dto.request.DeclarerLotRequest;
import com.NTFOODS.Api_tanty.modules.production.application.lot.usecase.declarer.DeclarerLotUseCase;
import com.NTFOODS.Api_tanty.modules.production.application.lot.dto.response.LotResponse;
import com.NTFOODS.Api_tanty.modules.production.application.lot.mapper.LotMapper;
import com.NTFOODS.Api_tanty.modules.production.domain.lot.aggregate.LotProduitFini;
import com.NTFOODS.Api_tanty.modules.production.domain.lot.enums.StatutLot;
import com.NTFOODS.Api_tanty.modules.production.domain.lot.repository.LotRepository;
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
import java.util.Map;

@RestController
@RequestMapping("/api/production/lots")
@Tag(name = "Production — Lots", description = "Déclaration et validation des lots de produits finis")
@SecurityRequirement(name = "bearerAuth")
public class LotController {

    private final DeclarerLotUseCase declarerLotUseCase;
    private final LotRepository lotRepository;
    private final LotMapper lotMapper;
    private final AuthenticatedUserResolver userResolver;
    private final com.NTFOODS.Api_tanty.shared.kernel.event.EventPublisher eventPublisher;

    public LotController(DeclarerLotUseCase declarerLotUseCase, LotRepository lotRepository,
                       LotMapper lotMapper, AuthenticatedUserResolver userResolver,
                       com.NTFOODS.Api_tanty.shared.kernel.event.EventPublisher eventPublisher) {
        this.declarerLotUseCase = declarerLotUseCase;
        this.lotRepository = lotRepository;
        this.lotMapper = lotMapper;
        this.userResolver = userResolver;
        this.eventPublisher = eventPublisher;
    }

    @PostMapping
    @PreAuthorize("hasAuthority('PRODUCTION_LOT_GERER')")
    @Operation(summary = "Déclarer un lot produit fini")
    public ResponseEntity<ApiResponse<LotResponse>> declarer(@RequestBody @Valid DeclarerLotRequest req) {
        LotResponse response = declarerLotUseCase.executer(req, userResolver.getUtilisateurCourant());
        return ResponseEntity.status(HttpStatus.CREATED)
            .body(ApiResponse.succes(response, "Lot déclaré — en attente de validation stock"));
    }

    @GetMapping
    // La lecture s'ouvre aussi à qui valide les lots en stock : le gestionnaire
    // détient PRODUCTION_LOT_VALIDER_STOCK et doit donc pouvoir voir ce qu'il
    // valide. Sans cela il pouvait valider un lot qu'il lui était interdit
    // de lister — et son tableau de bord partait en « accès refusé ».
    @PreAuthorize("hasAnyAuthority('PRODUCTION_LOT_GERER', 'PRODUCTION_LOT_VALIDER_STOCK')")
    @Operation(summary = "Lister les lots (filtre optionnel par statut)")
    public ResponseEntity<ApiResponse<List<LotResponse>>> lister(@RequestParam(required = false) StatutLot statut) {
        List<LotProduitFini> lots;
        if (statut != null) {
            lots = lotRepository.listerParStatut(statut);
        } else {
            lots = new java.util.ArrayList<>();
            for (StatutLot s : StatutLot.values()) {
                lots.addAll(lotRepository.listerParStatut(s));
            }
        }
        return ResponseEntity.ok(ApiResponse.succes(lots.stream().map(lotMapper::versResponse).toList(),
            lots.size() + " lot(s)"));
    }

    @GetMapping("/a-valider")
    // La lecture s'ouvre aussi à qui valide les lots en stock : le gestionnaire
    // détient PRODUCTION_LOT_VALIDER_STOCK et doit donc pouvoir voir ce qu'il
    // valide. Sans cela il pouvait valider un lot qu'il lui était interdit
    // de lister — et son tableau de bord partait en « accès refusé ».
    @PreAuthorize("hasAnyAuthority('PRODUCTION_LOT_GERER', 'PRODUCTION_LOT_VALIDER_STOCK')")
    @Operation(summary = "Lots déclarés en attente de validation stock")
    public ResponseEntity<ApiResponse<List<LotResponse>>> aValider() {
        var lots = lotRepository.listerParStatut(StatutLot.DECLARE).stream().map(lotMapper::versResponse).toList();
        return ResponseEntity.ok(ApiResponse.succes(lots, lots.size() + " lot(s) à valider"));
    }

    @PatchMapping("/{numeroLot}/valider")
    @PreAuthorize("hasAuthority('PRODUCTION_LOT_VALIDER_STOCK')")
    @Operation(summary = "Valider un lot (entrée en stock central)",
        description = "Déclenche également le verrouillage numérique de la fiche de production correspondante " +
                      "(Pilier 1 — Immuabilité) : l'responsable de salle ne peut plus en modifier les chiffres.")
    public ResponseEntity<ApiResponse<LotResponse>> valider(@PathVariable String numeroLot) {
        LotProduitFini lot = lotRepository.trouverParNumero(numeroLot)
            .orElseThrow(() -> EntiteNonTrouveeException.de("Lot", numeroLot));
        lot.validerParStock(userResolver.getMatriculeCourant());
        LotProduitFini sauvegarde = lotRepository.sauvegarder(lot);
        lot.getDomainEvents().forEach(eventPublisher::publish);
        lot.clearDomainEvents();
        return ResponseEntity.ok(ApiResponse.succes(lotMapper.versResponse(sauvegarde),
            "Lot validé — fiche de production verrouillée"));
    }

    @PatchMapping("/{numeroLot}/rejeter")
    @PreAuthorize("hasAuthority('PRODUCTION_LOT_VALIDER_STOCK')")
    @Operation(summary = "Rejeter un lot")
    public ResponseEntity<ApiResponse<LotResponse>> rejeter(@PathVariable String numeroLot, @RequestParam String motif) {
        LotProduitFini lot = lotRepository.trouverParNumero(numeroLot)
            .orElseThrow(() -> EntiteNonTrouveeException.de("Lot", numeroLot));
        lot.rejeter(motif, userResolver.getMatriculeCourant());
        return ResponseEntity.ok(ApiResponse.succes(lotMapper.versResponse(lotRepository.sauvegarder(lot)),
            "Lot rejeté"));
    }
}
