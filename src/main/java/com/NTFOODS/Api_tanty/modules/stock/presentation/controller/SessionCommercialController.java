package com.NTFOODS.Api_tanty.modules.stock.presentation.controller;

import com.NTFOODS.Api_tanty.modules.stock.application.sessioncommercial.dto.response.SessionCommercialResponse;
import com.NTFOODS.Api_tanty.modules.stock.application.sessioncommercial.mapper.SessionCommercialMapper;
import com.NTFOODS.Api_tanty.modules.stock.application.sessioncommercial.usecase.DeclarerFinTourneeUseCase;
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
    private final DeclarerFinTourneeUseCase declarerFinTourneeUseCase;

    public SessionCommercialController(SessionCommercialRepository repository,
                                        SessionCommercialMapper mapper,
                                        DeclarerFinTourneeUseCase declarerFinTourneeUseCase) {
        this.repository = repository;
        this.mapper = mapper;
        this.declarerFinTourneeUseCase = declarerFinTourneeUseCase;
    }

    @PostMapping("/preparer")
    @PreAuthorize("hasAuthority('STOCK_DOTATION_GERER')")
    @Operation(summary = "Préparer une session pour le lendemain (le commercial prend ses produits à la veille)")
    public ResponseEntity<ApiResponse<SessionCommercialResponse>> preparer(
        @jakarta.validation.Valid @RequestBody PreparerSessionRequest req) {
        String num = "SESS-" + DateTimeFormatter.ofPattern("yyyyMMdd").format(req.datePreparation())
            + "-" + req.matriculeCommercial().replace("MAT-COM-", "");
        SessionCommercial session = SessionCommercial.preparer(num, req.matriculeCommercial(), req.datePreparation());
        return ResponseEntity.status(HttpStatus.CREATED)
            .body(ApiResponse.succes(mapper.versResponse(repository.sauvegarder(session)), "Session préparée : " + num));
    }

    @PostMapping("/{numero}/lier-dotation")
    @PreAuthorize("hasAuthority('STOCK_DOTATION_GERER')")
    @Operation(summary = "Lier une dotation à la session (plusieurs dotations possibles par session)")
    public ResponseEntity<ApiResponse<SessionCommercialResponse>> lierDotation(@PathVariable String numero,
                                                                                @RequestParam String dotationId) {
        SessionCommercial session = trouverOuErreur(numero);
        session.lierDotation(dotationId);
        return ResponseEntity.ok(ApiResponse.succes(mapper.versResponse(repository.sauvegarder(session)), "Dotation liée à la session"));
    }

    @PatchMapping("/{numero}/ouvrir")
    @PreAuthorize("hasAuthority('STOCK_SESSION_OUVRIR')")
    @Operation(summary = "Ouvrir la session (le lendemain, le commercial ouvre sa journée)",
        description = "Acte du commercial, symétrique de la déclaration de fin de tournée. " +
                      "Le gestionnaire, qui prépare la session la veille, peut également l'ouvrir.")
    public ResponseEntity<ApiResponse<SessionCommercialResponse>> ouvrir(@PathVariable String numero) {
        SessionCommercial session = trouverOuErreur(numero);
        session.ouvrir();
        return ResponseEntity.ok(ApiResponse.succes(mapper.versResponse(repository.sauvegarder(session)), "Session ouverte"));
    }

    @PatchMapping("/{numero}/declarer-fin-tournee")
    @PreAuthorize("hasAuthority('STOCK_SESSION_DECLARER_FIN_TOURNEE')")
    @Operation(summary = "Déclarer la fin de sa tournée",
        description = "Acte du commercial : il signale que sa journée est terminée et soumet sa " +
                      "session au contrôle du siège. La session passe à CLOTUREE et attend le " +
                      "rapprochement de la secrétaire, puis la validation du comptable. Le " +
                      "commercial ne valide jamais lui-même la fin du cycle.")
    public ResponseEntity<ApiResponse<SessionCommercialResponse>> declarerFinTournee(@PathVariable String numero,
                                                                                      @RequestParam String matricule) {
        // La clôture ferme aussi la tournée, qui n'a plus d'état propre : le use
        // case vérifie qu'aucun client ne reste sans constat avant de la laisser
        // partir au siège.
        SessionCommercial session = declarerFinTourneeUseCase.executer(numero, matricule);
        return ResponseEntity.ok(ApiResponse.succes(mapper.versResponse(session),
            "Fin de tournée déclarée — en attente du rapprochement de la secrétaire"));
    }

    @PatchMapping("/{numero}/valider-secretaire")
    @PreAuthorize("hasAuthority('STOCK_SESSION_VALIDER_SECRETAIRE')")
    @Operation(summary = "Rapprochement et validation par la secrétaire",
        description = "Intervient après la déclaration de fin de tournée : la secrétaire a rapproché " +
                      "la remise d'espèces du montant attendu avant de valider.")
    public ResponseEntity<ApiResponse<SessionCommercialResponse>> validerSecretaire(@PathVariable String numero,
                                                                                     @RequestParam String matricule) {
        SessionCommercial session = trouverOuErreur(numero);
        session.validerParSecretaire(matricule);
        return ResponseEntity.ok(ApiResponse.succes(mapper.versResponse(repository.sauvegarder(session)), "Validée par la secrétaire"));
    }

    @PatchMapping("/{numero}/valider-comptable")
    @PreAuthorize("hasAuthority('STOCK_SESSION_VALIDER_COMPTABLE')")
    @Operation(summary = "Validation par le comptable (session terminée)")
    public ResponseEntity<ApiResponse<SessionCommercialResponse>> validerComptable(@PathVariable String numero,
                                                                                     @RequestParam String matricule) {
        SessionCommercial session = trouverOuErreur(numero);
        session.validerParComptable(matricule);
        return ResponseEntity.ok(ApiResponse.succes(mapper.versResponse(repository.sauvegarder(session)), "Session terminée"));
    }

    @GetMapping("/{numero}")
    @PreAuthorize("hasAuthority('STOCK_NIVEAU_CONSULTER')")
    @Operation(summary = "Consulter une session")
    public ResponseEntity<ApiResponse<SessionCommercialResponse>> consulter(@PathVariable String numero) {
        return ResponseEntity.ok(ApiResponse.succes(mapper.versResponse(trouverOuErreur(numero)), "Session"));
    }

    @GetMapping("/commercial/{matricule}")
    @PreAuthorize("hasAuthority('STOCK_NIVEAU_CONSULTER')")
    @Operation(summary = "Lister les sessions d'un commercial")
    public ResponseEntity<ApiResponse<List<SessionCommercialResponse>>> listerParCommercial(@PathVariable String matricule) {
        var list = mapper.versResponseList(repository.listerParCommercial(matricule));
        return ResponseEntity.ok(ApiResponse.succes(list, list.size() + " session(s)"));
    }

    @GetMapping("/date/{date}")
    @PreAuthorize("hasAuthority('STOCK_NIVEAU_CONSULTER')")
    @Operation(summary = "Lister les sessions par date d'effet")
    public ResponseEntity<ApiResponse<List<SessionCommercialResponse>>> listerParDate(@PathVariable String date) {
        var list = mapper.versResponseList(repository.listerParDateEffet(LocalDate.parse(date)));
        return ResponseEntity.ok(ApiResponse.succes(list, list.size() + " session(s)"));
    }

    @GetMapping("/statut/{statut}")
    @PreAuthorize("hasAuthority('STOCK_NIVEAU_CONSULTER')")
    @Operation(summary = "Lister les sessions par statut")
    public ResponseEntity<ApiResponse<List<SessionCommercialResponse>>> listerParStatut(@PathVariable String statut) {
        var list = mapper.versResponseList(repository.listerParStatut(statut));
        return ResponseEntity.ok(ApiResponse.succes(list, list.size() + " session(s)"));
    }

    private SessionCommercial trouverOuErreur(String numero) {
        return repository.trouverParNumero(numero)
            .orElseThrow(() -> EntiteNonTrouveeException.de("SessionCommercial", numero));
    }

    /**
     * Les deux champs sont requis. Sans validation, une date absente traversait
     * jusqu'au formateur et ressortait en 500 « temporal » — une erreur de
     * saisie présentée comme une panne du serveur.
     */
    public record PreparerSessionRequest(
        @jakarta.validation.constraints.NotBlank(message = "Le matricule du commercial est obligatoire")
        String matriculeCommercial,
        @jakarta.validation.constraints.NotNull(message = "La date de préparation est obligatoire")
        LocalDate datePreparation) {}
}
