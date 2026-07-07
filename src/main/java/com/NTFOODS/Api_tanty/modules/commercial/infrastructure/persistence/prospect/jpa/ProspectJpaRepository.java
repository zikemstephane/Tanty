package com.NTFOODS.Api_tanty.modules.commercial.infrastructure.persistence.prospect.jpa;

import com.NTFOODS.Api_tanty.modules.commercial.domain.prospect.enums.StatutProspect;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface ProspectJpaRepository extends JpaRepository<ProspectJpaEntity, Long> {
    Optional<ProspectJpaEntity> findByCodeProspect(String code);
    List<ProspectJpaEntity> findByMatriculeCommercial(String matricule);
    List<ProspectJpaEntity> findByStatut(StatutProspect statut);
}
