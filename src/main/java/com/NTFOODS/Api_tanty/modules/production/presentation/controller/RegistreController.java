package com.NTFOODS.Api_tanty.modules.production.presentation.controller;

import com.NTFOODS.Api_tanty.modules.production.application.registre.dto.request.EnregistrerRegistreRequest;
import com.NTFOODS.Api_tanty.modules.production.application.registre.dto.request.ModifierRegistreRequest;
import com.NTFOODS.Api_tanty.modules.production.application.registre.dto.response.RegistreProductionResponse;
import com.NTFOODS.Api_tanty.modules.production.application.registre.mapper.RegistreMapper;
import com.NTFOODS.Api_tanty.modules.production.application.registre.usecase.EnregistrerRegistreUseCase;
import com.NTFOODS.Api_tanty.modules.production.application.registre.usecase.ModifierRegistreUseCase;
import com.NTFOODS.Api_tanty.modules.production.domain.registre.repository.RegistreProductionRepository;
import com.NTFOODS.Api_tanty.shared.application.dto.ApiResponse;
import com.NTFOODS.Api_tanty.shared.infrastructure.security.AuthenticatedUserResolver;
import com.NTFOODS.Api_tanty.shared.kernel.exception.EntiteNonTrouveeException;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/api/production/registres")
@Tag(name = "Production — Registre", description = "Registre de production journalier (présences et rendements employés)")
@SecurityRequirement(name = "bearerAuth")
public class RegistreController {

    private final EnregistrerRegistreUseCase enregistrerUseCase;
    private final ModifierRegistreUseCase modifierUseCase;
    private final RegistreProductionRepository repository;
    private final RegistreMapper mapper;
    private final AuthenticatedUserResolver userResolver;

    public RegistreController(EnregistrerRegistreUseCase enregistrerUseCase,
                               ModifierRegistreUseCase modifierUseCase,
                               RegistreProductionRepository repository,
                               RegistreMapper mapper,
                               AuthenticatedUserResolver userResolver) {
        this.enregistrerUseCase = enregistrerUseCase;
        this.modifierUseCase = modifierUseCase;
        this.repository = repository;
        this.mapper = mapper;
        this.userResolver = userResolver;
    }

    @PostMapping
    @PreAuthorize("hasAuthority('PRODUCTION_REGISTRE_CONSULTER')")
    @Operation(summary = "Enregistrer le registre de production journalier")
    public ResponseEntity<ApiResponse<RegistreProductionResponse>> enregistrer(
            @RequestBody @Valid EnregistrerRegistreRequest req) {
        RegistreProductionResponse response = enregistrerUseCase.executer(req, userResolver.getUtilisateurCourant());
        return ResponseEntity.status(HttpStatus.CREATED)
            .body(ApiResponse.succes(response, "Registre enregistré"));
    }

    @PatchMapping("/pph/{referencePPH}/date/{date}")
    @PreAuthorize("hasAuthority('PRODUCTION_REGISTRE_CONSULTER')")
    @Operation(summary = "Corriger le registre du jour",
        description = "Refusé si la date du registre n'est plus la journée en cours : passé ce délai, il devient définitif.")
    public ResponseEntity<ApiResponse<RegistreProductionResponse>> modifier(
            @PathVariable String referencePPH,
            @PathVariable @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date,
            @RequestBody @Valid ModifierRegistreRequest req) {
        RegistreProductionResponse response = modifierUseCase.executer(referencePPH, date, req);
        return ResponseEntity.ok(ApiResponse.succes(response, "Registre corrigé"));
    }

    @GetMapping
    @PreAuthorize("hasAuthority('PRODUCTION_REGISTRE_CONSULTER')")
    @Operation(summary = "Lister les registres (par PPH ou par période)")
    public ResponseEntity<ApiResponse<List<RegistreProductionResponse>>> lister(
            @RequestParam(required = false) String referencePPH,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate debut,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate fin) {
        List<RegistreProductionResponse> list;
        if (referencePPH != null) {
            list = repository.listerParPPH(referencePPH).stream().map(mapper::versResponse).toList();
        } else if (debut != null && fin != null) {
            list = repository.listerParPeriode(debut, fin).stream().map(mapper::versResponse).toList();
        } else {
            LocalDate today = LocalDate.now();
            list = repository.listerParPeriode(today.minusDays(30), today).stream().map(mapper::versResponse).toList();
        }
        return ResponseEntity.ok(ApiResponse.succes(list, list.size() + " registre(s)"));
    }

    @GetMapping("/pph/{referencePPH}/date/{date}")
    @PreAuthorize("hasAuthority('PRODUCTION_REGISTRE_CONSULTER')")
    @Operation(summary = "Consulter un registre par PPH et date")
    public ResponseEntity<ApiResponse<RegistreProductionResponse>> consulter(
            @PathVariable String referencePPH,
            @PathVariable @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date) {
        return repository.trouverParPPHEtDate(referencePPH, date)
            .map(r -> ResponseEntity.ok(ApiResponse.succes(mapper.versResponse(r), "Registre trouvé")))
            .orElseThrow(() -> EntiteNonTrouveeException.de("Registre", referencePPH + " / " + date));
    }
}
