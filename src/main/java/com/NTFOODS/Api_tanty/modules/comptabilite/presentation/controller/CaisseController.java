package com.NTFOODS.Api_tanty.modules.comptabilite.presentation.controller;

import com.NTFOODS.Api_tanty.modules.comptabilite.application.caisse.dto.response.CaisseResponse;
import com.NTFOODS.Api_tanty.modules.comptabilite.application.caisse.mapper.CaisseMapper;
import com.NTFOODS.Api_tanty.modules.comptabilite.domain.caisse.aggregate.JournalCaisse;
import com.NTFOODS.Api_tanty.modules.comptabilite.domain.caisse.repository.JournalCaisseRepository;
import com.NTFOODS.Api_tanty.shared.application.dto.ApiResponse;
import com.NTFOODS.Api_tanty.shared.kernel.event.DomainEventPublisher;
import com.NTFOODS.Api_tanty.shared.kernel.exception.EntiteNonTrouveeException;
import com.NTFOODS.Api_tanty.shared.kernel.exception.RegleMetierException;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.format.annotation.DateTimeFormat;
import com.NTFOODS.Api_tanty.modules.comptabilite.application.caisse.service.ParametrageCaisseService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/api/comptabilite/caisse")
@Tag(name = "Comptabilité — Caisse", description = "Journal de caisse et sécurisation")
public class CaisseController {

    private final JournalCaisseRepository caisseRepository;
    private final CaisseMapper caisseMapper;
    private final DomainEventPublisher eventPublisher;
    private final ParametrageCaisseService parametrageCaisseService;

    public CaisseController(JournalCaisseRepository caisseRepository, CaisseMapper caisseMapper,
                          DomainEventPublisher eventPublisher,
                          ParametrageCaisseService parametrageCaisseService) {
        this.caisseRepository = caisseRepository;
        this.caisseMapper = caisseMapper;
        this.eventPublisher = eventPublisher;
        this.parametrageCaisseService = parametrageCaisseService;
    }

    @PostMapping("/ouvrir")
    @PreAuthorize("hasAnyRole('COMMERCIAL', 'SECRETAIRE', 'COMPTABLE', 'ADMIN')")
    public ResponseEntity<ApiResponse<CaisseResponse>> ouvrir(@RequestBody OuvrirCaisseRequest req) {
        LocalDate date = req.date() != null ? req.date() : LocalDate.now();
        if (caisseRepository.existeParDate(date)) {
            throw new RegleMetierException("Un journal de caisse existe déjà pour le " + date);
        }
        // Le journal fige le seuil du jour de son ouverture : un paramétrage ultérieur
        // ne redéfinit pas rétroactivement le moment où la caisse aurait dû être sécurisée.
        double seuil = parametrageCaisseService.parametrageApplicableA(date).getSeuilSecurisationFCFA();
        JournalCaisse journal = JournalCaisse.ouvrirAvecSeuil(date, req.soldeInitial(), seuil);
        return ResponseEntity.status(HttpStatus.CREATED)
            .body(ApiResponse.succes(caisseMapper.versResponse(caisseRepository.sauvegarder(journal)), "Caisse ouverte"));
    }

    @PostMapping("/{date}/entree")
    @PreAuthorize("hasAnyRole('COMMERCIAL', 'SECRETAIRE', 'COMPTABLE', 'ADMIN')")
    public ResponseEntity<ApiResponse<CaisseResponse>> entree(
        @PathVariable @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date,
        @RequestBody MouvementRequest req) {
        JournalCaisse journal = caisseRepository.trouverParDate(date)
            .orElseThrow(() -> EntiteNonTrouveeException.de("Journal caisse", date.toString()));
        journal.enregistrerEntree(req.referenceOperation(), req.montant(), req.motif(), req.matriculeOperateur());
        JournalCaisse sauvegarde = caisseRepository.sauvegarder(journal);
        sauvegarde.getDomainEvents().forEach(eventPublisher::publish);
        sauvegarde.clearDomainEvents();
        return ResponseEntity.ok(ApiResponse.succes(caisseMapper.versResponse(sauvegarde), "Entrée enregistrée"));
    }

    @PostMapping("/{date}/sortie")
    @PreAuthorize("hasAnyRole('COMMERCIAL', 'SECRETAIRE', 'COMPTABLE', 'ADMIN')")
    public ResponseEntity<ApiResponse<CaisseResponse>> sortie(
        @PathVariable @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date,
        @RequestBody MouvementRequest req) {
        JournalCaisse journal = caisseRepository.trouverParDate(date)
            .orElseThrow(() -> EntiteNonTrouveeException.de("Journal caisse", date.toString()));
        journal.enregistrerSortie(req.referenceOperation(), req.montant(), req.motif(), req.matriculeOperateur());
        return ResponseEntity.ok(ApiResponse.succes(caisseMapper.versResponse(caisseRepository.sauvegarder(journal)), "Sortie enregistrée"));
    }

    @GetMapping("/{date}")
    @PreAuthorize("hasAnyRole('COMMERCIAL', 'SECRETAIRE', 'COMPTABLE', 'CONTROLEUR_GENERAL', 'DIRECTEUR_GENERAL', 'ADMIN')")
    public ResponseEntity<ApiResponse<CaisseResponse>> consulter(
        @PathVariable @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date) {
        return caisseRepository.trouverParDate(date)
            .map(j -> ResponseEntity.ok(ApiResponse.succes(caisseMapper.versResponse(j))))
            .orElseThrow(() -> EntiteNonTrouveeException.de("Journal caisse", date.toString()));
    }

    @GetMapping
    @PreAuthorize("hasAnyRole('COMMERCIAL', 'COMPTABLE', 'CONTROLEUR_GENERAL', 'DIRECTEUR_GENERAL', 'ADMIN')")
    public ResponseEntity<ApiResponse<List<CaisseResponse>>> parPeriode(
        @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate debut,
        @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate fin) {
        var j = caisseRepository.listerParPeriode(debut, fin).stream().map(caisseMapper::versResponse).toList();
        return ResponseEntity.ok(ApiResponse.succes(j, j.size() + " journal(aux)"));
    }

    public record OuvrirCaisseRequest(double soldeInitial, LocalDate date) {}
    public record MouvementRequest(String referenceOperation, double montant, String motif, String matriculeOperateur) {}
}
