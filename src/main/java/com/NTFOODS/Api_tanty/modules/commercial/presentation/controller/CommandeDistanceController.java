package com.NTFOODS.Api_tanty.modules.commercial.presentation.controller;

import com.NTFOODS.Api_tanty.modules.commercial.application.commandedistance.dto.response.CommandeDistanceResponse;
import com.NTFOODS.Api_tanty.modules.commercial.application.commandedistance.mapper.CommandeDistanceMapper;
import com.NTFOODS.Api_tanty.modules.commercial.domain.commandedistance.aggregate.CommandeDistance;
import com.NTFOODS.Api_tanty.modules.commercial.domain.commandedistance.entity.LigneCommandeDistance;
import com.NTFOODS.Api_tanty.modules.commercial.domain.commandedistance.enums.CanalCommande;
import com.NTFOODS.Api_tanty.modules.commercial.domain.commandedistance.repository.CommandeDistanceRepository;
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
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.concurrent.atomic.AtomicLong;

@RestController
@RequestMapping("/api/commercial/commandes-distance")
@Tag(name = "Commercial — Commandes à Distance", description = "Commandes hors tournée (appel, WhatsApp, SMS, email) — §5.7")
@SecurityRequirement(name = "bearerAuth")
public class CommandeDistanceController {

    private static final AtomicLong COUNTER = new AtomicLong(0);

    private final CommandeDistanceRepository repository;
    private final CommandeDistanceMapper mapper;

    public CommandeDistanceController(CommandeDistanceRepository repository, CommandeDistanceMapper mapper) {
        this.repository = repository;
        this.mapper = mapper;
    }

    @PostMapping
    @PreAuthorize("hasAuthority('COMMERCIAL_COMMANDE_DISTANCE_GERER')")
    @Operation(summary = "Enregistrer une commande à distance")
    public ResponseEntity<ApiResponse<CommandeDistanceResponse>> recevoir(@RequestBody RecevoirCommandeRequest req) {
        String num = "CDIST-" + DateTimeFormatter.ofPattern("yyyyMMdd").format(req.dateCommande())
            + "-" + String.format("%03d", COUNTER.incrementAndGet());
        List<LigneCommandeDistance> lignes = req.lignes().stream()
            .map(l -> new LigneCommandeDistance(l.codeProduit(), l.designation(), l.conditionnement(),
                l.quantite(), l.prixUnitaire()))
            .toList();
        CommandeDistance cd = CommandeDistance.recevoir(num, req.codeClient(), req.matriculeCommercial(),
            req.canal(), req.dateCommande(), lignes, req.observations());
        return ResponseEntity.status(HttpStatus.CREATED)
            .body(ApiResponse.succes(mapper.versResponse(repository.sauvegarder(cd)), "Commande à distance reçue : " + num));
    }

    @PatchMapping("/{numero}/valider-secretaire")
    @PreAuthorize("hasAuthority('COMMERCIAL_COMMANDE_DISTANCE_GERER')")
    public ResponseEntity<ApiResponse<CommandeDistanceResponse>> validerSecretaire(@PathVariable String numero,
                                                                                     @RequestParam String matricule) {
        CommandeDistance cd = trouverOuErreur(numero);
        cd.validerParSecretaire(matricule);
        return ResponseEntity.ok(ApiResponse.succes(mapper.versResponse(repository.sauvegarder(cd)), "Validée par la secrétaire"));
    }

    @PatchMapping("/{numero}/valider-comptable")
    @PreAuthorize("hasAuthority('COMMERCIAL_COMMANDE_DISTANCE_GERER')")
    public ResponseEntity<ApiResponse<CommandeDistanceResponse>> validerComptable(@PathVariable String numero,
                                                                                     @RequestParam String matricule) {
        CommandeDistance cd = trouverOuErreur(numero);
        cd.validerParComptable(matricule);
        return ResponseEntity.ok(ApiResponse.succes(mapper.versResponse(repository.sauvegarder(cd)), "Validée par le comptable"));
    }

    @PatchMapping("/{numero}/livrer")
    @PreAuthorize("hasAuthority('COMMERCIAL_COMMANDE_DISTANCE_GERER')")
    public ResponseEntity<ApiResponse<CommandeDistanceResponse>> livrer(@PathVariable String numero) {
        CommandeDistance cd = trouverOuErreur(numero);
        cd.marquerLivree();
        return ResponseEntity.ok(ApiResponse.succes(mapper.versResponse(repository.sauvegarder(cd)), "Commande livrée"));
    }

    @PatchMapping("/{numero}/annuler")
    @PreAuthorize("hasAuthority('COMMERCIAL_COMMANDE_DISTANCE_GERER')")
    @Operation(summary = "Annuler une commande à distance")
    public ResponseEntity<ApiResponse<CommandeDistanceResponse>> annuler(@PathVariable String numero,
                                                                           @RequestParam String motif) {
        CommandeDistance cd = trouverOuErreur(numero);
        cd.annuler(motif);
        return ResponseEntity.ok(ApiResponse.succes(mapper.versResponse(repository.sauvegarder(cd)), "Commande annulée"));
    }

    @GetMapping("/{numero}")
    @PreAuthorize("hasAuthority('COMMERCIAL_COMMANDE_DISTANCE_GERER')")
    public ResponseEntity<ApiResponse<CommandeDistanceResponse>> consulter(@PathVariable String numero) {
        return ResponseEntity.ok(ApiResponse.succes(mapper.versResponse(trouverOuErreur(numero)), "Commande à distance"));
    }

    @GetMapping
    @PreAuthorize("hasAuthority('COMMERCIAL_COMMANDE_DISTANCE_GERER')")
    public ResponseEntity<ApiResponse<List<CommandeDistanceResponse>>> lister() {
        var list = repository.listerParStatut(
            com.NTFOODS.Api_tanty.modules.commercial.domain.commandedistance.enums.StatutCommandeDistance.RECUE
        ).stream().map(mapper::versResponse).toList();
        return ResponseEntity.ok(ApiResponse.succes(list, list.size() + " commande(s)"));
    }

    private CommandeDistance trouverOuErreur(String numero) {
        return repository.trouverParNumero(numero)
            .orElseThrow(() -> EntiteNonTrouveeException.de("CommandeDistance", numero));
    }

    public record RecevoirCommandeRequest(String codeClient, String matriculeCommercial, CanalCommande canal,
                                           LocalDate dateCommande, String observations,
                                           List<LigneDto> lignes) {}
    public record LigneDto(String codeProduit, String designation, String conditionnement,
                           int quantite, double prixUnitaire) {}
}
