package com.NTFOODS.Api_tanty.modules.auth.application.auth.usecase;

import com.NTFOODS.Api_tanty.modules.auth.domain.utilisateur.aggregate.Utilisateur;
import com.NTFOODS.Api_tanty.modules.auth.domain.utilisateur.repository.UtilisateurRepository;
import com.NTFOODS.Api_tanty.modules.auth.domain.utilisateur.valueobject.Matricule;
import com.NTFOODS.Api_tanty.shared.kernel.exception.EntiteNonTrouveeException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Use Case — Déconnexion explicite. Clôture la session active
 * ({@code sessionActuelle} redevient {@code null}), ce qui invalide
 * immédiatement le jeton JWT en cours (voir {@code JwtAuthFilter}, règle de
 * session unique) — utile en cas de perte/vol d'appareil, sans attendre
 * l'expiration naturelle du token.
 */
@Service
public class LogoutUseCase {

    private final UtilisateurRepository utilisateurRepository;

    public LogoutUseCase(UtilisateurRepository utilisateurRepository) {
        this.utilisateurRepository = utilisateurRepository;
    }

    @Transactional
    public void executer(String matricule) {
        Utilisateur utilisateur = utilisateurRepository.trouverParMatricule(Matricule.de(matricule))
            .orElseThrow(() -> EntiteNonTrouveeException.de("Utilisateur", matricule));
        utilisateur.cloreSession();
        utilisateurRepository.sauvegarder(utilisateur);
    }
}
