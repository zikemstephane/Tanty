package com.NTFOODS.Api_tanty.modules.commercial.presentation.controller;

import com.NTFOODS.Api_tanty.modules.commercial.application.precommande.dto.response.PreCommandeResponse;
import com.NTFOODS.Api_tanty.modules.commercial.application.precommande.mapper.PreCommandeMapper;
import com.NTFOODS.Api_tanty.modules.commercial.application.precommande.service.EligibilitePrecommandeService;
import com.NTFOODS.Api_tanty.modules.commercial.domain.precommande.aggregate.PreCommande;
import com.NTFOODS.Api_tanty.modules.commercial.domain.precommande.entity.LignePreCommande;
import com.NTFOODS.Api_tanty.modules.commercial.domain.precommande.enums.StatutPreCommande;
import com.NTFOODS.Api_tanty.modules.commercial.domain.precommande.repository.PreCommandeRepository;
import com.NTFOODS.Api_tanty.shared.application.dto.ApiResponse;
import com.NTFOODS.Api_tanty.shared.application.dto.PageResponse;
import com.NTFOODS.Api_tanty.shared.infrastructure.numerotation.FamilleNumero;
import com.NTFOODS.Api_tanty.shared.infrastructure.numerotation.GenerateurNumeroMetier;
import com.NTFOODS.Api_tanty.shared.kernel.event.EventPublisher;
import com.NTFOODS.Api_tanty.shared.kernel.exception.EntiteNonTrouveeException;
import com.NTFOODS.Api_tanty.shared.kernel.exception.RegleMetierException;
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
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/commercial/precommandes")
@Tag(name = "Commercial — Précommandes J+1", description = "Précommandes journalières des commerciaux")
@SecurityRequirement(name = "bearerAuth")
public class PreCommandeController {

    private final PreCommandeRepository preCommandeRepository;
    private final PreCommandeMapper preCommandeMapper;
    private final EventPublisher eventPublisher;
    private final GenerateurNumeroMetier generateurNumero;
    private final EligibilitePrecommandeService eligibiliteService;

    public PreCommandeController(PreCommandeRepository preCommandeRepository, PreCommandeMapper preCommandeMapper,
                                EventPublisher eventPublisher, GenerateurNumeroMetier generateurNumero,
                                EligibilitePrecommandeService eligibiliteService) {
        this.preCommandeRepository = preCommandeRepository;
        this.preCommandeMapper = preCommandeMapper;
        this.eventPublisher = eventPublisher;
        this.generateurNumero = generateurNumero;
        this.eligibiliteService = eligibiliteService;
    }

    @GetMapping("/eligibilite")
    @PreAuthorize("hasAuthority('COMMERCIAL_PRECOMMANDE_GERER')")
    @Operation(summary = "RG-01 — le commercial peut-il préparer la journée suivante ?",
        description = "Répond sur l'état réel du versement et de l'inventaire de retour de la dernière " +
                      "journée travaillée. Un commercial qui n'a jamais fait de tournée est autorisé : " +
                      "il n'a aucune journée à apurer, et l'en empêcher l'enfermerait dehors — pas de " +
                      "commande, donc pas de vente, donc jamais de versement.")
    public ResponseEntity<ApiResponse<EligibilitePrecommandeService.Eligibilite>> eligibilite(
        @RequestParam String matricule) {
        var eligibilite = eligibiliteService.evaluer(matricule);
        return ResponseEntity.ok(ApiResponse.succes(eligibilite, eligibilite.motif()));
    }

    @PostMapping
    @PreAuthorize("hasAuthority('COMMERCIAL_PRECOMMANDE_GERER')")
    public ResponseEntity<ApiResponse<PreCommandeResponse>> soumettre(@RequestBody SoumettrePreCommandeRequest req) {
        // RG-01 vérifiée ici, et pas seulement affichée par le terminal : une règle
        // que seul le client applique n'est pas une règle.
        var eligibilite = eligibiliteService.evaluer(req.matriculeCommercial());
        if (!eligibilite.autorisee()) {
            throw new RegleMetierException(eligibilite.motif());
        }

        String num = generateurNumero.genererNumero(FamilleNumero.PRECOMMANDE, req.dateSoumission());
        List<LignePreCommande> lignes = req.lignes().stream()
            .map(l -> new LignePreCommande(l.codeProduit(), l.designation(), l.conditionnement(), l.quantite()))
            .toList();
        PreCommande pc = PreCommande.soumettre(num, req.matriculeCommercial(), req.dateSoumission(), lignes);
        return ResponseEntity.status(HttpStatus.CREATED)
            .body(ApiResponse.succes(preCommandeMapper.versResponse(preCommandeRepository.sauvegarder(pc)), "Précommande soumise : " + num));
    }

    @GetMapping("/a-valider")
    @PreAuthorize("hasAuthority('COMMERCIAL_PRECOMMANDE_VALIDER_SECRETAIRE')")
    @Operation(summary = "Précommandes à valider par la secrétaire (paginé)",
        description = "Paramètres optionnels: page (déf. 0), size (déf. 20), sort (ex: dateSoumission,desc).")
    public ResponseEntity<ApiResponse<PageResponse<PreCommandeResponse>>> aValider(
        @PageableDefault(size = 20, sort = "dateSoumission", direction = Sort.Direction.DESC) Pageable pageable) {
        Page<PreCommandeResponse> page = preCommandeRepository.listerParStatutPagine(StatutPreCommande.SOUMISE, pageable)
            .map(preCommandeMapper::versResponse);
        return ResponseEntity.ok(ApiResponse.succes(PageResponse.de(page), page.getTotalElements() + " précommande(s)"));
    }

    @GetMapping("/a-valider-comptable")
    @PreAuthorize("hasAuthority('COMMERCIAL_PRECOMMANDE_VALIDER_COMPTABLE')")
    @Operation(summary = "Précommandes en attente de validation comptable (paginé)",
        description = "Précommandes déjà validées par la secrétaire, en attente du comptable. Paramètres optionnels: page, size, sort.")
    public ResponseEntity<ApiResponse<PageResponse<PreCommandeResponse>>> aValiderComptable(
        @PageableDefault(size = 20, sort = "dateSoumission", direction = Sort.Direction.DESC) Pageable pageable) {
        Page<PreCommandeResponse> page = preCommandeRepository.listerParStatutPagine(StatutPreCommande.VALIDEE_SECRETAIRE, pageable)
            .map(preCommandeMapper::versResponse);
        return ResponseEntity.ok(ApiResponse.succes(PageResponse.de(page), page.getTotalElements() + " précommande(s) en attente comptable"));
    }

    @GetMapping("/a-livrer")
    @PreAuthorize("isAuthenticated()")
    @Operation(summary = "Précommandes prêtes à être livrées (paginé)",
        description = "Précommandes validées par le comptable, en attente de livraison/sortie stock. Paramètres optionnels: page, size, sort.")
    public ResponseEntity<ApiResponse<PageResponse<PreCommandeResponse>>> aLivrer(
        @PageableDefault(size = 20, sort = "dateSoumission", direction = Sort.Direction.DESC) Pageable pageable) {
        Page<PreCommandeResponse> page = preCommandeRepository.listerParStatutPagine(StatutPreCommande.VALIDEE_COMPTABLE, pageable)
            .map(preCommandeMapper::versResponse);
        return ResponseEntity.ok(ApiResponse.succes(PageResponse.de(page), page.getTotalElements() + " précommande(s) à livrer"));
    }

    @GetMapping("/date-effet/{date}")
    @PreAuthorize("isAuthenticated()")
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
    @PreAuthorize("hasAuthority('COMMERCIAL_PRECOMMANDE_VALIDER_SECRETAIRE')")
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
    @PreAuthorize("hasAuthority('COMMERCIAL_PRECOMMANDE_VALIDER_COMPTABLE')")
    public ResponseEntity<ApiResponse<PreCommandeResponse>> validerComptable(
            @PathVariable String numero,
            @RequestParam String matriculeComptable,
            @RequestBody(required = false) Map<String, Integer> quantitesValideesParProduit) {
        PreCommande pc = preCommandeRepository.trouverParNumero(numero)
            .orElseThrow(() -> EntiteNonTrouveeException.de("Précommande", numero));
        pc.validerParComptable(matriculeComptable, quantitesValideesParProduit);
        PreCommande sauvegarde = preCommandeRepository.sauvegarder(pc);
        pc.getDomainEvents().forEach(eventPublisher::publish);
        pc.clearDomainEvents();
        return ResponseEntity.ok(ApiResponse.succes(preCommandeMapper.versResponse(sauvegarde), "Validée (comptable)"));
    }

    @PatchMapping("/{numero}/refuser")
    @PreAuthorize("hasAnyAuthority('COMMERCIAL_PRECOMMANDE_VALIDER_SECRETAIRE','COMMERCIAL_PRECOMMANDE_VALIDER_COMPTABLE')")
    @Operation(summary = "Refuser une précommande")
    public ResponseEntity<ApiResponse<PreCommandeResponse>> refuser(@PathVariable String numero,
                                                                      @RequestParam String motif,
                                                                      @RequestParam String matriculeValidateur) {
        PreCommande pc = preCommandeRepository.trouverParNumero(numero)
            .orElseThrow(() -> EntiteNonTrouveeException.de("Précommande", numero));
        pc.refuser(motif, matriculeValidateur);
        return ResponseEntity.ok(ApiResponse.succes(preCommandeMapper.versResponse(preCommandeRepository.sauvegarder(pc)), "Précommande refusée"));
    }

    @PatchMapping("/{numero}/arbitrer")
    @PreAuthorize("hasAuthority('COMMERCIAL_PRECOMMANDE_ARBITRER')")
    @Operation(summary = "Arbitrer les quantités d'une précommande",
        description = "Le gestionnaire de stock ajuste les quantités servies au vu de la disponibilité " +
                      "réelle, avant livraison. La quantité demandée par le commercial n'est jamais " +
                      "modifiée : seule la quantité validée l'est, et l'écart reste consultable. " +
                      "L'auteur et l'horodatage de l'arbitrage sont conservés.")
    public ResponseEntity<ApiResponse<PreCommandeResponse>> arbitrer(@PathVariable String numero,
                                                                      @RequestBody ArbitrerPreCommandeRequest req) {
        PreCommande pc = preCommandeRepository.trouverParNumero(numero)
            .orElseThrow(() -> EntiteNonTrouveeException.de("Précommande", numero));

        Map<String, Integer> quantites = req.lignes() == null ? Map.of() : req.lignes().stream()
            .collect(Collectors.toMap(LigneArbitrageDto::codeProduit, LigneArbitrageDto::quantiteValidee));

        pc.arbitrerQuantites(quantites, req.matriculeArbitre(), req.motif());
        return ResponseEntity.ok(ApiResponse.succes(
            preCommandeMapper.versResponse(preCommandeRepository.sauvegarder(pc)),
            "Quantités arbitrées pour " + numero));
    }

    @PatchMapping("/{numero}/livrer")
    @PreAuthorize("hasAuthority('STOCK_DOTATION_GERER')")
    @Operation(summary = "Marquer une précommande comme livrée",
        description = "Réservé au gestionnaire de stock : la livraison déclenche la sortie de stock " +
                      "et ne peut donc pas être ouverte à tout utilisateur authentifié.")
    public ResponseEntity<ApiResponse<PreCommandeResponse>> livrer(@PathVariable String numero) {
        PreCommande pc = preCommandeRepository.trouverParNumero(numero)
            .orElseThrow(() -> EntiteNonTrouveeException.de("Précommande", numero));
        pc.marquerLivree();
        return ResponseEntity.ok(ApiResponse.succes(preCommandeMapper.versResponse(preCommandeRepository.sauvegarder(pc)), "Précommande livrée"));
    }

    public record SoumettrePreCommandeRequest(String matriculeCommercial, LocalDate dateSoumission,
                                              List<LignePreCommandeDto> lignes) {}
    public record LignePreCommandeDto(String codeProduit, String designation, String conditionnement, int quantite) {}

    /** Demande d'arbitrage : quantités retenues, auteur, et motif facultatif. */
    public record ArbitrerPreCommandeRequest(String matriculeArbitre, String motif,
                                              List<LigneArbitrageDto> lignes) {}
    public record LigneArbitrageDto(String codeProduit, int quantiteValidee) {}
}
