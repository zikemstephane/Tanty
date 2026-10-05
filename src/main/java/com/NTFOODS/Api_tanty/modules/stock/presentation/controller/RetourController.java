package com.NTFOODS.Api_tanty.modules.stock.presentation.controller;

import com.NTFOODS.Api_tanty.modules.stock.application.retour.dto.request.CompterInvendusRequest;
import com.NTFOODS.Api_tanty.modules.stock.application.retour.dto.request.EnregistrerRetourRequest;
import com.NTFOODS.Api_tanty.modules.stock.application.retour.dto.response.RetourResponse;
import com.NTFOODS.Api_tanty.modules.stock.application.retour.usecase.compter.CompterInvendusUseCase;
import com.NTFOODS.Api_tanty.modules.stock.application.retour.usecase.declarer.DeclarerInvendusUseCase;
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

/**
 * Constats d'invendus de fin de tournée.
 *
 * <p>Aucun endpoint de ce contrôleur ne déplace du stock : les invendus restent
 * dans le stock mobile du commercial. Le commercial déclare, le gestionnaire
 * compte, et l'écart entre les deux est conservé.</p>
 */
@RestController
@RequestMapping("/api/stock/retours")
@Tag(name = "Stock — Invendus", description = "Constat de fin de tournée : déclaration du commercial, comptage du gestionnaire")
@SecurityRequirement(name = "bearerAuth")
public class RetourController {

    private final DeclarerInvendusUseCase declarerInvendusUseCase;
    private final CompterInvendusUseCase compterInvendusUseCase;
    private final ListerRetoursUseCase listerRetoursUseCase;
    private final AuthenticatedUserResolver userResolver;

    public RetourController(DeclarerInvendusUseCase declarerInvendusUseCase,
                            CompterInvendusUseCase compterInvendusUseCase,
                            ListerRetoursUseCase listerRetoursUseCase,
                            AuthenticatedUserResolver userResolver) {
        this.declarerInvendusUseCase = declarerInvendusUseCase;
        this.compterInvendusUseCase = compterInvendusUseCase;
        this.listerRetoursUseCase = listerRetoursUseCase;
        this.userResolver = userResolver;
    }

    @PostMapping
    @PreAuthorize("hasAuthority('STOCK_RETOUR_DECLARER')")
    @Operation(summary = "Déclarer ses invendus de fin de tournée",
        description = "Le commercial annonce ce qu'il rapporte. Aucun mouvement de stock n'est produit : " +
                      "les invendus restent dans son stock mobile jusqu'au comptage du gestionnaire.")
    public ResponseEntity<ApiResponse<RetourResponse>> declarer(@Valid @RequestBody EnregistrerRetourRequest request) {
        RetourResponse response = declarerInvendusUseCase.executer(request, userResolver.getUtilisateurCourant());
        return ResponseEntity.status(HttpStatus.CREATED)
            .body(ApiResponse.succes(response, "Invendus déclarés : " + response.numero()));
    }

    @PostMapping("/{numero}/comptage")
    @PreAuthorize("hasAuthority('STOCK_RETOUR_GERER')")
    @Operation(summary = "Compter les invendus déclarés",
        description = "Le gestionnaire saisit les quantités qu'il constate. La quantité déclarée par le " +
                      "commercial est conservée : c'est l'écart entre les deux qui est enregistré.")
    public ResponseEntity<ApiResponse<RetourResponse>> compter(@PathVariable String numero,
                                                               @Valid @RequestBody CompterInvendusRequest request) {
        RetourResponse response = compterInvendusUseCase.executer(numero, request,
            userResolver.getUtilisateurCourant());
        return ResponseEntity.ok(ApiResponse.succes(response, "Constat " + numero + " compté"));
    }

    @GetMapping
    @PreAuthorize("isAuthenticated()")
    @Operation(summary = "Lister les constats d'invendus (filtres optionnels par commercial et par statut)")
    public ResponseEntity<ApiResponse<List<RetourResponse>>> lister(
            @RequestParam(required = false) String commercial,
            @RequestParam(required = false) String statut) {
        List<RetourResponse> retours = listerRetoursUseCase.executer(commercial, statut);
        return ResponseEntity.ok(ApiResponse.succes(retours, retours.size() + " constat(s)"));
    }
}
