package com.NTFOODS.Api_tanty.modules.stock.presentation.controller;

import com.NTFOODS.Api_tanty.modules.stock.application.sessioncommercial.dto.response.SessionCommercialResponse;
import com.NTFOODS.Api_tanty.modules.stock.application.sessioncommercial.mapper.SessionCommercialMapper;
import com.NTFOODS.Api_tanty.modules.stock.domain.sessioncommercial.model.SessionCommercial;
import com.NTFOODS.Api_tanty.modules.stock.domain.sessioncommercial.repository.SessionCommercialRepository;
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
@RequestMapping("/api/stock/sessions")
@Tag(name = "Stock — Sessions Commerciaux", description = "Journées des commerciaux : préparation la veille, dotations multiples, clôture sur validation")
@SecurityRequirement(name = "bearerAuth")
public class SessionCommercialController {

    private static final AtomicLong COUNTER = new AtomicLong(0);

    private final SessionCommercialRepository repository;
    private final SessionCommercialMapper mapper;

    public SessionCommercialController(SessionCommercialRepository repository, SessionCommercialMapper mapper) {
        this.repository = repository;
        this.mapper = mapper;
    }

    @PostMapping("/preparer")
    @PreAuthorize("hasAnyRole('GESTIONNAIRE_STOCK', 'ADMIN')")
    @Operation(summary = "Préparer une session pour le lendemain (le commercial prend ses produits à la veille)")
    public ResponseEntity<ApiResponse<SessionCommercialResponse>> preparer(@RequestBody PreparerSessionRequest req) {
        String num = "SESS-" + DateTimeFormatter.ofPattern("yyyyMMdd").format(req.datePreparation())
            + "-" + req.matriculeCommercial().replace("MAT-COM-", "");
        SessionCommercial session = SessionCommercial.preparer(num, req.matriculeCommercial(), req.datePreparation());
        return ResponseEntity.status(HttpStatus.CREATED)
            .body(ApiResponse.succes(mapper.versResponse(repository.sauvegarder(session)), "Session préparée : " + num));
    }

    @PostMapping("/{numero}/lier-dotation")
    @PreAuthorize("hasAnyRole('GESTIONNAIRE_STOCK', 'ADMIN')")
    @Operation(summary = "Lier une dotation à la session (plusieurs dotations possibles par session)")
    public ResponseEntity<ApiResponse<SessionCommercialResponse>> lierDotation(@PathVariable String numero,
                                                                                @RequestParam String dotationId) {
        SessionCommercial session = trouverOuErreur(numero);
        session.lierDotation(dotationId);
        return ResponseEntity.ok(ApiResponse.succes(mapper.versResponse(repository.sauvegarder(session)), "Dotation liée à la session"));
    }

    @PatchMapping("/{numero}/ouvrir")
    @PreAuthorize("hasAnyRole('COMMERCIAL', 'ADMIN')")
    @Operation(summary = "Ouvrir la session (le lendemain, le commercial ouvre sa journée)")
    public ResponseEntity<ApiResponse<SessionCommercialResponse>> ouvrir(@PathVariable String numero) {
        SessionCommercial session = trouverOuErreur(numero);
        session.ouvrir();
        return ResponseEntity.ok(ApiResponse.succes(mapper.versResponse(repository.sauvegarder(session)), "Session ouverte"));
    }

    @PatchMapping("/{numero}/cloturer")
    @PreAuthorize("hasAnyRole('COMMERCIAL', 'ADMIN')")
    @Operation(summary = "Clôturer la session par le commercial")
    public ResponseEntity<ApiResponse<SessionCommercialResponse>> cloturer(@PathVariable String numero,
                                                                            @RequestParam String matricule) {
        SessionCommercial session = trouverOuErreur(numero);
        session.cloturer(matricule);
        return ResponseEntity.ok(ApiResponse.succes(mapper.versResponse(repository.sauvegarder(session)), "Session clôturée"));
    }

    @PatchMapping("/{numero}/valider-secretaire")
    @PreAuthorize("hasAnyRole('SECRETAIRE', 'ADMIN')")
    @Operation(summary = "Validation par la secrétaire")
    public ResponseEntity<ApiResponse<SessionCommercialResponse>> validerSecretaire(@PathVariable String numero,
                                                                                     @RequestParam String matricule) {
        SessionCommercial session = trouverOuErreur(numero);
        session.validerParSecretaire(matricule);
        return ResponseEntity.ok(ApiResponse.succes(mapper.versResponse(repository.sauvegarder(session)), "Validée par la secrétaire"));
    }

    @PatchMapping("/{numero}/valider-comptable")
    @PreAuthorize("hasAnyRole('COMPTABLE', 'ADMIN')")
    @Operation(summary = "Validation par le comptable (session terminée)")
    public ResponseEntity<ApiResponse<SessionCommercialResponse>> validerComptable(@PathVariable String numero,
                                                                                     @RequestParam String matricule) {
        SessionCommercial session = trouverOuErreur(numero);
        session.validerParComptable(matricule);
        return ResponseEntity.ok(ApiResponse.succes(mapper.versResponse(repository.sauvegarder(session)), "Session terminée"));
    }

    @GetMapping("/{numero}")
    @PreAuthorize("hasAnyRole('GESTIONNAIRE_STOCK', 'DIRECTEUR_GENERAL', 'CONTROLEUR_GENERAL', 'COMMERCIAL', 'ADMIN')")
    @Operation(summary = "Consulter une session")
    public ResponseEntity<ApiResponse<SessionCommercialResponse>> consulter(@PathVariable String numero) {
        return ResponseEntity.ok(ApiResponse.succes(mapper.versResponse(trouverOuErreur(numero)), "Session"));
    }

    @GetMapping("/commercial/{matricule}")
    @PreAuthorize("hasAnyRole('GESTIONNAIRE_STOCK', 'DIRECTEUR_GENERAL', 'CONTROLEUR_GENERAL', 'COMMERCIAL', 'ADMIN')")
    @Operation(summary = "Lister les sessions d'un commercial")
    public ResponseEntity<ApiResponse<List<SessionCommercialResponse>>> listerParCommercial(@PathVariable String matricule) {
        var list = mapper.versResponseList(repository.listerParCommercial(matricule));
        return ResponseEntity.ok(ApiResponse.succes(list, list.size() + " session(s)"));
    }

    @GetMapping("/date/{date}")
    @PreAuthorize("hasAnyRole('COMMERCIAL', 'GESTIONNAIRE_STOCK', 'DIRECTEUR_GENERAL', 'CONTROLEUR_GENERAL', 'ADMIN')")
    @Operation(summary = "Lister les sessions par date d'effet")
    public ResponseEntity<ApiResponse<List<SessionCommercialResponse>>> listerParDate(@PathVariable String date) {
        var list = mapper.versResponseList(repository.listerParDateEffet(LocalDate.parse(date)));
        return ResponseEntity.ok(ApiResponse.succes(list, list.size() + " session(s)"));
    }

    @GetMapping("/statut/{statut}")
    @PreAuthorize("hasAnyRole('COMMERCIAL', 'GESTIONNAIRE_STOCK', 'DIRECTEUR_GENERAL', 'CONTROLEUR_GENERAL', 'ADMIN')")
    @Operation(summary = "Lister les sessions par statut")
    public ResponseEntity<ApiResponse<List<SessionCommercialResponse>>> listerParStatut(@PathVariable String statut) {
        var list = mapper.versResponseList(repository.listerParStatut(statut));
        return ResponseEntity.ok(ApiResponse.succes(list, list.size() + " session(s)"));
    }

    private SessionCommercial trouverOuErreur(String numero) {
        return repository.trouverParNumero(numero)
            .orElseThrow(() -> EntiteNonTrouveeException.de("SessionCommercial", numero));
    }

    public record PreparerSessionRequest(String matriculeCommercial, LocalDate datePreparation) {}
}
