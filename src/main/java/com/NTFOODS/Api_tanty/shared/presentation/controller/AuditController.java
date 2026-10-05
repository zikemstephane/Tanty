package com.NTFOODS.Api_tanty.shared.presentation.controller;

import com.NTFOODS.Api_tanty.shared.application.audit.dto.ActiviteSuspecteResponse;
import com.NTFOODS.Api_tanty.shared.application.audit.dto.JournalAuditResponse;
import com.NTFOODS.Api_tanty.shared.application.dto.ApiResponse;
import com.NTFOODS.Api_tanty.shared.application.dto.PageResponse;
import com.NTFOODS.Api_tanty.shared.infrastructure.audit.JournalAuditJpaEntity;
import com.NTFOODS.Api_tanty.shared.infrastructure.audit.JournalAuditJpaRepository;
import com.NTFOODS.Api_tanty.shared.kernel.audit.TypeActionAudit;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

/**
 * Contrôleur REST — Consultation du Journal d'Audit (Pilier 1 & Pilier 3).
 *
 * Réservé au Directeur Général, au Contrôleur Général et à l'Administrateur :
 * ce sont les rôles chargés de la supervision globale et de la détection des
 * comportements à risque, quel que soit le module d'origine de l'action.
 *
 * Tous les endpoints de liste sont désormais paginés (page/size/sort optionnels,
 * défaut: page=0, size=20, sort=horodatage,desc).
 */
@RestController
@RequestMapping("/api/audit")
@Tag(name = "Audit — Journal transversal", description = "Traçabilité de toutes les actions du système, tous modules confondus")
@SecurityRequirement(name = "bearerAuth")
@PreAuthorize("hasAnyRole('DIRECTEUR_GENERAL', 'CONTROLEUR_GENERAL', 'ADMIN')")
public class AuditController {

    private final JournalAuditJpaRepository repository;

    public AuditController(JournalAuditJpaRepository repository) {
        this.repository = repository;
    }

    @GetMapping("/utilisateur/{matricule}")
    @Operation(summary = "Historique des actions d'un utilisateur (paginé)",
        description = "Paramètres optionnels: page (déf. 0), size (déf. 20), sort (ex: horodatage,desc).")
    public ResponseEntity<ApiResponse<PageResponse<JournalAuditResponse>>> parUtilisateur(
        @PathVariable String matricule,
        @PageableDefault(size = 20, sort = "horodatage", direction = Sort.Direction.DESC) Pageable pageable) {
        Page<JournalAuditResponse> page = repository.findByMatriculeOrderByHorodatageDesc(matricule, pageable).map(this::versResponse);
        return ResponseEntity.ok(ApiResponse.succes(PageResponse.de(page), page.getTotalElements() + " action(s)"));
    }

    @GetMapping("/periode")
    @Operation(summary = "Journal d'audit sur une période donnée (paginé)",
        description = "Paramètres optionnels: page (déf. 0), size (déf. 20), sort (ex: horodatage,desc).")
    public ResponseEntity<ApiResponse<PageResponse<JournalAuditResponse>>> parPeriode(
        @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate debut,
        @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate fin,
        @PageableDefault(size = 20, sort = "horodatage", direction = Sort.Direction.DESC) Pageable pageable) {
        LocalDateTime debutDt = debut.atStartOfDay();
        LocalDateTime finDt = fin.atTime(23, 59, 59);
        Page<JournalAuditResponse> page = repository.findByHorodatageBetweenOrderByHorodatageDesc(debutDt, finDt, pageable).map(this::versResponse);
        return ResponseEntity.ok(ApiResponse.succes(PageResponse.de(page), page.getTotalElements() + " action(s)"));
    }

    @GetMapping("/module/{module}")
    @Operation(summary = "Journal d'audit filtré par module (paginé)",
        description = "Paramètres optionnels: page (déf. 0), size (déf. 20), sort (ex: horodatage,desc).")
    public ResponseEntity<ApiResponse<PageResponse<JournalAuditResponse>>> parModule(
        @PathVariable String module,
        @PageableDefault(size = 20, sort = "horodatage", direction = Sort.Direction.DESC) Pageable pageable) {
        Page<JournalAuditResponse> page = repository.findByModuleOrderByHorodatageDesc(module, pageable).map(this::versResponse);
        return ResponseEntity.ok(ApiResponse.succes(PageResponse.de(page), page.getTotalElements() + " action(s)"));
    }

    @GetMapping("/type/{typeAction}")
    @Operation(summary = "Journal d'audit filtré par type d'action (paginé)",
        description = "Paramètres optionnels: page (déf. 0), size (déf. 20), sort (ex: horodatage,desc).")
    public ResponseEntity<ApiResponse<PageResponse<JournalAuditResponse>>> parType(
        @PathVariable TypeActionAudit typeAction,
        @PageableDefault(size = 20, sort = "horodatage", direction = Sort.Direction.DESC) Pageable pageable) {
        Page<JournalAuditResponse> page = repository.findByTypeActionOrderByHorodatageDesc(typeAction, pageable).map(this::versResponse);
        return ResponseEntity.ok(ApiResponse.succes(PageResponse.de(page), page.getTotalElements() + " action(s)"));
    }

    @GetMapping("/echecs")
    @Operation(summary = "Toutes les actions ayant échoué (paginé)",
        description = "Paramètres optionnels: page (déf. 0), size (déf. 20), sort (ex: horodatage,desc).")
    public ResponseEntity<ApiResponse<PageResponse<JournalAuditResponse>>> echecs(
        @PageableDefault(size = 20, sort = "horodatage", direction = Sort.Direction.DESC) Pageable pageable) {
        Page<JournalAuditResponse> page = repository.findBySuccesFalseOrderByHorodatageDesc(pageable).map(this::versResponse);
        return ResponseEntity.ok(ApiResponse.succes(PageResponse.de(page), page.getTotalElements() + " échec(s)"));
    }

    @GetMapping("/activites-suspectes")
    @Operation(summary = "Détecte les comportements à risque : annulations/modifications répétées",
        description = "Repère les matricules qui multiplient les tentatives d'annulation ou de modification " +
                      "sur une même ressource au-delà du seuil, dans la fenêtre glissante (en heures) demandée.")
    public ResponseEntity<ApiResponse<List<ActiviteSuspecteResponse>>> activitesSuspectes(
        @RequestParam(defaultValue = "24") long fenetreHeures,
        @RequestParam(defaultValue = "5") long seuil) {
        LocalDateTime depuis = LocalDateTime.now().minusHours(fenetreHeures);
        var r = repository.detecterActivitesSuspectes(depuis, seuil).stream()
            .map(p -> new ActiviteSuspecteResponse(p.getMatricule(), p.getRessource(), p.getOccurrences()))
            .toList();
        return ResponseEntity.ok(ApiResponse.succes(r, r.size() + " comportement(s) suspect(s) détecté(s)"));
    }

    private JournalAuditResponse versResponse(JournalAuditJpaEntity e) {
        return new JournalAuditResponse(e.getId(), e.getMatricule(), e.getRole(), e.getTypeAction(), e.getModule(),
            e.getRessource(), e.getReferenceEntite(), e.getMethodeHttp(), e.getEndpoint(), e.getDetails(),
            e.isSucces(), e.getMessageErreur(), e.getAdresseIp(), e.getDureeMs(), e.getHorodatage());
    }
}
