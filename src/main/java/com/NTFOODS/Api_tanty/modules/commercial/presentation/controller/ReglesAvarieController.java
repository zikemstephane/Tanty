package com.NTFOODS.Api_tanty.modules.commercial.presentation.controller;

import com.NTFOODS.Api_tanty.modules.commercial.application.vente.service.ReglesAvarieService;
import com.NTFOODS.Api_tanty.modules.commercial.domain.vente.aggregate.ReglesAvarie;
import com.NTFOODS.Api_tanty.shared.application.dto.ApiResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

/**
 * Contrôleur REST — Règles de déclaration d'avarie.
 *
 * <p>La consultation est ouverte à tout utilisateur authentifié : l'application
 * mobile doit connaître le seuil de photo obligatoire pour l'appliquer à la
 * saisie et l'énoncer correctement au commercial, plutôt que de le porter en dur
 * dans une version livrée.</p>
 */
@RestController
@RequestMapping("/api/commercial/regles-avarie")
@Tag(name = "Commercial — Règles d'avarie", description = "Seuil de preuve photographique, daté et administrable")
@SecurityRequirement(name = "bearerAuth")
public class ReglesAvarieController {

    private final ReglesAvarieService service;

    public ReglesAvarieController(ReglesAvarieService service) {
        this.service = service;
    }

    @GetMapping("/courantes")
    @PreAuthorize("isAuthenticated()")
    @Operation(summary = "Règles d'avarie en vigueur",
        description = "Consommé par le mobile pour déterminer à partir de quelle quantité une photo " +
                      "est exigée, et afficher un libellé conforme à la règle réellement appliquée.")
    public ResponseEntity<ApiResponse<ReglesAvarieResponse>> courantes() {
        return ResponseEntity.ok(ApiResponse.succes(versResponse(service.reglesCourantes()),
            "Règles d'avarie en vigueur"));
    }

    @GetMapping("/historique")
    @PreAuthorize("hasAuthority('COMMERCIAL_VENTE_CONSULTER')")
    @Operation(summary = "Succession des règles d'avarie")
    public ResponseEntity<ApiResponse<List<ReglesAvarieResponse>>> historique() {
        List<ReglesAvarieResponse> h = service.historique().stream().map(this::versResponse).toList();
        return ResponseEntity.ok(ApiResponse.succes(h, h.size() + " jeu(x) de règles"));
    }

    @PostMapping
    @PreAuthorize("hasAuthority('COMMERCIAL_REGLES_AVARIE_GERER')")
    @Operation(summary = "Mettre de nouvelles règles d'avarie en vigueur")
    public ResponseEntity<ApiResponse<ReglesAvarieResponse>> ouvrir(@RequestBody OuvrirReglesAvarieRequest req) {
        ReglesAvarie nouvelles = service.ouvrirNouvellesRegles(req.quantiteSeuilPhotoObligatoire(),
            req.dateEffet(), req.matriculeAuteur(), req.motif());
        return ResponseEntity.status(HttpStatus.CREATED).body(ApiResponse.succes(versResponse(nouvelles),
            "Règles " + nouvelles.getReference() + " en vigueur à compter du " + nouvelles.getDateDebut()));
    }

    private ReglesAvarieResponse versResponse(ReglesAvarie r) {
        return new ReglesAvarieResponse(r.getId(), r.getReference(),
            r.getQuantiteSeuilPhotoObligatoire(), r.getDateDebut(), r.getDateFin(), r.estCourant(),
            r.getMatriculeAuteur(), r.getMotif(), r.getDateCreation());
    }

    public record OuvrirReglesAvarieRequest(int quantiteSeuilPhotoObligatoire, LocalDate dateEffet,
                                             String matriculeAuteur, String motif) {}

    public record ReglesAvarieResponse(Long id, String reference, int quantiteSeuilPhotoObligatoire,
                                        LocalDate dateDebut, LocalDate dateFin, boolean courant,
                                        String matriculeAuteur, String motif, LocalDateTime dateCreation) {}
}
