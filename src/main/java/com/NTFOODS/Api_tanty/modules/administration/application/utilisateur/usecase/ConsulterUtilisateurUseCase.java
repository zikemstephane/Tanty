package com.NTFOODS.Api_tanty.modules.administration.application.utilisateur.usecase;

import com.NTFOODS.Api_tanty.modules.administration.application.utilisateur.dto.response.UtilisateurResponse;
import com.NTFOODS.Api_tanty.modules.administration.application.utilisateur.mapper.UtilisateurAdminMapper;
import com.NTFOODS.Api_tanty.modules.auth.domain.utilisateur.repository.UtilisateurRepository;
import com.NTFOODS.Api_tanty.modules.auth.domain.utilisateur.valueobject.Matricule;
import com.NTFOODS.Api_tanty.shared.kernel.exception.EntiteNonTrouveeException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
public class ConsulterUtilisateurUseCase {

    private final UtilisateurRepository utilisateurRepository;
    private final UtilisateurAdminMapper mapper;

    public ConsulterUtilisateurUseCase(UtilisateurRepository utilisateurRepository, UtilisateurAdminMapper mapper) {
        this.utilisateurRepository = utilisateurRepository;
        this.mapper = mapper;
    }

    public UtilisateurResponse executer(String matricule) {
        return utilisateurRepository.trouverParMatricule(Matricule.de(matricule))
            .map(mapper::versResponse)
            .orElseThrow(() -> EntiteNonTrouveeException.de("Utilisateur", matricule));
    }
}
