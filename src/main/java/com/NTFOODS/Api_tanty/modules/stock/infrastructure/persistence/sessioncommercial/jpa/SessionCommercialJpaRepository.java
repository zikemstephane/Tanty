package com.NTFOODS.Api_tanty.modules.stock.infrastructure.persistence.sessioncommercial.jpa;

import com.NTFOODS.Api_tanty.modules.stock.domain.sessioncommercial.enums.StatutSession;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface SessionCommercialJpaRepository extends JpaRepository<SessionCommercialJpaEntity, Long> {
    Optional<SessionCommercialJpaEntity> findByNumeroSession(String numero);
    Optional<SessionCommercialJpaEntity> findFirstByMatriculeCommercialAndStatutInOrderByDateCreationDesc(
        String matricule, List<StatutSession> statuts);
    List<SessionCommercialJpaEntity> findByMatriculeCommercial(String matricule);
    List<SessionCommercialJpaEntity> findByDateEffet(LocalDate dateEffet);
    List<SessionCommercialJpaEntity> findByStatut(StatutSession statut);
}
