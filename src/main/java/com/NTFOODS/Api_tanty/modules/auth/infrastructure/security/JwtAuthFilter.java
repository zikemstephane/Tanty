package com.NTFOODS.Api_tanty.modules.auth.infrastructure.security;

import com.NTFOODS.Api_tanty.modules.auth.domain.utilisateur.aggregate.Utilisateur;
import com.NTFOODS.Api_tanty.modules.auth.domain.utilisateur.enums.Permission;
import com.NTFOODS.Api_tanty.modules.auth.domain.utilisateur.repository.UtilisateurRepository;
import com.NTFOODS.Api_tanty.modules.auth.domain.utilisateur.valueobject.Matricule;
import com.NTFOODS.Api_tanty.modules.auth.infrastructure.persistence.jpa.PermissionResolverService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.lang.NonNull;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;

/**
 * Filtre JWT — authentification + application de la règle "Session Unique".
 *
 * Un jeton n'est accepté que si son claim "sid" correspond exactement à la
 * session actuellement active en base pour ce matricule. Toute nouvelle
 * connexion sur un autre appareil régénère cette session et invalide donc
 * automatiquement, sans état supplémentaire à gérer (pas de blacklist),
 * tout jeton émis précédemment.
 *
 * IMPORTANT : ce filtre s'exécute sur TOUTES les requêtes, y compris celles
 * en permitAll() (ex: /api/auth/login), car il est inséré dans la chaîne
 * avant l'évaluation des règles d'autorisation. Toute exception non
 * interceptée ici (jeton expiré/mal formé) casserait donc aussi les routes
 * publiques. Le parsing du jeton est donc entièrement encadré par un
 * try-catch : un jeton invalide est traité comme une absence de jeton.
 */
@Component
public class JwtAuthFilter extends OncePerRequestFilter {

    private static final Logger log = LoggerFactory.getLogger(JwtAuthFilter.class);

    private final JwtService jwtService;
    private final UtilisateurRepository utilisateurRepository;
    private final PermissionResolverService permissionResolverService;

    public JwtAuthFilter(JwtService jwtService, UtilisateurRepository utilisateurRepository,
                         PermissionResolverService permissionResolverService) {
        this.jwtService = jwtService;
        this.utilisateurRepository = utilisateurRepository;
        this.permissionResolverService = permissionResolverService;
    }

    @Override
    protected void doFilterInternal(@NonNull HttpServletRequest request,
                                    @NonNull HttpServletResponse response,
                                    @NonNull FilterChain filterChain)
        throws ServletException, IOException {

        String authHeader = request.getHeader("Authorization");
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            filterChain.doFilter(request, response);
            return;
        }

        String token = authHeader.substring(7);

        try {
            authentifierSiPossible(request, token);
        } catch (Exception e) {
            // Jeton expiré, mal formé, signature invalide, etc. : on ne bloque
            // jamais la requête pour ça. La route publique continue de
            // fonctionner ; une route protégée sera simplement rejetée plus
            // loin par Spring Security (401/403) faute d'authentification.
            log.debug("Jeton JWT invalide ignoré : {}", e.getMessage());
            SecurityContextHolder.clearContext();
        }

        filterChain.doFilter(request, response);
    }

    private void authentifierSiPossible(HttpServletRequest request, String token) {
        if (!jwtService.estValide(token)) {
            return;
        }

        String matricule = jwtService.extraireMatricule(token);
        String role = jwtService.extraireRole(token);
        String sessionIdToken = jwtService.extraireSessionId(token);

        if (matricule == null || SecurityContextHolder.getContext().getAuthentication() != null) {
            return;
        }

        // Charge l'agrégat Utilisateur et le place comme principal : c'est ce
        // qu'attend AuthenticatedUserResolver (et donc tous les use-cases protégés).
        Optional<Utilisateur> utilisateurOpt = chargerUtilisateur(matricule);
        if (utilisateurOpt.isEmpty()) {
            return;
        }

        Utilisateur utilisateur = utilisateurOpt.get();

        boolean actifEtNonBloque = utilisateur.isActif() && !utilisateur.isCompteBloque();
        // Règle "Session Unique" : si le jeton porte un sid (jetons émis après
        // l'introduction de la fonctionnalité), il doit correspondre à la session
        // active. Un sid absent (anciens tokens) ou une session non initialisée
        // ne bloque pas — comportement rétro-compatible et fail-open côté lecture,
        // le contrôle strict des droits restant assuré par les rôles (@PreAuthorize).
        boolean sessionValide = sessionIdToken == null
            || utilisateur.getSessionActuelle() == null
            || Objects.equals(sessionIdToken, utilisateur.getSessionActuelle());

        if (actifEtNonBloque && sessionValide) {
            // Charge les permissions effectives : (permissions du rôle + ACCORDER) - REFUSER
            Set<Permission> permissions = permissionResolverService.permissionsEffectives(
                matricule, utilisateur.getRole());

            List<SimpleGrantedAuthority> authorities = new ArrayList<>();
            authorities.add(new SimpleGrantedAuthority("ROLE_" + role));
            permissions.forEach(p -> authorities.add(new SimpleGrantedAuthority(p.name())));

            var auth = new UsernamePasswordAuthenticationToken(
                utilisateur,
                null,
                authorities
            );
            auth.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
            SecurityContextHolder.getContext().setAuthentication(auth);
        }
    }

    private Optional<Utilisateur> chargerUtilisateur(String matricule) {
        try {
            return utilisateurRepository.trouverParMatricule(Matricule.de(matricule));
        } catch (RuntimeException e) {
            // Matricule mal formé ou indisponible : on laisse la requête passer
            // sans authentification plutôt que de provoquer une 500.
            return Optional.empty();
        }
    }
}