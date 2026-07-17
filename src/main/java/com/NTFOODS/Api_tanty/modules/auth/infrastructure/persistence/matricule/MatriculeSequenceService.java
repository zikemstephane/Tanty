package com.NTFOODS.Api_tanty.modules.auth.infrastructure.persistence.matricule;

import com.NTFOODS.Api_tanty.modules.auth.domain.utilisateur.enums.Role;
import com.NTFOODS.Api_tanty.modules.auth.infrastructure.persistence.jpa.UtilisateurJpaRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Map;

@Service
public class MatriculeSequenceService {

    private static final Map<Role, String> PREFIXES = Map.ofEntries(
    Map.entry(Role.COMMERCIAL, "COM"),
    Map.entry(Role.CHEF_PRODUCTION, "CP"),
    Map.entry(Role.RESPONSABLE_SALLE, "AP"),
    Map.entry(Role.AGENT_DOSEUR, "AD"),
    Map.entry(Role.GESTIONNAIRE_STOCK, "GS"),
    Map.entry(Role.SECRETAIRE, "SEC"),
    Map.entry(Role.COMPTABLE, "CPT"),
    Map.entry(Role.AGENT_PRODUCTION, "AGP"),
    Map.entry(Role.MACHINISTE, "MCH"),
    Map.entry(Role.CHEF_MACHINISTE, "CHM"),
    Map.entry(Role.CONTROLEUR_GENERAL, "CG"),
    Map.entry(Role.DIRECTEUR_GENERAL, "DG"),
    Map.entry(Role.DIRECTEUR_COMMERCIAL, "DC"),
    Map.entry(Role.ADMIN, "ADM")
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
