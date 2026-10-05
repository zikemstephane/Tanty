package com.NTFOODS.Api_tanty.modules.auth.presentation.controller;

import com.NTFOODS.Api_tanty.modules.auth.domain.utilisateur.enums.Permission;
import com.NTFOODS.Api_tanty.modules.auth.domain.utilisateur.enums.Role;
import com.NTFOODS.Api_tanty.modules.auth.domain.utilisateur.enums.RolePermissionMatrix;
import com.NTFOODS.Api_tanty.modules.auth.domain.utilisateur.repository.UtilisateurRepository;
import com.NTFOODS.Api_tanty.modules.auth.domain.utilisateur.valueobject.Matricule;
import com.NTFOODS.Api_tanty.modules.auth.infrastructure.persistence.jpa.PermissionResolverService;
import com.NTFOODS.Api_tanty.modules.auth.infrastructure.persistence.jpa.UtilisateurPermissionJpaEntity;
import com.NTFOODS.Api_tanty.shared.application.dto.ApiResponse;
import com.NTFOODS.Api_tanty.shared.infrastructure.security.AuthenticatedUserResolver;
import com.NTFOODS.Api_tanty.shared.kernel.exception.EntiteNonTrouveeException;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

/**
 * Contrôleur REST — Gestion des permissions (RBAC).
 *
 * Consultation:
 * GET  /api/auth/permissions                    → Lister toutes les permissions disponibles
 * GET  /api/auth/permissions/matrice            → Consulter la matrice rôle→permissions
 * GET  /api/auth/permissions/{matricule}        → Consulter les permissions effectives d'un utilisateur
 * GET  /api/auth/permissions/{matricule}/overrides → Lister les overrides d'un utilisateur
 *
 * Gestion des overrides (DG/ADMIN):
 * POST /api/auth/permissions/{matricule}/overrides     → Ajouter/modifier un override
 * DELETE /api/auth/permissions/{matricule}/overrides/{permission} → Supprimer un override
 */
@RestController
@RequestMapping("/api/auth/permissions")
@Tag(name = "Permissions RBAC", description = "Gestion des permissions et matrice rôle-permission")
@SecurityRequirement(name = "bearerAuth")
public class PermissionController {

    private final PermissionResolverService permissionResolverService;
    private final AuthenticatedUserResolver userResolver;
    private final UtilisateurRepository utilisateurRepository;

    public PermissionController(PermissionResolverService permissionResolverService,
                                AuthenticatedUserResolver userResolver,
                                UtilisateurRepository utilisateurRepository) {
        this.permissionResolverService = permissionResolverService;
        this.userResolver = userResolver;
        this.utilisateurRepository = utilisateurRepository;
    }

    @GetMapping
    @PreAuthorize("isAuthenticated()")
    @Operation(summary = "Lister toutes les permissions disponibles")
    public ResponseEntity<ApiResponse<List<PermissionDto>>> listerPermissions() {
        List<PermissionDto> perms = Arrays.stream(Permission.values())
            .map(p -> new PermissionDto(p.name(), p.getLibelle()))
            .toList();
        return ResponseEntity.ok(ApiResponse.succes(perms, perms.size() + " permission(s)"));
    }

    @GetMapping("/matrice")
    @PreAuthorize("hasAuthority('AUTH_PERMISSION_CONSULTER')")
    @Operation(summary = "Consulter la matrice rôle→permissions")
    public ResponseEntity<ApiResponse<Map<String, List<String>>>> consulterMatrice() {
        Map<String, List<String>> matrice = new LinkedHashMap<>();
        RolePermissionMatrix.matriceComplete().forEach((role, perms) ->
            matrice.put(role.name(), perms.stream().map(Enum::name).sorted().toList())
        );
        return ResponseEntity.ok(ApiResponse.succes(matrice, "Matrice rôle-permission"));
    }

    @GetMapping("/{matricule}")
    @PreAuthorize("hasAuthority('AUTH_PERMISSION_CONSULTER')")
    @Operation(summary = "Consulter les permissions effectives d'un utilisateur")
    public ResponseEntity<ApiResponse<PermissionsEffectivesDto>> permissionsEffectives(
        @PathVariable String matricule) {
        // Le rôle à résoudre est celui de l'utilisateur demandé, pas celui de
        // l'appelant. Prendre le rôle courant faisait répondre à « que peut
        // faire COM-001 ? » par « voici ce que je peux faire, moi » — un audit
        // de permissions qui affirmait le contraire de la réalité, et d'autant
        // plus trompeur que le matricule et les overrides affichés, eux,
        // étaient bien ceux de la cible.
        Role role = utilisateurRepository.trouverParMatricule(Matricule.de(matricule))
            .orElseThrow(() -> EntiteNonTrouveeException.de("Utilisateur", matricule))
            .getRole();
        Set<Permission> effectives = permissionResolverService.permissionsEffectives(matricule, role);
        Set<Permission> baseRole = RolePermissionMatrix.permissionsDe(role);

        List<UtilisateurPermissionJpaEntity> overrides = permissionResolverService.overridesDe(matricule);

        PermissionsEffectivesDto dto = new PermissionsEffectivesDto(
            matricule,
            role.name(),
            baseRole.stream().map(Enum::name).sorted().toList(),
            effectives.stream().map(Enum::name).sorted().toList(),
            overrides.stream().map(this::toOverrideDto).toList()
        );
        return ResponseEntity.ok(ApiResponse.succes(dto, "Permissions effectives"));
    }

    @GetMapping("/{matricule}/overrides")
    @PreAuthorize("hasAuthority('AUTH_PERMISSION_CONSULTER')")
    @Operation(summary = "Lister les overrides de permissions d'un utilisateur")
    public ResponseEntity<ApiResponse<List<OverrideDto>>> listerOverrides(@PathVariable String matricule) {
        List<OverrideDto> overrides = permissionResolverService.overridesDe(matricule).stream()
            .map(this::toOverrideDto)
            .toList();
        return ResponseEntity.ok(ApiResponse.succes(overrides, overrides.size() + " override(s)"));
    }

    @PostMapping("/{matricule}/overrides")
    @PreAuthorize("hasAuthority('AUTH_PERMISSION_GERER')")
    @Operation(summary = "Ajouter ou modifier un override de permission")
    public ResponseEntity<ApiResponse<OverrideDto>> ajouterOverride(
        @PathVariable String matricule,
        @RequestBody AjouterOverrideRequest request) {
        Permission permission = Permission.valueOf(request.permission());
        UtilisateurPermissionJpaEntity.TypeOverride type =
            UtilisateurPermissionJpaEntity.TypeOverride.valueOf(request.typeOverride());

        UtilisateurPermissionJpaEntity saved = permissionResolverService.ajouterOverride(
            matricule, permission, type, request.motif(), userResolver.getMatriculeCourant());
        return ResponseEntity.status(HttpStatus.CREATED)
            .body(ApiResponse.succes(toOverrideDto(saved), "Override créé"));
    }

    @DeleteMapping("/{matricule}/overrides/{permission}")
    @PreAuthorize("hasAuthority('AUTH_PERMISSION_GERER')")
    @Operation(summary = "Supprimer un override de permission")
    public ResponseEntity<ApiResponse<Void>> supprimerOverride(
        @PathVariable String matricule,
        @PathVariable String permission) {
        permissionResolverService.supprimerOverride(matricule, Permission.valueOf(permission));
        return ResponseEntity.ok(ApiResponse.succes(null, "Override supprimé"));
    }

    private OverrideDto toOverrideDto(UtilisateurPermissionJpaEntity e) {
        return new OverrideDto(
            e.getPermission().name(),
            e.getPermission().getLibelle(),
            e.getTypeOverride().name(),
            e.getMotif(),
            e.getMatriculeAuteur(),
            e.getDateCreation()
        );
    }

    // ── DTOs ────────────────────────────────────────────────────────────────

    public record PermissionDto(String code, String libelle) {}

    public record OverrideDto(String permission, String libellePermission, String typeOverride,
                              String motif, String matriculeAuteur, LocalDateTime dateCreation) {}

    public record PermissionsEffectivesDto(String matricule, String role,
                                           List<String> permissionsBaseRole,
                                           List<String> permissionsEffectives,
                                           List<OverrideDto> overrides) {}

    public record AjouterOverrideRequest(String permission, String typeOverride, String motif) {}
}
