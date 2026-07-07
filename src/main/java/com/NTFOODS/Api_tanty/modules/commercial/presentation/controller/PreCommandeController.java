package com.NTFOODS.Api_tanty.modules.commercial.presentation.controller;

import com.NTFOODS.Api_tanty.modules.commercial.application.precommande.dto.response.PreCommandeResponse;
import com.NTFOODS.Api_tanty.modules.commercial.application.precommande.mapper.PreCommandeMapper;
import com.NTFOODS.Api_tanty.modules.commercial.domain.precommande.aggregate.PreCommande;
import com.NTFOODS.Api_tanty.modules.commercial.domain.precommande.entity.LignePreCommande;
import com.NTFOODS.Api_tanty.modules.commercial.domain.precommande.enums.StatutPreCommande;
import com.NTFOODS.Api_tanty.modules.commercial.domain.precommande.repository.PreCommandeRepository;
import com.NTFOODS.Api_tanty.shared.application.dto.ApiResponse;
import com.NTFOODS.Api_tanty.shared.application.dto.PageResponse;
import com.NTFOODS.Api_tanty.shared.kernel.event.EventPublisher;
import com.NTFOODS.Api_tanty.shared.kernel.exception.EntiteNonTrouveeException;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
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
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.concurrent.atomic.AtomicLong;

@RestController
@RequestMapping("/api/commercial/precommandes")
@Tag(name = "Commercial — Précommandes J+1", description = "Précommandes journalières des commerciaux")
@SecurityRequirement(name = "bearerAuth")
public class PreCommandeController {

    private static final AtomicLong COUNTER = new AtomicLong(0);

    private final PreCommandeRepository preCommandeRepository;
    private final PreCommandeMapper preCommandeMapper;
    private final EventPublisher eventPublisher;

    public PreCommandeController(PreCommandeRepository preCommandeRepository, PreCommandeMapper preCommandeMapper,
                                EventPublisher eventPublisher) {
        this.preCommandeRepository = preCommandeRepository;
        this.preCommandeMapper = preCommandeMapper;
        this.eventPublisher = eventPublisher;
    }

    @PostMapping
    @PreAuthorize("hasAnyRole('COMMERCIAL', 'ADMIN')")
    public ResponseEntity<ApiResponse<PreCommandeResponse>> soumettre(@RequestBody SoumettrePreCommandeRequest req) {
        String num = "PCM-" + DateTimeFormatter.ofPattern("yyyyMMdd").format(req.dateSoumission())
            + "-" + String.format("%03d", COUNTER.incrementAndGet());
        List<LignePreCommande> lignes = req.lignes().stream()
            .map(l -> new LignePreCommande(l.codeProduit(), l.designation(), l.conditionnement(), l.quantite()))
            .toList();
        PreCommande pc = PreCommande.soumettre(num, req.matriculeCommercial(), req.dateSoumission(), lignes);
        return ResponseEntity.status(HttpStatus.CREATED)
            .body(ApiResponse.succes(preCommandeMapper.versResponse(preCommandeRepository.sauvegarder(pc)), "Précommande soumise : " + num));
    }

    @GetMapping("/a-valider")
    @PreAuthorize("hasAnyRole('SECRETAIRE', 'COMPTABLE', 'DIRECTEUR_GENERAL', 'ADMIN')")
    @Operation(summary = "Précommandes à valider (paginé)",
        description = "Paramètres optionnels: page (déf. 0), size (déf. 20), sort (ex: dateSoumission,desc).")
    public ResponseEntity<ApiResponse<PageResponse<PreCommandeResponse>>> aValider(
        @PageableDefault(size = 20, sort = "dateSoumission", direction = Sort.Direction.DESC) Pageable pageable) {
        Page<PreCommandeResponse> page = preCommandeRepository.listerParStatutPagine(StatutPreCommande.SOUMISE, pageable)
            .map(preCommandeMapper::versResponse);
        return ResponseEntity.ok(ApiResponse.succes(PageResponse.de(page), page.getTotalElements() + " précommande(s)"));
    }

    @GetMapping("/date-effet/{date}")
    @PreAuthorize("hasAnyRole('COMMERCIAL', 'SECRETAIRE', 'COMPTABLE', 'GESTIONNAIRE_STOCK', 'DIRECTEUR_GENERAL', 'ADMIN')")
    @Operation(summary = "Précommandes par date d'effet (paginé)",
        description = "Paramètres optionnels: page (déf. 0), size (déf. 20), sort (ex: dateSoumission,desc).")
    public ResponseEntity<ApiResponse<PageResponse<PreCommandeResponse>>> parDateEffet(
        @PathVariable @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date,
        @PageableDefault(size = 20, sort = "dateSoumission", direction = Sort.Direction.DESC) Pageable pageable) {
        Page<PreCommandeResponse> page = preCommandeRepository.listerParDateEffetPagine(date, pageable)
            .map(preCommandeMapper::versResponse);
        return ResponseEntity.ok(ApiResponse.succes(PageResponse.de(page), page.getTotalElements() + " précommande(s)"));
    }

    @PatchMapping("/{numero}/valider-secretaire")
    @PreAuthorize("hasAnyRole('SECRETAIRE', 'ADMIN')")
    public ResponseEntity<ApiResponse<PreCommandeResponse>> validerSecretaire(@PathVariable String numero,
                                                                              @RequestParam String matriculeSecretaire) {
        PreCommande pc = preCommandeRepository.trouverParNumero(numero)
            .orElseThrow(() -> EntiteNonTrouveeException.de("Précommande", numero));
        pc.validerParSecretaire(matriculeSecretaire);
        PreCommande sauvegarde = preCommandeRepository.sauvegarder(pc);
        pc.getDomainEvents().forEach(eventPublisher::publish);
        pc.clearDomainEvents();
        return ResponseEntity.ok(ApiResponse.succes(preCommandeMapper.versResponse(sauvegarde), "Validée (secrétaire)"));
    }

    @PatchMapping("/{numero}/valider-comptable")
    @PreAuthorize("hasAnyRole('COMPTABLE', 'ADMIN')")
    public ResponseEntity<ApiResponse<PreCommandeResponse>> validerComptable(@PathVariable String numero,
                                                                             @RequestParam String matriculeComptable) {
        PreCommande pc = preCommandeRepository.trouverParNumero(numero)
            .orElseThrow(() -> EntiteNonTrouveeException.de("Précommande", numero));
        pc.validerParComptable(matriculeComptable);
        PreCommande sauvegarde = preCommandeRepository.sauvegarder(pc);
        pc.getDomainEvents().forEach(eventPublisher::publish);
        pc.clearDomainEvents();
        return ResponseEntity.ok(ApiResponse.succes(preCommandeMapper.versResponse(sauvegarde), "Validée (comptable)"));
    }

    @PatchMapping("/{numero}/refuser")
    @PreAuthorize("hasAnyRole('SECRETAIRE', 'COMPTABLE', 'ADMIN')")
    @Operation(summary = "Refuser une précommande")
    public ResponseEntity<ApiResponse<PreCommandeResponse>> refuser(@PathVariable String numero,
                                                                      @RequestParam String motif,
                                                                      @RequestParam String matriculeValidateur) {
        PreCommande pc = preCommandeRepository.trouverParNumero(numero)
            .orElseThrow(() -> EntiteNonTrouveeException.de("Précommande", numero));
        pc.refuser(motif, matriculeValidateur);
        return ResponseEntity.ok(ApiResponse.succes(preCommandeMapper.versResponse(preCommandeRepository.sauvegarder(pc)), "Précommande refusée"));
    }

    @PatchMapping("/{numero}/livrer")
    @PreAuthorize("hasAnyRole('SECRETAIRE', 'COMPTABLE', 'GESTIONNAIRE_STOCK', 'ADMIN')")
    @Operation(summary = "Marquer une précommande comme livrée")
    public ResponseEntity<ApiResponse<PreCommandeResponse>> livrer(@PathVariable String numero) {
        PreCommande pc = preCommandeRepository.trouverParNumero(numero)
            .orElseThrow(() -> EntiteNonTrouveeException.de("Précommande", numero));
        pc.marquerLivree();
        return ResponseEntity.ok(ApiResponse.succes(preCommandeMapper.versResponse(preCommandeRepository.sauvegarder(pc)), "Précommande livrée"));
    }

    public record SoumettrePreCommandeRequest(String matriculeCommercial, LocalDate dateSoumission,
                                              List<LignePreCommandeDto> lignes) {}
    public record LignePreCommandeDto(String codeProduit, String designation, String conditionnement, int quantite) {}
}
