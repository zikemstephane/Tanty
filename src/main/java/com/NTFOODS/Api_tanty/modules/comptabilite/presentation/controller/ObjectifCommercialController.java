package com.NTFOODS.Api_tanty.modules.comptabilite.presentation.controller;

import com.NTFOODS.Api_tanty.modules.comptabilite.application.objectif.dto.request.DefinirObjectifRequest;
import com.NTFOODS.Api_tanty.modules.comptabilite.application.objectif.dto.response.ObjectifCommercialResponse;
import com.NTFOODS.Api_tanty.modules.comptabilite.application.objectif.mapper.ObjectifCommercialMapper;
import com.NTFOODS.Api_tanty.modules.comptabilite.application.objectif.usecase.DefinirObjectifUseCase;
import com.NTFOODS.Api_tanty.modules.comptabilite.domain.objectif.repository.ObjectifCommercialRepository;
import com.NTFOODS.Api_tanty.shared.application.dto.ApiResponse;
import com.NTFOODS.Api_tanty.shared.infrastructure.security.AuthenticatedUserResolver;
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

/**
 * Contrôleur REST — Objectifs Commerciaux (base de la Règle d'or des Primes 80/75).
 */
@RestController
@RequestMapping("/api/comptabilite/objectifs")
@Tag(name = "Comptabilité — Objectifs commerciaux", description = "Objectifs hebdomadaires global + par gamme")
@SecurityRequirement(name = "bearerAuth")
public class ObjectifCommercialController {

    private final DefinirObjectifUseCase definirObjectifUseCase;
    private final ObjectifCommercialRepository repository;
    private final ObjectifCommercialMapper mapper;
    private final AuthenticatedUserResolver userResolver;

    public ObjectifCommercialController(DefinirObjectifUseCase definirObjectifUseCase,
                                         ObjectifCommercialRepository repository,
                                         ObjectifCommercialMapper mapper,
                                         AuthenticatedUserResolver userResolver) {
        this.definirObjectifUseCase = definirObjectifUseCase;
        this.repository = repository;
        this.mapper = mapper;
        this.userResolver = userResolver;
    }

    @PostMapping
    @PreAuthorize("hasAuthority('COMPTABILITE_OBJECTIF_GERER')")
    @Operation(summary = "Définir / réviser les objectifs hebdomadaires (global + par gamme) d'un commercial",
        description = "Une redéfinition sur une semaine déjà pourvue est une révision : le motif devient " +
                      "obligatoire et la valeur remplacée est inscrite au journal de l'objectif. " +
                      "Le garde précédent autorisait le rôle COMMERCIAL — qui ne doit pas fixer ses propres " +
                      "objectifs — et excluait la Chargée RP, qui en a la charge.")
    public ResponseEntity<ApiResponse<ObjectifCommercialResponse>> definir(@Valid @RequestBody DefinirObjectifRequest request) {
        var reponse = definirObjectifUseCase.executer(request, userResolver.getUtilisateurCourant());
        return ResponseEntity.status(HttpStatus.CREATED).body(ApiResponse.succes(reponse, "Objectif défini"));
    }

    @GetMapping("/commercial/{matricule}")
    @PreAuthorize("isAuthenticated()")
    @Operation(summary = "Historique des objectifs d'un commercial")
    public ResponseEntity<ApiResponse<List<ObjectifCommercialResponse>>> parCommercial(@PathVariable String matricule) {
        var r = repository.listerParCommercial(matricule).stream().map(mapper::versResponse).toList();
        return ResponseEntity.ok(ApiResponse.succes(r, r.size() + " objectif(s)"));
    }

    @GetMapping("/semaine")
    @PreAuthorize("hasAnyAuthority('COMPTABILITE_OBJECTIF_CONSULTER','COMPTABILITE_OBJECTIF_GERER')")
    @Operation(summary = "Objectifs de tous les commerciaux pour une semaine donnée")
    public ResponseEntity<ApiResponse<List<ObjectifCommercialResponse>>> parSemaine(
        @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate semaineDebut) {
        var r = repository.listerParSemaine(semaineDebut).stream().map(mapper::versResponse).toList();
        return ResponseEntity.ok(ApiResponse.succes(r, r.size() + " objectif(s)"));
    }
}
