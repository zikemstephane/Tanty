package com.NTFOODS.Api_tanty.modules.auth.presentation.controller;

import com.NTFOODS.Api_tanty.modules.auth.application.auth.dto.DemanderOtpRequest;
import com.NTFOODS.Api_tanty.modules.auth.application.auth.dto.LoginRequest;
import com.NTFOODS.Api_tanty.modules.auth.application.auth.dto.LoginResponse;
import com.NTFOODS.Api_tanty.modules.auth.application.auth.dto.ModifierProfilRequest;
import com.NTFOODS.Api_tanty.modules.auth.application.auth.dto.ModifierUtilisateurRequest;
import com.NTFOODS.Api_tanty.modules.auth.application.auth.dto.OtpResponse;
import com.NTFOODS.Api_tanty.modules.auth.application.auth.dto.ProfilResponse;
import com.NTFOODS.Api_tanty.modules.auth.application.auth.dto.RegisterRequest;
import com.NTFOODS.Api_tanty.modules.auth.application.auth.dto.VerifierOtpRequest;
import com.NTFOODS.Api_tanty.modules.auth.application.auth.usecase.DemanderOtpUseCase;
import com.NTFOODS.Api_tanty.modules.auth.application.auth.usecase.GererProfilUseCase;
import com.NTFOODS.Api_tanty.modules.auth.application.auth.usecase.GererUtilisateursUseCase;
import com.NTFOODS.Api_tanty.modules.auth.application.auth.usecase.LoginUseCase;
import com.NTFOODS.Api_tanty.modules.auth.application.auth.usecase.LogoutUseCase;
import com.NTFOODS.Api_tanty.modules.auth.application.auth.usecase.RegisterUseCase;
import com.NTFOODS.Api_tanty.modules.auth.application.auth.usecase.VerifierOtpUseCase;
import com.NTFOODS.Api_tanty.modules.auth.domain.utilisateur.enums.Role;
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
import java.util.Map;

/**
 * Contrôleur REST — Authentification & Gestion des Utilisateurs (module auth).
 *
 * Auth:
 * POST /api/auth/login         → connexion par mot de passe, retourne JWT
 * POST /api/auth/otp/demander  → demander un code OTP par SMS
 * POST /api/auth/otp/verifier  → vérifier OTP et obtenir JWT
 * POST /api/auth/register      → création d'un utilisateur (admin/DG)
 * POST /api/auth/logout        → déconnexion explicite
 *
 * Profil self-service:
 * GET  /api/auth/me            → consulter son propre profil
 * PUT  /api/auth/profil        → modifier ses informations personnelles
 *
 * Gestion utilisateurs (DG/ADMIN):
 * GET  /api/auth/utilisateurs           → lister tous les utilisateurs
 * GET  /api/auth/utilisateurs/{role}    → lister par rôle
 * GET  /api/auth/utilisateurs/{matricule} → consulter un utilisateur
 * PUT  /api/auth/utilisateurs/{matricule} → modifier un utilisateur
 * PUT  /api/auth/utilisateurs/{matricule}/desactiver → désactiver
 * PUT  /api/auth/utilisateurs/{matricule}/reactiver  → réactiver
 * PUT  /api/auth/utilisateurs/{matricule}/debloquer  → débloquer compte
 * PUT  /api/auth/utilisateurs/{matricule}/mot-de-passe → changer mot de passe
 * PUT  /api/auth/utilisateurs/{matricule}/role        → changer rôle
 */
@RestController
@RequestMapping("/api/auth")
@Tag(name = "Authentification & Utilisateurs", description = "Login, OTP, enregistrement, gestion des utilisateurs et tokens JWT")
public class AuthController {

    private final LoginUseCase loginUseCase;
    private final RegisterUseCase registerUseCase;
    private final LogoutUseCase logoutUseCase;
    private final GererProfilUseCase gererProfilUseCase;
    private final GererUtilisateursUseCase gererUtilisateursUseCase;
    private final DemanderOtpUseCase demanderOtpUseCase;
    private final VerifierOtpUseCase verifierOtpUseCase;
    private final AuthenticatedUserResolver userResolver;

    public AuthController(LoginUseCase loginUseCase, RegisterUseCase registerUseCase,
                           LogoutUseCase logoutUseCase, GererProfilUseCase gererProfilUseCase,
                           GererUtilisateursUseCase gererUtilisateursUseCase,
                           DemanderOtpUseCase demanderOtpUseCase,
                           VerifierOtpUseCase verifierOtpUseCase,
                           AuthenticatedUserResolver userResolver) {
        this.loginUseCase = loginUseCase;
        this.registerUseCase = registerUseCase;
        this.logoutUseCase = logoutUseCase;
        this.gererProfilUseCase = gererProfilUseCase;
        this.gererUtilisateursUseCase = gererUtilisateursUseCase;
        this.demanderOtpUseCase = demanderOtpUseCase;
        this.verifierOtpUseCase = verifierOtpUseCase;
        this.userResolver = userResolver;
    }

    @PostMapping("/login")
    @Operation(summary = "Connexion utilisateur",
        description = "Retourne un token JWT avec le rôle de l'utilisateur. Ouvre une nouvelle session " +
                      "unique : toute session précédemment active sur un autre appareil est invalidée.")
    public ResponseEntity<ApiResponse<LoginResponse>> login(@RequestBody @Valid LoginRequest request) {
        return ResponseEntity.ok(ApiResponse.succes(loginUseCase.executer(request), "Connexion réussie"));
    }

    @PostMapping("/otp/demander")
    @Operation(summary = "Demander un code OTP par SMS",
        description = "Génère un code OTP à 6 chiffres valable 5 minutes et l'envoie par SMS au téléphone enregistré.")
    public ResponseEntity<ApiResponse<OtpResponse>> demanderOtp(@RequestBody @Valid DemanderOtpRequest request) {
        return ResponseEntity.ok(ApiResponse.succes(demanderOtpUseCase.executer(request), "OTP envoyé par SMS"));
    }

    @PostMapping("/otp/verifier")
    @Operation(summary = "Vérifier un code OTP et obtenir un JWT",
        description = "Vérifie le code OTP. Si valide, ouvre une session et retourne un token JWT.")
    public ResponseEntity<ApiResponse<LoginResponse>> verifierOtp(@RequestBody @Valid VerifierOtpRequest request) {
        return ResponseEntity.ok(ApiResponse.succes(verifierOtpUseCase.executer(request), "Connexion OTP réussie"));
    }

    @PostMapping("/register")
    @PreAuthorize("hasAuthority('AUTH_USER_CREER')")
    @SecurityRequirement(name = "bearerAuth")
    @Operation(summary = "Créer un utilisateur", description = "Réservé au DG/ADMIN — génère un matricule automatiquement")
    public ResponseEntity<ApiResponse<Map<String, String>>> register(@RequestBody @Valid RegisterRequest request) {
        String matricule = registerUseCase.executer(request);
        return ResponseEntity.status(HttpStatus.CREATED)
            .body(ApiResponse.succes(Map.of("matricule", matricule), "Utilisateur créé avec succès"));
    }

    @PostMapping("/logout")
    @PreAuthorize("isAuthenticated()")
    @SecurityRequirement(name = "bearerAuth")
    @Operation(summary = "Déconnexion explicite", description = "Clôture la session active (utile en cas de perte/vol d'appareil).")
    public ResponseEntity<ApiResponse<Void>> logout() {
        logoutUseCase.executer(userResolver.getMatriculeCourant());
        return ResponseEntity.ok(ApiResponse.succes(null, "Déconnexion réussie"));
    }

    @GetMapping("/me")
    @PreAuthorize("isAuthenticated()")
    @SecurityRequirement(name = "bearerAuth")
    @Operation(summary = "Consulter son propre profil")
    public ResponseEntity<ApiResponse<ProfilResponse>> monProfil() {
        String matricule = userResolver.getMatriculeCourant();
        return ResponseEntity.ok(ApiResponse.succes(gererProfilUseCase.consulter(matricule), "Profil"));
    }

    @PutMapping("/profil")
    @PreAuthorize("isAuthenticated()")
    @SecurityRequirement(name = "bearerAuth")
    @Operation(summary = "Modifier ses informations personnelles",
        description = "Permet à l'utilisateur connecté de mettre à jour son téléphone, résidence, CNI, photo, etc.")
    public ResponseEntity<ApiResponse<ProfilResponse>> modifierProfil(@RequestBody ModifierProfilRequest request) {
        String matricule = userResolver.getMatriculeCourant();
        return ResponseEntity.ok(ApiResponse.succes(gererProfilUseCase.modifier(matricule, request), "Profil mis à jour"));
    }

    @GetMapping("/utilisateurs")
    @PreAuthorize("hasAuthority('AUTH_USER_CONSULTER')")
    @SecurityRequirement(name = "bearerAuth")
    @Operation(summary = "Lister tous les utilisateurs")
    public ResponseEntity<ApiResponse<List<ProfilResponse>>> listerUtilisateurs(
        @RequestParam(required = false) Role role) {
        List<ProfilResponse> r = (role != null)
            ? gererUtilisateursUseCase.listerParRole(role)
            : gererUtilisateursUseCase.listerTous();
        return ResponseEntity.ok(ApiResponse.succes(r, r.size() + " utilisateur(s)"));
    }

    @GetMapping("/utilisateurs/{matricule}")
    @PreAuthorize("hasAuthority('AUTH_USER_CONSULTER')")
    @SecurityRequirement(name = "bearerAuth")
    @Operation(summary = "Consulter le profil d'un utilisateur")
    public ResponseEntity<ApiResponse<ProfilResponse>> consulterUtilisateur(@PathVariable String matricule) {
        return ResponseEntity.ok(ApiResponse.succes(gererUtilisateursUseCase.consulter(matricule), "Utilisateur"));
    }

    @PutMapping("/utilisateurs/{matricule}")
    @PreAuthorize("hasAuthority('AUTH_USER_MODIFIER')")
    @SecurityRequirement(name = "bearerAuth")
    @Operation(summary = "Modifier un utilisateur (tous champs)")
    public ResponseEntity<ApiResponse<ProfilResponse>> modifierUtilisateur(
        @PathVariable String matricule,
        @RequestBody @Valid ModifierUtilisateurRequest request) {
        return ResponseEntity.ok(ApiResponse.succes(gererUtilisateursUseCase.modifier(matricule, request), "Utilisateur modifié"));
    }

    @PutMapping("/utilisateurs/{matricule}/desactiver")
    @PreAuthorize("hasAuthority('AUTH_USER_DESACTIVER')")
    @SecurityRequirement(name = "bearerAuth")
    @Operation(summary = "Désactiver un utilisateur")
    public ResponseEntity<ApiResponse<Void>> desactiverUtilisateur(@PathVariable String matricule) {
        gererUtilisateursUseCase.desactiver(matricule);
        return ResponseEntity.ok(ApiResponse.succes(null, "Utilisateur désactivé"));
    }

    @PutMapping("/utilisateurs/{matricule}/reactiver")
    @PreAuthorize("hasAuthority('AUTH_USER_DESACTIVER')")
    @SecurityRequirement(name = "bearerAuth")
    @Operation(summary = "Réactiver un utilisateur")
    public ResponseEntity<ApiResponse<Void>> reactiverUtilisateur(@PathVariable String matricule) {
        gererUtilisateursUseCase.reactiver(matricule);
        return ResponseEntity.ok(ApiResponse.succes(null, "Utilisateur réactivé"));
    }

    @PutMapping("/utilisateurs/{matricule}/debloquer")
    @PreAuthorize("hasAuthority('AUTH_USER_DEBLOQUER')")
    @SecurityRequirement(name = "bearerAuth")
    @Operation(summary = "Débloquer un compte (après blocage automatique)")
    public ResponseEntity<ApiResponse<Void>> debloquerUtilisateur(@PathVariable String matricule) {
        gererUtilisateursUseCase.debloquer(matricule);
        return ResponseEntity.ok(ApiResponse.succes(null, "Compte débloqué"));
    }

    @PutMapping("/utilisateurs/{matricule}/mot-de-passe")
    @PreAuthorize("hasAuthority('AUTH_USER_RESET_PASSWORD')")
    @SecurityRequirement(name = "bearerAuth")
    @Operation(summary = "Réinitialiser le mot de passe d'un utilisateur")
    public ResponseEntity<ApiResponse<Void>> changerMotDePasse(
        @PathVariable String matricule,
        @RequestBody Map<String, String> body) {
        gererUtilisateursUseCase.changerMotDePasse(matricule, body.get("nouveauMotDePasse"));
        return ResponseEntity.ok(ApiResponse.succes(null, "Mot de passe réinitialisé"));
    }

    @PutMapping("/utilisateurs/{matricule}/role")
    @PreAuthorize("hasAuthority('AUTH_USER_CHANGER_ROLE')")
    @SecurityRequirement(name = "bearerAuth")
    @Operation(summary = "Changer le rôle d'un utilisateur — DG exclusivement")
    public ResponseEntity<ApiResponse<Void>> changerRole(
        @PathVariable String matricule,
        @RequestBody Map<String, Role> body) {
        gererUtilisateursUseCase.changerRole(matricule, body.get("role"));
        return ResponseEntity.ok(ApiResponse.succes(null, "Rôle modifié"));
    }
}
