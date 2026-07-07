package com.NTFOODS.Api_tanty.modules.auth.infrastructure.persistence.matricule;

import com.NTFOODS.Api_tanty.modules.auth.domain.utilisateur.enums.Role;
import com.NTFOODS.Api_tanty.modules.auth.infrastructure.persistence.jpa.UtilisateurJpaRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Map;

@Service
public class MatriculeSequenceService {

    private static final Map<Role, String> PREFIXES = Map.of(
        Role.COMMERCIAL,         "COM",
        Role.CHEF_PRODUCTION,    "CP",
        Role.RESPONSABLE_SALLE,   "AP",
        Role.GESTIONNAIRE_STOCK, "GS",
        Role.SECRETAIRE,         "SEC",
        Role.COMPTABLE,          "CPT",
        Role.CONTROLEUR_GENERAL, "CG",
        Role.DIRECTEUR_GENERAL,  "DG",
        Role.DIRECTEUR_COMMERCIAL, "DC",
        Role.ADMIN,              "ADM"
    );

    private final UtilisateurJpaRepository jpaRepository;

    public MatriculeSequenceService(UtilisateurJpaRepository jpaRepository) {
        this.jpaRepository = jpaRepository;
    }

    @Transactional
    public String generer(Role role) {
        String prefix = PREFIXES.getOrDefault(role, "USR");
        long count = jpaRepository.findByRole(role).size() + 1;
        return prefix + String.format("%03d", count);
    }
}
