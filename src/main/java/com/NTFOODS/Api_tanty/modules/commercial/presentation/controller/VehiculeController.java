package com.NTFOODS.Api_tanty.modules.commercial.presentation.controller;

import com.NTFOODS.Api_tanty.modules.commercial.domain.vehicule.aggregate.Vehicule;
import com.NTFOODS.Api_tanty.modules.commercial.domain.vehicule.repository.VehiculeRepository;
import com.NTFOODS.Api_tanty.shared.application.dto.ApiResponse;
import com.NTFOODS.Api_tanty.shared.kernel.exception.EntiteNonTrouveeException;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/commercial/vehicules")
@Tag(name = "Commercial — Véhicules", description = "Gestion du parc automobile")
public class VehiculeController {

    private final VehiculeRepository vehiculeRepository;

    public VehiculeController(VehiculeRepository vehiculeRepository) {
        this.vehiculeRepository = vehiculeRepository;
    }

    @PostMapping
    @PreAuthorize("hasAuthority('COMMERCIAL_VEHICULE_GERER')")
    public ResponseEntity<ApiResponse<VehiculeResponse>> enregistrer(@RequestBody CreerVehiculeRequest req) {
        Vehicule vehicule = Vehicule.enregistrer(req.immatriculation(), req.marque(), req.modele(),
            req.matriculeCommercial(), req.consommationMoyenneL100());
        return ResponseEntity.status(HttpStatus.CREATED)
            .body(ApiResponse.succes(toResponse(vehiculeRepository.sauvegarder(vehicule)), "Véhicule enregistré"));
    }

    @GetMapping("/{immatriculation}")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<ApiResponse<VehiculeResponse>> trouverParImmat(@PathVariable String immatriculation) {
        Vehicule vehicule = vehiculeRepository.trouverParImmatriculation(immatriculation)
            .orElseThrow(() -> EntiteNonTrouveeException.de("Véhicule", immatriculation));
        return ResponseEntity.ok(ApiResponse.succes(toResponse(vehicule), "Véhicule trouvé"));
    }

    @GetMapping
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<ApiResponse<List<VehiculeResponse>>> lister(
        @RequestParam(required = false) String commercial,
        @RequestParam(required = false) Boolean actifSeulement) {
        List<Vehicule> vehicules;
        if (commercial != null) vehicules = vehiculeRepository.listerParCommercial(commercial);
        else if (Boolean.TRUE.equals(actifSeulement)) vehicules = vehiculeRepository.listerActifs();
        else vehicules = vehiculeRepository.listerTous();
        return ResponseEntity.ok(ApiResponse.succes(vehicules.stream().map(this::toResponse).toList(),
            vehicules.size() + " véhicule(s)"));
    }

    @PatchMapping("/{immatriculation}/reassigner")
    @PreAuthorize("hasAnyRole('ADMIN')")
    public ResponseEntity<ApiResponse<VehiculeResponse>> reassigner(@PathVariable String immatriculation,
                                                                     @RequestParam String matricule) {
        Vehicule vehicule = vehiculeRepository.trouverParImmatriculation(immatriculation)
            .orElseThrow(() -> EntiteNonTrouveeException.de("Véhicule", immatriculation));
        vehicule.reassigner(matricule);
        return ResponseEntity.ok(ApiResponse.succes(toResponse(vehiculeRepository.sauvegarder(vehicule)),
            "Véhicule réassigné à " + matricule));
    }

    @PatchMapping("/{immatriculation}/consommation")
    @PreAuthorize("hasAuthority('COMMERCIAL_VEHICULE_GERER')")
    public ResponseEntity<ApiResponse<VehiculeResponse>> mettreAJourConso(@PathVariable String immatriculation,
                                                                           @RequestParam double conso) {
        Vehicule vehicule = vehiculeRepository.trouverParImmatriculation(immatriculation)
            .orElseThrow(() -> EntiteNonTrouveeException.de("Véhicule", immatriculation));
        vehicule.mettreAJourConsommation(conso);
        return ResponseEntity.ok(ApiResponse.succes(toResponse(vehiculeRepository.sauvegarder(vehicule)),
            "Consommation mise à jour"));
    }

    private VehiculeResponse toResponse(Vehicule v) {
        return new VehiculeResponse(v.getId(), v.getImmatriculation(), v.getMarque(), v.getModele(),
            v.getMatriculeCommercial(), v.isActif(), v.getConsommationMoyenneL100(),
            v.getDateCreation());
    }

    public record CreerVehiculeRequest(String immatriculation, String marque, String modele,
        String matriculeCommercial, double consommationMoyenneL100) {}
    public record VehiculeResponse(Long id, String immatriculation, String marque, String modele,
        String matriculeCommercial, boolean actif, double consommationMoyenneL100,
        java.time.LocalDateTime dateCreation) {}
}
