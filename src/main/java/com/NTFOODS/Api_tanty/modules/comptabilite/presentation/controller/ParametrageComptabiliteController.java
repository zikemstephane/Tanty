package com.NTFOODS.Api_tanty.modules.comptabilite.presentation.controller;

import com.NTFOODS.Api_tanty.modules.comptabilite.application.caisse.service.ParametrageCaisseService;
import com.NTFOODS.Api_tanty.modules.comptabilite.application.objectif.service.ParametrageObjectifService;
import com.NTFOODS.Api_tanty.modules.comptabilite.domain.caisse.aggregate.ParametrageCaisse;
import com.NTFOODS.Api_tanty.modules.comptabilite.domain.objectif.aggregate.ParametrageObjectif;
import com.NTFOODS.Api_tanty.shared.application.dto.ApiResponse;
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
import java.util.List;

/**
 * Contrôleur REST — Paramétrages datés de la comptabilité.
 *
 * <p>Regroupe deux familles dont les valeurs gouvernaient des comportements
 * automatiques depuis des constantes compilées : les seuils de caisse et de
 * versement d'une part, le découpage hebdomadaire des objectifs d'autre part.</p>
 */
@RestController
@RequestMapping("/api/comptabilite/parametrages")
@Tag(name = "Comptabilité — Paramétrages",
    description = "Seuils de caisse et de versement, jours ouvrés des objectifs")
@SecurityRequirement(name = "bearerAuth")
public class ParametrageComptabiliteController {

    private final ParametrageCaisseService caisseService;
    private final ParametrageObjectifService objectifService;

    public ParametrageComptabiliteController(ParametrageCaisseService caisseService,
                                              ParametrageObjectifService objectifService) {
        this.caisseService = caisseService;
        this.objectifService = objectifService;
    }

    // ── Caisse et versements ────────────────────────────────────────────────

    @GetMapping("/caisse/courant")
    @PreAuthorize("hasAuthority('COMPTABILITE_CAISSE_CONSULTER')")
    @Operation(summary = "Seuils de caisse en vigueur")
    public ResponseEntity<ApiResponse<ParametrageCaisseResponse>> caisseCourant() {
        return ResponseEntity.ok(ApiResponse.succes(versResponse(caisseService.parametrageCourant()),
            "Paramétrage caisse en vigueur"));
    }

    @GetMapping("/caisse/applicable")
    @PreAuthorize("hasAuthority('COMPTABILITE_CAISSE_CONSULTER')")
    @Operation(summary = "Seuils de caisse en vigueur à une date donnée")
    public ResponseEntity<ApiResponse<ParametrageCaisseResponse>> caisseApplicable(
        @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date) {
        return ResponseEntity.ok(ApiResponse.succes(versResponse(caisseService.parametrageApplicableA(date)),
            "Paramétrage caisse applicable au " + date));
    }

    @GetMapping("/caisse/historique")
    @PreAuthorize("hasAuthority('COMPTABILITE_CAISSE_CONSULTER')")
    @Operation(summary = "Succession des paramétrages de caisse")
    public ResponseEntity<ApiResponse<List<ParametrageCaisseResponse>>> caisseHistorique() {
        List<ParametrageCaisseResponse> h = caisseService.historique().stream().map(this::versResponse).toList();
        return ResponseEntity.ok(ApiResponse.succes(h, h.size() + " paramétrage(s)"));
    }

    @PostMapping("/caisse")
    @PreAuthorize("hasAuthority('COMPTABILITE_PARAMETRAGE_CAISSE_GERER')")
    @Operation(summary = "Mettre de nouveaux seuils de caisse en vigueur",
        description = "Le paramétrage courant est clôturé la veille de la prise d'effet du nouveau. " +
                      "Les journaux de caisse déjà ouverts conservent le seuil qui leur a été appliqué.")
    public ResponseEntity<ApiResponse<ParametrageCaisseResponse>> ouvrirCaisse(
        @RequestBody OuvrirParametrageCaisseRequest req) {
        ParametrageCaisse nouveau = caisseService.ouvrirNouveauParametrage(
            req.seuilSecurisationFCFA(), req.seuilAlerteRougeVersementFCFA(),
            req.dateEffet(), req.matriculeAuteur(), req.motif());
        return ResponseEntity.status(HttpStatus.CREATED).body(ApiResponse.succes(versResponse(nouveau),
            "Paramétrage " + nouveau.getReference() + " en vigueur à compter du " + nouveau.getDateDebut()));
    }

    // ── Objectifs ───────────────────────────────────────────────────────────

    @GetMapping("/objectif/courant")
    @PreAuthorize("hasAuthority('COMPTABILITE_OBJECTIF_CONSULTER')")
    @Operation(summary = "Jours ouvrés en vigueur")
    public ResponseEntity<ApiResponse<ParametrageObjectifResponse>> objectifCourant() {
        return ResponseEntity.ok(ApiResponse.succes(versResponse(objectifService.parametrageCourant()),
            "Paramétrage objectif en vigueur"));
    }

    @GetMapping("/objectif/historique")
    @PreAuthorize("hasAuthority('COMPTABILITE_OBJECTIF_CONSULTER')")
    @Operation(summary = "Succession des paramétrages d'objectif")
    public ResponseEntity<ApiResponse<List<ParametrageObjectifResponse>>> objectifHistorique() {
        List<ParametrageObjectifResponse> h = objectifService.historique().stream().map(this::versResponse).toList();
        return ResponseEntity.ok(ApiResponse.succes(h, h.size() + " paramétrage(s)"));
    }

    @PostMapping("/objectif")
    @PreAuthorize("hasAuthority('COMPTABILITE_PARAMETRAGE_OBJECTIF_GERER')")
    @Operation(summary = "Modifier le nombre de jours ouvrés",
        description = "Les objectifs déjà définis conservent le découpage qui leur a été appliqué.")
    public ResponseEntity<ApiResponse<ParametrageObjectifResponse>> ouvrirObjectif(
        @RequestBody OuvrirParametrageObjectifRequest req) {
        ParametrageObjectif nouveau = objectifService.ouvrirNouveauParametrage(
            req.nombreJoursOuvres(), req.dateEffet(), req.matriculeAuteur(), req.motif());
        return ResponseEntity.status(HttpStatus.CREATED).body(ApiResponse.succes(versResponse(nouveau),
            "Paramétrage " + nouveau.getReference() + " en vigueur à compter du " + nouveau.getDateDebut()));
    }

    // ── Mapping ─────────────────────────────────────────────────────────────

    private ParametrageCaisseResponse versResponse(ParametrageCaisse p) {
        return new ParametrageCaisseResponse(p.getId(), p.getReference(),
            p.getSeuilSecurisationFCFA(), p.getSeuilAlerteRougeVersementFCFA(),
            p.getDateDebut(), p.getDateFin(), p.estCourant(),
            p.getMatriculeAuteur(), p.getMotif(), p.getDateCreation());
    }

    private ParametrageObjectifResponse versResponse(ParametrageObjectif p) {
        return new ParametrageObjectifResponse(p.getId(), p.getReference(), p.getNombreJoursOuvres(),
            p.getDateDebut(), p.getDateFin(), p.estCourant(),
            p.getMatriculeAuteur(), p.getMotif(), p.getDateCreation());
    }

    public record OuvrirParametrageCaisseRequest(double seuilSecurisationFCFA,
                                                  double seuilAlerteRougeVersementFCFA,
                                                  LocalDate dateEffet, String matriculeAuteur, String motif) {}

    public record OuvrirParametrageObjectifRequest(int nombreJoursOuvres, LocalDate dateEffet,
                                                    String matriculeAuteur, String motif) {}

    public record ParametrageCaisseResponse(Long id, String reference,
                                             double seuilSecurisationFCFA, double seuilAlerteRougeVersementFCFA,
                                             LocalDate dateDebut, LocalDate dateFin, boolean courant,
                                             String matriculeAuteur, String motif, LocalDateTime dateCreation) {}

    public record ParametrageObjectifResponse(Long id, String reference, int nombreJoursOuvres,
                                               LocalDate dateDebut, LocalDate dateFin, boolean courant,
                                               String matriculeAuteur, String motif, LocalDateTime dateCreation) {}
}
