package com.NTFOODS.Api_tanty.modules.production.presentation.controller;

import com.NTFOODS.Api_tanty.modules.production.application.of.dto.request.CreerOFRequest;
import com.NTFOODS.Api_tanty.modules.production.application.of.dto.response.OFResponse;
import com.NTFOODS.Api_tanty.modules.production.application.of.mapper.OFMapper;
import com.NTFOODS.Api_tanty.modules.production.application.of.usecase.AnnulerOFUseCase;
import com.NTFOODS.Api_tanty.modules.production.application.of.usecase.CreerOFUseCase;
import com.NTFOODS.Api_tanty.modules.production.application.of.usecase.DemarrerOFUseCase;
import com.NTFOODS.Api_tanty.modules.production.application.of.usecase.EnregistrerProductionOFUseCase;
import com.NTFOODS.Api_tanty.modules.production.domain.of.enums.StatutOF;
import com.NTFOODS.Api_tanty.modules.production.domain.of.repository.OFRepository;
import com.NTFOODS.Api_tanty.shared.application.dto.ApiResponse;
import com.NTFOODS.Api_tanty.shared.kernel.exception.EntiteNonTrouveeException;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/production/ordres-fabrication")
@Tag(name = "Production — Ordres de fabrication", description = "OF générés à partir des PPH")
@SecurityRequirement(name = "bearerAuth")
public class OFController {

    private final OFRepository ofRepository;
    private final OFMapper ofMapper;
    private final CreerOFUseCase creerOFUseCase;
    private final DemarrerOFUseCase demarrerOFUseCase;
    private final EnregistrerProductionOFUseCase enregistrerProductionOFUseCase;
    private final AnnulerOFUseCase annulerOFUseCase;

    public OFController(OFRepository ofRepository, OFMapper ofMapper,
                        CreerOFUseCase creerOFUseCase, DemarrerOFUseCase demarrerOFUseCase,
                        EnregistrerProductionOFUseCase enregistrerProductionOFUseCase,
                        AnnulerOFUseCase annulerOFUseCase) {
        this.ofRepository = ofRepository;
        this.ofMapper = ofMapper;
        this.creerOFUseCase = creerOFUseCase;
        this.demarrerOFUseCase = demarrerOFUseCase;
        this.enregistrerProductionOFUseCase = enregistrerProductionOFUseCase;
        this.annulerOFUseCase = annulerOFUseCase;
    }

    @PostMapping
    @PreAuthorize("hasAnyRole('CHEF_PRODUCTION', 'ADMIN')")
    @Operation(summary = "Créer un ordre de fabrication")
    public ResponseEntity<ApiResponse<OFResponse>> creer(@RequestBody @Valid CreerOFRequest req) {
        OFResponse response = creerOFUseCase.executer(req);
        return ResponseEntity.status(HttpStatus.CREATED).body(ApiResponse.succes(response, "OF créé"));
    }

    @PatchMapping("/{idOF}/demarrer")
    @PreAuthorize("hasAnyRole('CHEF_PRODUCTION', 'ADMIN')")
    @Operation(summary = "Démarrer un OF (PLANIFIE → EN_COURS)")
    public ResponseEntity<ApiResponse<OFResponse>> demarrer(@PathVariable String idOF) {
        return ResponseEntity.ok(ApiResponse.succes(demarrerOFUseCase.executer(idOF), "OF démarré"));
    }

    @PatchMapping("/{idOF}/produire")
    @PreAuthorize("hasAnyRole('CHEF_PRODUCTION', 'RESPONSABLE_SALLE', 'ADMIN')")
    @Operation(summary = "Enregistrer une production sur un OF")
    public ResponseEntity<ApiResponse<OFResponse>> produire(@PathVariable String idOF, @RequestParam int quantite) {
        return ResponseEntity.ok(ApiResponse.succes(enregistrerProductionOFUseCase.executer(idOF, quantite),
            "Production enregistrée"));
    }

    @PatchMapping("/{idOF}/annuler")
    @PreAuthorize("hasAnyRole('CHEF_PRODUCTION', 'ADMIN')")
    @Operation(summary = "Annuler un OF")
    public ResponseEntity<ApiResponse<OFResponse>> annuler(@PathVariable String idOF) {
        return ResponseEntity.ok(ApiResponse.succes(annulerOFUseCase.executer(idOF), "OF annulé"));
    }

    @GetMapping
    @PreAuthorize("hasAnyRole('CHEF_PRODUCTION', 'RESPONSABLE_SALLE', 'DIRECTEUR_GENERAL', 'ADMIN')")
    @Operation(summary = "Lister les OF (filtres : statut, pph)")
    public ResponseEntity<ApiResponse<List<OFResponse>>> lister(
        @RequestParam(required = false) String statut,
        @RequestParam(required = false) String pph) {
        List<com.NTFOODS.Api_tanty.modules.production.domain.of.aggregate.OrdreFabrication> ofs;
        if (statut != null && !statut.isBlank()) {
            ofs = ofRepository.listerParStatut(StatutOF.valueOf(statut.trim().toUpperCase()));
        } else if (pph != null && !pph.isBlank()) {
            ofs = ofRepository.listerParPPH(pph);
        } else {
            ofs = ofRepository.listerTous();
        }
        return ResponseEntity.ok(ApiResponse.succes(ofs.stream().map(ofMapper::versResponse).toList(),
            ofs.size() + " OF"));
    }

    @GetMapping("/{idOF}")
    @PreAuthorize("hasAnyRole('CHEF_PRODUCTION', 'RESPONSABLE_SALLE', 'DIRECTEUR_GENERAL', 'ADMIN')")
    @Operation(summary = "Consulter un OF")
    public ResponseEntity<ApiResponse<OFResponse>> consulter(@PathVariable String idOF) {
        return ofRepository.trouverParId(idOF)
            .map(of -> ResponseEntity.ok(ApiResponse.succes(ofMapper.versResponse(of))))
            .orElseThrow(() -> EntiteNonTrouveeException.de("Ordre de fabrication", idOF));
    }
}
