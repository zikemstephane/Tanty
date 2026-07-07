package com.NTFOODS.Api_tanty.modules.auth.infrastructure.security;

import com.NTFOODS.Api_tanty.modules.auth.domain.utilisateur.aggregate.Utilisateur;
import com.NTFOODS.Api_tanty.modules.auth.domain.utilisateur.repository.UtilisateurRepository;
import com.NTFOODS.Api_tanty.modules.auth.domain.utilisateur.valueobject.Matricule;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.lang.NonNull;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

/**
 * Filtre JWT — authentification + application de la règle "Session Unique".
 *
 * Un jeton n'est accepté que si son claim "sid" correspond exactement à la
 * session actuellement active en base pour ce matricule. Toute nouvelle
 * connexion sur un autre appareil régénère cette session et invalide donc
 * automatiquement, sans état supplémentaire à gérer (pas de blacklist),
 * tout jeton émis précédemment.
 */
@Component
public class JwtAuthFilter extends OncePerRequestFilter {

    private final JwtService jwtService;
    private final UtilisateurRepository utilisateurRepository;

    public JwtAuthFilter(JwtService jwtService, UtilisateurRepository utilisateurRepository) {
        this.jwtService = jwtService;
        this.utilisateurRepository = utilisateurRepository;
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
        if (!jwtService.estValide(token)) {
            filterChain.doFilter(request, response);
            return;
        }

        String matricule = jwtService.extraireMatricule(token);
        String role = jwtService.extraireRole(token);
        String sessionIdToken = jwtService.extraireSessionId(token);

        if (matricule != null && SecurityContextHolder.getContext().getAuthentication() == null) {
            // Charge l'agrégat Utilisateur et le place comme principal : c'est ce
            // qu'attend AuthenticatedUserResolver (et donc tous les use-cases protégés).
            Optional<Utilisateur> utilisateurOpt = chargerUtilisateur(matricule);
            if (utilisateurOpt.isPresent()) {
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
                    var auth = new UsernamePasswordAuthenticationToken(
                        utilisateur,
                        null,
                        List.of(new SimpleGrantedAuthority("ROLE_" + role))
                    );
                    auth.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                    SecurityContextHolder.getContext().setAuthentication(auth);
                }
            }
        }

        filterChain.doFilter(request, response);
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
