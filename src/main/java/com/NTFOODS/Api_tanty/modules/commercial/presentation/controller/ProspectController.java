package com.NTFOODS.Api_tanty.modules.commercial.presentation.controller;

import com.NTFOODS.Api_tanty.modules.commercial.application.prospect.dto.response.ProspectResponse;
import com.NTFOODS.Api_tanty.modules.commercial.application.prospect.mapper.ProspectMapper;
import com.NTFOODS.Api_tanty.modules.commercial.domain.prospect.aggregate.Prospect;
import com.NTFOODS.Api_tanty.modules.commercial.domain.prospect.enums.CanalProspection;
import com.NTFOODS.Api_tanty.modules.commercial.domain.prospect.repository.ProspectRepository;
import com.NTFOODS.Api_tanty.shared.application.dto.ApiResponse;
import com.NTFOODS.Api_tanty.shared.kernel.exception.EntiteNonTrouveeException;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;
import java.util.concurrent.atomic.AtomicLong;

@RestController
@RequestMapping("/api/commercial/prospects")
@Tag(name = "Commercial — Prospects", description = "Prospection terrain — clients potentiels (§5.13)")
@SecurityRequirement(name = "bearerAuth")
public class ProspectController {

    private static final AtomicLong COUNTER = new AtomicLong(0);

    private final ProspectRepository prospectRepository;
    private final ProspectMapper prospectMapper;

    public ProspectController(ProspectRepository prospectRepository, ProspectMapper prospectMapper) {
        this.prospectRepository = prospectRepository;
        this.prospectMapper = prospectMapper;
    }

    @PostMapping
    @PreAuthorize("hasAnyRole('COMMERCIAL', 'ADMIN')")
    @Operation(summary = "Créer un prospect", description = "Enregistre un client potentiel identifié en tournée.")
    public ResponseEntity<ApiResponse<ProspectResponse>> creer(@RequestBody CreerProspectRequest req) {
        String code = "PROS-" + String.format("%05d", COUNTER.incrementAndGet());
        Prospect p = Prospect.creer(code, req.nom(), req.telephone(), req.adresse(), req.localite(),
            req.matriculeCommercial(), req.canal(), req.dateProspection(), req.observations());
        return ResponseEntity.status(HttpStatus.CREATED)
            .body(ApiResponse.succes(prospectMapper.versResponse(prospectRepository.sauvegarder(p)), "Prospect créé : " + code));
    }

    @PatchMapping("/{code}/convertir")
    @PreAuthorize("hasAnyRole('COMMERCIAL', 'SECRETAIRE', 'ADMIN')")
    @Operation(summary = "Convertir un prospect en client")
    public ResponseEntity<ApiResponse<ProspectResponse>> convertir(@PathVariable String code,
                                                                    @RequestParam String codeClient) {
        Prospect p = trouverOuErreur(code);
        p.convertir(codeClient);
        return ResponseEntity.ok(ApiResponse.succes(prospectMapper.versResponse(prospectRepository.sauvegarder(p)), "Prospect converti en client " + codeClient));
    }

    @PatchMapping("/{code}/non-interesse")
    @PreAuthorize("hasAnyRole('COMMERCIAL', 'ADMIN')")
    @Operation(summary = "Marquer un prospect comme non intéressé")
    public ResponseEntity<ApiResponse<ProspectResponse>> marquerNonInteresse(@PathVariable String code,
                                                                               @RequestParam String motif) {
        Prospect p = trouverOuErreur(code);
        p.marquerNonInteresse(motif);
        return ResponseEntity.ok(ApiResponse.succes(prospectMapper.versResponse(prospectRepository.sauvegarder(p)), "Prospect marqué non intéressé"));
    }

    @PatchMapping("/{code}/archiver")
    @PreAuthorize("hasAnyRole('COMMERCIAL', 'DIRECTEUR_COMMERCIAL', 'ADMIN')")
    @Operation(summary = "Archiver un prospect")
    public ResponseEntity<ApiResponse<ProspectResponse>> archiver(@PathVariable String code) {
        Prospect p = trouverOuErreur(code);
        p.archiver();
        return ResponseEntity.ok(ApiResponse.succes(prospectMapper.versResponse(prospectRepository.sauvegarder(p)), "Prospect archivé"));
    }

    @GetMapping("/{code}")
    @PreAuthorize("hasAnyRole('COMMERCIAL', 'DIRECTEUR_GENERAL', 'DIRECTEUR_COMMERCIAL', 'ADMIN')")
    public ResponseEntity<ApiResponse<ProspectResponse>> consulter(@PathVariable String code) {
        return ResponseEntity.ok(ApiResponse.succes(prospectMapper.versResponse(trouverOuErreur(code)), "Prospect"));
    }

    @GetMapping
    @PreAuthorize("hasAnyRole('DIRECTEUR_GENERAL', 'DIRECTEUR_COMMERCIAL', 'ADMIN')")
    public ResponseEntity<ApiResponse<List<ProspectResponse>>> lister() {
        var list = prospectRepository.listerTous().stream().map(prospectMapper::versResponse).toList();
        return ResponseEntity.ok(ApiResponse.succes(list, list.size() + " prospect(s)"));
    }

    @GetMapping("/commercial/{matricule}")
    @PreAuthorize("hasAnyRole('COMMERCIAL', 'DIRECTEUR_GENERAL', 'DIRECTEUR_COMMERCIAL', 'ADMIN')")
    public ResponseEntity<ApiResponse<List<ProspectResponse>>> parCommercial(@PathVariable String matricule) {
        var list = prospectRepository.listerParCommercial(matricule).stream().map(prospectMapper::versResponse).toList();
        return ResponseEntity.ok(ApiResponse.succes(list, list.size() + " prospect(s)"));
    }

    private Prospect trouverOuErreur(String code) {
        return prospectRepository.trouverParCode(code)
            .orElseThrow(() -> EntiteNonTrouveeException.de("Prospect", code));
    }

    public record CreerProspectRequest(String nom, String telephone, String adresse, String localite,
                                       String matriculeCommercial, CanalProspection canal,
                                       LocalDate dateProspection, String observations) {}
}
