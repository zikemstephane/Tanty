package com.NTFOODS.Api_tanty.modules.production.presentation.controller;

import com.NTFOODS.Api_tanty.modules.production.application.pph.dto.request.AjusterRepartitionPPHRequest;
import com.NTFOODS.Api_tanty.modules.production.application.pph.dto.request.CreerPPHRequest;
import com.NTFOODS.Api_tanty.modules.production.application.pph.dto.request.ModifierLignesPPHRequest;
import com.NTFOODS.Api_tanty.modules.production.application.pph.dto.response.PPHResponse;
import com.NTFOODS.Api_tanty.modules.production.application.pph.mapper.PPHMapper;
import com.NTFOODS.Api_tanty.modules.production.application.pph.usecase.AjusterRepartitionPPHUseCase;
import com.NTFOODS.Api_tanty.modules.production.application.pph.usecase.CreerPPHUseCase;
import com.NTFOODS.Api_tanty.modules.production.application.pph.usecase.ModifierLignePPHUseCase;
import com.NTFOODS.Api_tanty.modules.production.application.pph.usecase.ValiderPPHUseCase;
import com.NTFOODS.Api_tanty.modules.production.domain.pph.aggregate.PlanProductionHebdomadaire;
import com.NTFOODS.Api_tanty.modules.production.domain.pph.enums.StatutPPH;
import com.NTFOODS.Api_tanty.modules.production.domain.pph.repository.PPHRepository;
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

/**
 * Contrôleur REST — Plan de Production Hebdomadaire (PPH).
 *
 * Document racine de tout le module Production : fiches journalières,
 * affectations, sessions de broyage et ordres de fabrication référencent
 * tous un {@code referencePPH} existant.
 */
@RestController
@RequestMapping("/api/production/pph")
@Tag(name = "Production — PPH", description = "Plans de production hebdomadaires")
@SecurityRequirement(name = "bearerAuth")
public class PPHController {

    private final PPHRepository pphRepository;
    private final PPHMapper pphMapper;
    private final CreerPPHUseCase creerPPHUseCase;
    private final ValiderPPHUseCase validerPPHUseCase;
    private final ModifierLignePPHUseCase modifierLignePPHUseCase;
    private final AjusterRepartitionPPHUseCase ajusterRepartitionPPHUseCase;
    private final AuthenticatedUserResolver userResolver;

    public PPHController(PPHRepository pphRepository, PPHMapper pphMapper, CreerPPHUseCase creerPPHUseCase,
                          ValiderPPHUseCase validerPPHUseCase, ModifierLignePPHUseCase modifierLignePPHUseCase,
                          AjusterRepartitionPPHUseCase ajusterRepartitionPPHUseCase,
                          AuthenticatedUserResolver userResolver) {
        this.pphRepository = pphRepository;
        this.pphMapper = pphMapper;
        this.creerPPHUseCase = creerPPHUseCase;
        this.validerPPHUseCase = validerPPHUseCase;
        this.modifierLignePPHUseCase = modifierLignePPHUseCase;
        this.ajusterRepartitionPPHUseCase = ajusterRepartitionPPHUseCase;
        this.userResolver = userResolver;
    }

    @PostMapping
    @PreAuthorize("hasAnyRole('CHEF_PRODUCTION', 'ADMIN')")
    @Operation(summary = "Créer un PPH (BROUILLON)")
    public ResponseEntity<ApiResponse<PPHResponse>> creer(@Valid @RequestBody CreerPPHRequest req) {
        PPHResponse response = creerPPHUseCase.executer(req, userResolver.getUtilisateurCourant());
        return ResponseEntity.status(HttpStatus.CREATED).body(ApiResponse.succes(response, "PPH créé"));
    }

    @PatchMapping("/{reference}/valider")
    @PreAuthorize("hasAnyRole('CHEF_PRODUCTION', 'ADMIN')")
    @Operation(summary = "Valider un PPH (BROUILLON → VALIDE)")
    public ResponseEntity<ApiResponse<PPHResponse>> valider(@PathVariable String reference) {
        PPHResponse response = validerPPHUseCase.executer(reference, userResolver.getUtilisateurCourant());
        return ResponseEntity.ok(ApiResponse.succes(response, "PPH validé"));
    }

    @PatchMapping("/{reference}/demarrer")
    @PreAuthorize("hasAnyRole('CHEF_PRODUCTION','RESPONSABLE_SALLE', 'ADMIN')")
    @Operation(summary = "Démarrer un PPH (VALIDE → EN_COURS)",
        description = "Un PPH doit être EN_COURS pour accepter des fiches journalières de production.")
    public ResponseEntity<ApiResponse<PPHResponse>> demarrer(@PathVariable String reference) {
        PlanProductionHebdomadaire pph = pphRepository.trouverParReference(reference)
            .orElseThrow(() -> EntiteNonTrouveeException.de("PPH", reference));
        pph.demarrer();
        return ResponseEntity.ok(ApiResponse.succes(pphMapper.versResponse(pphRepository.sauvegarder(pph)), "PPH démarré"));
    }

    @PatchMapping("/{reference}/cloturer")
    @PreAuthorize("hasAnyRole('CHEF_PRODUCTION','RESPONSABLE_SALLE', 'ADMIN')")
    @Operation(summary = "Clôturer un PPH (EN_COURS → CLOTURE)")
    public ResponseEntity<ApiResponse<PPHResponse>> cloturer(@PathVariable String reference) {
        PlanProductionHebdomadaire pph = pphRepository.trouverParReference(reference)
            .orElseThrow(() -> EntiteNonTrouveeException.de("PPH", reference));
        pph.cloturer();
        return ResponseEntity.ok(ApiResponse.succes(pphMapper.versResponse(pphRepository.sauvegarder(pph)), "PPH clôturé"));
    }

    @GetMapping
    @PreAuthorize("hasAnyRole('CHEF_PRODUCTION', 'RESPONSABLE_SALLE', 'CHEF_MACHINISTE', 'DIRECTEUR_GENERAL', 'ADMIN')")
    @Operation(summary = "Lister les PPH (filtre optionnel par statut)")
    public ResponseEntity<ApiResponse<List<PPHResponse>>> lister(@RequestParam(required = false) String statut) {
        var pphs = (statut == null || statut.isBlank())
            ? pphRepository.listerTous()
            : pphRepository.listerParStatut(StatutPPH.valueOf(statut.trim().toUpperCase()));
        return ResponseEntity.ok(ApiResponse.succes(pphs.stream().map(pphMapper::versResponse).toList(),
            pphs.size() + " PPH"));
    }

    @PutMapping("/{reference}/lignes")
    @PreAuthorize("hasAnyRole('RESPONSABLE_SALLE', 'ADMIN')")
    @Operation(summary = "Modifier les lignes d'un PPH (Responsable de Salle)",
        description = "Seul l'objectif de production est modifiable. La référence BC et la date de soumission ne le sont pas.")
    public ResponseEntity<ApiResponse<PPHResponse>> modifierLignes(
        @PathVariable String reference,
        @Valid @RequestBody ModifierLignesPPHRequest req) {
        var pph = modifierLignePPHUseCase.executer(reference,
            req.lignes().stream().map(l -> new ModifierLignePPHUseCase.LigneModifiable(l.codeProduit(), l.objectifSemaine())).toList(),
            userResolver.getUtilisateurCourant());
        return ResponseEntity.ok(ApiResponse.succes(pphMapper.versResponse(pph), "Lignes modifiées"));
    }

    @PutMapping("/{reference}/repartition")
    @PreAuthorize("hasAnyRole('RESPONSABLE_SALLE', 'ADMIN')")
    @Operation(summary = "Ajuster la répartition journalière des ressources (Responsable de Salle)",
        description = "Permet de corriger manuellement les quantités de poudre, fûts et emballages par jour.")
    public ResponseEntity<ApiResponse<PPHResponse>> ajusterRepartition(
        @PathVariable String reference,
        @Valid @RequestBody AjusterRepartitionPPHRequest req) {
        var pph = ajusterRepartitionPPHUseCase.executer(reference, req);
        return ResponseEntity.ok(ApiResponse.succes(pphMapper.versResponse(pph), "Répartition ajustée"));
    }

    @GetMapping("/en-cours")
    @PreAuthorize("hasAnyRole('CHEF_PRODUCTION', 'RESPONSABLE_SALLE', 'DIRECTEUR_GENERAL', 'ADMIN')")
    @Operation(summary = "Obtenir le PPH en cours")
    public ResponseEntity<ApiResponse<PPHResponse>> enCours() {
        return pphRepository.trouverEnCours()
            .map(pph -> ResponseEntity.ok(ApiResponse.succes(pphMapper.versResponse(pph))))
            .orElseThrow(() -> new EntiteNonTrouveeException("Aucun PPH en cours"));
    }

    @GetMapping("/{reference}")
    @PreAuthorize("hasAnyRole('CHEF_PRODUCTION', 'RESPONSABLE_SALLE', 'DIRECTEUR_GENERAL', 'ADMIN')")
    @Operation(summary = "Consulter un PPH par référence")
    public ResponseEntity<ApiResponse<PPHResponse>> consulter(@PathVariable String reference) {
        return pphRepository.trouverParReference(reference)
            .map(pph -> ResponseEntity.ok(ApiResponse.succes(pphMapper.versResponse(pph))))
            .orElseThrow(() -> EntiteNonTrouveeException.de("PPH", reference));
    }

    @GetMapping("/bc/{referenceBC}")
    @PreAuthorize("hasAnyRole('CHEF_PRODUCTION', 'RESPONSABLE_SALLE', 'DIRECTEUR_GENERAL', 'ADMIN')")
    @Operation(summary = "Lister tous les PPH liés à un BC (multi-semaines)",
        description = "Permet de suivre le taux de complétion global d'un BC couvrant plusieurs semaines")
    public ResponseEntity<ApiResponse<List<PPHResponse>>> listerParBC(@PathVariable String referenceBC) {
        var pphs = pphRepository.listerParBC(referenceBC);
        return ResponseEntity.ok(ApiResponse.succes(pphs.stream().map(pphMapper::versResponse).toList(),
            pphs.size() + " PPH liés au BC " + referenceBC));
    }
}
