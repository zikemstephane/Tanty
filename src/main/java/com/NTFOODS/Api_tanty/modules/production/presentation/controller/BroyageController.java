package com.NTFOODS.Api_tanty.modules.production.presentation.controller;

import com.NTFOODS.Api_tanty.modules.production.application.broyage.dto.request.AutoriserDepassementBroyageRequest;
import com.NTFOODS.Api_tanty.modules.production.application.broyage.dto.request.CloturerSessionBroyageRequest;
import com.NTFOODS.Api_tanty.modules.production.application.broyage.dto.request.EnregistrerRealisationBroyageRequest;
import com.NTFOODS.Api_tanty.modules.production.application.broyage.dto.request.JustifierNonValidationBroyageRequest;
import com.NTFOODS.Api_tanty.modules.production.application.broyage.dto.request.OuvrirSessionBroyageRequest;
import com.NTFOODS.Api_tanty.modules.production.application.broyage.dto.response.ClassementMachinisteResponse;
import com.NTFOODS.Api_tanty.modules.production.application.broyage.dto.response.SessionBroyageResponse;
import com.NTFOODS.Api_tanty.modules.production.application.broyage.mapper.SessionBroyageMapper;
import com.NTFOODS.Api_tanty.modules.production.application.broyage.usecase.AutoriserDepassementBroyageUseCase;
import com.NTFOODS.Api_tanty.modules.production.application.broyage.usecase.ClassementMachinistesUseCase;
import com.NTFOODS.Api_tanty.modules.production.application.broyage.usecase.CloturerSessionBroyageUseCase;
import com.NTFOODS.Api_tanty.modules.production.application.broyage.usecase.EnregistrerRealisationBroyageUseCase;
import com.NTFOODS.Api_tanty.modules.production.application.broyage.usecase.JustifierNonValidationBroyageUseCase;
import com.NTFOODS.Api_tanty.modules.production.application.broyage.usecase.OuvrirSessionBroyageUseCase;
import com.NTFOODS.Api_tanty.modules.production.domain.broyage.enums.TypePoudre;
import com.NTFOODS.Api_tanty.modules.production.domain.broyage.repository.SessionBroyageRepository;
import com.NTFOODS.Api_tanty.shared.application.dto.ApiResponse;
import com.NTFOODS.Api_tanty.shared.infrastructure.security.AuthenticatedUserResolver;
import com.NTFOODS.Api_tanty.shared.kernel.exception.EntiteNonTrouveeException;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import com.NTFOODS.Api_tanty.modules.production.application.broyage.dto.response.AvancementBroyageResponse;
import com.NTFOODS.Api_tanty.modules.production.application.broyage.usecase.AvancementBroyageUseCase;
import com.NTFOODS.Api_tanty.modules.production.application.broyage.usecase.ApercuBroyageUseCase;
import com.NTFOODS.Api_tanty.modules.production.application.broyage.dto.response.ApercuBroyageResponse;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/api/production/broyage")
@Tag(name = "Production — Broyage", description = "Sessions journalières de broyage (Chef Machiniste)")
@SecurityRequirement(name = "bearerAuth")
public class BroyageController {

    private final AvancementBroyageUseCase avancementUseCase;
    private final ApercuBroyageUseCase apercuUseCase;
    private final OuvrirSessionBroyageUseCase ouvrirUseCase;
    private final EnregistrerRealisationBroyageUseCase enregistrerUseCase;
    private final CloturerSessionBroyageUseCase cloturerUseCase;
    private final AutoriserDepassementBroyageUseCase autoriserDepassementUseCase;
    private final JustifierNonValidationBroyageUseCase justifierNonValidationUseCase;
    private final ClassementMachinistesUseCase classementUseCase;
    private final SessionBroyageRepository repository;
    private final SessionBroyageMapper mapper;
    private final AuthenticatedUserResolver userResolver;

    public BroyageController(OuvrirSessionBroyageUseCase ouvrirUseCase,
                              EnregistrerRealisationBroyageUseCase enregistrerUseCase,
                              CloturerSessionBroyageUseCase cloturerUseCase,
                              AutoriserDepassementBroyageUseCase autoriserDepassementUseCase,
                              JustifierNonValidationBroyageUseCase justifierNonValidationUseCase,
                              ClassementMachinistesUseCase classementUseCase,
                              SessionBroyageRepository repository,
                              SessionBroyageMapper mapper,
                              AuthenticatedUserResolver userResolver,
                              AvancementBroyageUseCase avancementUseCase,
                              ApercuBroyageUseCase apercuUseCase) {
        this.avancementUseCase = avancementUseCase;
        this.apercuUseCase = apercuUseCase;
        this.ouvrirUseCase = ouvrirUseCase;
        this.enregistrerUseCase = enregistrerUseCase;
        this.cloturerUseCase = cloturerUseCase;
        this.autoriserDepassementUseCase = autoriserDepassementUseCase;
        this.justifierNonValidationUseCase = justifierNonValidationUseCase;
        this.classementUseCase = classementUseCase;
        this.repository = repository;
        this.mapper = mapper;
        this.userResolver = userResolver;
    }

    @PostMapping("/sessions")
    @PreAuthorize("hasAuthority('PRODUCTION_BROYAGE_GERER')")
    @Operation(summary = "Ouvrir une session broyage journalière")
    public ResponseEntity<ApiResponse<SessionBroyageResponse>> ouvrir(
            @RequestBody @Valid OuvrirSessionBroyageRequest req) {
        SessionBroyageResponse response = ouvrirUseCase.executer(req, userResolver.getUtilisateurCourant());
        return ResponseEntity.status(HttpStatus.CREATED)
            .body(ApiResponse.succes(response, "Session broyage ouverte"));
    }

    @PostMapping("/sessions/realisations")
    @PreAuthorize("hasAuthority('PRODUCTION_BROYAGE_GERER')")
    @Operation(summary = "Enregistrer la réalisation d'un machiniste")
    public ResponseEntity<ApiResponse<SessionBroyageResponse>> enregistrerRealisation(
            @RequestBody @Valid EnregistrerRealisationBroyageRequest req) {
        SessionBroyageResponse response = enregistrerUseCase.executer(req, userResolver.getUtilisateurCourant());
        return ResponseEntity.ok(ApiResponse.succes(response, "Réalisation enregistrée"));
    }

    @PostMapping("/sessions/cloturer")
    @PreAuthorize("hasAuthority('PRODUCTION_BROYAGE_GERER')")
    @Operation(summary = "Clôturer la session et saisir les indicateurs globaux")
    public ResponseEntity<ApiResponse<SessionBroyageResponse>> cloturer(
            @RequestBody @Valid CloturerSessionBroyageRequest req) {
        SessionBroyageResponse response = cloturerUseCase.executer(req, userResolver.getUtilisateurCourant());
        return ResponseEntity.ok(ApiResponse.succes(response, "Session clôturée"));
    }

    @GetMapping("/apercu")
    @PreAuthorize("hasAuthority('PRODUCTION_REGISTRE_CONSULTER')")
    @Operation(summary = "Vue d'ensemble de l'atelier de broyage sur une période : synthèses, poudres, série journalière")
    public ResponseEntity<ApiResponse<ApercuBroyageResponse>> apercu(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate debut,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate fin) {
        return ResponseEntity.ok(ApiResponse.succes(apercuUseCase.executer(debut, fin), "Vue d'ensemble du broyage"));
    }

    @GetMapping("/avancement/{reference}")
    @PreAuthorize("hasAuthority('PRODUCTION_REGISTRE_CONSULTER')")
    @Operation(summary = "Poudre prévue et poudre écrasée d'un plan, par matière (lecture seule)")
    public ResponseEntity<ApiResponse<AvancementBroyageResponse>> avancement(@PathVariable String reference) {
        return ResponseEntity.ok(ApiResponse.succes(avancementUseCase.executer(reference), "Avancement du broyage"));
    }

    @PostMapping("/sessions/autoriser-depassement")
    @PreAuthorize("hasAuthority('PRODUCTION_BROYAGE_GERER')")
    @Operation(summary = "Autoriser un dépassement de quantité par rapport à l'objectif journalier")
    public ResponseEntity<ApiResponse<SessionBroyageResponse>> autoriserDepassement(
            @RequestBody @Valid AutoriserDepassementBroyageRequest req) {
        SessionBroyageResponse response = autoriserDepassementUseCase.executer(req, userResolver.getUtilisateurCourant());
        return ResponseEntity.ok(ApiResponse.succes(response, "Dépassement autorisé"));
    }

    @PostMapping("/sessions/justifier")
    @PreAuthorize("hasAuthority('PRODUCTION_BROYAGE_GERER')")
    @Operation(summary = "Justifier une journée en dessous du seuil de validation (85 %)")
    public ResponseEntity<ApiResponse<SessionBroyageResponse>> justifierNonValidation(
            @RequestBody @Valid JustifierNonValidationBroyageRequest req) {
        SessionBroyageResponse response = justifierNonValidationUseCase.executer(req, userResolver.getUtilisateurCourant());
        return ResponseEntity.ok(ApiResponse.succes(response, "Justificatif enregistré"));
    }

    @GetMapping("/sessions")
    @PreAuthorize("hasAuthority('PRODUCTION_REGISTRE_CONSULTER')")
    @Operation(summary = "Lister les sessions broyage (par PPH ou par période)")
    public ResponseEntity<ApiResponse<List<SessionBroyageResponse>>> lister(
            @RequestParam(required = false) String referencePPH,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate debut,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate fin) {
        List<SessionBroyageResponse> sessions;
        if (referencePPH != null) {
            sessions = repository.listerParPPH(referencePPH).stream().map(mapper::versResponse).toList();
        } else if (debut != null && fin != null) {
            sessions = repository.listerParPeriode(debut, fin).stream().map(mapper::versResponse).toList();
        } else {
            LocalDate today = LocalDate.now();
            sessions = repository.listerParPeriode(today.minusDays(30), today).stream().map(mapper::versResponse).toList();
        }
        return ResponseEntity.ok(ApiResponse.succes(sessions, sessions.size() + " session(s)"));
    }

    @GetMapping("/sessions/{id}")
    @PreAuthorize("hasAuthority('PRODUCTION_REGISTRE_CONSULTER')")
    @Operation(summary = "Détail d'une session broyage")
    public ResponseEntity<ApiResponse<SessionBroyageResponse>> detail(@PathVariable Long id) {
        SessionBroyageResponse response = repository.trouverParId(id)
            .map(mapper::versResponse)
            .orElseThrow(() -> EntiteNonTrouveeException.de("Session broyage", String.valueOf(id)));
        return ResponseEntity.ok(ApiResponse.succes(response, "Session trouvée"));
    }

    @GetMapping("/performances")
    @PreAuthorize("hasAuthority('PRODUCTION_REGISTRE_CONSULTER')")
    @Operation(summary = "Performances d'un machiniste sur une période")
    public ResponseEntity<ApiResponse<List<SessionBroyageResponse>>> performances(
            @RequestParam String machinisteMatricule,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate debut,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate fin) {
        List<SessionBroyageResponse> sessions = repository
            .listerParMachinisteEtPeriode(machinisteMatricule, debut, fin)
            .stream().map(mapper::versResponse).toList();
        return ResponseEntity.ok(ApiResponse.succes(sessions,
            sessions.size() + " session(s) pour " + machinisteMatricule));
    }

    @GetMapping("/classement")
    @PreAuthorize("hasAuthority('PRODUCTION_REGISTRE_CONSULTER')")
    @Operation(summary = "Classement des machinistes sur une période (efficacité, productivité, taux de réalisation)")
    public ResponseEntity<ApiResponse<ClassementMachinisteResponse>> classement(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate debut,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate fin) {
        ClassementMachinisteResponse response = classementUseCase.executer(debut, fin);
        return ResponseEntity.ok(ApiResponse.succes(response,
            response.classements().size() + " machiniste(s) classé(s)"));
    }
}
