package com.NTFOODS.Api_tanty.modules.stock.presentation.controller;

import com.NTFOODS.Api_tanty.modules.auth.domain.utilisateur.aggregate.Utilisateur;
import com.NTFOODS.Api_tanty.modules.auth.domain.utilisateur.enums.Role;
import com.NTFOODS.Api_tanty.modules.auth.domain.utilisateur.repository.UtilisateurRepository;
import com.NTFOODS.Api_tanty.modules.stock.application.niveau.dto.response.NiveauStockDetailResponse;
import com.NTFOODS.Api_tanty.modules.stock.application.niveau.usecase.consulter.ConsulterNiveauxDetaillesUseCase;
import com.NTFOODS.Api_tanty.modules.stock.domain.entrepot.enums.TypeEntrepot;
import com.NTFOODS.Api_tanty.modules.stock.domain.entrepot.model.Entrepot;
import com.NTFOODS.Api_tanty.modules.stock.domain.entrepot.repository.EntrepotRepository;
import com.NTFOODS.Api_tanty.modules.stock.domain.niveau.model.NiveauStock;
import com.NTFOODS.Api_tanty.modules.stock.domain.niveau.repository.NiveauStockRepository;
import com.NTFOODS.Api_tanty.shared.application.dto.ApiResponse;
import com.NTFOODS.Api_tanty.shared.kernel.exception.EntiteNonTrouveeException;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;

/**
 * Contrôleur REST — Données commerciales pour le module Stock (Angular).
 *
 * GET  /api/stock/commercials          → liste des commerciaux actifs
 * GET  /api/stock/info-produits        → info produits par commercial et date
 */
@RestController
@RequestMapping("/api/stock")
@Tag(name = "Stock — Données Commerciales", description = "Liste des commerciaux et info-produits journalières")
@SecurityRequirement(name = "bearerAuth")
public class CommercialStockController {

    private final UtilisateurRepository utilisateurRepository;
    private final EntrepotRepository entrepotRepository;
    private final NiveauStockRepository niveauStockRepository;
    private final ConsulterNiveauxDetaillesUseCase consulterNiveauxUseCase;

    public CommercialStockController(UtilisateurRepository utilisateurRepository,
                                      EntrepotRepository entrepotRepository,
                                      NiveauStockRepository niveauStockRepository,
                                      ConsulterNiveauxDetaillesUseCase consulterNiveauxUseCase) {
        this.utilisateurRepository = utilisateurRepository;
        this.entrepotRepository = entrepotRepository;
        this.niveauStockRepository = niveauStockRepository;
        this.consulterNiveauxUseCase = consulterNiveauxUseCase;
    }

    @GetMapping("/commercials")
    @PreAuthorize("isAuthenticated()")
    @Operation(summary = "Lister les commerciaux actifs")
    public ResponseEntity<ApiResponse<List<CommercialResponse>>> listerCommerciaux() {
        List<CommercialResponse> commerciaux = utilisateurRepository.listerParRole(Role.COMMERCIAL).stream()
            .filter(Utilisateur::isActif)
            .map(u -> new CommercialResponse(
                u.getId(),
                u.getNomComplet().getNomComplet(),
                u.getTelephone() != null ? u.getTelephone() : "",
                true
            ))
            .toList();
        return ResponseEntity.ok(ApiResponse.succes(commerciaux, commerciaux.size() + " commercial(aux)"));
    }

    @GetMapping("/info-produits")
    @PreAuthorize("isAuthenticated()")
    @Operation(summary = "Info produits par commercial (stock mobile à une date donnée)")
    public ResponseEntity<ApiResponse<List<InfoProduitsResponse>>> infoProduits(
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date) {

        LocalDate dateEffet = date != null ? date : LocalDate.now();

        List<Utilisateur> commerciaux = utilisateurRepository.listerParRole(Role.COMMERCIAL).stream()
            .filter(Utilisateur::isActif)
            .toList();

        List<InfoProduitsResponse> resultats = commerciaux.stream().map(c -> {
            Entrepot mobile = entrepotRepository.trouverMobileParCommercial(c.getMatricule().getValeur()).orElse(null);
            List<NiveauStockDetailResponse> niveaux = (mobile != null)
                ? consulterNiveauxUseCase.lister(mobile.getCode())
                : List.of();
            List<InfoProduitsLigneResponse> lignes = niveaux.stream()
                .map(n -> new InfoProduitsLigneResponse(
                    n.id(),
                    n.produitId() != null ? n.produitId() : 0,
                    n.designation(),
                    n.sku(),
                    n.quantite(),
                    n.valeurStock()
                ))
                .toList();
            return new InfoProduitsResponse(
                c.getId(),
                c.getMatricule().getValeur(),
                c.getNomComplet().getNomComplet(),
                dateEffet.toString(),
                "DRAFT",
                lignes
            );
        }).toList();

        return ResponseEntity.ok(ApiResponse.succes(resultats, resultats.size() + " info(s) produits"));
    }

    public record CommercialResponse(Long id, String name, String phone, boolean active) {}

    public record InfoProduitsResponse(
        Long id,
        String matriculeCommercial,
        String nomCommercial,
        String date,
        String statut,
        List<InfoProduitsLigneResponse> lignes
    ) {}

    public record InfoProduitsLigneResponse(
        Long niveauId,
        Long produitId,
        String designation,
        String sku,
        long quantite,
        java.math.BigDecimal valeurStock
    ) {}
}
