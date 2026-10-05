package com.NTFOODS.Api_tanty.modules.administration.application.utilisateur.usecase;

import com.NTFOODS.Api_tanty.modules.administration.application.utilisateur.dto.response.UtilisateurResponse;
import com.NTFOODS.Api_tanty.modules.administration.application.utilisateur.mapper.UtilisateurAdminMapper;
import com.NTFOODS.Api_tanty.modules.auth.domain.utilisateur.aggregate.Utilisateur;
import com.NTFOODS.Api_tanty.modules.auth.domain.utilisateur.repository.UtilisateurRepository;
import com.NTFOODS.Api_tanty.modules.auth.domain.utilisateur.valueobject.Matricule;
import com.NTFOODS.Api_tanty.shared.kernel.exception.EntiteNonTrouveeException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Use Case — Déblocage manuel d'un compte après blocage automatique
 * (3 tentatives de connexion erronées). Réservé à l'administrateur.
 */
@Service
@Transactional
public class DebloquerUtilisateurUseCase {

    private final UtilisateurRepository utilisateurRepository;
    private final UtilisateurAdminMapper mapper;

    public DebloquerUtilisateurUseCase(UtilisateurRepository utilisateurRepository, UtilisateurAdminMapper mapper) {
        this.utilisateurRepository = utilisateurRepository;
        this.mapper = mapper;
    }

    public UtilisateurResponse executer(String matricule) {
        Utilisateur u = utilisateurRepository.trouverParMatricule(Matricule.de(matricule))
            .orElseThrow(() -> EntiteNonTrouveeException.de("Utilisateur", matricule));
        u.debloquer();
        return mapper.versResponse(utilisateurRepository.sauvegarder(u));
    }
}
