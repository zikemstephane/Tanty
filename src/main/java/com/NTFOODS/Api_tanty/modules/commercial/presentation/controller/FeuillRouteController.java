package com.NTFOODS.Api_tanty.modules.commercial.presentation.controller;

import com.NTFOODS.Api_tanty.modules.commercial.application.feuillroute.dto.response.FeuillRouteResponse;
import com.NTFOODS.Api_tanty.modules.commercial.application.feuillroute.mapper.FeuillRouteMapper;
import com.NTFOODS.Api_tanty.modules.commercial.domain.feuillroute.aggregate.FeuillRoute;
import com.NTFOODS.Api_tanty.modules.commercial.domain.feuillroute.entity.ArretFeuillRoute;
import com.NTFOODS.Api_tanty.modules.commercial.domain.feuillroute.enums.SaisieArret;
import com.NTFOODS.Api_tanty.modules.commercial.domain.feuillroute.repository.FeuillRouteRepository;
import com.NTFOODS.Api_tanty.modules.commercial.domain.precommande.enums.InfoClientEnum;
import com.NTFOODS.Api_tanty.shared.application.dto.ApiResponse;
import com.NTFOODS.Api_tanty.shared.kernel.exception.EntiteNonTrouveeException;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.concurrent.atomic.AtomicLong;

@RestController
@RequestMapping("/api/commercial/feuills-route")
@Tag(name = "Commercial — Feuilles de Route", description = "Tournées journalières des commerciaux (§5.8)")
@SecurityRequirement(name = "bearerAuth")
public class FeuillRouteController {

    private static final AtomicLong COUNTER = new AtomicLong(0);

    private final FeuillRouteRepository feuillRouteRepository;
    private final FeuillRouteMapper feuillRouteMapper;

    public FeuillRouteController(FeuillRouteRepository feuillRouteRepository, FeuillRouteMapper feuillRouteMapper) {
        this.feuillRouteRepository = feuillRouteRepository;
        this.feuillRouteMapper = feuillRouteMapper;
    }

    @PostMapping
    @PreAuthorize("hasAnyRole('COMMERCIAL', 'SECRETAIRE', 'ADMIN')")
    @Operation(summary = "Planifier une feuille de route",
        description = "Crée la tournée journalière avec la liste ordonnée des arrêts (clients à visiter).")
    public ResponseEntity<ApiResponse<FeuillRouteResponse>> planifier(@RequestBody PlanifierFeuillRouteRequest req) {
        String num = "FR-" + DateTimeFormatter.ofPattern("yyyyMMdd").format(req.date())
            + "-" + String.format("%03d", COUNTER.incrementAndGet());
        List<ArretFeuillRoute> arrets = req.arrets().stream()
            .map(a -> new ArretFeuillRoute(a.codeClient(), a.ordre(), LocalDateTime.now()))
            .toList();
        FeuillRoute fr = FeuillRoute.planifier(num, req.matriculeCommercial(), req.date(), arrets);
        return ResponseEntity.status(HttpStatus.CREATED)
            .body(ApiResponse.succes(feuillRouteMapper.versResponse(feuillRouteRepository.sauvegarder(fr)), "Feuille de route créée : " + num));
    }

    @PatchMapping("/{numero}/demarrer")
    @PreAuthorize("hasAnyRole('COMMERCIAL', 'ADMIN')")
    public ResponseEntity<ApiResponse<FeuillRouteResponse>> demarrer(@PathVariable String numero) {
        FeuillRoute fr = trouverOuErreur(numero);
        fr.demarrer();
        return ResponseEntity.ok(ApiResponse.succes(feuillRouteMapper.versResponse(feuillRouteRepository.sauvegarder(fr)), "Feuille de route démarrée"));
    }

    @PatchMapping("/{numero}/viser")
    @PreAuthorize("hasAnyRole('COMMERCIAL', 'ADMIN')")
    @Operation(summary = "Viser un arrêt (vente réalisée)",
        description = "Marque un client comme visité avec une vente. Le numéro de vente est obligatoire.")
    public ResponseEntity<ApiResponse<FeuillRouteResponse>> viserArret(@PathVariable String numero,
                                                                        @RequestBody ViserArretRequest req) {
        FeuillRoute fr = trouverOuErreur(numero);
        fr.viserArret(req.codeClient(), req.numeroVente(), req.coordonneesGPS());
        return ResponseEntity.ok(ApiResponse.succes(feuillRouteMapper.versResponse(feuillRouteRepository.sauvegarder(fr)), "Arrêt visé"));
    }

    @PatchMapping("/{numero}/non-vise")
    @PreAuthorize("hasAnyRole('COMMERCIAL', 'ADMIN')")
    @Operation(summary = "Marquer un arrêt non-visé (§5.9)",
        description = "Enregistre un code InfoClient (Ab, NC, PA, etc.). Les codes EV, AV, AF exigent une quantité.")
    public ResponseEntity<ApiResponse<FeuillRouteResponse>> marquerNonVise(@PathVariable String numero,
                                                                             @RequestBody NonViseArretRequest req) {
        FeuillRoute fr = trouverOuErreur(numero);
        SaisieArret saisie = new SaisieArret(req.codeClient(), req.codeInfoClient(), req.quantiteInfo(), req.observations());
        fr.marquerArretNonVise(req.codeClient(), saisie, req.coordonneesGPS());
        return ResponseEntity.ok(ApiResponse.succes(feuillRouteMapper.versResponse(feuillRouteRepository.sauvegarder(fr)), "Arrêt marqué non-visé"));
    }

    @PatchMapping("/{numero}/cloturer")
    @PreAuthorize("hasAnyRole('COMMERCIAL', 'ADMIN')")
    public ResponseEntity<ApiResponse<FeuillRouteResponse>> cloturer(@PathVariable String numero,
                                                                      @RequestParam String matriculeCloture) {
        FeuillRoute fr = trouverOuErreur(numero);
        fr.cloturer(matriculeCloture);
        return ResponseEntity.ok(ApiResponse.succes(feuillRouteMapper.versResponse(feuillRouteRepository.sauvegarder(fr)), "Feuille de route clôturée"));
    }

    @GetMapping("/{numero}")
    @PreAuthorize("hasAnyRole('COMMERCIAL', 'DIRECTEUR_GENERAL', 'DIRECTEUR_COMMERCIAL', 'COMPTABLE', 'ADMIN')")
    public ResponseEntity<ApiResponse<FeuillRouteResponse>> consulter(@PathVariable String numero) {
        FeuillRoute fr = trouverOuErreur(numero);
        return ResponseEntity.ok(ApiResponse.succes(feuillRouteMapper.versResponse(fr), "Feuille de route"));
    }

    @GetMapping("/commercial/{matricule}")
    @PreAuthorize("hasAnyRole('COMMERCIAL', 'DIRECTEUR_GENERAL', 'DIRECTEUR_COMMERCIAL', 'ADMIN')")
    public ResponseEntity<ApiResponse<List<FeuillRouteResponse>>> parCommercial(@PathVariable String matricule) {
        var list = feuillRouteRepository.listerParCommercial(matricule).stream().map(feuillRouteMapper::versResponse).toList();
        return ResponseEntity.ok(ApiResponse.succes(list, list.size() + " feuille(s) de route"));
    }

    @GetMapping("/commercial/{matricule}/date/{date}")
    @PreAuthorize("hasAnyRole('COMMERCIAL', 'DIRECTEUR_GENERAL', 'DIRECTEUR_COMMERCIAL', 'ADMIN')")
    public ResponseEntity<ApiResponse<List<FeuillRouteResponse>>> parCommercialEtDate(
        @PathVariable String matricule,
        @PathVariable @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date) {
        var list = feuillRouteRepository.listerParCommercialEtDate(matricule, date).stream().map(feuillRouteMapper::versResponse).toList();
        return ResponseEntity.ok(ApiResponse.succes(list, list.size() + " feuille(s) de route"));
    }

    private FeuillRoute trouverOuErreur(String numero) {
        return feuillRouteRepository.trouverParNumero(numero)
            .orElseThrow(() -> EntiteNonTrouveeException.de("FeuillRoute", numero));
    }

    public record PlanifierFeuillRouteRequest(String matriculeCommercial, LocalDate date, List<ArretDto> arrets) {}
    public record ArretDto(String codeClient, int ordre) {}
    public record ViserArretRequest(String codeClient, String numeroVente, String coordonneesGPS) {}
    public record NonViseArretRequest(String codeClient, InfoClientEnum codeInfoClient, Integer quantiteInfo,
                                       String observations, String coordonneesGPS) {}
}
