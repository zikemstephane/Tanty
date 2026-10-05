package com.NTFOODS.Api_tanty.modules.production.presentation.controller;

import com.NTFOODS.Api_tanty.modules.administration.application.utilisateur.dto.response.UtilisateurResponse;
import com.NTFOODS.Api_tanty.modules.administration.application.utilisateur.usecase.ListerUtilisateursUseCase;
import com.NTFOODS.Api_tanty.modules.production.application.affectation.dto.request.CreerAffectationJourRequest;
import com.NTFOODS.Api_tanty.modules.production.application.affectation.dto.request.LigneAffectationRequest;
import com.NTFOODS.Api_tanty.modules.production.application.affectation.dto.request.SaisirRealisationPosteRequest;
import com.NTFOODS.Api_tanty.modules.production.application.affectation.dto.response.AffectationJournaliereResponse;
import com.NTFOODS.Api_tanty.modules.production.application.affectation.dto.response.ClassementExecuteurResponse;
import com.NTFOODS.Api_tanty.modules.production.application.affectation.dto.response.ComparaisonFutsProductionResponse;
import com.NTFOODS.Api_tanty.modules.production.application.affectation.dto.response.RapportHebdomadaireProductionResponse;
import com.NTFOODS.Api_tanty.modules.production.application.affectation.mapper.AffectationMapper;
import com.NTFOODS.Api_tanty.modules.production.application.affectation.usecase.ClassementExecuteursHebdomadaireUseCase;
import com.NTFOODS.Api_tanty.modules.production.application.affectation.usecase.ComparaisonFutsProductionUseCase;
import com.NTFOODS.Api_tanty.modules.production.application.affectation.usecase.CreerAffectationJourUseCase;
import com.NTFOODS.Api_tanty.modules.production.application.affectation.usecase.ModifierLignesAffectationUseCase;
import com.NTFOODS.Api_tanty.modules.production.application.affectation.usecase.RapportHebdomadaireProductionUseCase;
import com.NTFOODS.Api_tanty.modules.production.application.affectation.usecase.SaisirRealisationPosteUseCase;
import com.NTFOODS.Api_tanty.modules.production.application.affectation.usecase.ValiderAffectationUseCase;
import com.NTFOODS.Api_tanty.modules.production.domain.affectation.repository.AffectationJournaliereRepository;
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
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/api/production/affectations")
@Tag(name = "Production — Affectations", description = "Affectation journalière des postes et saisie des réalisations (responsable de salle)")
@SecurityRequirement(name = "bearerAuth")
public class AffectationController {

    private final CreerAffectationJourUseCase creerUseCase;
    private final SaisirRealisationPosteUseCase saisirUseCase;
    private final ValiderAffectationUseCase validerUseCase;
    private final ModifierLignesAffectationUseCase modifierLignesUseCase;
    private final ComparaisonFutsProductionUseCase comparaisonFutsProductionUseCase;
    private final ClassementExecuteursHebdomadaireUseCase classementExecuteursUseCase;
    private final RapportHebdomadaireProductionUseCase rapportHebdomadaireUseCase;
    private final AffectationJournaliereRepository repository;
    private final AffectationMapper mapper;
    private final AuthenticatedUserResolver userResolver;
    private final ListerUtilisateursUseCase listerUtilisateursUseCase;

    public AffectationController(CreerAffectationJourUseCase creerUseCase,
                                  SaisirRealisationPosteUseCase saisirUseCase,
                                  ValiderAffectationUseCase validerUseCase,
                                  ModifierLignesAffectationUseCase modifierLignesUseCase,
                                  ComparaisonFutsProductionUseCase comparaisonFutsProductionUseCase,
                                  ClassementExecuteursHebdomadaireUseCase classementExecuteursUseCase,
                                  RapportHebdomadaireProductionUseCase rapportHebdomadaireUseCase,
                                  AffectationJournaliereRepository repository,
                                  AffectationMapper mapper,
                                  AuthenticatedUserResolver userResolver,
                                  ListerUtilisateursUseCase listerUtilisateursUseCase) {
        this.creerUseCase = creerUseCase;
        this.saisirUseCase = saisirUseCase;
        this.validerUseCase = validerUseCase;
        this.modifierLignesUseCase = modifierLignesUseCase;
        this.comparaisonFutsProductionUseCase = comparaisonFutsProductionUseCase;
        this.classementExecuteursUseCase = classementExecuteursUseCase;
        this.rapportHebdomadaireUseCase = rapportHebdomadaireUseCase;
        this.repository = repository;
        this.mapper = mapper;
        this.userResolver = userResolver;
        this.listerUtilisateursUseCase = listerUtilisateursUseCase;
    }

    @GetMapping("/agents-production")
    @PreAuthorize("hasAuthority('PRODUCTION_AFFECTATION_GERER')")
    @Operation(summary = "Lister les agents de production (pour sélection lors d'une affectation)")
    public ResponseEntity<ApiResponse<List<UtilisateurResponse>>> listerAgentsProduction() {
        List<UtilisateurResponse> agents = listerUtilisateursUseCase.executer("AGENT_PRODUCTION");
        return ResponseEntity.ok(ApiResponse.succes(agents, agents.size() + " agent(s) de production"));
    }

    @PostMapping
    @PreAuthorize("hasAuthority('PRODUCTION_AFFECTATION_GERER')")
    @Operation(summary = "Créer l'affectation journalière des postes")
    public ResponseEntity<ApiResponse<AffectationJournaliereResponse>> creer(
            @RequestBody @Valid CreerAffectationJourRequest req) {
        AffectationJournaliereResponse response = creerUseCase.executer(req, userResolver.getUtilisateurCourant());
        return ResponseEntity.status(HttpStatus.CREATED)
            .body(ApiResponse.succes(response, "Affectation journalière créée"));
    }

    @PostMapping("/realisations")
    @PreAuthorize("hasAuthority('PRODUCTION_AFFECTATION_GERER')")
    @Operation(summary = "Saisir la réalisation d'un poste (avec productions individuelles)")
    public ResponseEntity<ApiResponse<AffectationJournaliereResponse>> saisirRealisation(
            @RequestBody @Valid SaisirRealisationPosteRequest req) {
        AffectationJournaliereResponse response = saisirUseCase.executer(req, userResolver.getUtilisateurCourant());
        return ResponseEntity.ok(ApiResponse.succes(response, "Réalisation enregistrée"));
    }

    @PatchMapping("/{id}/valider")
    @PreAuthorize("hasAuthority('PRODUCTION_AFFECTATION_GERER')")
    @Operation(summary = "Confirmer/Valider l'affectation journalière (Lock Numérique)",
        description = "Une fois validée, plus aucune saisie de réalisation n'est possible sur cette journée.")
    public ResponseEntity<ApiResponse<AffectationJournaliereResponse>> valider(@PathVariable Long id) {
        AffectationJournaliereResponse response = validerUseCase.executer(id, userResolver.getUtilisateurCourant());
        return ResponseEntity.ok(ApiResponse.succes(response, "Affectation confirmée et verrouillée"));
    }

    @PatchMapping("/{id}/lignes")
    @PreAuthorize("hasAuthority('PRODUCTION_AFFECTATION_GERER')")
    @Operation(summary = "Modifier les lignes d'une affectation journalière (Responsable de Salle)",
        description = "Permet de corriger les postes, employés et durées tant que l'affectation est en BROUILLON.")
    public ResponseEntity<ApiResponse<AffectationJournaliereResponse>> modifierLignes(
            @PathVariable Long id,
            @RequestBody @Valid List<LigneAffectationRequest> lignes) {
        AffectationJournaliereResponse response = modifierLignesUseCase.executer(id, lignes, userResolver.getUtilisateurCourant());
        return ResponseEntity.ok(ApiResponse.succes(response, "Lignes d'affectation mises à jour"));
    }

    @GetMapping
    @PreAuthorize("hasAuthority('PRODUCTION_REGISTRE_CONSULTER')")
    @Operation(summary = "Lister les affectations (par PPH ou par période)")
    public ResponseEntity<ApiResponse<List<AffectationJournaliereResponse>>> lister(
            @RequestParam(required = false) String referencePPH,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate debut,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate fin) {
        List<AffectationJournaliereResponse> list;
        if (referencePPH != null) {
            list = repository.listerParPPH(referencePPH).stream().map(mapper::versResponse).toList();
        } else if (debut != null && fin != null) {
            list = repository.listerParPeriode(debut, fin).stream().map(mapper::versResponse).toList();
        } else {
            LocalDate today = LocalDate.now();
            list = repository.listerParPeriode(today.minusDays(30), today).stream().map(mapper::versResponse).toList();
        }
        return ResponseEntity.ok(ApiResponse.succes(list, list.size() + " affectation(s)"));
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasAuthority('PRODUCTION_REGISTRE_CONSULTER')")
    @Operation(summary = "Détail d'une affectation journalière (avec classement des postes)")
    public ResponseEntity<ApiResponse<AffectationJournaliereResponse>> detail(@PathVariable Long id) {
        AffectationJournaliereResponse response = repository.trouverParId(id)
            .map(mapper::versResponse)
            .orElseThrow(() -> EntiteNonTrouveeException.de("Affectation", String.valueOf(id)));
        return ResponseEntity.ok(ApiResponse.succes(response, "Affectation trouvée"));
    }

    @GetMapping("/aujourd-hui")
    @PreAuthorize("hasAuthority('PRODUCTION_REGISTRE_CONSULTER')")
    @Operation(summary = "Affectation du jour pour un PPH donné")
    public ResponseEntity<ApiResponse<AffectationJournaliereResponse>> aujourdhui(
            @RequestParam String referencePPH) {
        AffectationJournaliereResponse response = repository
            .trouverParPPHEtDate(referencePPH, LocalDate.now())
            .map(mapper::versResponse)
            .orElseThrow(() -> EntiteNonTrouveeException.de("Affectation du jour", referencePPH));
        return ResponseEntity.ok(ApiResponse.succes(response, "Affectation du jour"));
    }

    @GetMapping("/{referencePPH}/comparaison-futs")
    @PreAuthorize("hasAuthority('PRODUCTION_REGISTRE_CONSULTER')")
    @Operation(summary = "Comparer les fûts dosés à la production réelle en aval")
    public ResponseEntity<ApiResponse<ComparaisonFutsProductionResponse>> comparerFutsProduction(
            @PathVariable String referencePPH,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date) {
        ComparaisonFutsProductionResponse response = comparaisonFutsProductionUseCase.executer(referencePPH, date);
        return ResponseEntity.ok(ApiResponse.succes(response, "Comparaison fûts / production"));
    }

    @GetMapping("/classement-executeurs")
    @PreAuthorize("hasAuthority('PRODUCTION_REGISTRE_CONSULTER')")
    @Operation(summary = "Classement hebdomadaire des exécutants par production réalisée")
    public ResponseEntity<ApiResponse<List<ClassementExecuteurResponse>>> classementExecuteurs(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate debut,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate fin) {
        List<ClassementExecuteurResponse> response = classementExecuteursUseCase.executer(debut, fin);
        return ResponseEntity.ok(ApiResponse.succes(response, response.size() + " exécutant(s) classé(s)"));
    }

    @GetMapping("/rapport-hebdomadaire")
    @PreAuthorize("hasAuthority('PRODUCTION_REGISTRE_CONSULTER')")
    @Operation(summary = "Rapport hebdomadaire de production (fûts, production, classement, écarts)")
    public ResponseEntity<ApiResponse<RapportHebdomadaireProductionResponse>> rapportHebdomadaire(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate debut,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate fin) {
        RapportHebdomadaireProductionResponse response = rapportHebdomadaireUseCase.executer(debut, fin);
        return ResponseEntity.ok(ApiResponse.succes(response, "Rapport hebdomadaire de production"));
    }
}
