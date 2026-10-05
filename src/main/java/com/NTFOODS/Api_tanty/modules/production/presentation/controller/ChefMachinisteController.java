package com.NTFOODS.Api_tanty.modules.production.presentation.controller;

import com.NTFOODS.Api_tanty.modules.auth.application.rh.dto.response.EmployeResponse;
import com.NTFOODS.Api_tanty.modules.auth.application.rh.usecase.ListerMachinistesUseCase;
import com.NTFOODS.Api_tanty.shared.application.dto.ApiResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * Contrôleur REST — Espace Chef Machiniste.
 *
 * Le Chef Machiniste consulte/sélectionne les machinistes créés par le DG.
 * Il ne peut pas créer de machiniste.
 */
@RestController
@RequestMapping("/api/production/chef-machiniste")
@Tag(name = "Production — Chef Machiniste", description = "Gestion de l'équipe machinistes")
@SecurityRequirement(name = "bearerAuth")
public class ChefMachinisteController {

    private final ListerMachinistesUseCase listerMachinistesUseCase;

    public ChefMachinisteController(ListerMachinistesUseCase listerMachinistesUseCase) {
        this.listerMachinistesUseCase = listerMachinistesUseCase;
    }

    @GetMapping("/machinistes")
    @PreAuthorize("hasAuthority('PRODUCTION_REGISTRE_CONSULTER')")
    @Operation(summary = "Lister les machinistes actifs (créés par le DG)")
    public ResponseEntity<ApiResponse<List<EmployeResponse>>> listerMachinistes() {
        var r = listerMachinistesUseCase.executer();
        return ResponseEntity.ok(ApiResponse.succes(r, r.size() + " machiniste(s)"));
    }
}
