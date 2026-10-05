package com.NTFOODS.Api_tanty.modules.auth.application.auth.usecase;

import com.NTFOODS.Api_tanty.modules.auth.application.auth.dto.LoginRequest;
import com.NTFOODS.Api_tanty.modules.auth.application.auth.dto.LoginResponse;
import com.NTFOODS.Api_tanty.modules.auth.domain.utilisateur.aggregate.Utilisateur;
import com.NTFOODS.Api_tanty.modules.auth.domain.utilisateur.exceptions.CompteBloqueException;
import com.NTFOODS.Api_tanty.modules.auth.domain.utilisateur.exceptions.CompteDesactiveException;
import com.NTFOODS.Api_tanty.modules.auth.domain.utilisateur.exceptions.CredentialsInvalidesException;
import com.NTFOODS.Api_tanty.modules.auth.domain.utilisateur.repository.UtilisateurRepository;
import com.NTFOODS.Api_tanty.modules.auth.domain.utilisateur.valueobject.Matricule;
import com.NTFOODS.Api_tanty.modules.auth.domain.utilisateur.enums.Role;
import com.NTFOODS.Api_tanty.modules.auth.infrastructure.security.JwtService;
import com.NTFOODS.Api_tanty.shared.kernel.event.EventPublisher;
import com.NTFOODS.Api_tanty.shared.kernel.exception.AccesRefuseException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Set;

/**
 * Cas d'usage — Connexion.
 *
 * Applique le Pilier 3 (Sécurisation des Données et Anti-Fraude) :
 * - Blocage automatique après 3 tentatives de mot de passe erronées (comme
 *   une carte bancaire) ; déblocage manuel réservé à l'administrateur.
 * - Session unique : la connexion réussie ouvre une nouvelle session qui
 *   invalide implicitement tout jeton précédemment émis pour ce matricule.
 */
@Service
public class LoginUseCase {

    /** Exécutants (machinistes, agents de production) : pas de compte d'accès au système. */
    private static final Set<Role> ROLES_SANS_ACCES = Set.of(Role.MACHINISTE, Role.AGENT_PRODUCTION);

    private final UtilisateurRepository utilisateurRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final EventPublisher eventPublisher;

    public LoginUseCase(UtilisateurRepository utilisateurRepository,
                        PasswordEncoder passwordEncoder,
                        JwtService jwtService,
                        EventPublisher eventPublisher) {
        this.utilisateurRepository = utilisateurRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtService = jwtService;
        this.eventPublisher = eventPublisher;
    }

    @Transactional
    public LoginResponse executer(LoginRequest request) {
        Matricule matricule = Matricule.de(request.matricule());

        Utilisateur utilisateur = utilisateurRepository.trouverParMatricule(matricule)
            .orElseThrow(CredentialsInvalidesException::new);

        if (!utilisateur.isActif()) {
            throw new CompteDesactiveException(matricule.getValeur());
        }
        if (utilisateur.isCompteBloque()) {
            throw new CompteBloqueException(matricule.getValeur());
        }
        // Les exécutants ne se connectent pas au système, quel que soit leur mot de passe.
        if (ROLES_SANS_ACCES.contains(utilisateur.getRole())) {
            throw new AccesRefuseException("Ce rôle n'a pas accès au système.");
        }

        boolean motDePasseValide = passwordEncoder.matches(
            request.motDePasse(),
            utilisateur.getMotDePasse().getHash()
        );

        if (!motDePasseValide) {
            utilisateur.enregistrerEchecConnexion();
            Utilisateur sauvegarde = utilisateurRepository.sauvegarder(utilisateur);
            sauvegarde.getDomainEvents().forEach(eventPublisher::publish);
            sauvegarde.clearDomainEvents();
            if (sauvegarde.isCompteBloque()) {
                throw new CompteBloqueException(matricule.getValeur());
            }
            throw new CredentialsInvalidesException();
        }

        String sessionId = utilisateur.ouvrirNouvelleSession();
        utilisateur.getDomainEvents().forEach(eventPublisher::publish);
        utilisateur.clearDomainEvents();
        utilisateurRepository.sauvegarder(utilisateur);

        String token = jwtService.genererToken(
            utilisateur.getMatricule().getValeur(),
            utilisateur.getRole().name(),
            sessionId
        );

        return new LoginResponse(
            token,
            utilisateur.getMatricule().getValeur(),
            utilisateur.getNomComplet().getNomComplet(),
            utilisateur.getRole(),
            jwtService.getExpirationMs()
        );
    }
}
