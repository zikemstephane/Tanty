package com.NTFOODS.Api_tanty.modules.comptabilite.presentation.controller;

import com.NTFOODS.Api_tanty.modules.comptabilite.application.prime.dto.response.PrimeResponse;
import com.NTFOODS.Api_tanty.modules.comptabilite.application.prime.mapper.PrimeMapper;
import com.NTFOODS.Api_tanty.modules.comptabilite.application.prime.service.BaremePrimeService;
import com.NTFOODS.Api_tanty.modules.comptabilite.domain.prime.aggregate.BaremePrime;
import com.NTFOODS.Api_tanty.modules.comptabilite.domain.objectif.aggregate.ObjectifCommercial;
import com.NTFOODS.Api_tanty.modules.comptabilite.domain.objectif.repository.ObjectifCommercialRepository;
import com.NTFOODS.Api_tanty.modules.comptabilite.domain.prime.aggregate.PrimeCommerciale;
import com.NTFOODS.Api_tanty.modules.comptabilite.domain.prime.repository.PrimeRepository;
import com.NTFOODS.Api_tanty.shared.application.dto.ApiResponse;
import com.NTFOODS.Api_tanty.shared.kernel.event.DomainEventPublisher;
import com.NTFOODS.Api_tanty.shared.kernel.exception.EntiteNonTrouveeException;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;

/**
 * Contrôleur REST — Primes Commerciales (Règle d'or 80/75).
 *
 * Le calcul lit automatiquement l'objectif défini par le DG / Contrôleur
 * Général pour la semaine ({@link ObjectifCommercialRepository}) et le
 * compare au chiffre d'affaires réellement réalisé (fourni ici par
 * l'appelant — typiquement une agrégation des ventes de la semaine, calculée
 * en amont par le module Commercial ou un job de reporting).
 */
@RestController
@RequestMapping("/api/comptabilite/primes")
@Tag(name = "Comptabilité — Primes", description = "Primes commerciales hebdomadaires — Règle d'or 80/75")
public class PrimeController {

    private final PrimeRepository primeRepository;
    private final ObjectifCommercialRepository objectifCommercialRepository;
    private final PrimeMapper primeMapper;
    private final DomainEventPublisher eventPublisher;
    private final BaremePrimeService baremePrimeService;

    public PrimeController(PrimeRepository primeRepository,
                            ObjectifCommercialRepository objectifCommercialRepository,
                            PrimeMapper primeMapper, DomainEventPublisher eventPublisher,
                            BaremePrimeService baremePrimeService) {
        this.primeRepository = primeRepository;
        this.objectifCommercialRepository = objectifCommercialRepository;
        this.primeMapper = primeMapper;
        this.eventPublisher = eventPublisher;
        this.baremePrimeService = baremePrimeService;
    }

    @PostMapping("/calculer")
    @PreAuthorize("hasAnyRole('COMPTABLE', 'DIRECTEUR_GENERAL', 'ADMIN')")
    @Operation(summary = "Calculer la prime hebdomadaire d'un commercial",
        description = "Le montant est dérivé automatiquement, jamais fourni par l'appelant. Les seuils " +
                      "d'éligibilité, le montant par gamme et le plafond proviennent du barème en vigueur " +
                      "la semaine concernée : recalculer une semaine passée redonne donc le montant d'origine, " +
                      "même si le barème a changé depuis.")
    public ResponseEntity<ApiResponse<PrimeResponse>> calculer(@RequestBody CalculerPrimeRequest req) {
        ObjectifCommercial objectif = objectifCommercialRepository
            .trouver(req.matriculeCommercial(), req.semaineDebut())
            .orElseThrow(() -> EntiteNonTrouveeException.de("Objectif commercial",
                req.matriculeCommercial() + " / semaine du " + req.semaineDebut()));

        String reference = primeRepository.genererReference(req.matriculeCommercial(), req.semaineDebut());
        BaremePrime bareme = baremePrimeService.baremeApplicableA(objectif.getSemaineDebut());

        PrimeCommerciale prime = PrimeCommerciale.calculer(reference, req.matriculeCommercial(),
            objectif.getSemaineDebut(), objectif.getSemaineFin(), objectif.getObjectifGlobalFCFA(),
            req.totalVentesGlobalFCFA(), objectif.getObjectifsParGammeFCFA(), req.realisationsParGammeFCFA(),
            bareme);

        return ResponseEntity.status(HttpStatus.CREATED)
            .body(ApiResponse.succes(primeMapper.versResponse(primeRepository.sauvegarder(prime)),
                "Prime calculée selon le barème " + bareme.getReference()));
    }

    @PatchMapping("/{reference}/valider")
    @PreAuthorize("hasAnyRole('COMPTABLE', 'DIRECTEUR_GENERAL', 'ADMIN')")
    @Operation(summary = "Valider une prime calculée")
    public ResponseEntity<ApiResponse<PrimeResponse>> valider(@PathVariable String reference,
                                                              @RequestParam String matriculeValidateur) {
        PrimeCommerciale prime = primeRepository.trouverParReference(reference)
            .orElseThrow(() -> EntiteNonTrouveeException.de("Prime", reference));
        prime.valider(matriculeValidateur);
        PrimeCommerciale sauvegarde = primeRepository.sauvegarder(prime);
        sauvegarde.getDomainEvents().forEach(eventPublisher::publish);
        sauvegarde.clearDomainEvents();
        return ResponseEntity.ok(ApiResponse.succes(primeMapper.versResponse(sauvegarde), "Prime validée"));
    }

    @PatchMapping("/{reference}/verser")
    @PreAuthorize("hasAnyRole('COMPTABLE', 'ADMIN')")
    @Operation(summary = "Marquer une prime comme versée (impossible si non éligible)")
    public ResponseEntity<ApiResponse<PrimeResponse>> marquerVersee(@PathVariable String reference) {
        PrimeCommerciale prime = primeRepository.trouverParReference(reference)
            .orElseThrow(() -> EntiteNonTrouveeException.de("Prime", reference));
        prime.marquerVersee();
        return ResponseEntity.ok(ApiResponse.succes(primeMapper.versResponse(primeRepository.sauvegarder(prime)), "Prime versée"));
    }

    // Les trois lectures ci-dessous sont gardées par la permission, non par une
    // liste de rôles. Elles énuméraient DIRECTEUR_GENERAL, COMPTABLE, COMMERCIAL
    // — et oubliaient la Chargée RP, à qui la matrice accorde pourtant
    // COMPTABILITE_PRIME_CONSULTER *et* COMPTABILITE_PRIME_GERER, puisqu'elle
    // tient le rôle de Directeur Commercial et pilote objectifs et primes. Elle
    // pouvait donc administrer le barème des primes sans jamais voir une seule
    // prime : son écran Objectifs recevait un 403 sur chaque chargement.
    //
    // La permission est déjà la source de vérité de ce système ; la liste de
    // rôles en était une copie divergente. Aucun accès n'est ouvert au passage :
    // COMPTABILITE_PRIME_CONSULTER est détenue par le Commercial, le Comptable,
    // le Contrôleur Général, la Chargée RP, le DG et l'ADMIN. Les écritures
    // (calcul, validation, versement) restent délibérément sur leurs rôles :
    // verser une prime est un décaissement, pas une consultation.
    @GetMapping("/{reference}")
    @PreAuthorize("hasAuthority('COMPTABILITE_PRIME_CONSULTER')")
    @Operation(summary = "Consulter une prime par référence")
    public ResponseEntity<ApiResponse<PrimeResponse>> consulter(@PathVariable String reference) {
        PrimeCommerciale prime = primeRepository.trouverParReference(reference)
            .orElseThrow(() -> EntiteNonTrouveeException.de("Prime", reference));
        return ResponseEntity.ok(ApiResponse.succes(primeMapper.versResponse(prime), "Prime"));
    }

    @GetMapping("/commercial/{matricule}")
    @PreAuthorize("hasAuthority('COMPTABILITE_PRIME_CONSULTER')")
    @Operation(summary = "Historique des primes d'un commercial")
    public ResponseEntity<ApiResponse<List<PrimeResponse>>> parCommercial(@PathVariable String matricule) {
        var p = primeRepository.listerParCommercial(matricule).stream().map(primeMapper::versResponse).toList();
        return ResponseEntity.ok(ApiResponse.succes(p, p.size() + " prime(s)"));
    }

    @GetMapping("/semaine")
    @PreAuthorize("hasAuthority('COMPTABILITE_PRIME_CONSULTER')")
    @Operation(summary = "Primes de tous les commerciaux pour une semaine donnée")
    public ResponseEntity<ApiResponse<List<PrimeResponse>>> parSemaine(
        @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate semaineDebut) {
        var p = primeRepository.listerParSemaine(semaineDebut).stream().map(primeMapper::versResponse).toList();
        return ResponseEntity.ok(ApiResponse.succes(p, p.size() + " prime(s)"));
    }

    public record CalculerPrimeRequest(String matriculeCommercial, LocalDate semaineDebut,
                                       double totalVentesGlobalFCFA, Map<String, Double> realisationsParGammeFCFA) {}
}
