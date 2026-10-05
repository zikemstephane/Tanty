package com.NTFOODS.Api_tanty.modules.auth.infrastructure.persistence.jpa;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UtilisateurPermissionJpaRepository extends JpaRepository<UtilisateurPermissionJpaEntity, Long> {

    List<UtilisateurPermissionJpaEntity> findByMatricule(String matricule);

    @Query("SELECT p FROM UtilisateurPermissionJpaEntity p WHERE p.matricule = :matricule AND p.permission = :permission")
    UtilisateurPermissionJpaEntity findByMatriculeAndPermission(@Param("matricule") String matricule,
                                                                 @Param("permission") String permission);

    void deleteByMatriculeAndPermission(String matricule, String permission);

    boolean existsByMatriculeAndPermission(String matricule, String permission);
}
