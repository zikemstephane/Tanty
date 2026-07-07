package com.NTFOODS.Api_tanty.modules.administration.application.utilisateur.usecase;

import com.NTFOODS.Api_tanty.modules.administration.application.utilisateur.dto.response.UtilisateurResponse;
import com.NTFOODS.Api_tanty.modules.administration.application.utilisateur.mapper.UtilisateurAdminMapper;
import com.NTFOODS.Api_tanty.modules.auth.domain.utilisateur.aggregate.Utilisateur;
import com.NTFOODS.Api_tanty.modules.auth.domain.utilisateur.enums.Role;
import com.NTFOODS.Api_tanty.modules.auth.domain.utilisateur.repository.UtilisateurRepository;
import com.NTFOODS.Api_tanty.modules.auth.domain.utilisateur.valueobject.Matricule;
import com.NTFOODS.Api_tanty.shared.kernel.exception.EntiteNonTrouveeException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/** Use Case — changer le rôle d'un utilisateur. */
@Service
@Transactional
public class ChangerRoleUtilisateurUseCase {

    private final UtilisateurRepository utilisateurRepository;
    private final UtilisateurAdminMapper mapper;

    public ChangerRoleUtilisateurUseCase(UtilisateurRepository utilisateurRepository, UtilisateurAdminMapper mapper) {
        this.utilisateurRepository = utilisateurRepository;
        this.mapper = mapper;
    }

    public UtilisateurResponse executer(String matricule, String nouveauRole) {
        Utilisateur u = utilisateurRepository.trouverParMatricule(Matricule.de(matricule))
            .orElseThrow(() -> EntiteNonTrouveeException.de("Utilisateur", matricule));
        Role role;
        try {
            role = Role.valueOf(nouveauRole.trim().toUpperCase());
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException("Rôle invalide : '" + nouveauRole + "'");
        }
        u.changerRole(role);
        return mapper.versResponse(utilisateurRepository.sauvegarder(u));
    }
}
