package com.NTFOODS.Api_tanty.modules.administration.application.utilisateur.usecase;

import com.NTFOODS.Api_tanty.modules.administration.application.utilisateur.dto.response.UtilisateurResponse;
import com.NTFOODS.Api_tanty.modules.administration.application.utilisateur.mapper.UtilisateurAdminMapper;
import com.NTFOODS.Api_tanty.modules.auth.domain.utilisateur.enums.Role;
import com.NTFOODS.Api_tanty.modules.auth.domain.utilisateur.repository.UtilisateurRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/** Use Case — lister les utilisateurs (tous ou par rôle). */
@Service
@Transactional(readOnly = true)
public class ListerUtilisateursUseCase {

    private final UtilisateurRepository utilisateurRepository;
    private final UtilisateurAdminMapper mapper;

    public ListerUtilisateursUseCase(UtilisateurRepository utilisateurRepository, UtilisateurAdminMapper mapper) {
        this.utilisateurRepository = utilisateurRepository;
        this.mapper = mapper;
    }

    public List<UtilisateurResponse> executer(String role) {
        var utilisateurs = (role == null || role.isBlank())
            ? utilisateurRepository.listerTous()
            : utilisateurRepository.listerParRole(Role.valueOf(role.trim().toUpperCase()));
        return utilisateurs.stream().map(mapper::versResponse).toList();
    }
}
