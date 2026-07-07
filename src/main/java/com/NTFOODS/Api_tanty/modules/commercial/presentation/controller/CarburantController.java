package com.NTFOODS.Api_tanty.modules.commercial.presentation.controller;

import com.NTFOODS.Api_tanty.modules.commercial.application.carburant.dto.response.CarburantResponse;
import com.NTFOODS.Api_tanty.modules.commercial.application.carburant.mapper.CarburantMapper;
import com.NTFOODS.Api_tanty.modules.commercial.domain.carburant.aggregate.DemandeCarburant;
import com.NTFOODS.Api_tanty.modules.commercial.domain.carburant.enums.StatutCarburant;
import com.NTFOODS.Api_tanty.modules.commercial.domain.carburant.repository.CarburantRepository;
import com.NTFOODS.Api_tanty.shared.application.dto.ApiResponse;
import com.NTFOODS.Api_tanty.shared.kernel.exception.EntiteNonTrouveeException;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/api/commercial/carburant")
@Tag(name = "Commercial — Carburant", description = "Demandes de carburant (double signature)")
public class CarburantController {

    private final CarburantRepository carburantRepository;
    private final CarburantMapper carburantMapper;

    public CarburantController(CarburantRepository carburantRepository, CarburantMapper carburantMapper) {
        this.carburantRepository = carburantRepository;
        this.carburantMapper = carburantMapper;
    }

    @PostMapping
    @PreAuthorize("hasAnyRole('COMMERCIAL', 'ADMIN')")
    public ResponseEntity<ApiResponse<CarburantResponse>> creer(@RequestBody CreerCarburantRequest req) {
        String reference = carburantRepository.genererReference(LocalDate.now());
        DemandeCarburant demande = DemandeCarburant.creer(reference, req.matriculeCommercial(),
            req.immatriculation(), req.stationPartenaire(), req.kilometrage(),
            req.litresDemandes(), req.montant(), LocalDate.now());
        if (req.photoCompteur() != null && !req.photoCompteur().isBlank()) {
            demande.setPhotoCompteur(req.photoCompteur());
        }
        return ResponseEntity.status(HttpStatus.CREATED)
            .body(ApiResponse.succes(carburantMapper.versResponse(carburantRepository.sauvegarder(demande)), "Demande créée"));
    }

    @PatchMapping("/{reference}/signer")
    @PreAuthorize("hasAnyRole('COMMERCIAL', 'CHEF_PRODUCTION', 'CONTROLEUR_GENERAL', 'ADMIN')")
    public ResponseEntity<ApiResponse<CarburantResponse>> signer(@PathVariable String reference,
                                                                 @RequestBody SignerCarburantRequest req) {
        DemandeCarburant demande = carburantRepository.trouverParReference(reference)
            .orElseThrow(() -> EntiteNonTrouveeException.de("Demande carburant", reference));
        demande.signer(req.signatureCommercial(), req.signatureSuperviseur());
        return ResponseEntity.ok(ApiResponse.succes(carburantMapper.versResponse(carburantRepository.sauvegarder(demande)), "Demande signée"));
    }

    @PatchMapping("/{reference}/refuser")
    @PreAuthorize("hasAnyRole('ADMIN', 'DIRECTEUR_GENERAL')")
    public ResponseEntity<ApiResponse<CarburantResponse>> refuser(@PathVariable String reference, @RequestParam String motif) {
        DemandeCarburant demande = carburantRepository.trouverParReference(reference)
            .orElseThrow(() -> EntiteNonTrouveeException.de("Demande carburant", reference));
        demande.refuser(motif);
        return ResponseEntity.ok(ApiResponse.succes(carburantMapper.versResponse(carburantRepository.sauvegarder(demande)), "Demande refusée"));
    }

    @GetMapping
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<ApiResponse<List<CarburantResponse>>> lister(@RequestParam(required = false) StatutCarburant statut,
                                                                       @RequestParam(required = false) String commercial) {
        List<DemandeCarburant> demandes;
        if (statut != null) demandes = carburantRepository.listerParStatut(statut);
        else if (commercial != null) demandes = carburantRepository.listerParCommercial(commercial);
        else demandes = carburantRepository.listerParStatut(StatutCarburant.EN_ATTENTE_SIGNATURE);
        return ResponseEntity.ok(ApiResponse.succes(demandes.stream().map(carburantMapper::versResponse).toList(),
            demandes.size() + " demande(s)"));
    }

    public record CreerCarburantRequest(String matriculeCommercial, String immatriculation,
        String stationPartenaire, int kilometrage, double litresDemandes, double montant,
        String photoCompteur) {}
    public record SignerCarburantRequest(String signatureCommercial, String signatureSuperviseur) {}
}
