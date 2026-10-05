package com.NTFOODS.Api_tanty.modules.administration.application.utilisateur.usecase;

import com.NTFOODS.Api_tanty.modules.administration.application.utilisateur.dto.response.UtilisateurResponse;
import com.NTFOODS.Api_tanty.modules.administration.application.utilisateur.mapper.UtilisateurAdminMapper;
import com.NTFOODS.Api_tanty.modules.auth.domain.utilisateur.aggregate.Utilisateur;
import com.NTFOODS.Api_tanty.modules.auth.domain.utilisateur.repository.UtilisateurRepository;
import com.NTFOODS.Api_tanty.modules.auth.domain.utilisateur.valueobject.Matricule;
import com.NTFOODS.Api_tanty.modules.auth.domain.utilisateur.valueobject.MotDePasse;
import com.NTFOODS.Api_tanty.shared.kernel.exception.EntiteNonTrouveeException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class ReinitialiserMotDePasseUseCase {

    private final UtilisateurRepository utilisateurRepository;
    private final UtilisateurAdminMapper mapper;
    private final PasswordEncoder passwordEncoder;

    public ReinitialiserMotDePasseUseCase(UtilisateurRepository utilisateurRepository,
                                           UtilisateurAdminMapper mapper,
                                           PasswordEncoder passwordEncoder) {
        this.utilisateurRepository = utilisateurRepository;
        this.mapper = mapper;
        this.passwordEncoder = passwordEncoder;
    }

    public UtilisateurResponse executer(String matricule, String nouveauMotDePasse) {
        Utilisateur u = utilisateurRepository.trouverParMatricule(Matricule.de(matricule))
            .orElseThrow(() -> EntiteNonTrouveeException.de("Utilisateur", matricule));
        u.changerMotDePasse(MotDePasse.deHash(passwordEncoder.encode(nouveauMotDePasse)));
        return mapper.versResponse(utilisateurRepository.sauvegarder(u));
    }
}
