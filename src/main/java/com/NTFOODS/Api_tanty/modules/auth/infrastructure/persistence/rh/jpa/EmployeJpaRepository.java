package com.NTFOODS.Api_tanty.modules.auth.infrastructure.persistence.rh.jpa;

import com.NTFOODS.Api_tanty.modules.auth.domain.utilisateur.enums.Role;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface EmployeJpaRepository extends JpaRepository<EmployeJpaEntity, Long> {

    Optional<EmployeJpaEntity> findByMatricule(String matricule);

    List<EmployeJpaEntity> findByRole(Role role);

    List<EmployeJpaEntity> findByRoleAndActif(Role role, boolean actif);

    List<EmployeJpaEntity> findBySuperviseurMatricule(String superviseurMatricule);

    boolean existsByMatricule(String matricule);
}
