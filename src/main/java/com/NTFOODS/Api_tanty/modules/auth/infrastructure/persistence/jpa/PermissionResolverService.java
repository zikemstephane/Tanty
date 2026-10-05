package com.NTFOODS.Api_tanty.modules.auth.infrastructure.persistence.jpa;

import com.NTFOODS.Api_tanty.modules.auth.domain.utilisateur.enums.Permission;
import com.NTFOODS.Api_tanty.modules.auth.domain.utilisateur.enums.Role;
import com.NTFOODS.Api_tanty.modules.auth.domain.utilisateur.enums.RolePermissionMatrix;
import org.springframework.stereotype.Service;

import java.util.EnumSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * Service — Calcul des permissions effectives d'un utilisateur.
 *
 * Permissions effectives = (permissions de base du rôle + overrides ACCORDER) - overrides REFUSER.
 *
 * Utilisé par JwtAuthFilter pour charger les authorities Spring Security
 * et par les use cases/API pour consulter/modifier les overrides.
 */
@Service
public class PermissionResolverService {

    private final UtilisateurPermissionJpaRepository overrideRepo;

    public PermissionResolverService(UtilisateurPermissionJpaRepository overrideRepo) {
        this.overrideRepo = overrideRepo;
    }

    /**
     * Calcule les permissions effectives d'un utilisateur.
     */
    public Set<Permission> permissionsEffectives(String matricule, Role role) {
        Set<Permission> base = EnumSet.copyOf(RolePermissionMatrix.permissionsDe(role));
        List<UtilisateurPermissionJpaEntity> overrides = overrideRepo.findByMatricule(matricule);

        Set<Permission> accordees = overrides.stream()
            .filter(o -> o.getTypeOverride() == UtilisateurPermissionJpaEntity.TypeOverride.ACCORDER)
            .map(UtilisateurPermissionJpaEntity::getPermission)
            .collect(Collectors.toSet());

        Set<Permission> refusees = overrides.stream()
            .filter(o -> o.getTypeOverride() == UtilisateurPermissionJpaEntity.TypeOverride.REFUSER)
            .map(UtilisateurPermissionJpaEntity::getPermission)
            .collect(Collectors.toSet());

        Set<Permission> effectives = EnumSet.copyOf(base);
        effectives.addAll(accordees);
        effectives.removeAll(refusees);

        return effectives;
    }

    /**
     * Ajoute ou met à jour un override de permission pour un utilisateur.
     */
    public UtilisateurPermissionJpaEntity ajouterOverride(String matricule, Permission permission,
                                                          UtilisateurPermissionJpaEntity.TypeOverride type,
                                                          String motif, String matriculeAuteur) {
        UtilisateurPermissionJpaEntity existing = overrideRepo.findByMatriculeAndPermission(
            matricule, permission.name());
        if (existing != null) {
            existing.setTypeOverride(type);
            existing.setMotif(motif);
            existing.setMatriculeAuteur(matriculeAuteur);
            return overrideRepo.save(existing);
        }
        return overrideRepo.save(UtilisateurPermissionJpaEntity.builder()
            .matricule(matricule)
            .permission(permission)
            .typeOverride(type)
            .motif(motif)
            .matriculeAuteur(matriculeAuteur)
            .dateCreation(java.time.LocalDateTime.now())
            .build());
    }

    /**
     * Supprime un override de permission.
     */
    public void supprimerOverride(String matricule, Permission permission) {
        overrideRepo.deleteByMatriculeAndPermission(matricule, permission.name());
    }

    /**
     * Liste tous les overrides d'un utilisateur.
     */
    public List<UtilisateurPermissionJpaEntity> overridesDe(String matricule) {
        return overrideRepo.findByMatricule(matricule);
    }
}
