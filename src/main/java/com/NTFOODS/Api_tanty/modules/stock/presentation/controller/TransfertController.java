package com.NTFOODS.Api_tanty.modules.stock.presentation.controller;

import com.NTFOODS.Api_tanty.modules.stock.application.transfert.dto.request.CreerTransfertRequest;
import com.NTFOODS.Api_tanty.modules.stock.application.transfert.dto.response.TransfertResponse;
import com.NTFOODS.Api_tanty.modules.stock.application.transfert.usecase.executer.ExecuterTransfertUseCase;
import com.NTFOODS.Api_tanty.modules.stock.application.transfert.usecase.lister.ListerTransfertsUseCase;
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
@RequestMapping("/api/stock/transferts")
@Tag(name = "Stock — Transferts", description = "Transferts Stock central → Stock tampon")
@SecurityRequirement(name = "bearerAuth")
public class TransfertController {

    private final ExecuterTransfertUseCase executerTransfertUseCase;
    private final ListerTransfertsUseCase listerTransfertsUseCase;
    private final AuthenticatedUserResolver userResolver;

    public TransfertController(ExecuterTransfertUseCase executerTransfertUseCase,
                             ListerTransfertsUseCase listerTransfertsUseCase,
                             AuthenticatedUserResolver userResolver) {
        this.executerTransfertUseCase = executerTransfertUseCase;
        this.listerTransfertsUseCase = listerTransfertsUseCase;
        this.userResolver = userResolver;
    }

    @PostMapping
    @PreAuthorize("hasAnyRole('COMMERCIAL', 'GESTIONNAIRE_STOCK', 'ADMIN')")
    @Operation(summary = "Créer et exécuter un transfert central → tampon")
    public ResponseEntity<ApiResponse<TransfertResponse>> executer(@Valid @RequestBody CreerTransfertRequest request) {
        TransfertResponse response = executerTransfertUseCase.executer(request, userResolver.getUtilisateurCourant());
        return ResponseEntity.status(HttpStatus.CREATED)
            .body(ApiResponse.succes(response, "Transfert exécuté : " + response.numero()));
    }

    @GetMapping
    @PreAuthorize("isAuthenticated()")
    @Operation(summary = "Lister les transferts (filtre optionnel par statut)")
    public ResponseEntity<ApiResponse<List<TransfertResponse>>> lister(@RequestParam(required = false) String statut) {
        List<TransfertResponse> transferts = listerTransfertsUseCase.executer(statut);
        return ResponseEntity.ok(ApiResponse.succes(transferts, transferts.size() + " transfert(s)"));
    }
}
