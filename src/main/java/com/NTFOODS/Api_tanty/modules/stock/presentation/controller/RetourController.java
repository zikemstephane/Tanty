package com.NTFOODS.Api_tanty.modules.stock.presentation.controller;

import com.NTFOODS.Api_tanty.modules.stock.application.retour.dto.request.EnregistrerRetourRequest;
import com.NTFOODS.Api_tanty.modules.stock.application.retour.dto.response.RetourResponse;
import com.NTFOODS.Api_tanty.modules.stock.application.retour.usecase.enregistrer.EnregistrerRetourUseCase;
import com.NTFOODS.Api_tanty.modules.stock.application.retour.usecase.lister.ListerRetoursUseCase;
import com.NTFOODS.Api_tanty.shared.application.dto.ApiResponse;
import com.NTFOODS.Api_tanty.shared.infrastructure.security.AuthenticatedUserResolver;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/stock/retours")
@Tag(name = "Stock — Retours", description = "Retours d'invendus (recrédit tampon / débit mobile)")
@SecurityRequirement(name = "bearerAuth")
public class RetourController {

    private final EnregistrerRetourUseCase enregistrerRetourUseCase;
    private final ListerRetoursUseCase listerRetoursUseCase;
    private final AuthenticatedUserResolver userResolver;

    public RetourController(EnregistrerRetourUseCase enregistrerRetourUseCase,
                          ListerRetoursUseCase listerRetoursUseCase,
                          AuthenticatedUserResolver userResolver) {
        this.enregistrerRetourUseCase = enregistrerRetourUseCase;
        this.listerRetoursUseCase = listerRetoursUseCase;
        this.userResolver = userResolver;
    }

    @PostMapping
    @PreAuthorize("hasAnyRole('COMMERCIAL', 'GESTIONNAIRE_STOCK', 'ADMIN')")
    @Operation(summary = "Enregistrer un retour d'invendus")
    public ResponseEntity<ApiResponse<RetourResponse>> enregistrer(@Valid @RequestBody EnregistrerRetourRequest request) {
        RetourResponse response = enregistrerRetourUseCase.executer(request, userResolver.getUtilisateurCourant());
        return ResponseEntity.status(HttpStatus.CREATED).body(ApiResponse.succes(response, "Retour enregistré : " + response.numero()));
    }

    @GetMapping
    @PreAuthorize("isAuthenticated()")
    @Operation(summary = "Lister les retours (filtre optionnel par commercial)")
    public ResponseEntity<ApiResponse<List<RetourResponse>>> lister(@RequestParam(required = false) String commercial) {
        List<RetourResponse> retours = listerRetoursUseCase.executer(commercial);
        return ResponseEntity.ok(ApiResponse.succes(retours, retours.size() + " retour(s)"));
    }
}
