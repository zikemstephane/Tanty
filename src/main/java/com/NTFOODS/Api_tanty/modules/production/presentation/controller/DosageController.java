package com.NTFOODS.Api_tanty.modules.production.presentation.controller;

import com.NTFOODS.Api_tanty.modules.production.application.dosage.dto.request.EnregistrerSessionDosageRequest;
import com.NTFOODS.Api_tanty.modules.production.application.dosage.dto.response.PredictionProduitFiniResponse;
import com.NTFOODS.Api_tanty.modules.production.application.dosage.dto.response.SessionDosageResponse;
import com.NTFOODS.Api_tanty.modules.production.application.dosage.dto.response.StatistiquesFutsResponse;
import com.NTFOODS.Api_tanty.modules.production.application.dosage.mapper.SessionDosageMapper;
import com.NTFOODS.Api_tanty.modules.production.application.dosage.service.DocumentsDosageService;
import com.NTFOODS.Api_tanty.modules.production.application.dosage.service.RegistreDosageExportService;
import com.NTFOODS.Api_tanty.modules.production.application.dosage.usecase.MettreAJourDosageAutomatiqueUseCase;
import com.NTFOODS.Api_tanty.modules.production.application.dosage.usecase.PredireProduitFiniDepuisFutsUseCase;
import com.NTFOODS.Api_tanty.modules.production.application.dosage.usecase.StatistiquesFutsUseCase;
import com.NTFOODS.Api_tanty.modules.production.domain.dosage.aggregate.SessionDosage;
import com.NTFOODS.Api_tanty.modules.production.domain.dosage.repository.SessionDosageRepository;
import com.NTFOODS.Api_tanty.shared.application.dto.ApiResponse;
import com.NTFOODS.Api_tanty.shared.kernel.exception.EntiteNonTrouveeException;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

/**
 * Contrôleur REST — Dosage & Mélange (Agent Doseur).
 *
 * L'Agent Doseur récupère les poudres, vérifie les quantités, procède au dosage-mélange,
 * conditionne en fûts de 50 kg, et enregistre matières utilisées + machines + fûts.
 * Le nombre de fûts sortis correspond au nombre de fûts produits.
 */
@RestController
@RequestMapping("/api/production/dosage")
@Tag(name = "Production — Dosage & Mélange", description = "Opérations de l'Agent Doseur : matières, machines, fûts")
@SecurityRequirement(name = "bearerAuth")
public class DosageController {

    private final SessionDosageRepository repository;
    private final SessionDosageMapper mapper;
    private final StatistiquesFutsUseCase statistiquesFutsUseCase;
    private final PredireProduitFiniDepuisFutsUseCase predireProduitFiniUseCase;
    private final MettreAJourDosageAutomatiqueUseCase mettreAJourDosageAutomatiqueUseCase;
    private final RegistreDosageExportService exportService;
    private final DocumentsDosageService documentsService;

    public DosageController(SessionDosageRepository repository, SessionDosageMapper mapper,
                           StatistiquesFutsUseCase statistiquesFutsUseCase,
                           PredireProduitFiniDepuisFutsUseCase predireProduitFiniUseCase,
                           MettreAJourDosageAutomatiqueUseCase mettreAJourDosageAutomatiqueUseCase,
                           RegistreDosageExportService exportService,
                           DocumentsDosageService documentsService) {
        this.repository = repository;
        this.mapper = mapper;
        this.statistiquesFutsUseCase = statistiquesFutsUseCase;
        this.predireProduitFiniUseCase = predireProduitFiniUseCase;
        this.mettreAJourDosageAutomatiqueUseCase = mettreAJourDosageAutomatiqueUseCase;
        this.exportService = exportService;
        this.documentsService = documentsService;
    }

    @PostMapping
    @PreAuthorize("hasAnyRole('AGENT_DOSEUR', 'RESPONSABLE_SALLE', 'CHEF_PRODUCTION', 'ADMIN')")
    @Operation(summary = "Ouvrir une session de dosage pour une date et un PPH")
    public ResponseEntity<ApiResponse<SessionDosageResponse>> ouvrir(@Valid @RequestBody EnregistrerSessionDosageRequest req) {
        SessionDosage session = SessionDosage.ouvrir(req.referencePPH(), req.date(), req.matriculeAgentDoseur());
        return ResponseEntity.status(HttpStatus.CREATED)
            .body(ApiResponse.succes(mapper.versResponse(repository.sauvegarder(session)), "Session de dosage ouverte"));
    }

    @PostMapping("/{id}/matiere")
    @PreAuthorize("hasAnyRole('AGENT_DOSEUR', 'RESPONSABLE_SALLE', 'CHEF_PRODUCTION', 'ADMIN')")
    @Operation(summary = "Enregistrer une matière première utilisée (maïs, soja, arachide, sucre, poisson)")
    public ResponseEntity<ApiResponse<SessionDosageResponse>> enregistrerMatiere(@PathVariable Long id,
                                                                                  @RequestParam String typeMatiere,
                                                                                  @RequestParam double quantiteKg) {
        SessionDosage session = trouverOuErreur(id);
        session.enregistrerMatiereUtilisee(typeMatiere, quantiteKg);
        return ResponseEntity.ok(ApiResponse.succes(mapper.versResponse(repository.sauvegarder(session)), "Matière enregistrée"));
    }

    @PostMapping("/{id}/machine")
    @PreAuthorize("hasAnyRole('AGENT_DOSEUR', 'RESPONSABLE_SALLE', 'CHEF_PRODUCTION', 'ADMIN','DIRECTEUR_GENERAL')")
    @Operation(summary = "Enregistrer une machine mobilisée")
    public ResponseEntity<ApiResponse<SessionDosageResponse>> enregistrerMachine(@PathVariable Long id,
                                                                                  @RequestParam String machineId) {
        SessionDosage session = trouverOuErreur(id);
        session.enregistrerMachine(machineId);
        return ResponseEntity.ok(ApiResponse.succes(mapper.versResponse(repository.sauvegarder(session)), "Machine enregistrée"));
    }

    @PostMapping("/{id}/futs")
    @PreAuthorize("hasAnyRole('AGENT_DOSEUR', 'CHEF_PRODUCTION')")
    @Operation(summary = "Enregistrer le nombre de fûts sortis")
    public ResponseEntity<ApiResponse<SessionDosageResponse>> enregistrerFuts(@PathVariable Long id,
                                                                               @RequestParam int nbProduits) {
        SessionDosage session = trouverOuErreur(id);
        session.enregistrerFuts(nbProduits);
        return ResponseEntity.ok(ApiResponse.succes(mapper.versResponse(repository.sauvegarder(session)), "Fûts enregistrés"));
    }

    @PostMapping("/{id}/dosage-auto")
    @PreAuthorize("hasAnyRole('AGENT_DOSEUR', 'RESPONSABLE_SALLE', 'CHEF_PRODUCTION', 'ADMIN')")
    @Operation(summary = "Calculer et mettre à jour automatiquement les matières premières (MAIS/SOJA/ARACHIDE) selon le nombre de fûts et le type de mélange")
    public ResponseEntity<ApiResponse<SessionDosageResponse>> mettreAJourDosageAutomatique(
            @PathVariable Long id,
            @RequestParam int nbFuts,
            @RequestParam String typeMelange) {
        SessionDosage session = mettreAJourDosageAutomatiqueUseCase.executer(id, nbFuts, typeMelange);
        return ResponseEntity.ok(ApiResponse.succes(mapper.versResponse(session), "Dosage automatique appliqué"));
    }

    @PostMapping("/{id}/justifier")
    @PreAuthorize("hasAnyRole('AGENT_DOSEUR', 'RESPONSABLE_SALLE', 'CHEF_PRODUCTION', 'ADMIN')")
    @Operation(summary = "Justifier un écart entre fûts prévus et fûts produits")
    public ResponseEntity<ApiResponse<SessionDosageResponse>> justifierEcart(@PathVariable Long id,
                                                                              @RequestParam String justification) {
        SessionDosage session = trouverOuErreur(id);
        session.justifierEcart(justification);
        return ResponseEntity.ok(ApiResponse.succes(mapper.versResponse(repository.sauvegarder(session)), "Écart justifié"));
    }

    @PatchMapping("/{id}/valider")
    @PreAuthorize("hasAnyRole('AGENT_DOSEUR', 'CHEF_PRODUCTION')")
    @Operation(summary = "Valider la session de dosage (verrouille les saisies)")
    public ResponseEntity<ApiResponse<SessionDosageResponse>> valider(@PathVariable Long id) {
        SessionDosage session = trouverOuErreur(id);
        session.valider();
        return ResponseEntity.ok(ApiResponse.succes(mapper.versResponse(repository.sauvegarder(session)), "Session validée"));
    }

    @PostMapping("/{id}/cloturer")
    @PreAuthorize("hasAnyRole('AGENT_DOSEUR', 'CHEF_PRODUCTION')")
    @Operation(summary = "Clôturer la session de dosage (équivaut à valider définitivement)")
    public ResponseEntity<ApiResponse<SessionDosageResponse>> cloturer(@PathVariable Long id) {
        SessionDosage session = trouverOuErreur(id);
        session.cloturer();
        return ResponseEntity.ok(ApiResponse.succes(mapper.versResponse(repository.sauvegarder(session)), "Session clôturée"));
    }

    @PostMapping("/{id}/pause")
    @PreAuthorize("hasAnyRole('AGENT_DOSEUR', 'CHEF_PRODUCTION')")
    @Operation(summary = "Mettre la session de dosage en pause")
    public ResponseEntity<ApiResponse<SessionDosageResponse>> mettreEnPause(@PathVariable Long id) {
        SessionDosage session = trouverOuErreur(id);
        session.mettreEnPause();
        return ResponseEntity.ok(ApiResponse.succes(mapper.versResponse(repository.sauvegarder(session)), "Session mise en pause"));
    }

    @PostMapping("/{id}/reprendre")
    @PreAuthorize("hasAnyRole('AGENT_DOSEUR', 'CHEF_PRODUCTION')")
    @Operation(summary = "Reprendre une session de dosage en pause")
    public ResponseEntity<ApiResponse<SessionDosageResponse>> reprendre(@PathVariable Long id) {
        SessionDosage session = trouverOuErreur(id);
        session.reprendre();
        return ResponseEntity.ok(ApiResponse.succes(mapper.versResponse(repository.sauvegarder(session)), "Session reprise"));
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasAnyRole('RESPONSABLE_SALLE', 'CHEF_PRODUCTION', 'DIRECTEUR_GENERAL', 'ADMIN')")
    @Operation(summary = "Consulter une session de dosage")
    public ResponseEntity<ApiResponse<SessionDosageResponse>> consulter(@PathVariable Long id) {
        return ResponseEntity.ok(ApiResponse.succes(mapper.versResponse(trouverOuErreur(id)), "Session"));
    }

    @GetMapping("/pph/{reference}")
    @PreAuthorize("hasAnyRole('RESPONSABLE_SALLE', 'CHEF_PRODUCTION', 'DIRECTEUR_GENERAL', 'ADMIN')")
    @Operation(summary = "Lister les sessions de dosage par PPH")
    public ResponseEntity<ApiResponse<List<SessionDosageResponse>>> listerParPPH(@PathVariable String reference) {
        return ResponseEntity.ok(ApiResponse.succes(mapper.versResponseList(repository.listerParPPH(reference)), "Sessions"));
    }

    @GetMapping("/date/{date}")
    @PreAuthorize("hasAnyRole('RESPONSABLE_SALLE', 'CHEF_PRODUCTION', 'DIRECTEUR_GENERAL', 'ADMIN','AGENT_DOSEUR')")
    @Operation(summary = "Lister les sessions de dosage par date (Registre Salle de Mélange)")
    public ResponseEntity<ApiResponse<List<SessionDosageResponse>>> listerParDate(@PathVariable String date) {
        return ResponseEntity.ok(ApiResponse.succes(mapper.versResponseList(repository.listerParDate(LocalDate.parse(date))), "Sessions"));
    }

    @GetMapping("/periode")
    @PreAuthorize("hasAnyRole('RESPONSABLE_SALLE', 'CHEF_PRODUCTION', 'DIRECTEUR_GENERAL', 'ADMIN','AGENT_DOSEUR')")
    @Operation(summary = "Lister les sessions de dosage par période")
    public ResponseEntity<ApiResponse<List<SessionDosageResponse>>> listerParPeriode(
            @RequestParam String debut, @RequestParam String fin) {
        return ResponseEntity.ok(ApiResponse.succes(
            mapper.versResponseList(repository.listerParPeriode(LocalDate.parse(debut), LocalDate.parse(fin))), "Sessions"));
    }

    @GetMapping("/statistiques/futs")
    @PreAuthorize("hasAnyRole('AGENT_DOSEUR', 'RESPONSABLE_SALLE', 'CHEF_PRODUCTION', 'DIRECTEUR_GENERAL', 'ADMIN')")
    @Operation(summary = "Statistiques de fûts sur une période avec comparaison N-1")
    public ResponseEntity<ApiResponse<StatistiquesFutsResponse>> statistiquesFuts(
            @RequestParam String debut, @RequestParam String fin) {
        StatistiquesFutsResponse stats = statistiquesFutsUseCase.executer(LocalDate.parse(debut), LocalDate.parse(fin));
        return ResponseEntity.ok(ApiResponse.succes(stats, "Statistiques de fûts"));
    }

    @GetMapping("/prediction")
    @PreAuthorize("hasAnyRole('AGENT_DOSEUR', 'RESPONSABLE_SALLE', 'CHEF_PRODUCTION', 'DIRECTEUR_GENERAL', 'ADMIN')")
    @Operation(summary = "Prédire la quantité de produit fini à partir d'une session de dosage")
    public ResponseEntity<ApiResponse<PredictionProduitFiniResponse>> predireProduitFini(
            @RequestParam Long sessionId) {
        PredictionProduitFiniResponse prediction = predireProduitFiniUseCase.executer(sessionId);
        return ResponseEntity.ok(ApiResponse.succes(prediction, "Prédiction de produit fini"));
    }

    @GetMapping("/{id}/registre")
    @PreAuthorize("hasAnyRole('AGENT_DOSEUR', 'RESPONSABLE_SALLE', 'CHEF_PRODUCTION', 'DIRECTEUR_GENERAL', 'ADMIN')")
    @Operation(summary = "Télécharger le registre de dosage (PDF ou Excel)")
    public ResponseEntity<byte[]> telechargerRegistre(@PathVariable Long id,
                                                       @RequestParam(defaultValue = "pdf") String format) {
        byte[] data = switch (format.toLowerCase()) {
            case "excel", "xlsx" -> exportService.exportExcel(id);
            default -> exportService.exportPdf(id);
        };
        String extension = format.equalsIgnoreCase("excel") || format.equalsIgnoreCase("xlsx") ? "xlsx" : "pdf";
        MediaType mediaType = extension.equals("xlsx") ? MediaType.APPLICATION_OCTET_STREAM : MediaType.APPLICATION_PDF;
        return ResponseEntity.ok()
            .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"registre-dosage-" + id + "." + extension + "\"")
            .contentType(mediaType)
            .body(data);
    }

    @GetMapping("/{id}/documents")
    @PreAuthorize("hasAnyRole('AGENT_DOSEUR', 'RESPONSABLE_SALLE', 'CHEF_PRODUCTION', 'DIRECTEUR_GENERAL', 'ADMIN')")
    @Operation(summary = "Télécharger l'ensemble des documents liés à la session (ZIP)")
    public ResponseEntity<byte[]> telechargerDocuments(@PathVariable Long id) {
        byte[] data = documentsService.genererZipDocuments(id);
        return ResponseEntity.ok()
            .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"documents-dosage-" + id + ".zip\"")
            .contentType(MediaType.APPLICATION_OCTET_STREAM)
            .body(data);
    }

    private SessionDosage trouverOuErreur(Long id) {
        return repository.trouverParId(id)
            .orElseThrow(() -> EntiteNonTrouveeException.de("SessionDosage", String.valueOf(id)));
    }
}
