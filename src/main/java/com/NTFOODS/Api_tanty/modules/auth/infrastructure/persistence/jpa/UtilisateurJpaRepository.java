package com.NTFOODS.Api_tanty.modules.auth.infrastructure.persistence.jpa;

import com.NTFOODS.Api_tanty.modules.auth.domain.utilisateur.enums.Role;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UtilisateurJpaRepository extends JpaRepository<UtilisateurJpaEntity, Long> {
    Optional<UtilisateurJpaEntity> findByMatricule(String matricule);
    Optional<UtilisateurJpaEntity> findByEmail(String email);
    boolean existsByEmail(String email);
    boolean existsByMatricule(String matricule);
    List<UtilisateurJpaEntity> findByRole(Role role);
}
