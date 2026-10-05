package com.NTFOODS.Api_tanty.modules.commercial.presentation.controller;

import com.NTFOODS.Api_tanty.modules.commercial.domain.terminal.aggregate.Terminal;
import com.NTFOODS.Api_tanty.modules.commercial.domain.terminal.repository.TerminalRepository;
import com.NTFOODS.Api_tanty.shared.application.dto.ApiResponse;
import com.NTFOODS.Api_tanty.shared.kernel.exception.EntiteNonTrouveeException;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/commercial/terminaux")
@Tag(name = "Commercial — Terminaux", description = "Gestion des smartphones préfinancés")
public class TerminalController {

    private final TerminalRepository terminalRepository;

    public TerminalController(TerminalRepository terminalRepository) {
        this.terminalRepository = terminalRepository;
    }

    @PostMapping
    @PreAuthorize("hasAuthority('COMMERCIAL_TERMINAL_GERER')")
    public ResponseEntity<ApiResponse<TerminalResponse>> attribuer(@RequestBody CreerTerminalRequest req) {
        Terminal terminal = Terminal.attribuer(req.imei(), req.modele(), req.matriculeCommercial(), req.prixAchat());
        return ResponseEntity.status(HttpStatus.CREATED)
            .body(ApiResponse.succes(toResponse(terminalRepository.sauvegarder(terminal)), "Terminal attribué"));
    }

    @GetMapping("/{imei}")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<ApiResponse<TerminalResponse>> trouverParImei(@PathVariable String imei) {
        Terminal terminal = terminalRepository.trouverParImei(imei)
            .orElseThrow(() -> EntiteNonTrouveeException.de("Terminal", imei));
        return ResponseEntity.ok(ApiResponse.succes(toResponse(terminal), "Terminal trouvé"));
    }

    @GetMapping
    @PreAuthorize("hasAuthority('COMMERCIAL_TERMINAL_GERER')")
    public ResponseEntity<ApiResponse<List<TerminalResponse>>> lister(@RequestParam(required = false) Boolean actifSeulement) {
        List<Terminal> terminaux = Boolean.TRUE.equals(actifSeulement)
            ? terminalRepository.listerActifs() : terminalRepository.listerTous();
        return ResponseEntity.ok(ApiResponse.succes(terminaux.stream().map(this::toResponse).toList(),
            terminaux.size() + " terminal(aux)"));
    }

    @PatchMapping("/{imei}/deduire")
    @PreAuthorize("hasAuthority('COMMERCIAL_TERMINAL_GERER')")
    public ResponseEntity<ApiResponse<TerminalResponse>> deduirePrime(@PathVariable String imei,
                                                                       @RequestParam double montant) {
        Terminal terminal = terminalRepository.trouverParImei(imei)
            .orElseThrow(() -> EntiteNonTrouveeException.de("Terminal", imei));
        terminal.deduirePrime(montant);
        return ResponseEntity.ok(ApiResponse.succes(toResponse(terminalRepository.sauvegarder(terminal)),
            "Déduction appliquée. Reste à payer: " + terminal.getResteAPayer()));
    }

    @PatchMapping("/{imei}/desactiver")
    @PreAuthorize("hasAuthority('COMMERCIAL_TERMINAL_GERER')")
    public ResponseEntity<ApiResponse<TerminalResponse>> desactiver(@PathVariable String imei) {
        Terminal terminal = terminalRepository.trouverParImei(imei)
            .orElseThrow(() -> EntiteNonTrouveeException.de("Terminal", imei));
        terminal.desactiver();
        return ResponseEntity.ok(ApiResponse.succes(toResponse(terminalRepository.sauvegarder(terminal)),
            "Terminal désactivé"));
    }

    @PatchMapping("/{imei}/reassigner")
    @PreAuthorize("hasAnyRole('ADMIN')")
    public ResponseEntity<ApiResponse<TerminalResponse>> reassigner(@PathVariable String imei,
                                                                     @RequestParam String matricule) {
        Terminal terminal = terminalRepository.trouverParImei(imei)
            .orElseThrow(() -> EntiteNonTrouveeException.de("Terminal", imei));
        terminal.reassigner(matricule);
        return ResponseEntity.ok(ApiResponse.succes(toResponse(terminalRepository.sauvegarder(terminal)),
            "Terminal réassigné à " + matricule));
    }

    private TerminalResponse toResponse(Terminal t) {
        return new TerminalResponse(t.getId(), t.getImei(), t.getModele(), t.getMatriculeCommercial(),
            t.getPrixAchat(), t.getResteAPayer(), t.isActif(), t.estRembourse(),
            t.getDateDerniereConnexion(), t.getDateCreation());
    }

    public record CreerTerminalRequest(String imei, String modele, String matriculeCommercial, double prixAchat) {}
    public record TerminalResponse(Long id, String imei, String modele, String matriculeCommercial,
        double prixAchat, double resteAPayer, boolean actif, boolean estRembourse,
        java.time.LocalDateTime dateDerniereConnexion, java.time.LocalDateTime dateCreation) {}
}
