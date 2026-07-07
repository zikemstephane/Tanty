package com.NTFOODS.Api_tanty.modules.comptabilite.presentation.controller;

import com.NTFOODS.Api_tanty.modules.comptabilite.application.facture.dto.response.FactureResponse;
import com.NTFOODS.Api_tanty.modules.comptabilite.application.facture.mapper.FactureMapper;
import com.NTFOODS.Api_tanty.modules.comptabilite.domain.facture.aggregate.Facture;
import com.NTFOODS.Api_tanty.modules.comptabilite.domain.facture.entity.LigneFacture;
import com.NTFOODS.Api_tanty.modules.comptabilite.domain.facture.enums.StatutFacture;
import com.NTFOODS.Api_tanty.modules.comptabilite.domain.facture.enums.TypeFacture;
import com.NTFOODS.Api_tanty.modules.comptabilite.domain.facture.repository.FactureRepository;
import com.NTFOODS.Api_tanty.shared.application.dto.ApiResponse;
import com.NTFOODS.Api_tanty.shared.application.dto.PageResponse;
import com.NTFOODS.Api_tanty.shared.kernel.event.EventPublisher;
import com.NTFOODS.Api_tanty.shared.kernel.exception.EntiteNonTrouveeException;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/api/comptabilite/factures")
@Tag(name = "Comptabilité — Factures", description = "Émission de factures et avoirs")
public class FactureController {

    private final FactureRepository factureRepository;
    private final FactureMapper factureMapper;
    private final EventPublisher eventPublisher;

    public FactureController(FactureRepository factureRepository, FactureMapper factureMapper,
                              EventPublisher eventPublisher) {
        this.factureRepository = factureRepository;
        this.factureMapper = factureMapper;
        this.eventPublisher = eventPublisher;
    }

    @PostMapping
    @PreAuthorize("hasAnyRole('SECRETAIRE', 'COMPTABLE', 'ADMIN')")
    public ResponseEntity<ApiResponse<FactureResponse>> emettre(@RequestBody EmettreFactureRequest req) {
        List<LigneFacture> lignes = req.lignes().stream()
            .map(l -> new LigneFacture(l.referenceArticle(), l.designation(), l.quantite(),
                l.prixUnitaireHT(), l.tauxTVA(), l.remise()))
            .toList();
        String numero = factureRepository.genererNumero(req.typeFacture(), LocalDate.now());
        Facture facture = Facture.emettre(numero, req.codeClient(), req.typeFacture(),
            req.referenceFactureOrigine(), LocalDate.now(), lignes, req.matriculeEmetteur());
        Facture sauvegarde = factureRepository.sauvegarder(facture);
        facture.getDomainEvents().forEach(eventPublisher::publish);
        facture.clearDomainEvents();
        return ResponseEntity.status(HttpStatus.CREATED)
            .body(ApiResponse.succes(factureMapper.versResponse(sauvegarde), "Facture émise : " + numero));
    }

    @PatchMapping("/{numero}/payer")
    @PreAuthorize("hasAnyRole('COMPTABLE', 'ADMIN')")
    public ResponseEntity<ApiResponse<FactureResponse>> marquerPayee(@PathVariable String numero) {
        Facture facture = factureRepository.trouverParNumero(numero)
            .orElseThrow(() -> EntiteNonTrouveeException.de("Facture", numero));
        facture.marquerPayee();
        return ResponseEntity.ok(ApiResponse.succes(factureMapper.versResponse(factureRepository.sauvegarder(facture)), "Facture payée"));
    }

    @PatchMapping("/{numero}/annuler")
    @PreAuthorize("hasAnyRole('COMPTABLE', 'DIRECTEUR_GENERAL', 'ADMIN')")
    public ResponseEntity<ApiResponse<FactureResponse>> annuler(@PathVariable String numero) {
        Facture facture = factureRepository.trouverParNumero(numero)
            .orElseThrow(() -> EntiteNonTrouveeException.de("Facture", numero));
        facture.annuler();
        return ResponseEntity.ok(ApiResponse.succes(factureMapper.versResponse(factureRepository.sauvegarder(facture)), "Facture annulée"));
    }

    @GetMapping("/{numero}")
    @PreAuthorize("hasAnyRole('SECRETAIRE', 'COMPTABLE', 'CONTROLEUR_GENERAL', 'DIRECTEUR_GENERAL', 'ADMIN')")
    public ResponseEntity<ApiResponse<FactureResponse>> consulter(@PathVariable String numero) {
        return ResponseEntity.ok(ApiResponse.succes(
            factureMapper.versResponse(factureRepository.trouverParNumero(numero)
                .orElseThrow(() -> EntiteNonTrouveeException.de("Facture", numero))), "Facture"));
    }

    @GetMapping("/statut/{statut}")
    @PreAuthorize("hasAnyRole('COMPTABLE', 'CONTROLEUR_GENERAL', 'DIRECTEUR_GENERAL', 'ADMIN')")
    @Operation(summary = "Factures par statut (paginé)",
        description = "Paramètres optionnels: page (déf. 0), size (déf. 20), sort (ex: dateEmission,desc).")
    public ResponseEntity<ApiResponse<PageResponse<FactureResponse>>> parStatut(
        @PathVariable StatutFacture statut,
        @PageableDefault(size = 20, sort = "dateEmission", direction = Sort.Direction.DESC) Pageable pageable) {
        Page<FactureResponse> page = factureRepository.listerParStatutPagine(statut, pageable).map(factureMapper::versResponse);
        return ResponseEntity.ok(ApiResponse.succes(PageResponse.de(page), page.getTotalElements() + " facture(s)"));
    }

    @GetMapping("/client/{codeClient}")
    @PreAuthorize("hasAnyRole('SECRETAIRE', 'COMPTABLE', 'CONTROLEUR_GENERAL', 'DIRECTEUR_GENERAL', 'ADMIN')")
    @Operation(summary = "Factures d'un client (paginé)",
        description = "Paramètres optionnels: page (déf. 0), size (déf. 20), sort (ex: dateEmission,desc).")
    public ResponseEntity<ApiResponse<PageResponse<FactureResponse>>> parClient(
        @PathVariable String codeClient,
        @PageableDefault(size = 20, sort = "dateEmission", direction = Sort.Direction.DESC) Pageable pageable) {
        Page<FactureResponse> page = factureRepository.listerParClientPagine(codeClient, pageable).map(factureMapper::versResponse);
        return ResponseEntity.ok(ApiResponse.succes(PageResponse.de(page), page.getTotalElements() + " facture(s)"));
    }

    @GetMapping
    @PreAuthorize("hasAnyRole('COMPTABLE', 'CONTROLEUR_GENERAL', 'DIRECTEUR_GENERAL', 'ADMIN')")
    @Operation(summary = "Factures par période (paginé)",
        description = "Paramètres optionnels: page (déf. 0), size (déf. 20), sort (ex: dateEmission,desc).")
    public ResponseEntity<ApiResponse<PageResponse<FactureResponse>>> parPeriode(
        @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate debut,
        @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate fin,
        @PageableDefault(size = 20, sort = "dateEmission", direction = Sort.Direction.DESC) Pageable pageable) {
        Page<FactureResponse> page = factureRepository.listerParPeriodePagine(debut, fin, pageable).map(factureMapper::versResponse);
        return ResponseEntity.ok(ApiResponse.succes(PageResponse.de(page), page.getTotalElements() + " facture(s)"));
    }

    public record EmettreFactureRequest(String codeClient, TypeFacture typeFacture, String referenceFactureOrigine,
                                        String matriculeEmetteur, List<LigneRequest> lignes) {}
    public record LigneRequest(String referenceArticle, String designation, int quantite,
                               double prixUnitaireHT, double tauxTVA, double remise) {}
}
