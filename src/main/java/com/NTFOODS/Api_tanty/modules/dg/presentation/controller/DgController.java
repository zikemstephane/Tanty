package com.NTFOODS.Api_tanty.modules.dg.presentation.controller;

import com.NTFOODS.Api_tanty.modules.auth.domain.utilisateur.enums.Role;
import com.NTFOODS.Api_tanty.modules.dg.application.dto.response.ClassementCommercialResponse;
import com.NTFOODS.Api_tanty.modules.dg.application.dto.response.JournalAuditResponse;
import com.NTFOODS.Api_tanty.modules.dg.application.dto.response.KpisDgResponse;
import com.NTFOODS.Api_tanty.modules.dg.application.dto.response.ParametreSystemeResponse;
import com.NTFOODS.Api_tanty.modules.dg.application.dto.response.UtilisateurDgResponse;
import com.NTFOODS.Api_tanty.modules.dg.application.rapport.AnomaliesService;
import com.NTFOODS.Api_tanty.modules.dg.application.rapport.AnomalieResponse;
import com.NTFOODS.Api_tanty.modules.dg.application.rapport.PeriodeComparaison;
import com.NTFOODS.Api_tanty.modules.dg.application.rapport.RapportComparatifResponse;
import com.NTFOODS.Api_tanty.modules.dg.application.rapport.RapportComparatifService;
import com.NTFOODS.Api_tanty.modules.dg.application.rapport.TauxOccupationMarcheService;
import com.NTFOODS.Api_tanty.modules.dg.application.rapport.TauxOccupationSecteurResponse;
import com.NTFOODS.Api_tanty.modules.dg.application.rapport.ValidationEnAttenteResponse;
import com.NTFOODS.Api_tanty.modules.dg.application.rapport.ValidationsEnAttenteService;
import com.NTFOODS.Api_tanty.modules.dg.application.rapport.ZonePerformanceResponse;
import com.NTFOODS.Api_tanty.modules.dg.application.rapport.ZonePerformanceService;
import com.NTFOODS.Api_tanty.modules.dg.application.usecase.ConsulterJournalAuditUseCase;
import com.NTFOODS.Api_tanty.modules.dg.application.usecase.DashboardFinancierDgUseCase;
import com.NTFOODS.Api_tanty.modules.dg.application.usecase.DashboardProductionDgUseCase;
import com.NTFOODS.Api_tanty.modules.dg.application.usecase.DashboardStockDgUseCase;
import com.NTFOODS.Api_tanty.modules.auth.application.rh.dto.request.CreerEmployeRequest;
import com.NTFOODS.Api_tanty.modules.auth.application.rh.dto.response.EmployeResponse;
import com.NTFOODS.Api_tanty.modules.auth.application.rh.usecase.CreerEmployeUseCase;
import com.NTFOODS.Api_tanty.modules.dg.application.usecase.GererParametresSystemeUseCase;
import com.NTFOODS.Api_tanty.modules.dg.application.usecase.ListerUtilisateursDgUseCase;
import com.NTFOODS.Api_tanty.modules.dg.application.usecase.ObtenirClassementDgUseCase;
import com.NTFOODS.Api_tanty.modules.dg.application.usecase.ObtenirKpisDgUseCase;
import com.NTFOODS.Api_tanty.shared.application.dto.ApiResponse;
import com.NTFOODS.Api_tanty.shared.kernel.audit.TypeActionAudit;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

/**
 * Contrôleur REST — Pilotage Directeur Général (Superadmin).
 *
 * Le DG voit TOUT, contrôle TOUT, fixe TOUS les seuils.
 *
 * GET  /api/dg/kpis                      → KPIs agrégés du mois courant (CA, stocks, trésorerie, marge)
 * GET  /api/dg/classement-commerciaux    → Classement CA commerciaux (mois courant)
 * GET  /api/dg/comparatif                → Comparaison jour/semaine/mois/année, N vs N-1
 * GET  /api/dg/taux-occupation-marches   → Pénétration réelle par secteur
 * GET  /api/dg/zones                     → Performance CA par secteur (mois courant)
 * GET  /api/dg/validations               → Vue unifiée de toutes les validations en attente
 * GET  /api/dg/anomalies                 → Vue unifiée des anomalies détectées
 * GET  /api/dg/dashboard-financier       → Dashboard financier consolidé (caisse, factures, décaissements, versements, primes, recouvrements)
 * GET  /api/dg/dashboard-production      → Dashboard production consolidé (PPH, OF, lots, quantités)
 * GET  /api/dg/dashboard-stock           → Dashboard stock consolidé (niveaux, alertes, seuils)
 * GET  /api/dg/audit                     → Journal d'audit complet (filtres: matricule, période, module, type)
 * GET  /api/dg/utilisateurs              → Tous les utilisateurs (filtres: rôle, actif)
 * GET  /api/dg/parametres                → Consulter tous les paramètres système (seuils)
 * PUT  /api/dg/parametres                → Modifier les paramètres système (DG uniquement)
 */
@RestController
@RequestMapping("/api/dg")
@Tag(name = "DG — Pilotage", description = "Tableaux de bord et KPIs réservés au Directeur Général")
@SecurityRequirement(name = "bearerAuth")
@PreAuthorize("hasAnyRole('DIRECTEUR_GENERAL', 'ADMIN')")
public class DgController {

    private final ObtenirKpisDgUseCase kpisUseCase;
    private final ObtenirClassementDgUseCase classementUseCase;
    private final RapportComparatifService rapportComparatifService;
    private final TauxOccupationMarcheService tauxOccupationMarcheService;
    private final ZonePerformanceService zonePerformanceService;
    private final ValidationsEnAttenteService validationsEnAttenteService;
    private final AnomaliesService anomaliesService;
    private final DashboardFinancierDgUseCase dashboardFinancierUseCase;
    private final DashboardProductionDgUseCase dashboardProductionUseCase;
    private final DashboardStockDgUseCase dashboardStockUseCase;
    private final ConsulterJournalAuditUseCase journalAuditUseCase;
    private final ListerUtilisateursDgUseCase listerUtilisateursUseCase;
    private final GererParametresSystemeUseCase parametresUseCase;
    private final CreerEmployeUseCase creerEmployeUseCase;

    public DgController(ObtenirKpisDgUseCase kpisUseCase, ObtenirClassementDgUseCase classementUseCase,
                         RapportComparatifService rapportComparatifService,
                         TauxOccupationMarcheService tauxOccupationMarcheService,
                         ZonePerformanceService zonePerformanceService,
                         ValidationsEnAttenteService validationsEnAttenteService,
                         AnomaliesService anomaliesService,
                         DashboardFinancierDgUseCase dashboardFinancierUseCase,
                         DashboardProductionDgUseCase dashboardProductionUseCase,
                         DashboardStockDgUseCase dashboardStockUseCase,
                         ConsulterJournalAuditUseCase journalAuditUseCase,
                         ListerUtilisateursDgUseCase listerUtilisateursUseCase,
                         GererParametresSystemeUseCase parametresUseCase,
                         CreerEmployeUseCase creerEmployeUseCase) {
        this.kpisUseCase = kpisUseCase;
        this.classementUseCase = classementUseCase;
        this.rapportComparatifService = rapportComparatifService;
        this.tauxOccupationMarcheService = tauxOccupationMarcheService;
        this.zonePerformanceService = zonePerformanceService;
        this.validationsEnAttenteService = validationsEnAttenteService;
        this.anomaliesService = anomaliesService;
        this.dashboardFinancierUseCase = dashboardFinancierUseCase;
        this.dashboardProductionUseCase = dashboardProductionUseCase;
        this.dashboardStockUseCase = dashboardStockUseCase;
        this.journalAuditUseCase = journalAuditUseCase;
        this.listerUtilisateursUseCase = listerUtilisateursUseCase;
        this.parametresUseCase = parametresUseCase;
        this.creerEmployeUseCase = creerEmployeUseCase;
    }

    @GetMapping("/kpis")
    @Operation(summary = "KPIs agrégés du mois courant")
    public ResponseEntity<ApiResponse<KpisDgResponse>> kpis() {
        return ResponseEntity.ok(ApiResponse.succes(kpisUseCase.executer(), "KPIs DG"));
    }

    @GetMapping("/classement-commerciaux")
    @PreAuthorize("hasAnyRole('DIRECTEUR_GENERAL', 'ADMIN')")
    @Operation(summary = "Classement des commerciaux par CA (mois courant)")
    public ResponseEntity<ApiResponse<List<ClassementCommercialResponse>>> classement() {
        return ResponseEntity.ok(ApiResponse.succes(classementUseCase.executer(), "Classement commerciaux"));
    }

    @GetMapping("/comparatif")
    @Operation(summary = "Comparaison du CA à tous les niveaux (jour/semaine/mois/année), en valeur absolue",
        description = "Compare la période en cours (du début de la période à la date de référence) à la même " +
                      "tranche relative de la période précédente — comparaison équitable même en cours de période.")
    public ResponseEntity<ApiResponse<RapportComparatifResponse>> comparatif(
        @RequestParam PeriodeComparaison periode,
        @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date) {
        return ResponseEntity.ok(ApiResponse.succes(rapportComparatifService.comparerCA(periode, date),
            "Comparaison " + periode));
    }

    @GetMapping("/taux-occupation-marches")
    @PreAuthorize("hasAnyRole('DIRECTEUR_GENERAL', 'ADMIN')")
    @Operation(summary = "Taux d'occupation des marchés par secteur (clients livrés / boutiques potentielles recensées)")
    public ResponseEntity<ApiResponse<List<TauxOccupationSecteurResponse>>> tauxOccupationMarches() {
        var r = tauxOccupationMarcheService.calculer();
        return ResponseEntity.ok(ApiResponse.succes(r, r.size() + " secteur(s)"));
    }

    @GetMapping("/zones")
    @PreAuthorize("hasAnyRole('DIRECTEUR_GENERAL', 'ADMIN')")
    @Operation(summary = "Performance commerciale par secteur (CA, clients, ventes — mois courant)")
    public ResponseEntity<ApiResponse<List<ZonePerformanceResponse>>> zones() {
        var r = zonePerformanceService.calculer();
        return ResponseEntity.ok(ApiResponse.succes(r, r.size() + " secteur(s)"));
    }

    @GetMapping("/validations")
    @Operation(summary = "Vue unifiée de toutes les validations en attente (précommandes, dotations, BC, réceptions, versements, carburant)")
    public ResponseEntity<ApiResponse<List<ValidationEnAttenteResponse>>> validations() {
        var r = validationsEnAttenteService.lister();
        return ResponseEntity.ok(ApiResponse.succes(r, r.size() + " validation(s) en attente"));
    }

    @GetMapping("/anomalies")
    @PreAuthorize("hasAnyRole('DIRECTEUR_GENERAL', 'ADMIN')")
    @Operation(summary = "Vue unifiée des anomalies détectées (alertes rouges, stocks anormaux, comportements suspects)")
    public ResponseEntity<ApiResponse<List<AnomalieResponse>>> anomalies() {
        var r = anomaliesService.detecter();
        return ResponseEntity.ok(ApiResponse.succes(r, r.size() + " anomalie(s)"));
    }

    @GetMapping("/dashboard-financier")
    @Operation(summary = "Dashboard financier consolidé (caisse, factures, décaissements, versements, primes, recouvrements)")
    public ResponseEntity<ApiResponse<DashboardFinancierDgUseCase.DashboardFinancier>> dashboardFinancier() {
        return ResponseEntity.ok(ApiResponse.succes(dashboardFinancierUseCase.executer(), "Dashboard financier"));
    }

    @GetMapping("/dashboard-production")
    @Operation(summary = "Dashboard production consolidé (PPH, OF, lots, quantités produites)")
    public ResponseEntity<ApiResponse<DashboardProductionDgUseCase.DashboardProduction>> dashboardProduction() {
        return ResponseEntity.ok(ApiResponse.succes(dashboardProductionUseCase.executer(), "Dashboard production"));
    }

    @GetMapping("/dashboard-stock")
    @Operation(summary = "Dashboard stock consolidé (niveaux par entrepôt, alertes, produits sous seuil)")
    public ResponseEntity<ApiResponse<DashboardStockDgUseCase.DashboardStock>> dashboardStock() {
        return ResponseEntity.ok(ApiResponse.succes(dashboardStockUseCase.executer(), "Dashboard stock"));
    }

    @GetMapping("/audit")
    @Operation(summary = "Journal d'audit complet — le DG voit TOUTES les actions tracées",
        description = "Filtres optionnels: matricule, debut, fin, module, typeAction, limite (défaut 100, max 1000)")
    public ResponseEntity<ApiResponse<List<JournalAuditResponse>>> audit(
        @RequestParam(required = false) String matricule,
        @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate debut,
        @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate fin,
        @RequestParam(required = false) String module,
        @RequestParam(required = false) TypeActionAudit typeAction,
        @RequestParam(required = false, defaultValue = "100") int limite) {
        LocalDateTime debutDt = debut != null ? debut.atStartOfDay() : null;
        LocalDateTime finDt = fin != null ? fin.atTime(23, 59, 59) : null;
        var r = journalAuditUseCase.executer(matricule, debutDt, finDt, module, typeAction, limite);
        return ResponseEntity.ok(ApiResponse.succes(r, r.size() + " entrée(s) d'audit"));
    }

    @GetMapping("/utilisateurs")
    @Operation(summary = "Liste de tous les utilisateurs — le DG voit tout le personnel")
    public ResponseEntity<ApiResponse<List<UtilisateurDgResponse>>> utilisateurs(
        @RequestParam(required = false) Role role,
        @RequestParam(required = false) Boolean actif) {
        var r = listerUtilisateursUseCase.executer(role, actif);
        return ResponseEntity.ok(ApiResponse.succes(r, r.size() + " utilisateur(s)"));
    }

    @PostMapping("/employes")
    @Operation(summary = "Créer un employé (responsable ou exécutant)",
        description = "Le DG crée tout le personnel. Les responsables reçoivent un compte utilisateur. " +
                      "Les exécutants (MACHINISTE, AGENT_PRODUCTION) n'ont pas de compte. " +
                      "Matricule généré automatiquement : début du nom + zone de travail + 3 chiffres aléatoires.")
    public ResponseEntity<ApiResponse<EmployeResponse>> creerEmploye(@RequestBody @Valid CreerEmployeRequest request) {
        var r = creerEmployeUseCase.executer(request);
        return ResponseEntity.status(201).body(ApiResponse.succes(r, "Employé créé"));
    }

    @GetMapping("/parametres")
    @Operation(summary = "Consulter tous les paramètres système (seuils) — DG uniquement")
    public ResponseEntity<ApiResponse<List<ParametreSystemeResponse>>> parametres() {
        var r = parametresUseCase.lister();
        return ResponseEntity.ok(ApiResponse.succes(r, r.size() + " paramètre(s)"));
    }

    @PutMapping("/parametres")
    @PreAuthorize("hasRole('DIRECTEUR_GENERAL')")
    @Operation(summary = "Modifier les paramètres système (seuils) — DG EXCLUSIVEMENT",
        description = "Corps: { \"SEUIL_SECURITE_CAISSE\": 600000, \"MONTANT_PRIME_HEBDO\": 15000, ... }")
    public ResponseEntity<ApiResponse<List<ParametreSystemeResponse>>> modifierParametres(
        @RequestBody Map<String, Double> modifications,
        Authentication authentication) {
        var r = parametresUseCase.modifier(modifications, authentication.getName());
        return ResponseEntity.ok(ApiResponse.succes(r, r.size() + " paramètre(s) mis à jour"));
    }
}
