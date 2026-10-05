package com.NTFOODS.Api_tanty.modules.comptabilite.presentation.controller;

import com.NTFOODS.Api_tanty.modules.comptabilite.application.prime.service.BaremePrimeService;
import com.NTFOODS.Api_tanty.modules.comptabilite.domain.prime.aggregate.BaremePrime;
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
 * Contrôleur REST — Barème de calcul des primes.
 *
 * <p>Les valeurs qui déterminent une prime — seuils d'éligibilité, montant par
 * gamme, plafond — cessent d'être compilées pour devenir administrables, avec
 * conservation des barèmes successifs : une prime reste ainsi justifiable des
 * mois après son calcul.</p>
 */
@RestController
@RequestMapping("/api/comptabilite/baremes-prime")
@Tag(name = "Comptabilité — Barème des primes",
    description = "Paramétrage daté des règles de calcul des primes commerciales")
@SecurityRequirement(name = "bearerAuth")
public class BaremePrimeController {

    private final BaremePrimeService service;

    public BaremePrimeController(BaremePrimeService service) {
        this.service = service;
    }

    @GetMapping("/courant")
    @PreAuthorize("hasAuthority('COMPTABILITE_PRIME_CONSULTER')")
    @Operation(summary = "Barème actuellement en vigueur")
    public ResponseEntity<ApiResponse<BaremePrimeResponse>> courant() {
        return ResponseEntity.ok(ApiResponse.succes(versResponse(service.baremeCourant()),
            "Barème en vigueur"));
    }

    @GetMapping("/applicable")
    @PreAuthorize("hasAuthority('COMPTABILITE_PRIME_CONSULTER')")
    @Operation(summary = "Barème en vigueur à une date donnée",
        description = "Permet de justifier a posteriori le montant d'une prime déjà calculée.")
    public ResponseEntity<ApiResponse<BaremePrimeResponse>> applicable(
        @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date) {
        return ResponseEntity.ok(ApiResponse.succes(versResponse(service.baremeApplicableA(date)),
            "Barème applicable au " + date));
    }

    @GetMapping("/historique")
    @PreAuthorize("hasAuthority('COMPTABILITE_PRIME_CONSULTER')")
    @Operation(summary = "Succession des barèmes, du plus récent au plus ancien")
    public ResponseEntity<ApiResponse<List<BaremePrimeResponse>>> historique() {
        List<BaremePrimeResponse> historique = service.historique().stream()
            .map(this::versResponse).toList();
        return ResponseEntity.ok(ApiResponse.succes(historique, historique.size() + " barème(s)"));
    }

    @PostMapping
    @PreAuthorize("hasAuthority('COMPTABILITE_BAREME_PRIME_GERER')")
    @Operation(summary = "Mettre un nouveau barème en vigueur",
        description = "Le barème courant est clôturé la veille de la prise d'effet du nouveau. " +
                      "Les primes déjà calculées conservent leur montant : seul le calcul des " +
                      "semaines couvertes par le nouveau barème en tient compte.")
    public ResponseEntity<ApiResponse<BaremePrimeResponse>> ouvrir(@RequestBody OuvrirBaremeRequest req) {
        BaremePrime nouveau = service.ouvrirNouveauBareme(
            req.tauxGlobalMinimumPourcent(), req.tauxGammeMinimumPourcent(),
            req.montantParGammeFCFA(), req.montantMaximumFCFA(),
            req.dateEffet(), req.matriculeAuteur(), req.motif());

        return ResponseEntity.status(HttpStatus.CREATED)
            .body(ApiResponse.succes(versResponse(nouveau),
                "Barème " + nouveau.getReference() + " en vigueur à compter du " + nouveau.getDateDebut()));
    }

    private BaremePrimeResponse versResponse(BaremePrime b) {
        return new BaremePrimeResponse(b.getId(), b.getReference(),
            b.getTauxGlobalMinimumPourcent(), b.getTauxGammeMinimumPourcent(),
            b.getMontantParGammeFCFA(), b.getMontantMaximumFCFA(),
            b.getDateDebut(), b.getDateFin(), b.estCourant(),
            b.getMatriculeAuteur(), b.getMotif(), b.getDateCreation());
    }

    public record OuvrirBaremeRequest(double tauxGlobalMinimumPourcent, double tauxGammeMinimumPourcent,
                                       double montantParGammeFCFA, double montantMaximumFCFA,
                                       LocalDate dateEffet, String matriculeAuteur, String motif) {}

    public record BaremePrimeResponse(Long id, String reference,
                                       double tauxGlobalMinimumPourcent, double tauxGammeMinimumPourcent,
                                       double montantParGammeFCFA, double montantMaximumFCFA,
                                       LocalDate dateDebut, LocalDate dateFin, boolean courant,
                                       String matriculeAuteur, String motif, LocalDateTime dateCreation) {}
}
