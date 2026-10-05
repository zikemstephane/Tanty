package com.NTFOODS.Api_tanty.modules.dg.application.usecase;

import com.NTFOODS.Api_tanty.modules.auth.domain.utilisateur.enums.Role;
import com.NTFOODS.Api_tanty.modules.auth.infrastructure.persistence.jpa.UtilisateurJpaEntity;
import com.NTFOODS.Api_tanty.modules.auth.infrastructure.persistence.jpa.UtilisateurJpaRepository;
import com.NTFOODS.Api_tanty.modules.dg.application.dto.response.UtilisateurDgResponse;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@Transactional(readOnly = true)
public class ListerUtilisateursDgUseCase {

    private final UtilisateurJpaRepository utilisateurJpaRepository;

    public ListerUtilisateursDgUseCase(UtilisateurJpaRepository utilisateurJpaRepository) {
        this.utilisateurJpaRepository = utilisateurJpaRepository;
    }

    public List<UtilisateurDgResponse> executer(Role roleFiltre, Boolean actifFiltre) {
        List<UtilisateurJpaEntity> utilisateurs;

        if (roleFiltre != null) {
            utilisateurs = utilisateurJpaRepository.findByRole(roleFiltre);
        } else {
            utilisateurs = utilisateurJpaRepository.findAll();
        }

        if (actifFiltre != null) {
            utilisateurs = utilisateurs.stream().filter(u -> u.isActif() == actifFiltre).toList();
        }

        return utilisateurs.stream()
            .map(this::versResponse)
            .toList();
    }

    public Optional<UtilisateurDgResponse> trouverParMatricule(String matricule) {
        return utilisateurJpaRepository.findByMatricule(matricule).map(this::versResponse);
    }

    private UtilisateurDgResponse versResponse(UtilisateurJpaEntity u) {
        return new UtilisateurDgResponse(
            u.getId(), u.getMatricule(), u.getPrenom(), u.getNom(),
            u.getEmail(), u.getRole(), u.isActif(), u.isCompteBloque(),
            u.getTentativesEchouees(),
            u.getTelephone(), u.getResidence(), u.getCni(), u.getPhoto(),
            u.getDateNaissance(), u.getLieuNaissance(), u.getDateEmbauche(), u.getSexe(),
            u.getDateCreation()
        );
    }
}
