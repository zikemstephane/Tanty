package com.NTFOODS.Api_tanty.modules.comptabilite.presentation.controller;

import com.NTFOODS.Api_tanty.modules.comptabilite.application.decaissement.dto.response.DecaissementResponse;
import com.NTFOODS.Api_tanty.modules.comptabilite.application.decaissement.mapper.DecaissementMapper;
import com.NTFOODS.Api_tanty.modules.comptabilite.domain.decaissement.aggregate.Decaissement;
import com.NTFOODS.Api_tanty.modules.comptabilite.domain.decaissement.enums.StatutDecaissement;
import com.NTFOODS.Api_tanty.modules.comptabilite.domain.decaissement.enums.TypeDepense;
import com.NTFOODS.Api_tanty.modules.comptabilite.domain.decaissement.repository.DecaissementRepository;
import com.NTFOODS.Api_tanty.shared.application.dto.ApiResponse;
import com.NTFOODS.Api_tanty.shared.kernel.exception.EntiteNonTrouveeException;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.concurrent.atomic.AtomicLong;

@RestController
@RequestMapping("/api/comptabilite/decaissements")
@Tag(name = "Comptabilité — Décaissements", description = "Workflow décaissement 3 étapes : CG propose → DG valide → Secrétaire exécute (§5.11)")
@SecurityRequirement(name = "bearerAuth")
public class DecaissementController {

    private static final AtomicLong COUNTER = new AtomicLong(0);

    private final DecaissementRepository decaissementRepository;
    private final DecaissementMapper decaissementMapper;

    public DecaissementController(DecaissementRepository decaissementRepository, DecaissementMapper decaissementMapper) {
        this.decaissementRepository = decaissementRepository;
        this.decaissementMapper = decaissementMapper;
    }

    @PostMapping
    @PreAuthorize("hasAnyRole('CONTROLEUR_GENERAL', 'ADMIN')")
    @Operation(summary = "Proposer un décaissement (CG)",
        description = "Le Contrôleur Général propose une dépense. Le DG devra la valider avant exécution.")
    public ResponseEntity<ApiResponse<DecaissementResponse>> proposer(@RequestBody ProposerDecaissementRequest req) {
        String num = "DEC-" + DateTimeFormatter.ofPattern("yyyyMMdd").format(req.dateDepense())
            + "-" + String.format("%03d", COUNTER.incrementAndGet());
        Decaissement d = Decaissement.proposer(num, req.typeDepense(), req.matriculeProposant(),
            req.montantFCFA(), req.beneficiaire(), req.dateDepense(), req.motif());
        return ResponseEntity.status(HttpStatus.CREATED)
            .body(ApiResponse.succes(decaissementMapper.versResponse(decaissementRepository.sauvegarder(d)), "Décaissement proposé : " + num));
    }

    @PatchMapping("/{numero}/valider-dg")
    @PreAuthorize("hasAnyRole('DIRECTEUR_GENERAL', 'ADMIN')")
    @Operation(summary = "Valider par le DG (étape 1)",
        description = "Le DG valide la proposition du CG. Séparation des fonctions : le DG ne peut pas valider sa propre proposition.")
    public ResponseEntity<ApiResponse<DecaissementResponse>> validerParDG(@PathVariable String numero,
                                                                            @RequestParam String matriculeDG) {
        Decaissement d = trouverOuErreur(numero);
        d.validerParDG(matriculeDG);
        return ResponseEntity.ok(ApiResponse.succes(decaissementMapper.versResponse(decaissementRepository.sauvegarder(d)), "Décaissement validé par le DG"));
    }

    @PatchMapping("/{numero}/approuver-dg")
    @PreAuthorize("hasAnyRole('DIRECTEUR_GENERAL', 'ADMIN')")
    @Operation(summary = "Approuver par le DG (étape 2)",
        description = "Le DG donne son approbation finale pour l'exécution.")
    public ResponseEntity<ApiResponse<DecaissementResponse>> approuverParDG(@PathVariable String numero,
                                                                              @RequestParam String matriculeDG) {
        Decaissement d = trouverOuErreur(numero);
        d.approuverParDG(matriculeDG);
        return ResponseEntity.ok(ApiResponse.succes(decaissementMapper.versResponse(decaissementRepository.sauvegarder(d)), "Décaissement approuvé par le DG"));
    }

    @PatchMapping("/{numero}/executer")
    @PreAuthorize("hasAnyRole('SECRETAIRE', 'ADMIN')")
    @Operation(summary = "Exécuter le paiement (étape 3)",
        description = "La Secrétaire exécute le paiement après approbation du DG. Séparation des fonctions : le proposant ne peut pas exécuter.")
    public ResponseEntity<ApiResponse<DecaissementResponse>> executer(@PathVariable String numero,
                                                                       @RequestParam String matriculeExecuteur) {
        Decaissement d = trouverOuErreur(numero);
        d.executer(matriculeExecuteur);
        return ResponseEntity.ok(ApiResponse.succes(decaissementMapper.versResponse(decaissementRepository.sauvegarder(d)), "Décaissement exécuté"));
    }

    @PatchMapping("/{numero}/annuler")
    @PreAuthorize("hasAnyRole('DIRECTEUR_GENERAL', 'CONTROLEUR_GENERAL', 'ADMIN')")
    public ResponseEntity<ApiResponse<DecaissementResponse>> annuler(@PathVariable String numero,
                                                                      @RequestParam String motif,
                                                                      @RequestParam String matricule) {
        Decaissement d = trouverOuErreur(numero);
        d.annuler(motif, matricule);
        return ResponseEntity.ok(ApiResponse.succes(decaissementMapper.versResponse(decaissementRepository.sauvegarder(d)), "Décaissement annulé"));
    }

    // Les quatre lectures qui suivent sont gardées par la permission, non par une
    // énumération de rôles. Celle-ci omettait la SECRETAIRE — alors que c'est
    // elle qui **exécute** le paiement à l'étape 3, comme le déclare l'endpoint
    // `/executer` juste au-dessus. Elle devait donc payer un décaissement
    // qu'aucun écran ne pouvait lui montrer : sa page Validation recevait un 403
    // sur chaque chargement de la liste.
    //
    // La permission COMPTABILITE_DECAISSEMENT_CONSULTER couvre exactement les
    // rôles qui figuraient dans la liste, plus la secrétaire à qui elle vient
    // d'être accordée. Les écritures — proposer, valider CG, approuver DG,
    // exécuter, annuler — gardent leurs rôles : la séparation des pouvoirs porte
    // sur les actes, pas sur le fait de regarder ce qu'on doit payer.
    @GetMapping("/{numero}")
    @PreAuthorize("hasAuthority('COMPTABILITE_DECAISSEMENT_CONSULTER')")
    public ResponseEntity<ApiResponse<DecaissementResponse>> consulter(@PathVariable String numero) {
        return ResponseEntity.ok(ApiResponse.succes(decaissementMapper.versResponse(trouverOuErreur(numero)), "Décaissement"));
    }

    @GetMapping
    @PreAuthorize("hasAuthority('COMPTABILITE_DECAISSEMENT_CONSULTER')")
    public ResponseEntity<ApiResponse<List<DecaissementResponse>>> lister() {
        var list = decaissementRepository.listerTous().stream().map(decaissementMapper::versResponse).toList();
        return ResponseEntity.ok(ApiResponse.succes(list, list.size() + " décaissement(s)"));
    }

    @GetMapping("/statut/{statut}")
    @PreAuthorize("hasAuthority('COMPTABILITE_DECAISSEMENT_CONSULTER')")
    @Operation(summary = "Lister les décaissements par statut")
    public ResponseEntity<ApiResponse<List<DecaissementResponse>>> listerParStatut(@PathVariable StatutDecaissement statut) {
        var list = decaissementRepository.listerParStatut(statut).stream().map(decaissementMapper::versResponse).toList();
        return ResponseEntity.ok(ApiResponse.succes(list, list.size() + " décaissement(s)"));
    }

    @GetMapping("/periode")
    @PreAuthorize("hasAuthority('COMPTABILITE_DECAISSEMENT_CONSULTER')")
    public ResponseEntity<ApiResponse<List<DecaissementResponse>>> listerParPeriode(
        @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate debut,
        @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate fin) {
        var list = decaissementRepository.listerParPeriode(debut, fin).stream().map(decaissementMapper::versResponse).toList();
        return ResponseEntity.ok(ApiResponse.succes(list, list.size() + " décaissement(s)"));
    }

    private Decaissement trouverOuErreur(String numero) {
        return decaissementRepository.trouverParNumero(numero)
            .orElseThrow(() -> EntiteNonTrouveeException.de("Decaissement", numero));
    }

    public record ProposerDecaissementRequest(TypeDepense typeDepense, String matriculeProposant,
                                               double montantFCFA, String beneficiaire,
                                               LocalDate dateDepense, String motif) {}
}
