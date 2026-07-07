package com.NTFOODS.Api_tanty.modules.commercial.infrastructure.persistence.recouvrement.jpa;

import com.NTFOODS.Api_tanty.modules.commercial.domain.recouvrement.enums.StatutRecouvrement;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface RecouvrementJpaRepository extends JpaRepository<RecouvrementJpaEntity, Long> {
    Optional<RecouvrementJpaEntity> findByReferenceRecouvrement(String reference);
    List<RecouvrementJpaEntity> findByCodeClient(String codeClient);
    List<RecouvrementJpaEntity> findByMatriculeCommercial(String matricule);
    List<RecouvrementJpaEntity> findByStatut(StatutRecouvrement statut);
    long countByMatriculeCommercial(String matricule);
}
