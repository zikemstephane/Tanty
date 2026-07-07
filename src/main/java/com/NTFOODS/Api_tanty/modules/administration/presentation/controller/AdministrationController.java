package com.NTFOODS.Api_tanty.modules.administration.presentation.controller;

import com.NTFOODS.Api_tanty.modules.administration.application.utilisateur.dto.response.UtilisateurResponse;
import com.NTFOODS.Api_tanty.modules.administration.application.utilisateur.usecase.ChangerRoleUtilisateurUseCase;
import com.NTFOODS.Api_tanty.modules.administration.application.utilisateur.usecase.ConsulterUtilisateurUseCase;
import com.NTFOODS.Api_tanty.modules.administration.application.utilisateur.usecase.DebloquerUtilisateurUseCase;
import com.NTFOODS.Api_tanty.modules.administration.application.utilisateur.usecase.GererStatutUtilisateurUseCase;
import com.NTFOODS.Api_tanty.modules.administration.application.utilisateur.usecase.ListerUtilisateursUseCase;
import com.NTFOODS.Api_tanty.modules.administration.application.utilisateur.usecase.ReinitialiserMotDePasseUseCase;
import com.NTFOODS.Api_tanty.shared.application.dto.ApiResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Contrôleur REST — Administration des utilisateurs (au-dessus du module auth).
 * Réservé au DG (superadmin) et à l'Administrateur Système.
 *
 * GET   /api/administration/utilisateurs                → lister tous (filtre par rôle)
 * GET   /api/administration/utilisateurs/{matricule}    → consulter un utilisateur
 * PATCH /api/administration/utilisateurs/{matricule}/activer       → activer
 * PATCH /api/administration/utilisateurs/{matricule}/desactiver    → désactiver
 * PATCH /api/administration/utilisateurs/{matricule}/role          → changer rôle
 * PATCH /api/administration/utilisateurs/{matricule}/debloquer     → débloquer compte
 * PATCH /api/administration/utilisateurs/{matricule}/mot-de-passe  → réinitialiser mot de passe
 */
@RestController
@RequestMapping("/api/administration/utilisateurs")
@Tag(name = "Administration — Utilisateurs", description = "Gestion des comptes, rôles et sécurité")
@SecurityRequirement(name = "bearerAuth")
@PreAuthorize("hasAnyRole('DIRECTEUR_GENERAL', 'ADMIN')")
public class AdministrationController {

    private final ListerUtilisateursUseCase listerUtilisateursUseCase;
    private final ConsulterUtilisateurUseCase consulterUtilisateurUseCase;
    private final GererStatutUtilisateurUseCase gererStatutUseCase;
    private final ChangerRoleUtilisateurUseCase changerRoleUseCase;
    private final DebloquerUtilisateurUseCase debloquerUtilisateurUseCase;
    private final ReinitialiserMotDePasseUseCase reinitialiserMotDePasseUseCase;

    public AdministrationController(ListerUtilisateursUseCase listerUtilisateursUseCase,
                                  ConsulterUtilisateurUseCase consulterUtilisateurUseCase,
                                  GererStatutUtilisateurUseCase gererStatutUseCase,
                                  ChangerRoleUtilisateurUseCase changerRoleUseCase,
                                  DebloquerUtilisateurUseCase debloquerUtilisateurUseCase,
                                  ReinitialiserMotDePasseUseCase reinitialiserMotDePasseUseCase) {
        this.listerUtilisateursUseCase = listerUtilisateursUseCase;
        this.consulterUtilisateurUseCase = consulterUtilisateurUseCase;
        this.gererStatutUseCase = gererStatutUseCase;
        this.changerRoleUseCase = changerRoleUseCase;
        this.debloquerUtilisateurUseCase = debloquerUtilisateurUseCase;
        this.reinitialiserMotDePasseUseCase = reinitialiserMotDePasseUseCase;
    }

    @GetMapping
    @Operation(summary = "Lister les utilisateurs (filtre optionnel par rôle)")
    public ResponseEntity<ApiResponse<List<UtilisateurResponse>>> lister(@RequestParam(required = false) String role) {
        List<UtilisateurResponse> users = listerUtilisateursUseCase.executer(role);
        return ResponseEntity.ok(ApiResponse.succes(users, users.size() + " utilisateur(s)"));
    }

    @GetMapping("/{matricule}")
    @Operation(summary = "Consulter le profil détaillé d'un utilisateur")
    public ResponseEntity<ApiResponse<UtilisateurResponse>> consulter(@PathVariable String matricule) {
        return ResponseEntity.ok(ApiResponse.succes(consulterUtilisateurUseCase.executer(matricule), "Utilisateur"));
    }

    @PatchMapping("/{matricule}/activer")
    @Operation(summary = "Activer un compte utilisateur")
    public ResponseEntity<ApiResponse<UtilisateurResponse>> activer(@PathVariable String matricule) {
        return ResponseEntity.ok(ApiResponse.succes(gererStatutUseCase.executer(matricule, true), "Compte activé"));
    }

    @PatchMapping("/{matricule}/desactiver")
    @Operation(summary = "Désactiver un compte utilisateur")
    public ResponseEntity<ApiResponse<UtilisateurResponse>> desactiver(@PathVariable String matricule) {
        return ResponseEntity.ok(ApiResponse.succes(gererStatutUseCase.executer(matricule, false), "Compte désactivé"));
    }

    @PatchMapping("/{matricule}/role")
    @PreAuthorize("hasRole('DIRECTEUR_GENERAL')")
    @Operation(summary = "Changer le rôle d'un utilisateur — DG exclusivement")
    public ResponseEntity<ApiResponse<UtilisateurResponse>> changerRole(@PathVariable String matricule,
                                                                        @RequestParam String role) {
        return ResponseEntity.ok(ApiResponse.succes(changerRoleUseCase.executer(matricule, role), "Rôle modifié"));
    }

    @PatchMapping("/{matricule}/debloquer")
    @Operation(summary = "Débloquer un compte verrouillé après 3 tentatives échouées")
    public ResponseEntity<ApiResponse<UtilisateurResponse>> debloquer(@PathVariable String matricule) {
        return ResponseEntity.ok(ApiResponse.succes(debloquerUtilisateurUseCase.executer(matricule), "Compte débloqué"));
    }

    @PatchMapping("/{matricule}/mot-de-passe")
    @Operation(summary = "Réinitialiser le mot de passe d'un utilisateur")
    public ResponseEntity<ApiResponse<UtilisateurResponse>> reinitialiserMotDePasse(
        @PathVariable String matricule,
        @RequestParam String nouveauMotDePasse) {
        return ResponseEntity.ok(ApiResponse.succes(
            reinitialiserMotDePasseUseCase.executer(matricule, nouveauMotDePasse),
            "Mot de passe réinitialisé"));
    }
}
