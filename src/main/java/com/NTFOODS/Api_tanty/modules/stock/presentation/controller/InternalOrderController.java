package com.NTFOODS.Api_tanty.modules.stock.presentation.controller;

import com.NTFOODS.Api_tanty.modules.stock.application.commande.dto.response.BonCommandeResponse;
import com.NTFOODS.Api_tanty.modules.stock.application.commande.mapper.BonCommandeMapper;
import com.NTFOODS.Api_tanty.modules.stock.domain.commande.enums.StatutBonCommande;
import com.NTFOODS.Api_tanty.modules.stock.domain.commande.model.BonCommande;
import com.NTFOODS.Api_tanty.modules.stock.domain.commande.repository.BonCommandeRepository;
import com.NTFOODS.Api_tanty.shared.application.dto.ApiResponse;
import com.NTFOODS.Api_tanty.shared.infrastructure.security.AuthenticatedUserResolver;
import com.NTFOODS.Api_tanty.shared.kernel.exception.EntiteNonTrouveeException;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

/**
 * Contrôleur REST — Commandes Internes (Angular StockApiRepository).
 *
 * Maps the Angular "internal-orders" concept onto existing BonCommande entities.
 * The Angular frontend treats internal orders as stock replenishment requests
 * directed to production, which maps naturally to purchase orders (bons de commande).
 *
 * GET    /api/stock/internal-orders           → list all (maps to BonCommande list)
 * GET    /api/stock/internal-orders/active    → list non-terminal BCs
 * POST   /api/stock/internal-orders           → create (delegates to BonCommande)
 * POST   /api/stock/internal-orders/{id}/approve → approve (validate BC)
 * POST   /api/stock/internal-orders/{id}/cancel  → cancel
 */
@RestController
@RequestMapping("/api/stock/internal-orders")
@Tag(name = "Stock — Commandes Internes", description = "Commandes internes (mappées sur les bons de commande)")
@SecurityRequirement(name = "bearerAuth")
public class InternalOrderController {

    private final BonCommandeRepository bonCommandeRepository;
    private final BonCommandeMapper mapper;
    private final AuthenticatedUserResolver userResolver;

    public InternalOrderController(BonCommandeRepository bonCommandeRepository,
                                    BonCommandeMapper mapper,
                                    AuthenticatedUserResolver userResolver) {
        this.bonCommandeRepository = bonCommandeRepository;
        this.mapper = mapper;
        this.userResolver = userResolver;
    }

    @GetMapping
    @PreAuthorize("isAuthenticated()")
    @Operation(summary = "Lister toutes les commandes internes")
    public ResponseEntity<ApiResponse<List<BonCommandeResponse>>> lister() {
        List<BonCommandeResponse> list = bonCommandeRepository.trouverTous().stream()
            .map(mapper::versResponse)
            .toList();
        return ResponseEntity.ok(ApiResponse.succes(list, list.size() + " commande(s) interne(s)"));
    }

    @GetMapping("/active")
    @PreAuthorize("isAuthenticated()")
    @Operation(summary = "Lister les commandes internes actives (non clôturées/annulées)")
    public ResponseEntity<ApiResponse<List<BonCommandeResponse>>> listerActives() {
        List<BonCommandeResponse> list = bonCommandeRepository.trouverTous().stream()
            .filter(bc -> !bc.getStatut().estTerminal())
            .map(mapper::versResponse)
            .toList();
        return ResponseEntity.ok(ApiResponse.succes(list, list.size() + " commande(s) active(s)"));
    }

    @PostMapping
    @PreAuthorize("hasAnyRole('GESTIONNAIRE_STOCK', 'DIRECTEUR_GENERAL', 'ADMIN')")
    @Operation(summary = "Créer une commande interne")
    public ResponseEntity<ApiResponse<BonCommandeResponse>> creer(@RequestBody CreerCommandeInterneRequest req) {
        String matricule = userResolver.getMatriculeCourant();
        BonCommande bc = BonCommande.creer(
            req.numero(),
            req.codeFournisseur(),
            req.dateCommande() != null ? req.dateCommande() : java.time.LocalDate.now(),
            req.dateLivraisonPrevue(),
            matricule
        );
        if (req.lignes() != null) {
            for (LigneCommandeInterneRequest ligne : req.lignes()) {
                bc.ajouterLigne(ligne.codeProduit(), ligne.designation(), ligne.sku(),
                    ligne.quantiteCommandee(), ligne.prixUnitaireHT());
            }
        }
        BonCommande sauvegarde = bonCommandeRepository.sauvegarder(bc);
        return ResponseEntity.status(HttpStatus.CREATED)
            .body(ApiResponse.succes(mapper.versResponse(sauvegarde), "Commande interne créée"));
    }

    @PostMapping("/{id}/approve")
    @PreAuthorize("hasAnyRole('GESTIONNAIRE_STOCK', 'DIRECTEUR_GENERAL', 'ADMIN')")
    @Operation(summary = "Approuver une commande interne")
    public ResponseEntity<ApiResponse<BonCommandeResponse>> approuver(@PathVariable Long id) {
        BonCommande bc = trouverParId(id);
        bc.valider();
        BonCommande sauvegarde = bonCommandeRepository.sauvegarder(bc);
        return ResponseEntity.ok(ApiResponse.succes(mapper.versResponse(sauvegarde), "Commande approuvée"));
    }

    @PostMapping("/{id}/cancel")
    @PreAuthorize("hasAnyRole('GESTIONNAIRE_STOCK', 'DIRECTEUR_GENERAL', 'ADMIN')")
    @Operation(summary = "Annuler une commande interne")
    public ResponseEntity<ApiResponse<BonCommandeResponse>> annuler(@PathVariable Long id) {
        BonCommande bc = trouverParId(id);
        bc.annuler();
        BonCommande sauvegarde = bonCommandeRepository.sauvegarder(bc);
        return ResponseEntity.ok(ApiResponse.succes(mapper.versResponse(sauvegarde), "Commande annulée"));
    }

    private BonCommande trouverParId(Long id) {
        return bonCommandeRepository.trouverTous().stream()
            .filter(bc -> id.equals(bc.getId()))
            .findFirst()
            .orElseThrow(() -> EntiteNonTrouveeException.de("Commande interne", String.valueOf(id)));
    }

    public record CreerCommandeInterneRequest(
        String numero,
        String codeFournisseur,
        java.time.LocalDate dateCommande,
        java.time.LocalDate dateLivraisonPrevue,
        List<LigneCommandeInterneRequest> lignes
    ) {}

    public record LigneCommandeInterneRequest(
        String codeProduit,
        String designation,
        String sku,
        long quantiteCommandee,
        java.math.BigDecimal prixUnitaireHT
    ) {}
}
