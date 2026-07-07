package com.NTFOODS.Api_tanty.modules.commercial.presentation.controller;

import com.NTFOODS.Api_tanty.modules.commercial.domain.marche.model.ZoneDeVente;
import com.NTFOODS.Api_tanty.modules.commercial.domain.marche.repository.ZoneDeVenteRepository;
import com.NTFOODS.Api_tanty.shared.application.dto.ApiResponse;
import com.NTFOODS.Api_tanty.shared.kernel.exception.EntiteNonTrouveeException;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/commercial/zones-vente")
@Tag(name = "Commercial — Zones de Vente", description = "Gestion des zones commerciales et taux d'occupation")
public class ZoneDeVenteController {

    private final ZoneDeVenteRepository zoneRepository;

    public ZoneDeVenteController(ZoneDeVenteRepository zoneRepository) {
        this.zoneRepository = zoneRepository;
    }

    @PostMapping
    @PreAuthorize("hasAnyRole('CHARGEE_RP', 'COMPTABLE', 'ADMIN')")
    public ResponseEntity<ApiResponse<ZoneResponse>> creer(@RequestBody CreerZoneRequest req) {
        ZoneDeVente zone = ZoneDeVente.creer(req.nom(), req.type(), req.description(), req.clientsPotentiels());
        return ResponseEntity.status(HttpStatus.CREATED)
            .body(ApiResponse.succes(toResponse(zoneRepository.sauvegarder(zone)), "Zone créée"));
    }

    @GetMapping
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<ApiResponse<List<ZoneResponse>>> lister(@RequestParam(required = false) Boolean actifSeulement) {
        List<ZoneDeVente> zones = Boolean.TRUE.equals(actifSeulement)
            ? zoneRepository.listerActives() : zoneRepository.listerToutes();
        return ResponseEntity.ok(ApiResponse.succes(zones.stream().map(this::toResponse).toList(),
            zones.size() + " zone(s)"));
    }

    @GetMapping("/{nom}")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<ApiResponse<ZoneResponse>> trouverParNom(@PathVariable String nom) {
        ZoneDeVente zone = zoneRepository.trouverParNom(nom)
            .orElseThrow(() -> EntiteNonTrouveeException.de("ZoneDeVente", nom));
        return ResponseEntity.ok(ApiResponse.succes(toResponse(zone), "Zone trouvée"));
    }

    @PatchMapping("/{nom}/calibrer")
    @PreAuthorize("hasAnyRole('CHARGEE_RP', 'ADMIN')")
    public ResponseEntity<ApiResponse<ZoneResponse>> calibrer(@PathVariable String nom,
                                                               @RequestParam int clientsPotentiels) {
        ZoneDeVente zone = zoneRepository.trouverParNom(nom)
            .orElseThrow(() -> EntiteNonTrouveeException.de("ZoneDeVente", nom));
        zone.calibrerClientsPotentiels(clientsPotentiels);
        return ResponseEntity.ok(ApiResponse.succes(toResponse(zoneRepository.sauvegarder(zone)),
            "Calibrage effectué"));
    }

    @GetMapping("/{nom}/taux-occupation")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<ApiResponse<TauxOccupationResponse>> tauxOccupation(@PathVariable String nom,
                                                                               @RequestParam int clientsLivres3Mois) {
        ZoneDeVente zone = zoneRepository.trouverParNom(nom)
            .orElseThrow(() -> EntiteNonTrouveeException.de("ZoneDeVente", nom));
        double taux = zone.calculerTauxOccupation(clientsLivres3Mois);
        boolean sousExploitee = zone.estSousExploitee(clientsLivres3Mois);
        return ResponseEntity.ok(ApiResponse.succes(
            new TauxOccupationResponse(zone.getNom(), zone.getClientsPotentiels(), clientsLivres3Mois,
                taux, sousExploitee), "Taux calculé"));
    }

    @PatchMapping("/{nom}/desactiver")
    @PreAuthorize("hasAnyRole('ADMIN')")
    public ResponseEntity<ApiResponse<ZoneResponse>> desactiver(@PathVariable String nom) {
        ZoneDeVente zone = zoneRepository.trouverParNom(nom)
            .orElseThrow(() -> EntiteNonTrouveeException.de("ZoneDeVente", nom));
        zone.desactiver();
        return ResponseEntity.ok(ApiResponse.succes(toResponse(zoneRepository.sauvegarder(zone)),
            "Zone désactivée"));
    }

    private ZoneResponse toResponse(ZoneDeVente z) {
        return new ZoneResponse(z.getId(), z.getNom(), z.getType(), z.getDescription(),
            z.getClientsPotentiels(), z.isActif(), z.getDateCreation());
    }

    public record CreerZoneRequest(String nom, String type, String description, int clientsPotentiels) {}
    public record ZoneResponse(Long id, String nom, String type, String description,
        int clientsPotentiels, boolean actif, java.time.LocalDateTime dateCreation) {}
    public record TauxOccupationResponse(String zone, int clientsPotentiels, int clientsLivres,
        double tauxOccupation, boolean sousExploitee) {}
}
