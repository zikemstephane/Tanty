package com.NTFOODS.Api_tanty.modules.commercial.presentation.controller;

import com.NTFOODS.Api_tanty.modules.commercial.application.feuillroute.dto.response.FeuillRouteResponse;
import com.NTFOODS.Api_tanty.modules.commercial.application.feuillroute.mapper.FeuillRouteMapper;
import com.NTFOODS.Api_tanty.modules.commercial.application.feuillroute.usecase.EnregistrerPassageArretUseCase;
import com.NTFOODS.Api_tanty.modules.commercial.application.feuillroute.usecase.PlanifierFeuillRouteUseCase;
import com.NTFOODS.Api_tanty.modules.commercial.domain.feuillroute.aggregate.FeuillRoute;
import com.NTFOODS.Api_tanty.modules.commercial.domain.feuillroute.enums.SaisieArret;
import com.NTFOODS.Api_tanty.modules.commercial.domain.feuillroute.repository.FeuillRouteRepository;
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
import java.util.List;

/**
 * Tournées journalières des commerciaux (§5.8).
 *
 * <p>La tournée n'a pas d'état propre : elle vit et meurt avec la journée de
 * travail. On ne la démarre ni ne la clôture ici — ouvrir la session ouvre la
 * tournée, déclarer la fin de tournée la ferme.</p>
 */
@RestController
@RequestMapping("/api/commercial/feuills-route")
@Tag(name = "Commercial — Feuilles de Route", description = "Tournées journalières des commerciaux (§5.8)")
@SecurityRequirement(name = "bearerAuth")
public class FeuillRouteController {

    private final FeuillRouteRepository feuillRouteRepository;
    private final FeuillRouteMapper feuillRouteMapper;
    private final PlanifierFeuillRouteUseCase planifierUseCase;
    private final EnregistrerPassageArretUseCase passageUseCase;

    public FeuillRouteController(FeuillRouteRepository feuillRouteRepository,
                                  FeuillRouteMapper feuillRouteMapper,
                                  PlanifierFeuillRouteUseCase planifierUseCase,
                                  EnregistrerPassageArretUseCase passageUseCase) {
        this.feuillRouteRepository = feuillRouteRepository;
        this.feuillRouteMapper = feuillRouteMapper;
        this.planifierUseCase = planifierUseCase;
        this.passageUseCase = passageUseCase;
    }

    @PostMapping
    @PreAuthorize("hasAuthority('COMMERCIAL_FEUILLE_ROUTE_GERER')")
    @Operation(summary = "Planifier sa tournée",
        description = "Le commercial arrête, sur son terminal, la liste ordonnée des clients qu'il "
            + "visite ce jour-là. La journée de travail doit exister et ne pas être close.")
    public ResponseEntity<ApiResponse<FeuillRouteResponse>> planifier(@RequestBody PlanifierFeuillRouteRequest req) {
        FeuillRoute fr = planifierUseCase.executer(req.matriculeCommercial(), req.date(),
            req.arrets().stream()
                .map(a -> new PlanifierFeuillRouteUseCase.Arret(a.codeClient(), a.ordre()))
                .toList());
        return ResponseEntity.status(HttpStatus.CREATED)
            .body(ApiResponse.succes(feuillRouteMapper.versResponse(fr),
                "Tournée créée : " + fr.getNumeroFeuillRoute()));
    }

    @PatchMapping("/ajouter-arret")
    @PreAuthorize("hasAuthority('COMMERCIAL_FEUILLE_ROUTE_GERER')")
    @Operation(summary = "Inscrire un client rencontré en cours de route")
    public ResponseEntity<ApiResponse<FeuillRouteResponse>> ajouterArret(@RequestBody AjouterArretRequest req) {
        FeuillRoute fr = passageUseCase.ajouterArret(req.matriculeCommercial(), req.date(), req.codeClient());
        return ResponseEntity.ok(ApiResponse.succes(feuillRouteMapper.versResponse(fr),
            "Client ajouté à la tournée"));
    }

    @PatchMapping("/visite")
    @PreAuthorize("hasAuthority('COMMERCIAL_FEUILLE_ROUTE_GERER')")
    @Operation(summary = "Marquer un client visité",
        description = "Constate le passage. La vente n'est pas transmise : si une vente existe pour ce "
            + "client dans la journée, l'arrêt s'y rattache de lui-même.")
    public ResponseEntity<ApiResponse<FeuillRouteResponse>> marquerVisite(@RequestBody VisiteRequest req) {
        FeuillRoute fr = passageUseCase.marquerVisite(req.matriculeCommercial(), req.date(), req.codeClient());
        return ResponseEntity.ok(ApiResponse.succes(feuillRouteMapper.versResponse(fr), "Client visité"));
    }

    @PatchMapping("/non-visite")
    @PreAuthorize("hasAuthority('COMMERCIAL_FEUILLE_ROUTE_GERER')")
    @Operation(summary = "Marquer un client non visité (§5.9)",
        description = "Exige un code InfoClient (Ab, NC, PA…). Les codes EV, AV et AF exigent une quantité.")
    public ResponseEntity<ApiResponse<FeuillRouteResponse>> marquerNonVisite(@RequestBody NonVisiteRequest req) {
        SaisieArret saisie = new SaisieArret(req.codeClient(), req.codeInfoClient(),
            req.quantiteInfo(), req.observations());
        FeuillRoute fr = passageUseCase.marquerNonVisite(req.matriculeCommercial(), req.date(), saisie);
        return ResponseEntity.ok(ApiResponse.succes(feuillRouteMapper.versResponse(fr), "Client non visité"));
    }

    @GetMapping("/{numero}")
    @PreAuthorize("hasAuthority('COMMERCIAL_FEUILLE_ROUTE_GERER')")
    public ResponseEntity<ApiResponse<FeuillRouteResponse>> consulter(@PathVariable String numero) {
        FeuillRoute fr = trouverOuErreur(numero);
        return ResponseEntity.ok(ApiResponse.succes(feuillRouteMapper.versResponse(fr), "Feuille de route"));
    }

    @GetMapping("/commercial/{matricule}")
    @PreAuthorize("hasAuthority('COMMERCIAL_FEUILLE_ROUTE_GERER')")
    public ResponseEntity<ApiResponse<List<FeuillRouteResponse>>> parCommercial(@PathVariable String matricule) {
        var list = feuillRouteRepository.listerParCommercial(matricule).stream().map(feuillRouteMapper::versResponse).toList();
        return ResponseEntity.ok(ApiResponse.succes(list, list.size() + " feuille(s) de route"));
    }

    @GetMapping("/commercial/{matricule}/date/{date}")
    @PreAuthorize("hasAuthority('COMMERCIAL_FEUILLE_ROUTE_GERER')")
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

    // Les actes sur un arrêt désignent la tournée par (commercial, date) et non
    // par son numéro : c'est ce que le terminal connaît, y compris hors ligne.
    public record AjouterArretRequest(String matriculeCommercial, LocalDate date, String codeClient) {}
    public record VisiteRequest(String matriculeCommercial, LocalDate date, String codeClient) {}
    public record NonVisiteRequest(
        String matriculeCommercial,
        LocalDate date,
        String codeClient,
        String codeInfoClient,
        Integer quantiteInfo,
        String observations
    ) {}
}
