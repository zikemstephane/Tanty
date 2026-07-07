package com.NTFOODS.Api_tanty.modules.commercial.presentation.controller;

import com.NTFOODS.Api_tanty.modules.commercial.domain.marche.model.BoutiquePotentielle;
import com.NTFOODS.Api_tanty.modules.commercial.domain.marche.repository.BoutiquePotentielleRepository;
import com.NTFOODS.Api_tanty.shared.application.dto.ApiResponse;
import com.NTFOODS.Api_tanty.shared.infrastructure.security.AuthenticatedUserResolver;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Contrôleur REST — Recensement des Boutiques Potentielles (référentiel
 * terrain utilisé pour calculer le Taux d'Occupation des Marchés côté DG).
 */
@RestController
@RequestMapping("/api/commercial/boutiques-potentielles")
@Tag(name = "Commercial — Boutiques potentielles", description = "Référentiel de recensement terrain par secteur")
@SecurityRequirement(name = "bearerAuth")
public class BoutiquePotentielleController {

    private final BoutiquePotentielleRepository repository;
    private final AuthenticatedUserResolver userResolver;

    public BoutiquePotentielleController(BoutiquePotentielleRepository repository, AuthenticatedUserResolver userResolver) {
        this.repository = repository;
        this.userResolver = userResolver;
    }

    @PostMapping
    @PreAuthorize("hasAnyRole('COMMERCIAL', 'GESTIONNAIRE_STOCK', 'DIRECTEUR_GENERAL', 'ADMIN')")
    @Operation(summary = "Recenser une boutique potentielle dans un secteur")
    public ResponseEntity<ApiResponse<Void>> recenser(@RequestBody RecenserBoutiqueRequest request) {
        BoutiquePotentielle boutique = BoutiquePotentielle.recenser(request.nom(), request.secteur(),
            request.adresse(), userResolver.getMatriculeCourant());
        repository.sauvegarder(boutique);
        return ResponseEntity.status(HttpStatus.CREATED).body(ApiResponse.succes(null, "Boutique recensée"));
    }

    @GetMapping("/secteur/{secteur}")
    @PreAuthorize("isAuthenticated()")
    @Operation(summary = "Lister les boutiques recensées d'un secteur")
    public ResponseEntity<ApiResponse<List<BoutiquePotentielle>>> parSecteur(@PathVariable String secteur) {
        var r = repository.listerParSecteur(secteur);
        return ResponseEntity.ok(ApiResponse.succes(r, r.size() + " boutique(s)"));
    }

    @GetMapping("/secteurs")
    @PreAuthorize("isAuthenticated()")
    @Operation(summary = "Lister les secteurs recensés")
    public ResponseEntity<ApiResponse<List<String>>> secteurs() {
        var r = repository.listerSecteurs();
        return ResponseEntity.ok(ApiResponse.succes(r, r.size() + " secteur(s)"));
    }

    public record RecenserBoutiqueRequest(String nom, String secteur, String adresse) {}
}
